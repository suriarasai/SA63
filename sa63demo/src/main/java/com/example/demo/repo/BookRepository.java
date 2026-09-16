package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Book;


/**
 * The Jpa Repository Interface already provides the followng method signature
 * <S extends T> S save(S entity);   
 *  Optional<T> findById(ID primaryKey);
 *   List<T> findAll(); 
 *    long count();
 *    void delete(T entity);               
 *     boolean existsById(ID primaryKey);   
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
	
	// Example for a find Query
	public Book findBooksById(Integer id);
	
	// Example find a book either by title or by author
	public List<Book> findBooksByTitleOrAuthor(String title, String author);
	
	// Another Query
	
	public List<Book> queryBooksByAuthor(String author);
	
	
	

}
