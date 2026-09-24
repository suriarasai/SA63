package sg.edu.choppee.service;

import sg.edu.choppee.model.Cart;
import sg.edu.choppee.model.User;
import sg.edu.choppee.repository.CartRepository;
import sg.edu.choppee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserService – handles user registration and login authentication.
 *
 * Security note (important for students):
 *   Passwords are stored in PLAIN TEXT in this demo so the code is easy
 *   to read and trace through.  In any real production application you
 *   MUST hash passwords before storing them, e.g.:
 *
 *     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
 *     user.setPassword(encoder.encode(rawPassword));
 *
 *   and then verify with:
 *     encoder.matches(rawPassword, storedHash)
 *
 *   Spring Security provides BCryptPasswordEncoder out of the box.
 *
 * Transaction notes:
 *   register()  – writes two rows (users + carts) in one transaction so they
 *                 succeed or fail together.  Uses REPEATABLE_READ to guard
 *                 against a race condition where two requests try to register
 *                 the same username at the exact same moment.
 *   login()     – a pure read; no transaction needed, but we use SUPPORTS so
 *                 it can participate in a surrounding one if ever needed.
 */
@Service
public class UserService {

    // Spring injects these automatically.
    @Autowired private UserRepository userRepository; // manages User entities
    @Autowired private CartRepository cartRepository; // manages Cart entities

    //  Registration 

    /**
     * Registers a new user and creates an empty cart for them.
     *
     * propagation = REQUIRED
     *   This method performs two inserts (users + carts) that must both succeed
     *   or both fail.  REQUIRED starts a new transaction (no outer tx exists
     *   at controller level) to wrap both inserts in one atomic unit.
     *
     * isolation = REPEATABLE_READ
     *   The registration flow is:
     *     1. Check whether the username is already taken (SELECT).
     *     2. Check whether the email is already taken (SELECT).
     *     3. Insert the new user row (INSERT).
     *     4. Insert the new cart row (INSERT).
     *
     *   With READ_COMMITTED, another transaction could INSERT the same username
     *   between our check in step 1 and our INSERT in step 3 – a classic
     *   "check-then-act" race condition resulting in duplicate usernames.
     *
     *   REPEATABLE_READ prevents this: the database holds a shared lock on the
     *   checked rows (or uses snapshot isolation) so no other transaction can
     *   insert a conflicting row until we commit.
     *
     *   Note: the UNIQUE constraint on users.username / users.email is the final
     *   safety net; REPEATABLE_READ just reduces the chance of wasted round-trips.
     *
     * rollbackFor = Exception.class
     *   Roll back on ANY exception (checked or unchecked) so we never end up
     *   with a User row but no Cart row, or vice versa.
     *
     * timeout = 20
     *   Abort if the registration transaction takes longer than 20 seconds.
     */
    @Transactional(
        propagation  = Propagation.REQUIRED,
        isolation    = Isolation.REPEATABLE_READ,
        rollbackFor  = Exception.class,
        timeout      = 20
    )
    public User register(User user) {
        // Prevent duplicate usernames (case-sensitive check against DB).
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + user.getUsername());
        }

        // Prevent duplicate email addresses.
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }

        // Persist the User entity. Spring Data generates:
        // INSERT INTO users(username, email, password, first_name, last_name, birth_date, ...) VALUES (...)
        User saved = userRepository.save(user);

        // Every registered user gets their own empty cart immediately.
        // This is the OneToOne relationship: one User → one Cart.
        // We create the Cart here so CartService never has to handle
        // the "cart missing" case for a valid user.
        Cart cart = new Cart(saved); // pass the saved User so the FK is set correctly
        cartRepository.save(cart);   // INSERT INTO carts(user_id, created_at) VALUES (...)

        return saved;
    }

    //  Authentication 

    /**
     * Checks username + password and returns the matching User on success.
     *
     * Returns Optional.empty() (not an exception) if:
     *   • The username does not exist.
     *   • The password does not match.
     *   • The account is inactive (active = false).
     *
     * propagation = SUPPORTS
     *   A pure read method.  If called within an existing transaction it joins
     *   it; if not, it runs without one.  This is slightly cheaper than REQUIRED
     *   which always guarantees a transaction object is created.
     *
     * readOnly = true
     *   Signals to JPA that no entities will be modified, so it can skip the
     *   expensive dirty-checking pass at the end of the method.
     *
     * isolation = READ_COMMITTED
     *   We just need to see committed user records; no need for stricter isolation.
     */
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation   = Isolation.READ_COMMITTED,
        readOnly    = true,
        timeout     = 10
    )
    public Optional<User> login(String username, String password) {
        // Step 1: look up the user by username.
        // Step 2: check the password matches (plain-text comparison in this demo).
        // Step 3: check the account is active (not banned / deactivated).
        // filter() returns Optional.empty() if any condition is false.
        return userRepository.findByUsername(username)
                             .filter(u -> u.getPassword().equals(password))
                             .filter(u -> Boolean.TRUE.equals(u.getActive()));
    }

    /**
     * Looks up a user by primary key.
     * Used by controllers that need the full User object (e.g., profile pages).
     *
     * readOnly = true  – pure read, no modifications.
     * propagation = SUPPORTS – lightweight; join surrounding tx if one exists.
     */
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation   = Isolation.READ_COMMITTED,
        readOnly    = true,
        timeout     = 10
    )
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
