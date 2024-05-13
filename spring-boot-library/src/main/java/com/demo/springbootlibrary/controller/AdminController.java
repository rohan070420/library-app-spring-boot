package com.demo.springbootlibrary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.springbootlibrary.requestModels.AddBookRequest;
import com.demo.springbootlibrary.service.AdminService;
import com.demo.springbootlibrary.utils.ExtractJwt;

@RestController
@CrossOrigin("https://localhost:3000")
@RequestMapping("/api/admin")
public class AdminController {

	private AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/secure/add/book")
	public void postBook(@RequestHeader(value = "Authorization") String token,
			@RequestBody AddBookRequest addBookRequest) throws Exception {
		String admin = ExtractJwt.payloadJWTExtraction(token, "userType");

		if (admin == null || !admin.equalsIgnoreCase("admin")) {
			throw new Exception("Admins Only");
		}

		adminService.postBook(addBookRequest);
	}

	@PutMapping("/secure/increase/book/quantity")
	public void increaseBookQuantity(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String admin = ExtractJwt.payloadJWTExtraction(token, "userType");

		if (admin == null || !admin.equalsIgnoreCase("admin")) {
			throw new Exception("Admins Only");
		}

		adminService.increaseBookQuantity(bookId);
	}

	@PutMapping("/secure/decrease/book/quantity")
	public void decreaseBookQuantity(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String admin = ExtractJwt.payloadJWTExtraction(token, "userType");

		if (admin == null || !admin.equalsIgnoreCase("admin")) {
			throw new Exception("Admins Only");
		}

		adminService.decreaseBookQuantity(bookId);
	}

	@DeleteMapping("/secure/delete/book")
	public void deleteBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId)
			throws Exception {
		String admin = ExtractJwt.payloadJWTExtraction(token, "userType");

		if (admin == null || !admin.equalsIgnoreCase("admin")) {
			throw new Exception("Admins Only");
		}

		adminService.deleteBook(bookId);
	}
}
