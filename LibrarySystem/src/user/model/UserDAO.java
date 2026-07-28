package user.model;
import java.util.List;
import book.domain.BookDTO;
import user.domain.UserDTO;


public interface UserDAO {

	 //****베스트 셀러 조회 메서드****//
	 List<BookDTO> bestSeller();
	
	 // **** 회원 회원가입을 해주는 메서드 **** //
	 int userRegister(UserDTO userDto);

	 // **** 사용자가 입력한 값이 존재하는 아이디인지 확인하는 메서드 **** //
	 boolean checkIdExists(String user_id);
	
	
	
	
	
	
	
	
	
	
	
	
}
