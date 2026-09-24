package sg.edu.choppee.repository;

import sg.edu.choppee.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrueOrderByCreatedAtDesc();

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           " LOWER(p.brand) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Product> search(String q);

    List<Product> findTop6ByActiveTrueOrderByCreatedAtDesc();

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.discountPercent > 0 ORDER BY p.discountPercent DESC")
    List<Product> findDeals();
}
