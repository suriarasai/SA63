package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



//class level annotation
@Entity
@Table(name = "book_table")
public class Book {
	//member variable annotation
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //MySQL will control this
	//Auto injects a simple annotation
	@Column(nullable = false, length = 200)
    private String title;
 
    @Column(nullable = false, length = 120)
    private String author;
 
    @Column(nullable = false, length = 17, updatable = false)
    private String isbn;
 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Genre genre;
 
    @Column(name = "published_on")
    private LocalDate publishedOn;
 
    @Column(precision = 8, scale = 2)
    private BigDecimal price;
	//constructor
	public Book(String title, String author, String isbn, Genre genre, LocalDate publishedOn, BigDecimal price) {
		super();
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.genre = genre;
		this.publishedOn = publishedOn;
		this.price = price;
	}
	protected Book() {
		super();
	}
	
	// Getter Setter
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	public LocalDate getPublishedOn() {
		return publishedOn;
	}
	public void setPublishedOn(LocalDate publishedOn) {
		this.publishedOn = publishedOn;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	// print the content of the entire
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", isbn=" + isbn + ", genre=" + genre
				+ ", publishedOn=" + publishedOn + ", price=" + price + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(isbn, other.isbn);
	}
    
	


	


	

}
