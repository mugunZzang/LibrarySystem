package book.model;

import java.util.List;
import java.util.Map;

import book.domain.LoanBookDTO;

public interface LoanBookDAO {

	// 해당 도서가 존재하는지 검사하는 메소드
	boolean bookIdExist(String bookId);

	// 해당 도서가 대출가능 상태인지 검사하는 메소드
	boolean isEnableToLoan(String bookId);

	// 대여도서 업데이트 메소드 - 대출중으로 수정
	int updateBookStatus(String bookId);
	
	// 대여도서 업데이트 메소드 - 대출중 아님으로 수정
	int updateBookStatus2(String bookId);

	// isbn, 수량을 받아 대여도서 등록하기
	int insertLoanBook(int isbn, int cnt);
	
	// 특정 도서의 상태를 업데이트하는 메소드
	int updateLoanBookStatus(String bookId, String status);

	// 대여도서로부터 대여중이 아닌 도서만 갖고오기
	List<Map<String, String>> getLoanBooks();

	// 정렬방식에 따른 모든 도서를 조회(select) 하는 메소드
	List<LoanBookDTO> showAllBooks(String sortChoice);

	

	

}
