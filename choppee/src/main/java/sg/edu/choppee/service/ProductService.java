package sg.edu.choppee.service;

import sg.edu.choppee.model.Category;
import sg.edu.choppee.model.Product;
import sg.edu.choppee.repository.CategoryRepository;
import sg.edu.choppee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Read-only product browsing operations.
 */
@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    public List<Product> findAll()                  { return productRepository.findByActiveTrueOrderByCreatedAtDesc(); }
    public List<Product> findFeatured()             { return productRepository.findTop6ByActiveTrueOrderByCreatedAtDesc(); }
    public List<Product> findDeals()                { return productRepository.findDeals(); }
    public Optional<Product> findById(Long id)      { return productRepository.findById(id); }
    public List<Category> findAllCategories()       { return categoryRepository.findAll(); }
    public Optional<Category> findCategoryById(Long id) { return categoryRepository.findById(id); }

    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    public List<Product> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return productRepository.search(query.trim());
    }
}
