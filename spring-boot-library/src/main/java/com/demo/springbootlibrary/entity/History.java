package com.demo.springbootlibrary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "history")
@Data
public class History {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "user_email")
	private String userEmail;
	
	@Column(name = "checkout_date")
	private String checkoutDate;
	
	@Column(name = "returned_date")
	private String returnedDate;
	
	@Column(name = "img")
	private String img;

	/**
	 * @param description
	 * @param title
	 * @param author
	 * @param userEmail
	 * @param checkoutDate
	 * @param returnDate
	 * @param img
	 */
	public History(String description, String title, String author, String userEmail, String checkoutDate,
			String returnDate, String img) {
		this.description = description;
		this.title = title;
		this.author = author;
		this.userEmail = userEmail;
		this.checkoutDate = checkoutDate;
		this.returnedDate = returnDate;
		this.img = img;
	}
	
	public History() {}

}
