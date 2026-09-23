package sg.edu.nus.firstapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.firstapp.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {

}
