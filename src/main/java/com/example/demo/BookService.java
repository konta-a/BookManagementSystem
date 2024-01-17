package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import java.lang.IllegalArgumentException;
import java.time.LocalDateTime;

@Service
public class BookService {
	
@Autowired
private BookRepository bookRepository;
	
public BookService(BookRepository bookRepository) {
	this.bookRepository = bookRepository;
}
@Autowired
private BookBorrowRepository bookBorrowRepository;
public BookService(BookBorrowRepository bookBorrowRepository) {
	this.bookBorrowRepository = bookBorrowRepository;
}
public BookService() {}

public List<Book> getAllData() {
	// TODO 自動生成されたメソッド・スタブ
	return bookRepository.findAll();
}


//検索結果を表示させるためのメソッド
public List<Book> searchBooks(String id)  {
    List<Book> bookList = new ArrayList<>();
    try {
        if (id == null || id.trim().isEmpty()) {
            bookList = bookRepository.findAll();
        } else if (id.chars().allMatch(Character::isDigit)) {
            Integer parsedId = Integer.parseInt(id);
            Book book = bookRepository.findById(parsedId).orElse(null);
            if (book != null) {
                bookList.add(book);
            } else {
                // 書籍が見つからなかった場合のメッセージを追加
                throw new IllegalArgumentException("該当する書籍が見つかりませんでした");
            }
        } else {
            // もし数字以外の形式のIDが渡された場合のエラーハンドリング
            throw new IllegalArgumentException("Invalid ID format");
        }
    } catch (NumberFormatException e) {
        // 数字に変換できなかった場合のエラーハンドリング
        throw new IllegalArgumentException("Invalid ID format");
    }

    return bookList;
}

public Book getBookById(Integer id) {
	// TODO 自動生成されたメソッド・スタブ
	return bookRepository.findById(id).orElse(null);
}

public void saveBook(Book book) {
	// TODO 自動生成されたメソッド・スタブ
 bookRepository.save(book);	
}

public void deleteBookById(int id) {
	// TODO 自動生成されたメソッド・スタブ
	bookRepository.deleteById(id);
	
}

public void addBookById(String title,String author,int isbn,String status) {
    Book newBook = new Book();
    newBook.setTitle(title);
    newBook.setAuthor(author);
    newBook.setIsbn(isbn);
    newBook.setStatus(status);

    bookRepository.save(newBook);
}

//貸し出し処理
public void borrowBook(int id) {
    Book book = getBookById(id);
    if (book != null && book.getStatus().equals("AVAILABLE")) {
        book.setStatus("BORROWED");
        bookRepository.save(book);
    
        //貸出日を設定
        BookBorrowing borrowing = new BookBorrowing();
        borrowing.setBook(book);
        borrowing.setBorrowDate(LocalDateTime.now());
        bookBorrowRepository.save(borrowing);
    } else {
        // 書籍が見つからないか、ステータスがAVAILABLEでない場合のエラーハンドリング
        throw new IllegalArgumentException("この書籍は貸出中です");
      
    }
}
//返却機能
public void returnBook(int id) {
	// TODO 自動生成されたメソッド・スタブ
	Book book = getBookById(id);
    if (book != null && book.getStatus().equals("BORROWED")) {
        book.setStatus("AVAILABLE");
        bookRepository.save(book);
       
        BookBorrowing borrowing = bookBorrowRepository.findByBookIdAndReturnDateIsNull(id);
        if (borrowing != null) {
        borrowing.setReturnDate(LocalDateTime.now());
        bookBorrowRepository.save(borrowing);
        } else {
            throw new IllegalArgumentException("貸出記録が見つかりません");
        }

    } else {
        // 書籍が見つからないか、ステータスがBORROWEDでない場合のエラーハンドリング
        throw new IllegalArgumentException("この書籍は返却済みです");	
    }
}

public List<BookBorrowing> getBookHistory() {
	// TODO 自動生成されたメソッド・スタブ
	return bookBorrowRepository.findAll();
}
}