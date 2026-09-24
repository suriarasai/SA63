package sg.edu.choppee.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Product category (e.g. "Chef Knives", "Axes & Hatchets").
 *
 * Associations:
 *   @OneToMany → Product  (a category groups many products)
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column(length = 255)
    private String description;

    /** @OneToMany – inverse side; Product holds the FK (category_id). */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    //  Constructors 

    public Category() {}

    public Category(String name, String description) {
        this.name        = name;
        this.description = description;
    }

    //  Getters & Setters 

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getName()                      { return name; }
    public void setName(String name)             { this.name = name; }

    public String getDescription()               { return description; }
    public void setDescription(String d)         { this.description = d; }

    public List<Product> getProducts()           { return products; }
    public void setProducts(List<Product> prods) { this.products = prods; }
}
