package com.example.demo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) 
@Column(name = "id")
private Integer id;
@Column(name = "title")
private String title;
@Column(name = "author")
private String author;
@Column(name = "isbn")
private int isbn;
@Column(columnDefinition = "VARCHAR(20) DEFAULT 'AVAILABLE'")
private String status;


public Integer getId() {
	return id;
}
public void setId(Integer id) {
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
public int getIsbn() {
	return isbn;
}
public void setIsbn(int isbn) {
	this.isbn = isbn;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}

@OneToMany(mappedBy = "book")
private List<BookBorrowing> borrowing;
	
private boolean borrowed;

public boolean isBorrowed() {
	return borrowed;
}

public void setBorrowed(boolean borrowed) {
	this.borrowed = borrowed;
}
}