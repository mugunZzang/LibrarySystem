package user.model;
import java.util.List;
import book.domain.BookDTO;
import user.domain.UserDTO;


public interface UserDAO2 {

	// 해당 회원이 연체료미납 또는 대출정지 상태인지 검사 메소드
	boolean userIsBanned(String userId);

	// 회원id로 회원번호를 찾는 메소드
	int getUserSeqById(String userId);

	// 반납시에 연체되었다면 연체료를 부과하는 메소드
	int setUserOverDueFee(int userSeq, int delayedDays);

	// 회원번호로 회원id를 찾는 메소드
	String getUserIdBySeq(String userSeq);

	 
	
	
	
	
	
	
	
	
	
	
}
