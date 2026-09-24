package sg.edu.choppee.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Descriptive label for products (e.g. "Handcrafted", "Premium", "Carbon Steel").
 *
 * Associations:
 *   @ManyToMany ← Product  (many products can share many tags)
 *                            Product owns the join table product_tags.
 */
@Entity
@Table(name = "tags")
@EnableJpaAuditing
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /** @ManyToMany – inverse (mappedBy); Product is the owner side. */
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    //  Constructors 

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    //  Getters & Setters 

    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }

    public String getName()                  { return name; }
    public void setName(String name)         { this.name = name; }

    public Set<Product> getProducts()        { return products; }
    public void setProducts(Set<Product> p)  { this.products = p; }
}
