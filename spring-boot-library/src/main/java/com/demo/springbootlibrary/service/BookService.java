package com.demo.springbootlibrary.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.springbootlibrary.dao.BookRepository;
import com.demo.springbootlibrary.dao.CheckoutRepository;
import com.demo.springbootlibrary.dao.HistoryRepository;
import com.demo.springbootlibrary.dao.PaymentRepository;
import com.demo.springbootlibrary.entity.Book;
import com.demo.springbootlibrary.entity.Checkout;
import com.demo.springbootlibrary.entity.History;
import com.demo.springbootlibrary.entity.Payment;
import com.demo.springbootlibrary.responseModel.ShelfCurrentLoansResponse;

@Service
@Transactional
public class BookService {

	private BookRepository bookRepository;
	private CheckoutRepository checkoutRepository;
	private HistoryRepository historyRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository,
			HistoryRepository historyRepository, PaymentRepository paymentRepository) {
		this.bookRepository = bookRepository;
		this.checkoutRepository = checkoutRepository;
		this.historyRepository = historyRepository;
		this.paymentRepository = paymentRepository;
	}

	public Book checkoutBook(String userEmail, Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);

		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
		if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
			throw new Exception("Book doesnt exist or already checked out by user");
		}

		List<Checkout> currentBooksCheckedOut = checkoutRepository.findBooksByUserEmail(userEmail);

		boolean booksNeedsReturned = false;

		booksNeedsReturned = currentBooksCheckedOut.stream()
				.anyMatch(checkout -> getDifferenceInTime(checkout.getReturnDate(), LocalDate.now().toString()) < 0);

//		for(Checkout currentBookCheckedOut : currentBooksCheckedOut) {
//			Long differenceInTime = getDifferenceInTime(currentBookCheckedOut.getReturnDate(), LocalDate.now().toString());
//			if(differenceInTime < 0) {
//				booksNeedsReturned = true;
//				break;
//			}
//		}

		Payment userPayment = paymentRepository.findByUserEmail(userEmail);

		if ((userPayment != null && userPayment.getAmount() > 0) || (userPayment != null && booksNeedsReturned)) {
			throw new Exception("Outstanding fees");
		}

		if (userPayment == null) {
			Payment payment = new Payment();
			payment.setAmount(0.00);
			payment.setUserEmail(userEmail);
			paymentRepository.save(payment);
		}

		book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
		bookRepository.save(book.get());

		Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(),
				book.get().getId());

		checkoutRepository.save(checkout);

		return book.get();

	}

	public Boolean isBookCheckoutByUser(String userEmail, Long bookId) {

		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
		if (validateCheckout != null) {
			return true;
		}
		return false;
	}

	public Integer currentLoansCount(String userEmail) {
		return checkoutRepository.findBooksByUserEmail(userEmail).size();
	}

	public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
		List<ShelfCurrentLoansResponse> currentLoansResponses = new ArrayList<>();

		List<Checkout> checkouts = checkoutRepository.findBooksByUserEmail(userEmail);
		List<Long> bookIdList = new ArrayList<Long>();

		checkouts.stream().map(Checkout::getBookId).forEach(bookIdList::add);

		List<Book> books = bookRepository.findAllById(bookIdList);

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Book book : books) {
			Optional<Checkout> checkout = checkouts.stream().filter(check -> check.getBookId() == book.getId())
					.findFirst();
			if (checkout.isPresent()) {

//				Date d1 = sdf.parse(checkout.get().getReturnDate());
//				Date d2 = sdf.parse(LocalDate.now().toString());
//				
//				TimeUnit timeUnit = TimeUnit.DAYS;

				long differenceInTime = getDifferenceInTime(checkout.get().getReturnDate(), LocalDate.now().toString());

				currentLoansResponses.add(new ShelfCurrentLoansResponse(book, differenceInTime));
			}
		}

		return currentLoansResponses;
	}

	private Long getDifferenceInTime(String startDate, String endDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date d1 = sdf.parse(startDate);
			Date d2 = sdf.parse(endDate);

			TimeUnit timeUnit = TimeUnit.DAYS;
			return timeUnit.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void returnBook(String userEmail, Long bookId) throws Exception {
		Optional<Book> book = bookRepository.findById(bookId);

		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

		if (!book.isPresent() || validateCheckout == null) {
			throw new Exception("Book doesnt exist or already checked out by user");
		}

		book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
		bookRepository.save(book.get());

		Long differenceInTime = getDifferenceInTime(validateCheckout.getReturnDate(), LocalDate.now().toString());

		if (differenceInTime < 0) {
			Payment payment = paymentRepository.findByUserEmail(userEmail);
			
			payment.setAmount(payment.getAmount() + (differenceInTime * -1));
			paymentRepository.save(payment);
		}

		checkoutRepository.delete(validateCheckout);

		History history = new History(book.get().getDescription(), book.get().getTitle(), book.get().getAuthor(),
				userEmail, validateCheckout.getCheckoutDate(), LocalDate.now().toString(), book.get().getImg());

		historyRepository.save(history);
	}

	public void renewLoan(String userEmail, Long bookId) throws Exception {

		Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
		if (validateCheckout == null) {
			throw new Exception("Book doesnt exist or already checked out by user");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date returnDate = sdf.parse(validateCheckout.getReturnDate());
		Date currentDate = sdf.parse(LocalDate.now().toString());

		if (returnDate.compareTo(currentDate) >= 0) {
			validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
			checkoutRepository.save(validateCheckout);
		}

	}
}
