package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.repo.BookRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepoTesting {
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
    private TestEntityManager entityManager;
	
	protected Book b = new Book("Java Interfaces", "Dai", "978-981-4234-01-4", Genre.LAW, LocalDate.of(1978, 9, 1), new BigDecimal("744.33"));
	 
	@BeforeEach
	void populaeData() {
	        // Two by the same author, so the collection finders have something to collect
	        entityManager.persist(new Book("Little Irony", "CHEN ZIXI", "978-981-4234-01-7",
	                Genre.FICTION, LocalDate.of(1978, 9, 1), new BigDecimal("78.56")));
	        entityManager.persist(new Book("Whimpy Kid", "CHEN QIJIANG", "978-981-4234-01-8",
	                Genre.HEALTHCARE, LocalDate.of(2007, 9, 1), new BigDecimal("12.30")));    
	        entityManager.persist(new Book("Singapore Gost Stries", "CHEN CHANGYU", "978-981-4234-01-9",
	                Genre.ROMCOM, LocalDate.of(2023, 9, 1), new BigDecimal("16.00"))); 
	        entityManager.persist(new Book("Java Programming", "Aufa Noor", "978-981-4234-01-1",
	                Genre.SCIENCE, LocalDate.of(2002, 9, 1), new BigDecimal("34.23")));
	        entityManager.persist(new Book("SQL Essentials", "CHELSEA NISBAN", "978-981-4234-01-2",
	                Genre.TECHNOLOGY, LocalDate.of(2026, 9, 1), new BigDecimal("58.78")));    
	        entityManager.persist(new Book("Mastering Spring MVC", "Chloe", "978-981-4234-01-3",
	                Genre.TECHNOLOGY, LocalDate.of(2014, 9, 1), new BigDecimal("82.99")));  
	        entityManager.flush();
	        entityManager.clear();
	    }

	 @Test
	 //@Order(1)
     void findAll() {
         List<Book> books = bookRepo.findAll();
         for (Book book : books) {
			System.out.println(book);
		}
         assertThat(books).hasSize(6);
     }
	 
	 @Test
	 //@Order(2)
	 void save(Book b) {
		 bookRepo.save(b);
		 entityManager.flush();
		 List<Book> books = bookRepo.findAll();
		 assertThat(books).hasSize(7);
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

	

}
