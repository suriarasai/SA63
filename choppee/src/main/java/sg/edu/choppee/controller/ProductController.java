package sg.edu.choppee.controller;

import sg.edu.choppee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Browse products: list, search, filter by category, view detail.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired private ProductService productService;

    /** GET /products  – optional ?q= search, optional ?category= filter */
    @GetMapping
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) Long category,
                       Model model) {

        if (q != null && !q.isBlank()) {
            model.addAttribute("products", productService.search(q));
            model.addAttribute("searchQuery", q);
        } else if (category != null) {
            model.addAttribute("products", productService.findByCategory(category));
            productService.findCategoryById(category)
                          .ifPresent(c -> model.addAttribute("activeCategory", c));
        } else {
            model.addAttribute("products", productService.findAll());
        }

        model.addAttribute("categories", productService.findAllCategories());
        return "products";
    }

    /** GET /products/{id}  – single product detail page */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        var product = productService.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        model.addAttribute("product", product);
        return "product-detail";
    }
}
