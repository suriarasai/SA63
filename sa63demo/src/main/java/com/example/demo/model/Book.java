package com.example.demo.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;



//class level annotation
@Entity
public class Book {
	//member variable annotation
	@Id
	private int id; //MySQL will control this
	private String title;
	private String author;
	private String ISBN;
	private LocalDate dataOfPublication;
	private Double price;
	private String description;
	private Genre genre;
	//constructor
	public Book() {
		super();
	}
	public Book(int id, String title, String author, String iSBN, LocalDate dataOfPublication, Double price,
			String description) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		ISBN = iSBN;
		this.dataOfPublication = dataOfPublication;
		this.price = price;
		this.description = description;
	}
	public Book(String title, String author, String iSBN, LocalDate dataOfPublication, Double price,
			String description) {
		super();
		this.title = title;
		this.author = author;
		ISBN = iSBN;
		this.dataOfPublication = dataOfPublication;
		this.price = price;
		this.description = description;
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
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public LocalDate getDataOfPublication() {
		return dataOfPublication;
	}
	public void setDataOfPublication(LocalDate dataOfPublication) {
		this.dataOfPublication = dataOfPublication;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	// print the content of the entire
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", ISBN=" + ISBN + ", dataOfPublication="
				+ dataOfPublication + ", price=" + price + ", description=" + description + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(ISBN);
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
		return Objects.equals(ISBN, other.ISBN);
	}
	
	
	//constructor

	

}
