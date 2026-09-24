package sg.edu.choppee.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * A single line-item in a user's shopping cart.
 *
 * Associations:
 *   @ManyToOne → Cart    (many items belong to one cart)
 *   @ManyToOne → Product (many cart-lines reference one product)
 *
 * Data types:
 *   Integer       – quantity
 *   LocalDateTime – addedAt
 */
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Integer – number of units the shopper wants to buy */
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    //  Associations 

    /** @ManyToOne – FK cart_id. Many items belong to one cart. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /** @ManyToOne – FK product_id. Many cart lines reference one product. */
    @ManyToOne(fetch = FetchType.EAGER)   // EAGER so Thymeleaf can read name/price
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //  Lifecycle 

    @PrePersist
    protected void onCreate() {
        this.addedAt = LocalDateTime.now();
    }

    //  Business helpers 

    public Double getSubtotal() {
        if (product == null || quantity == null) return 0.0;
        return product.getEffectivePrice() * quantity;
    }

    //  Constructors 

    public CartItem() {}

    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart     = cart;
        this.product  = product;
        this.quantity = quantity;
    }

    //  Getters & Setters 

    public Long getId()                         { return id; }
    public void setId(Long id)                  { this.id = id; }

    public Integer getQuantity()                { return quantity; }
    public void setQuantity(Integer quantity)   { this.quantity = quantity; }

    public LocalDateTime getAddedAt()           { return addedAt; }
    public void setAddedAt(LocalDateTime t)     { this.addedAt = t; }

    public Cart getCart()                       { return cart; }
    public void setCart(Cart cart)              { this.cart = cart; }

    public Product getProduct()                 { return product; }
    public void setProduct(Product product)     { this.product = product; }
}
