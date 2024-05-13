package com.demo.springbootlibrary.responseModel;

import com.demo.springbootlibrary.entity.Book;

import lombok.Data;

@Data
public class ShelfCurrentLoansResponse {

	private Book book;
	
	private Long daysLeft;
	
	public ShelfCurrentLoansResponse(Book book, Long daysLeft) {
		this.book = book;
		this.daysLeft = daysLeft;
	}
}
