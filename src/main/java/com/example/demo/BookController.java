package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController{

@Autowired
private BookService bookService;

//トップページ
@RequestMapping("/")
public ModelAndView books(ModelAndView mv) {
	List<Book> allbooks = bookService.getAllData();
	mv.addObject("books", allbooks);
	mv.setViewName("book_search");
	return mv;
}

//書籍の検索
@RequestMapping(value="/search",method = RequestMethod.GET)
public ModelAndView search(@RequestParam(name = "id",required = false)String id,ModelAndView mv) { 
List<Book> bookList = bookService.searchBooks(id);
mv.addObject("books",bookList);
mv.setViewName("book_search");
return mv;
    
}

@RequestMapping(value="/search",method = RequestMethod.POST)
public ModelAndView searchBook(@RequestParam(name = "id",required = false)String id,ModelAndView mv){
	List<Book> bookList = bookService.searchBooks(id);
	mv.addObject("books",bookList);
	mv.setViewName("book_borrow");
	return mv;
}
  
//書籍の編集画面に移動
@RequestMapping(value = "/edit", method = RequestMethod.GET)
public ModelAndView edit(@RequestParam(name = "id")int id,ModelAndView mv) {
	Book book = bookService.getBookById(id);
	mv.addObject("book",book);
	mv.addObject("status",book.getStatus());
    mv.setViewName("book_edit");
    return mv;
}
//書籍を編集
@RequestMapping(value = "/update", method = RequestMethod.POST)
public String update(@ModelAttribute("book") Book updatedbook, ModelAndView mv) { 
	Book existingBook = bookService.getBookById(updatedbook.getId());
    existingBook.setTitle(updatedbook.getTitle());
    existingBook.setAuthor(updatedbook.getAuthor());
    existingBook.setIsbn(updatedbook.getIsbn());
    existingBook.setStatus(updatedbook.getStatus());

bookService.saveBook(existingBook);//更新された情報をDBに保存

return "redirect:/";
}

//書籍の削除画面に移動
@RequestMapping(value = "/delete",method = RequestMethod.GET)
public ModelAndView delete(@RequestParam(name = "id")int id,ModelAndView mv) {
  Book book =bookService.getBookById(id)	;
  mv.addObject("book",book);
  mv.addObject("status",book.getStatus());
  mv.setViewName("book_delete");
  return mv;
}
//書籍を削除
@RequestMapping(value = "/delete",method = RequestMethod.POST)
public String deleteBook(@RequestParam(name = "id")int id) {
	bookService.deleteBookById(id);
	return "redirect:/";
}
//新規登録画面に移動
@RequestMapping(value = "/add",method = RequestMethod.GET)
public ModelAndView add(ModelAndView mv) {
	  Book book =new Book();
	  mv.addObject("book",book);
	  mv.setViewName("book_add");
	  return mv;
}
//書籍を新規登録
@RequestMapping(value = "/add",method = RequestMethod.POST)
public String addBook(@RequestParam(name = "title")String title,
        @RequestParam(name = "author")String author,
        @RequestParam(name = "isbn")int isbn,
        @RequestParam(name = "status")String status){
      bookService.addBookById(title,author,isbn,status);
return "redirect:/";
}

//書籍を借りる・返却するページへ移動
@RequestMapping(value = "/borrow",method = RequestMethod.GET)
public ModelAndView borrow(ModelAndView mv) {
	List<Book> allBooks = bookService.getAllData();
	mv.addObject("books",allBooks);
	mv.setViewName("book_borrow");
	return mv;
}
//借りる書籍の確認画面へ移動
@RequestMapping(value = "/borrowing", method = RequestMethod.GET)
public ModelAndView borrowing(@RequestParam(name = "id") int id, ModelAndView mv) {
    Book book = bookService.getBookById(id);
    mv.addObject("book", book);
    mv.setViewName("borrowing");
    return mv;
}
//ステータスをBORROWEDに変更
@RequestMapping(value = "/confirm-borrow", method = RequestMethod.POST)
public String confirmBorrow(@RequestParam(name = "id") int id) {
    bookService.borrowBook(id);
    return "redirect:/borrow";

}
//返却書籍の確認画面へ移動
@RequestMapping(value = "/return", method = RequestMethod.GET)
public ModelAndView bookReturn(@RequestParam(name = "id") int id, ModelAndView mv) {
    Book book = bookService.getBookById(id);
    mv.addObject("book", book);
    mv.setViewName("return");
    return mv;
}
//ステータスをAVAILABLEに変更
@RequestMapping(value = "/confirm-return", method = RequestMethod.POST)
public String confirmReturn(@RequestParam(name = "id") int id) {
    bookService.returnBook(id);
    return "redirect:/borrow";
}

//貸出履歴のページへ移動
@RequestMapping(value = "/history", method = RequestMethod.GET)
public ModelAndView bookHistory(ModelAndView mv) {
	List<BookBorrowing>bookHistory = bookService.getBookHistory();
	mv.addObject("bookHistory",bookHistory);
	mv.setViewName("bookhistory");
	return mv;
}


//該当書籍がない場合にエラーページへ移動
 @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
            ModelAndView modelAndView = new ModelAndView("error_page"); // エラーページの指定
            modelAndView.addObject("errorMessage", ex.getMessage()); // エラーメッセージを追加
            return modelAndView;
        }

 }
}

