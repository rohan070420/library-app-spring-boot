package com.demo.springbootlibrary.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.springbootlibrary.dao.MessageRepository;
import com.demo.springbootlibrary.entity.Message;
import com.demo.springbootlibrary.requestModels.AdminQuestionRequest;

@Service
@Transactional
public class MessagesService {

	private MessageRepository messageRepository;

	@Autowired
	public MessagesService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	
	public void postMessage(Message messageRequest, String userEmail) {
		Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
		
		message.setUserEmail(userEmail);
		
		messageRepository.save(message);
	}
	
	public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception{
		Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());
		if(!message.isPresent()) {
			throw new Exception("Message not found");
		}
		
		message.get().setAdminEmail(userEmail);
		message.get().setResponse(adminQuestionRequest.getResponse());
		message.get().setClosed(true);
		
		messageRepository.save(message.get());
	}
}
