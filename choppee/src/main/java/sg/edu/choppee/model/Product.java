package sg.edu.choppee.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * A sellable item (knife, hatchet, sharpener, protective gear, etc.).
 *
 * Associations:
 *   @ManyToOne  → Category  (many products belong to one category)
 *   @ManyToMany → Tag       (many products share many tags; owns the join table)
 *
 * Data types:
 *   Long          – id
 *   String        – name, description, imageUrl, brand
 *   Double        – price, discountPercent
 *   Integer       – stockQuantity
 *   LocalDateTime – createdAt
 *   Boolean       – active
 */
@Entity
@Table(name = "products")
@EnableJpaAuditing
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 80)
    private String brand;

    /** Double – retail price in USD */
    @Column(nullable = false)
    private Double price;

    /** Double – percentage discount (0.0 – 100.0) */
    @Column(name = "discount_percent")
    private Double discountPercent = 0.0;

    /** Integer – units in stock */
    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    /** LocalDateTime – timestamp of product listing */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Boolean active;

    //  Associations 

    /** @ManyToOne – many products belong to one category; FK category_id on this table. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * @ManyToMany – owner side.
     * Creates join table product_tags(product_id, tag_id).
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_tags",
        joinColumns        = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    //  Lifecycle 

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.active == null)          this.active          = true;
        if (this.discountPercent == null)  this.discountPercent = 0.0;
        if (this.stockQuantity   == null)  this.stockQuantity   = 0;
    }

    //  Business helpers 

    /** Effective price after applying discount. */
    public Double getEffectivePrice() {
        if (discountPercent == null || discountPercent == 0.0) return price;
        return price * (1.0 - discountPercent / 100.0);
    }

    public boolean isInStock() {
        return stockQuantity != null && stockQuantity > 0;
    }

    //  Constructors 

    public Product() {}

    //  Getters & Setters 

    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }

    public String getName()                         { return name; }
    public void setName(String name)                { this.name = name; }

    public String getDescription()                  { return description; }
    public void setDescription(String d)            { this.description = d; }

    public String getBrand()                        { return brand; }
    public void setBrand(String brand)              { this.brand = brand; }

    public Double getPrice()                        { return price; }
    public void setPrice(Double price)              { this.price = price; }

    public Double getDiscountPercent()              { return discountPercent; }
    public void setDiscountPercent(Double dp)       { this.discountPercent = dp; }

    public Integer getStockQuantity()               { return stockQuantity; }
    public void setStockQuantity(Integer sq)        { this.stockQuantity = sq; }

    public String getImageUrl()                     { return imageUrl; }
    public void setImageUrl(String imageUrl)        { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime t)       { this.createdAt = t; }

    public Boolean getActive()                      { return active; }
    public void setActive(Boolean active)           { this.active = active; }

    public Category getCategory()                   { return category; }
    public void setCategory(Category category)      { this.category = category; }

    public Set<Tag> getTags()                       { return tags; }
    public void setTags(Set<Tag> tags)              { this.tags = tags; }
}
