package com.example.demo.repo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Book;
import com.example.demo.model.Genre;


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
	
	Optional<Book> findByIsbn(String isbn);
	 
    List<Book> findByAuthor(String author);
 
    List<Book> findByAuthorOrderByPublishedOnDesc(String author);
 
    List<Book> findByTitleContainingIgnoreCase(String fragment);
 
    List<Book> findByAuthorAndGenre(String author, Genre genre);
 
    List<Book> findByPriceBetween(BigDecimal min, BigDecimal max);
 
    List<Book> findByPublishedOnAfter(LocalDate date);
 
    List<Book> findByPriceIsNull();
 
    List<Book> findByGenreIn(List<Genre> genres);
 
     
   // Optional<Book> findFirstByGenreOrderByPriceAsc(Genre genre);
 
   // List<Book> findTop3ByOrderByPublishedOnDesc();

	

}
