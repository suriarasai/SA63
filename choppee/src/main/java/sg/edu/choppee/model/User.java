package sg.edu.choppee.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Registered shopper.
 *
 * Associations demonstrated:
 *   @OneToOne  → Cart         (a user owns exactly one cart)
 *   @OneToMany → PurchaseOrder (a user places many orders)
 *
 * Data types:
 *   Long          – id
 *   String        – username, email, password, firstName, lastName
 *   LocalDate     – birthDate   (java.sql.Date equivalent)
 *   LocalDateTime – createdAt   (java.sql.Timestamp equivalent)
 *   Boolean       – active
 */
@Entity
@Table(name = "users")
@EnableJpaAuditing
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(name = "first_name", length = 60)
    private String firstName;

    @Column(name = "last_name", length = 60)
    private String lastName;

    /** java.time.LocalDate → DATE column */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /** java.time.LocalDateTime → TIMESTAMP column */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Boolean active;

    //  Associations 

    /** @OneToOne – bidirectional; Cart holds the FK (user_id). */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;

    /** @OneToMany – one user → many purchase orders. */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrder> orders = new ArrayList<>();

    //  Lifecycle 

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.active == null) this.active = true;
    }

    //  Constructors 

    public User() {}

    public User(String username, String email, String password,
                String firstName, String lastName, LocalDate birthDate) {
        this.username  = username;
        this.email     = email;
        this.password  = password;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.birthDate = birthDate;
    }

    //  Getters & Setters 

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getUsername()               { return username; }
    public void setUsername(String username)  { this.username = username; }

    public String getEmail()                  { return email; }
    public void setEmail(String email)        { this.email = email; }

    public String getPassword()               { return password; }
    public void setPassword(String password)  { this.password = password; }

    public String getFirstName()              { return firstName; }
    public void setFirstName(String fn)       { this.firstName = fn; }

    public String getLastName()               { return lastName; }
    public void setLastName(String ln)        { this.lastName = ln; }

    public LocalDate getBirthDate()           { return birthDate; }
    public void setBirthDate(LocalDate d)     { this.birthDate = d; }

    public LocalDateTime getCreatedAt()       { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }

    public Boolean getActive()                { return active; }
    public void setActive(Boolean active)     { this.active = active; }

    public Cart getCart()                     { return cart; }
    public void setCart(Cart cart)            { this.cart = cart; }

    public List<PurchaseOrder> getOrders()    { return orders; }
    public void setOrders(List<PurchaseOrder> o) { this.orders = o; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
