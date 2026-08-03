package reservation.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbconnection.ProjectDBConnection;

public class ReservationDAO_imple implements ReservationDAO {

	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// === 자원반납을 해주는 메서드 === //
	private void close() {
		try {
			if(rs != null)    {rs.close();    rs = null;}
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()--------------
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 기한 만기된 예약을 삭제한다.
	@Override
	public int deleteEndedReservation() {
		int result = 0;
		
		String sql = " DELETE FROM TBL_RESERVATION "
				+ " WHERE RESV_ID IN ( "
				+ "    SELECT A.FK_RESV_ID "
				+ "    FROM VIEW_RESV_POSSIBLE_DATE V "
				+ "    JOIN TBL_RESV_DETAIL A ON V.RESV_DETAIL_ID = A.RESV_DETAIL_ID "
				+ "    WHERE TRUNC(SYSDATE) > TO_DATE(V.POSSIBLE_DATE, 'YYYY-MM-DD') + 1 "
				+ " ) ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// **** 로그인한 회원의 예약 목록 SELECT **** //
	@Override
	public List<Map<String, String>> selectMyReservationList(int user_seq) {
		List<Map<String, String>> resvList = new ArrayList<>();
	      
	      try {
	            
	            String sql = " SELECT resv_detail_id, book_name, resv_rank, resv_date, loan_status, possible_date "
	                   + " FROM VIEW_RESV_POSSIBLE_DATE "
	                   + " WHERE user_seq = ? ";
	       
	        
	       pstmt = conn.prepareStatement(sql);
	       pstmt.setInt(1, user_seq);
	       
	       // sql문 실행
	       rs = pstmt.executeQuery();
	      
	       while(rs.next()) {
	         
	        // 1. 행마다 새로운 Map 객체 생성
	        Map<String, String> map = new HashMap<>();    
	     
	        // 2. RS에서 3개 컬럼 값을 가져와 Map 에 저장
	           map.put("resv_detail_id", String.valueOf(rs.getInt("resv_detail_id")));
	           map.put("book_name",  rs.getString("book_name"));
	           map.put("resv_rank", String.valueOf(rs.getInt("resv_rank")) );   
	           map.put("resv_date", rs.getString("resv_date"));
	           map.put("loan_status", String.valueOf(rs.getInt("loan_status")));
	           map.put("possible_date", rs.getString("possible_date"));   
	           
	           // 3. list에 Map 추가
	           resvList.add(map);
	           
	         }// end of while(rs.next())------------
	          
	       } catch (SQLException e) {
	               e.printStackTrace();
	               
	         } finally {
	            close();
	            
	         }
	         
	
	      return resvList;
	}

}
