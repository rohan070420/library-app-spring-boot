package com.demo.springbootlibrary.requestModels;

import lombok.Data;

@Data
public class PaymentInfoRequest {

	private int amount;
	private String receiptEmail;
	private String currency;
	
}
