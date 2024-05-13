package com.demo.springbootlibrary.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "review")
@Data
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "user_email")
	private String userEmail;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "rating")
	private double rating;
	
	@Column(name = "book_id")
	private long bookId;
	
	@Column(name = "review_description")
	private String reviewDescription;

}
