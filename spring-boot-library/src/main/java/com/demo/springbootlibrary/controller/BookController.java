package com.demo.springbootlibrary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.springbootlibrary.entity.Book;
import com.demo.springbootlibrary.errorResponse.CustomErrorResponse;
import com.demo.springbootlibrary.responseModel.ShelfCurrentLoansResponse;
import com.demo.springbootlibrary.service.BookService;
import com.demo.springbootlibrary.utils.ExtractJwt;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PutMapping("/secure/checkout")
	public ResponseEntity<?> checkoutBook(@RequestHeader(value = "Authorization") String token,
			@RequestParam Long bookId) throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		try {
			Book book = bookService.checkoutBook(userEmail, bookId);
			return ResponseEntity.ok(book);
		} catch (Exception e) {
			CustomErrorResponse errorResponse = new CustomErrorResponse();
			errorResponse.setMessage("An error occurred: " + e.getMessage());
			errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // or set a default book if needed
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/secure/ischeckedout/byuser")
	public Boolean isBookCheckoutByUser(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		return bookService.isBookCheckoutByUser(userEmail, bookId);
	}

	@GetMapping("/secure/currentLoans/count")
	public Integer currentLoansCount(@RequestHeader(value = "Authorization") String token) throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		return bookService.currentLoansCount(userEmail);
	}

	@GetMapping("/secure/currentLoans")
	public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)
			throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		return bookService.currentLoans(userEmail);
	}

	@PutMapping("/secure/returnBook")
	public void returnBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		bookService.returnBook(userEmail, bookId);
	}

	@PutMapping("/secure/renewLoan")
	public void renewLoan(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		bookService.renewLoan(userEmail, bookId);
	}
}
