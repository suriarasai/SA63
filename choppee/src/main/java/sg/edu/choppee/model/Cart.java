package sg.edu.choppee.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart – one per registered user.
 *
 * Associations:
 *   @OneToOne  → User      (owns the FK user_id; "owner" side of the OneToOne)
 *   @OneToMany → CartItem  (a cart contains many line-items)
 */
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //  Associations 

    /**
     * @OneToOne – owner side; FK user_id lives in the carts table.
     * User.cart is the inverse side (mappedBy = "user").
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /** @OneToMany – a cart owns many cart items; orphanRemoval cleans up removed items. */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<CartItem> items = new ArrayList<>();

    //  Lifecycle 

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //  Business helpers 

    public Double getTotal() {
        return items.stream()
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    //  Constructors 

    public Cart() {}

    public Cart(User user) {
        this.user = user;
    }

    //  Getters & Setters 

    public Long getId()                         { return id; }
    public void setId(Long id)                  { this.id = id; }

    public LocalDateTime getCreatedAt()         { return createdAt; }
    public void setCreatedAt(LocalDateTime t)   { this.createdAt = t; }

    public User getUser()                       { return user; }
    public void setUser(User user)              { this.user = user; }

    public List<CartItem> getItems()            { return items; }
    public void setItems(List<CartItem> items)  { this.items = items; }
}
