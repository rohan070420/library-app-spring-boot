package com.demo.springbootlibrary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.springbootlibrary.entity.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long>{

	Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

	List<Checkout> findBooksByUserEmail(String userEmail);
	
	@Modifying
	@Query(value = "DELETE FROM checkout c WHERE c.book_id =:book_id", nativeQuery = true)
	void deleteAllByBookById(@Param("book_id") Long bookId);
	
}
