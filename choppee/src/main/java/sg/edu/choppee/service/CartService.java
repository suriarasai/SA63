package sg.edu.choppee.service;

import sg.edu.choppee.model.*;
import sg.edu.choppee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * CartService – manages all shopping-cart operations.
 *
 * Responsibilities:
 *   - Fetch the cart that belongs to the currently logged-in user.
 *   - Add a new product (or increment its quantity if already present).
 *   - Update the quantity of an existing line-item.
 *   - Remove a single line-item.
 *   - Empty the entire cart (called by OrderService after checkout).
 *
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║  @Transactional attribute quick-reference                    ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  propagation  – what to do when a transaction already exists  ║
 * ║                 when this method is called:                   ║
 * ║    REQUIRED      join existing, or start a new one (default)  ║
 * ║    REQUIRES_NEW  always start fresh; suspend the outer tx     ║
 * ║    SUPPORTS      use existing if present; no tx otherwise     ║
 * ║    NOT_SUPPORTED always run outside any transaction           ║
 * ║    MANDATORY     must already be inside a transaction         ║
 * ║    NEVER         must NOT be inside a transaction             ║
 * ║    NESTED        savepoint inside the current transaction     ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  isolation    – what uncommitted changes from OTHER sessions   ║
 * ║                 are visible to this transaction:              ║
 * ║    DEFAULT          database default (usually READ_COMMITTED)  ║
 * ║    READ_UNCOMMITTED can see uncommitted rows (dirty reads OK)  ║
 * ║    READ_COMMITTED   only sees committed rows (no dirty reads)  ║
 * ║    REPEATABLE_READ  same SELECT returns same rows within tx    ║
 * ║    SERIALIZABLE     strictest; fully serialised execution      ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  readOnly     – hint to JPA: skip dirty-checking flush.       ║
 * ║                 Set true for pure SELECT methods.             ║
 * ║  rollbackFor  – exception types that trigger rollback.        ║
 * ║                 Default: RuntimeException + Error.            ║
 * ║  timeout      – max seconds before forced rollback (-1=none)  ║
 * ╚═══════════════════════════════════════════════════════════════╝
 */
@Service
public class CartService {

    // Spring injects these repository beans automatically via @Autowired.
    @Autowired private CartRepository     cartRepository;      // manages Cart entities
    @Autowired private CartItemRepository cartItemRepository;  // manages CartItem entities
    @Autowired private ProductRepository  productRepository;   // used to look up products

    //  Read operations 

    /**
     * Returns the Cart that belongs to this user.
     *
     * readOnly = true  – JPA skips the dirty-check flush at commit time,
     *                    which is a CPU/memory optimisation for pure reads.
     * propagation = REQUIRED – join an existing transaction (e.g., from
     *                    OrderService.placeOrder) or start a new one.
     * isolation = READ_COMMITTED – we only need to see committed cart data.
     */
    @Transactional(
        propagation = Propagation.REQUIRED,
        isolation   = Isolation.READ_COMMITTED,
        readOnly    = true,
        timeout     = 10
    )
    public Cart getCartForUser(Long userId) {
        // findByUserId is a Spring Data derived-query method in CartRepository.
        // It generates: SELECT * FROM carts WHERE user_id = ?
        return cartRepository.findByUserId(userId)
                             .orElseThrow(() -> new RuntimeException(
                                 "Cart not found for user " + userId));
    }

    /**
     * Returns the total number of distinct line-items in the user's cart.
     * Shown as a badge number in the navigation bar.
     *
     * propagation = SUPPORTS – participate in a surrounding transaction if one
     *   exists; otherwise run without a transaction.  This is intentionally
     *   lightweight because it is called on every page request via
     *   GlobalControllerAdvice.cartItemCount().
     */
    @Transactional(
        propagation = Propagation.SUPPORTS,
        isolation   = Isolation.READ_COMMITTED,
        readOnly    = true,
        timeout     = 5
    )
    public int countItems(Long userId) {
        // If the user has no cart yet (e.g., before registration completes),
        // return 0 gracefully rather than throwing an exception.
        return cartRepository.findByUserId(userId)
                             .map(c -> cartItemRepository.countByCartId(c.getId()))
                             .orElse(0);
    }

    //  Write operations 

    /**
     * Adds the specified product to the user's cart.
     * If that product is already in the cart, its quantity is incremented.
     * Throws RuntimeException if the product is out of stock.
     *
     * propagation = REQUIRED
     *   The most common propagation for write methods.  If the caller already
     *   holds a transaction we join it; otherwise Spring opens a new one.
     *
     * isolation = READ_COMMITTED
     *   We read the product's stock status and the existing cart item (if any),
     *   then update.  READ_COMMITTED prevents dirty reads (seeing another
     *   transaction's not-yet-committed stock deductions).
     *   We do NOT need REPEATABLE_READ or SERIALIZABLE here because the
     *   definitive stock-deduction happens inside OrderService.placeOrder(),
     *   which uses SERIALIZABLE isolation to prevent race conditions.
     *
     * rollbackFor = RuntimeException.class
     *   Spring rolls back on RuntimeException by default; listing it explicitly
     *   documents the intent and makes the code self-explanatory.
     *
     * timeout = 15
     *   Abort and roll back if the transaction takes longer than 15 seconds
     *   (guards against lock waits caused by concurrent writes to the same row).
     */
    @Transactional(
        propagation  = Propagation.REQUIRED,
        isolation    = Isolation.READ_COMMITTED,
        rollbackFor  = RuntimeException.class,
        timeout      = 15
    )
    public void addToCart(Long userId, Long productId, Integer quantity) {
        // Default to 1 if caller passes null or a zero/negative value.
        if (quantity == null || quantity < 1) quantity = 1;

        // Load the user's cart (throws if missing).
        Cart cart = getCartForUser(userId);

        // Load the product from the database.
        // Throws RuntimeException if the product ID does not exist.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        // Reject the request if the product has no stock.
        if (!product.isInStock()) {
            throw new RuntimeException("Product is out of stock: " + product.getName());
        }

        // Check whether this product already has a line-item in the cart.
        Optional<CartItem> existing = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId);

        if (existing.isPresent()) {
            // Product already in the cart → increment the quantity.
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);   // SQL: UPDATE cart_items SET quantity = ? WHERE id = ?
        } else {
            // New product → insert a fresh line-item.
            CartItem newItem = new CartItem(cart, product, quantity);
            cartItemRepository.save(newItem); // SQL: INSERT INTO cart_items(cart_id, product_id, quantity, added_at) VALUES (...)
        }
    }

    /**
     * Updates the quantity of a specific cart line-item.
     * If the new quantity is 0 or negative, the item is deleted.
     *
     * propagation = REQUIRED – standard write operation.
     * isolation   = READ_COMMITTED – load the item, verify ownership, then update.
     *
     * Security note: the ownership check ensures a logged-in user can only
     * modify their OWN cart items, not those belonging to other users.
     * We compare Long objects with .equals() (not ==) to avoid bugs caused
     * by Java's Integer/Long caching above 127.
     */
    @Transactional(
        propagation  = Propagation.REQUIRED,
        isolation    = Isolation.READ_COMMITTED,
        rollbackFor  = RuntimeException.class,
        timeout      = 15
    )
    public void updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        // Load the specific cart line-item by its primary key.
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Security check: the item must belong to the requesting user's cart.
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: cart item does not belong to this user.");
        }

        if (quantity == null || quantity <= 0) {
            // Quantity set to zero or less → delete the line-item.
            cartItemRepository.delete(item);   // SQL: DELETE FROM cart_items WHERE id = ?
        } else {
            // Update with the new quantity.
            item.setQuantity(quantity);
            cartItemRepository.save(item);     // SQL: UPDATE cart_items SET quantity = ? WHERE id = ?
        }
    }

    /**
     * Removes a single line-item from the cart entirely.
     *
     * propagation = REQUIRED  – join or start a transaction.
     * isolation   = READ_COMMITTED – simple read-then-delete operation.
     *
     * The ownership check prevents user A from deleting items from user B's cart.
     */
    @Transactional(
        propagation  = Propagation.REQUIRED,
        isolation    = Isolation.READ_COMMITTED,
        rollbackFor  = RuntimeException.class,
        timeout      = 15
    )
    public void removeItem(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // Ownership check (same pattern as updateQuantity).
        if (!item.getCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: cart item does not belong to this user.");
        }

        cartItemRepository.delete(item); // SQL: DELETE FROM cart_items WHERE id = ?
    }

    /**
     * Empties the entire cart by removing ALL line-items.
     * Called by OrderService.placeOrder() after a successful checkout.
     *
     * propagation = REQUIRED
     *   OrderService.placeOrder() is itself annotated @Transactional(REQUIRED),
     *   so clearCart() JOINS that outer transaction.  This is critical:
     *   if any later step in placeOrder() fails (e.g., saving the order),
     *   the entire transaction rolls back and the cart is automatically
     *   restored to its pre-clearCart state.
     *
     * isolation = READ_COMMITTED – load the cart, then clear it.
     */
    @Transactional(
        propagation  = Propagation.REQUIRED,
        isolation    = Isolation.READ_COMMITTED,
        rollbackFor  = RuntimeException.class,
        timeout      = 15
    )
    public void clearCart(Long userId) {
        Cart cart = getCartForUser(userId);

        // cart.getItems().clear() removes all CartItem objects from the list.
        // Because Cart.items is mapped with orphanRemoval = true, JPA will
        // automatically issue DELETE statements for every removed CartItem
        // when the transaction is committed.
        cart.getItems().clear();
        cartRepository.save(cart); // triggers the orphan-removal DELETE statements
    }
}
