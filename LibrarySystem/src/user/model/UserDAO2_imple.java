package user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbconnection.ProjectDBConnection;

public class UserDAO2_imple implements UserDAO2 {

	 // field
	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 해당 회원이 연체료미납 또는 대출정지 상태인지 검사 메소드
	@Override
	public boolean userIsBanned(String userId) {
		
		boolean result = false;
		
		String sql = " SELECT OVERDUE_FEE, LOAN_STOP "
				+ " FROM TBL_USER "
				+ " WHERE USER_ID = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			if(rs.getInt("OVERDUE_FEE") == 0) {
				result = true;
			}
			
			if(rs.getInt("LOAN_STOP") == 0) {
				result = true;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return result;
	}




	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public int getUserSeqById(String userId) {
		int result = 0;
		
		String sql = " SELECT USER_SEQ "
				+ " FROM TBL_USER "
				+ " WHERE USER_ID = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt("USER_SEQ");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ProjectDBConnection.closeConnection();
		}
		
		
		
		return result;
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	


	// 반납시에 연체되었다면 연체료를 부과하는 메소드
	@Override
	public int setUserOverDueFee(int userSeq, int delayedDays) {
		int result = 0;
		
		String sql = " UPDATE TBL_USER SET OVERDUE_FEE = OVERDUE_FEE + (? * 100) "
				+ " WHERE USER_SEQ = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, userSeq);
			pstmt.setInt(2, delayedDays);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		
		return result;
	}



	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	

	// 회원번호로 회원id 찾기
	@Override
	public String getUserIdBySeq(String userSeq) {
		String result = "";
		
		String sql = " SELECT USER_ID "
				+ " FROM TBL_USER "
				+ " WHERE USER_SEQ = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(userSeq));
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getString("USER_ID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ProjectDBConnection.closeConnection();
		}
		
		
		
		return result;
	}

	
}
