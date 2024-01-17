package com.example.demo;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BookBorrowing {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
@ManyToOne
@JoinColumn(name = "book_id")
private Book book;
@Column(name = "borrowed_date")
private LocalDateTime borrowDate;
@Column(name = "returned_date")
private LocalDateTime returnDate;


public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Book getBook() {
	return book;
}
public void setBook(Book book) {
	this.book = book;
}
public LocalDateTime getBorrowDate() {
	return borrowDate;
}
public void setBorrowDate(LocalDateTime borrowDate) {
	this.borrowDate = borrowDate;
}
public LocalDateTime getReturnDate() {
	return returnDate;
}
public void setReturnDate(LocalDateTime returnDate) {
	this.returnDate = returnDate;
}
	
}
