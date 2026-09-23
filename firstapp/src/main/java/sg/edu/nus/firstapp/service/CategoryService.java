package sg.edu.nus.firstapp.service;

import java.util.List;

import sg.edu.nus.firstapp.model.Category;

public interface CategoryService {
	
    Category create(Category category);
    List<Category> findAll();
    Category findById(Long id);
    void delete(Long id);

}
