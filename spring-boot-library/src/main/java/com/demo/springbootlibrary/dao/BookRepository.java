package com.demo.springbootlibrary.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.springbootlibrary.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);
	
	Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);

	@Query("SELECT b FROM Book b WHERE b.id IN :bookId")
	List<Book> findBooksByIds(@Param("bookId") List<Long> bookId);

}
