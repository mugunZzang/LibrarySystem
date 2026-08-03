package loan.domain;

import java.util.List;
import java.util.Map;

public interface LoanDAO {

	// 오늘 날짜로 생성된 대여목록이 있는지 검사하는 메소드
	String isExistTodayLoan();

	// 새로운 대여목록을 삽입하는 메소드
	int insertLoan(int userSeq, int librarianSeq);

	// 가장 최근 대여목록의 대여번호 가져오기
	int getRecentLoanNo();
	
	//-----------------------------------------------------
	
	// 반납책 확인
	   List<Map<String, String>> returnDate(int i);

    // 반납 3일 추가 1권일때
       int returnAdd(String menuNo);

   // 반납 3일 추가 2권일때
       int returnAdd2(String menuNo);


     // 반납 이력
       List<Map<String, String>> returnHistory(int i);
   
     // 사용자의 대출 이력 가져오는 메소드
       List<Map<String, String>> getloanList(int userSeq);

}
