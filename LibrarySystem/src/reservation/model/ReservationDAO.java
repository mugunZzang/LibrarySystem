package reservation.model;

import java.util.List;
import java.util.Map;

public interface ReservationDAO {
	
	// 기한 만기된 예약을 삭제한다.
	int deleteEndedReservation();
	
	
	// **** 로그인한 회원의 예약 목록 SELECT **** //
	   List<Map<String, String>> selectMyReservationList(int user_seq);

}
