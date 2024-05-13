package com.demo.springbootlibrary.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.springbootlibrary.requestModels.ReviewRequest;
import com.demo.springbootlibrary.service.ReviewService;
import com.demo.springbootlibrary.utils.ExtractJwt;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

	private ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping("/secure/postReview")
	public void postReview(@RequestHeader(value = "Authorization") String token,
			@RequestBody ReviewRequest reviewRequest) throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		if (userEmail == null) {
			throw new Exception("User email is missing");
		}
		reviewService.postReview(userEmail, reviewRequest);
	}

	@GetMapping("/secure/user/book")
	public boolean userReviewListed(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		return reviewService.userReviewListed(userEmail, bookId);
	}
}
