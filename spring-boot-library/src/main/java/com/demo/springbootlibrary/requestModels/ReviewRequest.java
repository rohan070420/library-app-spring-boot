package com.demo.springbootlibrary.requestModels;

import java.util.Optional;

import lombok.Data;

@Data
public class ReviewRequest {

	private double rating;
	
	private Long bookId;
	
	private Optional<String> reviewDescription;
}
