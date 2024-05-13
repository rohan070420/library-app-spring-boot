package com.demo.springbootlibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.springbootlibrary.requestModels.PaymentInfoRequest;
import com.demo.springbootlibrary.service.PaymentService;
import com.demo.springbootlibrary.utils.ExtractJwt;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	private PaymentService paymentService;
	
	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@PostMapping("/secure/payment-intent")
	public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest) throws StripeException{
		PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
		String paymentIntentJson = paymentIntent.toJson();
		return new ResponseEntity<String> (paymentIntentJson, HttpStatus.OK);
	}
	
	@PutMapping("/secure/payment-complete")
	public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value="Authorization") String token) throws Exception{
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		if(userEmail==null) {
			throw new Exception("User email is missing");
		}
		
		return paymentService.stripePayment(userEmail);
	}
	
}
