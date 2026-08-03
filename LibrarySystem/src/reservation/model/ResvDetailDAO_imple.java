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

public class ResvDetailDAO_imple implements ResvDetailDAO {

	
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
 

	@Override
	public int getResvDetailCnt(int userSeq) {
		
		int result = 0;
				
		String sql = "SELECT COUNT(*) AS CNT "
				+ " FROM TBL_LOAN_DETAIL A "
				+ " JOIN TBL_LOAN B ON A.LOAN_NO = B.LOAN_NO "
				+ " WHERE B.USER_SEQ = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, userSeq);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = rs.getInt("CNT");
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		
		return result;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

	// 해당 도서를 예약하고 있는 사람이 있는지 검사
	@Override
	public boolean isReserved(String bookId) {
		boolean result = false;
		
		String sql = " SELECT COUNT(*) AS CNT FROM TBL_RESV_DETAIL WHERE BOOK_ID = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(bookId));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int n = rs.getInt("CNT");
				if (n > 0)
					result = true;
			}
				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return result;
	}





	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// 예약상세목록 갖고오기( 예약번호, 예약상세번호, 회원id, 회원명, 도서id, 도서명 )
	// 단, 해당 예약의 도서 상태가 대출가능인 경우만을 갖고온다.
	// 또한, 각 도서별로 가장 먼저 예약된 건만 갖고온다.
	@Override
	public List<Map<String, String>> getResvDetailList() {
		List<Map<String, String>> resultList = new ArrayList<>();
		
		String sql = " SELECT "
				+ "    RESV_ID, "
				+ "    RESV_DETAIL_ID, "
				+ "    USER_SEQ, "
				+ "    USER_NAME, "
				+ "    BOOK_ID, "
				+ "    BOOK_NAME "
				+ " FROM ( "
				+ "    SELECT "
				+ "        R.RESV_ID AS RESV_ID, "
				+ "        RD.RESV_DETAIL_ID AS RESV_DETAIL_ID, "
				+ "        U.USER_SEQ AS USER_SEQ, "
				+ "        U.USER_NAME AS USER_NAME, "
				+ "        LB.BOOK_ID AS BOOK_ID, "
				+ "        B.BOOK_NAME AS BOOK_NAME, "
				+ "        ROW_NUMBER() OVER ( "
				+ "            PARTITION BY LB.BOOK_ID "
				+ "            ORDER BY R.RESV_DATE ASC, RD.RESV_DETAIL_ID ASC "
				+ "        ) AS RN "
				+ "    FROM TBL_RESERVATION R "
				+ "    JOIN TBL_RESV_DETAIL RD ON R.RESV_ID = RD.FK_RESV_ID "
				+ "    JOIN TBL_USER U ON R.FK_USER_SEQ = U.USER_SEQ "
				+ "    JOIN TBL_LOAN_BOOK LB ON RD.BOOK_ID = LB.BOOK_ID "
				+ "    JOIN TBL_BOOK B ON LB.ISBN = B.ISBN "
				+ "    WHERE LB.LOAN_STATUS = 0 "
				+ "      AND LB.BOOK_STATUS = '정상' "
				+ " ) "
				+ " WHERE RN = 1 ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				
				map.put("RESV_ID", String.valueOf(rs.getInt("RESV_ID")));
				map.put("RESV_DETAIL_ID", String.valueOf(rs.getInt("RESV_DETAIL_ID")));
				map.put("USER_SEQ", String.valueOf(rs.getInt("USER_SEQ")));
				map.put("USER_NAME", rs.getString("USER_NAME"));
				map.put("BOOK_ID", String.valueOf(rs.getInt("BOOK_ID")));
				map.put("BOOK_NAME", rs.getString("BOOK_NAME"));
				
				resultList.add(map);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return resultList;
	}


	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 예약상세목록 삭제하기
	@Override
	public int deleteResvDetail(String resvDetailId) {
		int result = 0;
		
		String sql = " DELETE FROM TBL_RESV_DETAIL "
				+ " WHERE RESV_DETAIL_ID = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(resvDetailId));
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}finally {
			close();
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	

	
	
}
