package book.model;

import java.util.List;

import book.domain.WishBookDTO;

public interface WishBookDAO {

	// 정렬방식에 따른 모든 희망도서를 조회(select) 해주는 메서드
	List<WishBookDTO> showAllWishBooks(String sortChoice);

}
