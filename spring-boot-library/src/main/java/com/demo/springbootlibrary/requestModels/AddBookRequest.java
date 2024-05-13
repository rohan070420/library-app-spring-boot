package com.demo.springbootlibrary.requestModels;

import lombok.Data;

@Data
public class AddBookRequest {

	private String title;
	private String author;
	private String description;
	private String category;
	private String img;
	private int copies;
}
