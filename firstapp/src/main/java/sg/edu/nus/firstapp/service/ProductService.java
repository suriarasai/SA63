package sg.edu.nus.firstapp.service;

import java.util.List;

import sg.edu.nus.firstapp.model.Product;

public interface ProductService {
	//Wireframes
	Product create(Product request);
    Product findById(Long id);
    List<Product> findAll();
    List<Product> findByCategory(Long categoryId);
    Product update(Long id, Product request);
    Product adjustStock(Long id, int delta);
    void delete(Long id);

}
