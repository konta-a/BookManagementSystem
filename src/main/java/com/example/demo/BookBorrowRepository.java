package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
 public interface BookBorrowRepository extends JpaRepository<BookBorrowing,Integer>{
	 List<BookBorrowing> findAll();
	
	 BookBorrowing findByBookIdAndReturnDateIsNull(Integer bookId);

	
}
