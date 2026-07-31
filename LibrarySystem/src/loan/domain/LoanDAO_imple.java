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

public class LoanDAO_imple implements LoanDAO {

	
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
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 오늘 날짜로 생성된 대여목록이 있는지 검사하는 메소드
	@Override
	public String isExistTodayLoan() {
		String result = "";
		
		String sql = " SELECT LOAN_NO "
				+ " FROM TBL_LOAN "
				+ " WHERE TO_CHAR(LOAN_DATE, 'YYYYMMDD') = TO_CHAR(SYSDATE, 'YYYYMMDD') ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = String.valueOf(rs.getInt("LOAN_NO"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally {
			//ProjectDBConnection.closeConnection();
		}
		
		return result;
	}


	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	// 새로운 대여목록을 삽입하는 메소드
	@Override
	public int insertLoan(int userSeq, int librarianSeq) {
		int result = 0;
		
		String sql = " INSERT INTO TBL_LOAN(LOAN_NO, LIB_SEQ, USER_SEQ, LOAN_DATE, RETURN_DUE_DATE) "
				+ " VALUES(LOAN_NO.NEXTVAL, ?, ?, SYSDATE, SYSDATE + 7) ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, librarianSeq);
			pstmt.setInt(2, userSeq);
			
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		
		
		return result;
	}


	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// 가장 최근 대여목록의 대여번호 가져오기
	@Override
	public int getRecentLoanNo() {
		int result = 0;
		
		// 대여목록을 가져와서 대여일자순(최신순 = DESC)으로 정렬해서 가장 첫번째 행만 갖고오기
		String sql = " SELECT LOAN_NO "
				+ " FROM TBL_LOAN "
				+ " ORDER BY LOAN_DATE DESC "
				+ " FETCH FIRST 1 ROWS ONLY ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("LOAN_NO");
			}
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//ProjectDBConnection.closeConnection();
		}
		
		return result;
	}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//반납책 확인
	@Override
	public List<Map<String, String>> returnDate(int i) {
		List<Map<String, String>> returnList = new ArrayList<>();
        
        try {
              
             String sql = " select LOAN_DETAIL_NO, book_name, to_char(LOAN_DATE,'yyyy-mm-dd') as LOAN_DATE, to_char(RETURN_DUE_DATE, 'yyyy-mm-dd') as RETURN_DUE_DATE "
                      + " from VIEW_LOAN_BOOK "
                      + " where user_seq = to_number(?) and return = 0 ";
             
              
             pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, i);
             
             // sql문 실행
             rs = pstmt.executeQuery();
            
             while(rs.next()) {
               
              // 1. 행마다 새로운 Map 객체 생성
              Map<String, String> map = new HashMap<>();    
           
              // 2. RS에서 3개 컬럼 값을 가져와 Map 에 저장
                 map.put("LOAN_DETAIL_NO", String.valueOf(rs.getInt("LOAN_DETAIL_NO")));
                 map.put("book_name", rs.getString("book_name"));
                 map.put("LOAN_DATE", rs.getString("LOAN_DATE"));   
                 map.put("RETURN_DUE_DATE", rs.getString("RETURN_DUE_DATE"));

                 
                 // 3. list에 Map 추가
                 returnList.add(map);
                 
               }// end of while(rs.next())------------
             
             
            
      } catch (SQLException e) {
              e.printStackTrace();
              
        } finally {
           close();
           
        }
           

        return returnList;
	}



    // 반납 3일 추가 업데이트
	@Override
	public int returnAdd(String menuNo) {
		 int result = 0;
	       
	       try {
	          String sql = " UPDATE tbl_loan "
	          			 + " SET return_due_date = return_due_date + 3 "
	          		     + " WHERE loan_no = (select l.loan_no "
	          		     + "                from tbl_loan l join tbl_loan_detail ld "
	          		     + "                on l.loan_no = ld.loan_no "
	          		     + "                WHERE loan_detail_no = to_number(?)) ";
	          
	          pstmt = conn.prepareStatement(sql);
	          pstmt.setString(1, menuNo);
	          
	          
	          result = pstmt.executeUpdate(); // sql문 실행 
	          
	       } catch (SQLException e) {
	             e.printStackTrace();
	             
	       } finally {
	          close();
	       }
	       
	       return result;
	}



	// 반납이력
	@Override
	public List<Map<String, String>> returnHistory(int i) {
		List<Map<String, String>> historyList = new ArrayList<>();
        
        try {
              
             String sql = " select LOAN_DETAIL_NO, book_name "
                      + " from VIEW_LOAN_BOOK "
                      + " where user_seq = to_number(?) and "
                      + " return = 1 ";
             
              
             pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, i);
             
             // sql문 실행
             rs = pstmt.executeQuery();
            
             while(rs.next()) {
               
              // 1. 행마다 새로운 Map 객체 생성
              Map<String, String> map = new HashMap<>();    
           
              // 2. RS에서 3개 컬럼 값을 가져와 Map 에 저장
              map.put("LOAN_DETAIL_NO", rs.getString("LOAN_DETAIL_NO"));
              map.put("book_name", rs.getString("book_name"));


                 
              // 3. list에 Map 추가
              historyList.add(map);
                 
         }// end of while(rs.next())------------
             
             
            
      } catch (SQLException e) {
              e.printStackTrace();
              
        } finally {
           close();
           
        }
           

        return historyList;
	}



    // 사용자의 대출 이력 가져오는 메소드
	@Override
	public List<Map<String, String>> getloanList(int userSeq) {
		List<Map<String, String>> loanList = new ArrayList<>();
	    
	       try {
	             
	             String sql = " SELECT loan_detail_no, book_name, to_char(RETURN_DUE_DATE, 'yyyy-mm-dd') as RETURN_DUE_DATE "
	                        + " FROM VIEW_LOAN_BOOK "
	                         + " WHERE user_seq = TO_NUMBER(?) ";
	            
	             
	             pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, userSeq);
	            
	            // sql문 실행
	            rs = pstmt.executeQuery();
	           
	            while(rs.next()) {
	              
	             // 1. 행마다 새로운 Map 객체 생성
	             Map<String, String> map = new HashMap<>();    
	          
	             // 2. RS에서 3개 컬럼 값을 가져와 Map 에 저장
	                map.put("loan_detail_no", String.valueOf(rs.getInt("loan_detail_no")));
	                map.put("book_name", rs.getString("book_name"));
	                map.put("RETURN_DUE_DATE", rs.getString("RETURN_DUE_DATE"));   
	                
	                // 3. list에 Map 추가
	                loanList.add(map);
	                
	              }// end of while(rs.next())------------
	           
	     } catch (SQLException e) {
	             e.printStackTrace();
	             
	       } finally {
	          close();
	          
	       }
	          

	       return loanList;
	}
	
	
	
	
	
	
	
	

}
