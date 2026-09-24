package sg.edu.choppee.model;

import jakarta.persistence.*;

/**
 * Immutable line-item inside a completed order.
 * Stores a price snapshot (unitPrice) so historical orders remain accurate
 * even if the product price changes later.
 *
 * Associations:
 *   @ManyToOne → PurchaseOrder  (many items belong to one order)
 *   @ManyToOne → Product        (many order-lines reference one product)
 *
 * Data types:
 *   Integer – quantity
 *   Double  – unitPrice (snapshot of product price at checkout)
 */
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Integer – units purchased */
    @Column(nullable = false)
    private Integer quantity;

    /** Double – price per unit captured at checkout (historical snapshot) */
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    //  Associations 

    /** @ManyToOne – FK order_id. Many items belong to one order. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private PurchaseOrder order;

    /** @ManyToOne – FK product_id. Reference to the product. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //  Business helpers 

    public Double getSubtotal() {
        return unitPrice * quantity;
    }

    //  Constructors 

    public OrderItem() {}

    public OrderItem(PurchaseOrder order, Product product, Integer quantity, Double unitPrice) {
        this.order     = order;
        this.product   = product;
        this.quantity  = quantity;
        this.unitPrice = unitPrice;
    }

    //  Getters & Setters 

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public Integer getQuantity()                     { return quantity; }
    public void setQuantity(Integer quantity)        { this.quantity = quantity; }

    public Double getUnitPrice()                     { return unitPrice; }
    public void setUnitPrice(Double unitPrice)       { this.unitPrice = unitPrice; }

    public PurchaseOrder getOrder()                  { return order; }
    public void setOrder(PurchaseOrder order)        { this.order = order; }

    public Product getProduct()                      { return product; }
    public void setProduct(Product product)          { this.product = product; }
}
