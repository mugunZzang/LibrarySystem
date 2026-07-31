package loan.domain;

import java.util.List;
import java.util.Map;

public interface LoanDetailDAO {

	
	
	
	// DB로부터 대여상세번호, 회원id, 회원명, 도서id, 도서명을 가져오는 메소드
	List<Map<String, String>> getNoReturnedList();

	
	// 반납처리하기 = 해당 도서id와 대여상세번호을 참조하여 대여상세테이블 및 대여도서테이블 수정하기
	int setReturnLoanDetail(String bookId);

	// 회원의 현재 대여권수 가져오는 메소드
	int getLoanDetailCnt(String userId);

	// 해당 대여번호를 부모로 삼는 대여상세삽입
	int insertResvDetail(int loanNo, String bookId);

	// 해당 대여상세가 반납기한일로부터 얼마나 지났는지 구하기
	int getReturnDelayedDays(String loanDetailNo);


	// 해당 대여상세가 어느 회원의 대여인지 회원번호 구하기 
	int getUserSeqFromLoanDetail(String loanDetailNo);

}
