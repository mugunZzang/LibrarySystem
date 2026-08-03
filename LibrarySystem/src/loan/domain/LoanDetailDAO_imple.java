package loan.domain;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import dbconnection.ProjectDBConnection;

public class LoanDetailDAO_imple implements LoanDetailDAO {

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
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// DB로부터 대여상세번호, 회원id, 회원명, 도서id, 도서명을 가져오는 메소드
	@Override
	public List<Map<String, String>> getNoReturnedList() {
		
		List<Map<String, String>> resultList = new ArrayList<>();
		
		String sql = " SELECT A.LOAN_DETAIL_NO, B.USER_SEQ, C.USER_NAME, A.BOOK_ID, E.BOOK_NAME "
				+ " FROM "
				+ "    TBL_LOAN_DETAIL A JOIN TBL_LOAN B "
				+ "    ON A.LOAN_NO = B.LOAN_NO JOIN TBL_USER C "
				+ "    ON B.USER_SEQ = C.USER_SEQ JOIN TBL_LOAN_BOOK D "
				+ "    ON A.BOOK_ID = D.BOOK_ID JOIN TBL_BOOK E "
				+ "    ON D.ISBN = E.ISBN "
				+ " WHERE A.RETURN = 0 ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				Map<String, String> map = new HashMap<>();
				
				map.put("LOAN_DETAIL_NO", String.valueOf(rs.getInt("LOAN_DETAIL_NO")));
				map.put("USER_SEQ", String.valueOf(rs.getInt("USER_SEQ")));
				map.put("USER_NAME", rs.getString("USER_NAME"));
				map.put("BOOK_ID", String.valueOf(rs.getInt("BOOK_ID")));
				map.put("BOOK_NAME", rs.getString("BOOK_NAME"));
				
				resultList.add(map);
			}
			
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			close();
		}
		

		
		return resultList;
	}



	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// 반납처리하기 = 해당 도서id와 대여상세번호을 참조하여 대여상세테이블 및 대여도서테이블 수정하기
	@Override
	public int setReturnLoanDetail(String loanDetailNo) {
		
		int result = 0;
		// 성공시 1, 실패시 -1
		
		
		// 대여상세테이블 업데이트
		String sql = " UPDATE TBL_LOAN_DETAIL "
				+ " SET RETURN = 1 "
				+ " WHERE LOAN_DETAIL_NO = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(loanDetailNo));
			
			
			int n = pstmt.executeUpdate();
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = -1;
			
		} finally {
			
			close();
		}
		
		return result;
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 회원의 현재 대여권수 가져오는 메소드
	@Override
	public int getLoanDetailCnt(String userId) {
		
		int cnt = 0;
		
		String sql = " SELECT count(*) AS CNT "
				+ " FROM TBL_LOAN_DETAIL A JOIN TBL_LOAN B "
				+ " ON A.LOAN_NO = B.LOAN_NO JOIN TBL_USER C "
				+ " ON B.USER_SEQ = C.USER_SEQ "
				+ " WHERE C.USER_ID = ? AND A.RETURN = 0 ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			cnt = rs.getInt("CNT");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			cnt = -1;
		} finally {
			
			close();
		}
		
		
		return cnt;
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 해당 대여번호를 부모로 삼는 대여상세삽입
	@Override
	public int insertResvDetail(int loanNo, String bookId) {
		int result = 0;
		
		String sql = " INSERT INTO TBL_LOAN_DETAIL(LOAN_DETAIL_NO, LOAN_NO, BOOK_ID, RETURN) "
				+ " VALUES(LOAN_DETAIL_NO.NEXTVAL, ?, ?, 0) ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, loanNo);
			pstmt.setInt(2, Integer.parseInt(bookId));
			
			result = pstmt.executeUpdate();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}  finally {
			
			close();
		}
		
		return result;
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 해당 대여상세가 반납기한일로부터 얼마나 지났는지 구하기
	@Override
	public int getReturnDelayedDays(String loanDetailNo) {
		int result = 0;
		
		String sql = " SELECT "
				+ " CASE WHEN SYSDATE > B.RETURN_DUE_DATE "
				+ " THEN TRUNC(SYSDATE) - TO_DATE(B.RETURN_DUE_DATE, 'RR-MM-DD') "
				+ " ELSE 0 END AS OVERDUE_DAYS "
				+ " FROM "
				+ " TBL_LOAN_DETAIL A JOIN TBL_LOAN B "
				+ " ON A.LOAN_NO = B.LOAN_NO "
				+ " WHERE A.LOAN_DETAIL_NO = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(loanDetailNo));
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			result = rs.getInt("OVERDUE_DAYS");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			close();
		}
		
		return result;
	}



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 해당 대여상세가 어느 회원의 대여인지 구하기
	@Override
	public int getUserSeqFromLoanDetail(String loanDetailNo) {
		int result = 0;
		
		String sql = " SELECT A.USER_SEQ "
				+ " FROM TBL_LOAN A JOIN TBL_LOAN_DETAIL B "
				+ " ON A.LOAN_NO = B.LOAN_NO "
				+ " WHERE B.LOAN_DETAIL_NO = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			
			
			
			pstmt.setInt(1, Integer.parseInt(loanDetailNo));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("USER_SEQ");
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}finally {
			
			close();
		}
		
		return result;
	}
	
	
	
	
}
