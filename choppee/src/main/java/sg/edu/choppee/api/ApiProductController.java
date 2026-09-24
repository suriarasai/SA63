// ApiProductController.java
// Replaces: ProductController (returns View → returns JSON)
package sg.edu.choppee.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.choppee.model.Category;
import sg.edu.choppee.model.Product;
import sg.edu.choppee.service.ProductService;

@RestController
@RequestMapping("/api")
public class ApiProductController {

    @Autowired private ProductService productService;

    // GET /api/products  [optional: ?q= or ?category=]
    // OLD: ProductController.list() returns "products" (view name)
    // NEW: returns List<Product> as JSON
    @GetMapping("/products")
    public List<Product> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long category) {
        if (q != null && !q.isBlank())
            return productService.search(q);
        if (category != null)
            return productService.findByCategory(category);
        return productService.findAll();
    }

    // GET /api/products/:id
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> detail(@PathVariable Long id) {
        return productService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/categories
    @GetMapping("/categories")
    public List<Category> categories() {
        return productService.findAllCategories();
    }

    // GET /api/products/featured  (first 6 active products)
    @GetMapping("/products/featured")
    public List<Product> featured() {
        return productService.findAll().stream()
            .filter(p -> p.getActive() != null && p.getActive())
            .limit(6).toList();
    }

    // GET /api/products/deals  (products with discount > 0)
    @GetMapping("/products/deals")
    public List<Product> deals() {
        return productService.findAll().stream()
            .filter(p -> p.getDiscountPercent() != null && p.getDiscountPercent() > 0)
            .toList();
    }
}