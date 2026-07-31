package reservation.model;

import java.util.List;
import java.util.Map;

public interface ResvDetailDAO {

	// 회원의 현재 예약권수 가져오기
	int getResvDetailCnt(int userSeq);

	// 해당 도서를 예약하고 있는 사람이 있는지 검색
	boolean isReserved(String bookId);

	// 예약상세목록 갖고오기( 예약번호, 예약상세번호, 회원id, 회원명, 도서id, 도서명 )
	// 단, 해당 예약의 도서 상태가 대출가능인 경우만을 갖고온다.
	// 또한, 각 도서별로 가장 먼저 예약된 건만 갖고온다.
	List<Map<String, String>> getResvDetailList();

	// 예약상세목록 삭제하기.
	int deleteResvDetail(String resvDetailId);


}
