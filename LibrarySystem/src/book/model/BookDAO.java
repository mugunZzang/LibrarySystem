package book.model;

import java.util.List;
import java.util.Map;

import book.domain.BookDTO;
import user.domain.UserDTO;

public interface BookDAO {

	// 책정보 삽입하기
	int insertBook(BookDTO bookDTO);
	
	// ISBN 가져오기
	int getIsbn();

	// 모든 도서목록 가져오기
	List<BookDTO> getAllBooks();

	// 해당 도서 삭제하기
	int deleteBook(int isbn);

	// isbn에 해당하는 도서가 있는지 검사
	boolean isExistBook(int isbn);

	// isbn에 해당하는 도서의 정보 갖고오기
	BookDTO getBookInfo(int isbn);

	// 특정 도서의 정보 수정하기
	int updateBookInfo(BookDTO bookDto);
	
	//------------------------------------------
	
	
   // 관심도서 조회
   List<BookDTO> bringBookList(List<BookDTO> favBookList, UserDTO loginUserDto);

   // 관심도서 취소
   int cancleFav(String isbnNo, UserDTO loginUserDto);
   
    // **** 모든 도서 조회 메서드 ****
    List<BookDTO> selectAllBook();
    
    // **** 관심도서 신청 메서드 **** //
    int favBookApply(int user_seq, int choice);
    
    // **** 관심 도서 조회용 메서드 ****
    List<Map<String, String>> getFavList(String id);

    // 도서의 대여료 가져오기
	int getRentFee(String bookId);


}
