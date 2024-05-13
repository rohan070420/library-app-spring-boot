package com.demo.springbootlibrary.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.springbootlibrary.dao.BookRepository;
import com.demo.springbootlibrary.dao.CheckoutRepository;
import com.demo.springbootlibrary.dao.ReviewRepository;
import com.demo.springbootlibrary.entity.Book;
import com.demo.springbootlibrary.requestModels.AddBookRequest;

@Service
@Transactional
public class AdminService {

	private BookRepository bookRepository;
	private CheckoutRepository checkoutRepository;
	private ReviewRepository reviewRepository;
	
	@Autowired
	public AdminService(BookRepository bookRepository, CheckoutRepository checkoutRepository, ReviewRepository reviewRepository) {
		this.bookRepository = bookRepository;
		this.checkoutRepository = checkoutRepository;
		this.reviewRepository = reviewRepository;
	}
	
	public void postBook(AddBookRequest addBookRequest) {
		Book book = new Book();
		
		book.setTitle(addBookRequest.getTitle());
		book.setAuthor(addBookRequest.getAuthor());
		book.setDescription(addBookRequest.getDescription());
		book.setCopies(addBookRequest.getCopies());
		book.setCopiesAvailable(addBookRequest.getCopies());
		book.setCategory(addBookRequest.getCategory());
		book.setImg(addBookRequest.getImg());
		bookRepository.save(book);
	}
	
	public void increaseBookQuantity(Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);
		if(!book.isPresent()) {
			throw new Exception("Book is not present");
		}

		book.get().setCopies(book.get().getCopies() + 1);
		book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
		
		bookRepository.save(book.get());
	}
	
	public void decreaseBookQuantity(Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);
		if(!book.isPresent() || book.get().getCopies() <= 0 || book.get().getCopiesAvailable() <= 0 ) {
			throw new Exception("Book is not present or quantity locked");
		}

		book.get().setCopies(book.get().getCopies() - 1);
		book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
		
		bookRepository.save(book.get());
	}
	
	public void deleteBook(Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);
		if(!book.isPresent()) {
			throw new Exception("Book is not present");
		}
		
		checkoutRepository.deleteAllByBookById(bookId);
		reviewRepository.deleteAllReviewsByBookById(bookId);
		bookRepository.delete(book.get());
	}
}
