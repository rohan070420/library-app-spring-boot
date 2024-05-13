package com.demo.springbootlibrary.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.springbootlibrary.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	Page<Review> findByBookId(@RequestParam("bookId") Long bookId, Pageable pageable);
	
	Review findByUserEmailAndBookId(String userEmail, Long bookId);
	
	@Modifying
	@Query(value = "DELETE FROM review c WHERE c.book_id =:book_id", nativeQuery = true)
	void deleteAllReviewsByBookById(@Param("book_id") Long bookId);
	

}
