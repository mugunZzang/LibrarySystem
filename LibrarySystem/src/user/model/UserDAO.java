package user.model;
import java.util.List;
import java.util.Map;

import book.domain.BookDTO;
import book.domain.WishBookDTO;
import user.domain.UserDTO;


public interface UserDAO {

	 
	
	 // **** 회원 회원가입을 해주는 메서드 **** //
	 int userRegister(UserDTO userDto);

	 // **** 사용자가 입력한 값이 존재하는 아이디인지 확인하는 메서드 **** //
	 boolean checkIdExists(String user_id);
	
	 
	 //****베스트 셀러 조회 메서드****//
	 List<BookDTO> bestSeller();
	
	 // ****도서 검색 메서드****//
	 List<BookDTO> searchBook(String type,String keyword);
	  
	 //****도서 예약 메서드****//
	 int reservation(int user_seq, int isbn);
	
	 //****관심 도서 등록 메서드****//
	 int favorite(int user_seq, int isbn);
	 
     
	
	 // 정렬방식에 따른 모든 회원을 조회(select) 해주는 메서드
	 List<UserDTO> showAllMember(String sortChoice);	
	 
	 // **** 회원번호가 존재하는지 확인 ****
	 boolean isExistenceUserSeq(String userSeq);

	 // **** 회원을 대출정지 하는 메서드 ****	
	 int loanStop(String userSeq);
	 
	 // 회원 로그인
	 UserDTO userLogin(Map<String, String> paraMap);
	 
      // ****회원의 대여 횟수 조회하는 메서드****//
	 int getLoanCount(int user_seq);
	 
      //****회원의 예약 횟수 조회하는 메서드****//
	 int getReservationCount(int user_seq);
	 
	  // 해당 회원이 연체료미납 또는 대출정지 상태인지 검사 메소드
		boolean userIsBanned(String userId);

		// 회원id로 회원번호를 찾는 메소드
		int getUserSeqById(String userId);

		// 반납시에 연체되었다면 연체료를 부과하는 메소드
		int setUserOverDueFee(int userSeq, int delayedDays);

		// 회원번호로 회원id를 찾는 메소드
		String getUserIdBySeq(String userSeq);
		
		//---------------------------------------------------



			
	       //****희망도서 신청 메서드****//
			int requestWishBook(int user_seq, WishBookDTO dto);

			// 연체료납부 메소드
			int payOverdueFee(Map<String, Object> map);
			
			//포인트충전
			int addPoint(UserDTO loginUserDto);
		
		    // tbl_user 테이블에서 정보 가져오기
		    UserDTO myInfo(String id);
		   
		    
		   // 내 정보를 수정(UPDATE)해주는 메서드
		    int updateMyinfo(Map<String, String> paraMap);

			
			
	     

			// 특정 회원의 포인트 가져오기
			int getUserPoint(int userSeq);

			// 대출처리 시 대여료 차감시키기
			int minusToPoint(int bookRentFee, int userSeq);
	
	
	
}
