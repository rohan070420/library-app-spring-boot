package com.demo.springbootlibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.springbootlibrary.entity.Message;
import com.demo.springbootlibrary.requestModels.AdminQuestionRequest;
import com.demo.springbootlibrary.service.MessagesService;
import com.demo.springbootlibrary.utils.ExtractJwt;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

	private MessagesService messagesServices;

	@Autowired
	public MessageController(MessagesService messagesServices) {
		this.messagesServices = messagesServices;
	}

	@PostMapping("/secure/add/messages")
	public void postMessage(@RequestHeader("Authorization") String token, @RequestBody Message messageRequest) {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		messagesServices.postMessage(messageRequest, userEmail);
	}

	@PutMapping("/secure/admin/message")
	public void putMessage(@RequestHeader("Authorization") String token,
			@RequestBody AdminQuestionRequest adminQuestionRequest) throws Exception {
		String userEmail = ExtractJwt.payloadJWTExtraction(token, "sub");
		String admin = ExtractJwt.payloadJWTExtraction(token, "userType");

		if (admin == null || !admin.equalsIgnoreCase("admin")) {
			throw new Exception("Admins Only");
		}

		messagesServices.putMessage(adminQuestionRequest, userEmail);
	}
}
