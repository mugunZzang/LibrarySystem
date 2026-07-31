package book.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.domain.LoanBookDTO;
import dbconnection.ProjectDBConnection;

public class LoanBookDAO_imple implements LoanBookDAO {

	private Connection conn = ProjectDBConnection.getConn(); 
	private PreparedStatement pstmt;   
	private ResultSet rs;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 해당 도서가 존재하는지 검사하는 메소드
	@Override
	public boolean bookIdExist(String bookId) {
		boolean result = false;
		
		String sql = " SELECT COUNT(*) AS CNT "
				+ " FROM TBL_LOAN_BOOK "
				+ " WHERE BOOK_ID = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(bookId) );
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			if(rs.getInt("CNT") > 0) {
				result = true;
			}
			
			
		} catch (SQLException e) {
			System.out.println(">>> 해당 도서의 유무검증에 실패하였습니다.\n");
			e.printStackTrace();
			result = false;
		} finally {
			//ProjectDBConnection.closeConnection();
		}
		
		return result;
	}

	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// 해당 도서가 대출가능한 상태인지 검사하는 메소드
	@Override
	public boolean isEnableToLoan(String bookId) {
		boolean result = false;
		
		String sql = " SELECT LOAN_STATUS, BOOK_STATUS "
				+ " FROM TBL_LOAN_BOOK "
				+ " WHERE BOOK_ID = ? ";
		
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(bookId));
			
			rs = pstmt.executeQuery();
			
			rs.next();			
			
			// book_status는 무슨 값이 들어오나?
			int loanStatus = rs.getInt("LOAN_STATUS");			// 0(대출가능, 미대여중) 1(대출불가, 대출중)
			String bookStatus = rs.getString("BOOK_STATUS");	// 정상 파손 분실 폐기
			
			// 대출 가능한지 검사한다.
			if (loanStatus == 0 && bookStatus.equals("정상")) {
				result = true;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		//	ProjectDBConnection.closeConnection();
		}
		
		return result;
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 대여도서 업데이트 메소드 - 대출중으로 수정
	@Override
	public int updateBookStatus(String bookId) {
		int result = 0;
		
		String sql = " UPDATE TBL_LOAN_BOOK "
				+ " SET LOAN_STATUS = 1, BOOK_STATUS = '정상' "	// 1이 대여중
				+ " WHERE BOOK_ID = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(bookId));
			
			result = pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}finally {
		//	ProjectDBConnection.closeConnection();
		}
		
		return result;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	// isbn, 수량을 받아 대여도서 등록하기
	@Override
	public int insertLoanBook(int isbn, int cnt) {
		int result = 0;
		
		String sql = " INSERT INTO TBL_LOAN_BOOK (BOOK_ID, ISBN, LOAN_STATUS, BOOK_STATUS)"
				+ " VALUES (BOOK_ID.NEXTVAL, ?, 0, '정상') ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, isbn);
			
			for(int i = 0; i<cnt; i++) {
				
				if( pstmt.executeUpdate() == 1) {
					result++;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}
		
		
		
		
		return 0;
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 특정도서의 상태를 업데이트 하는 메소드
	@Override
	public int updateLoanBookStatus(String bookId, String status) {
		int result = 0;

        String sql = " UPDATE TBL_LOAN_BOOK "
                + " SET BOOK_STATUS = ? "
                + " WHERE BOOK_ID = ? ";

        try {

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, status);
            pstmt.setInt(2, Integer.parseInt(bookId));

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
        }

        return result;
	}



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 대여도서로부터 대여중이 아닌 도서만 갖고오기
	@Override
	public List<Map<String, String>> getLoanBooks() {
		List<Map<String, String>> result = new ArrayList<>();
		
		String sql = " SELECT A.BOOK_ID, B.BOOK_NAME, A.LOAN_STATUS, A.BOOK_STATUS "
				+ " FROM TBL_LOAN_BOOK A JOIN TBL_BOOK B "
				+ " ON A.ISBN = B.ISBN "
				+ " WHERE A.LOAN_STATUS = 0 ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String,String> map = new HashMap<>();
				
				map.put("BOOK_ID", String.valueOf(rs.getInt("BOOK_ID")));
				map.put("BOOK_NAME", rs.getString("BOOK_NAME"));
				map.put("LOAN_STATUS", String.valueOf(rs.getInt("LOAN_STATUS")));
				map.put("BOOK_STATUS", rs.getString("BOOK_STATUS"));
				
				result.add(map);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 대여도서 업데이트 메소드 - 대출중 아님으로 수정
	@Override
	public int updateBookStatus2(String bookId) {
		int result = 0;
		
		String sql = " UPDATE TBL_LOAN_BOOK "
				+ " SET LOAN_STATUS = 0, BOOK_STATUS = '정상' "	// 1이 대여중
				+ " WHERE BOOK_ID = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(bookId));
			
			result = pstmt.executeUpdate();
			
		}catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		} finally {
		//	ProjectDBConnection.closeConnection();
		}
		
		return result;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 정렬방식에 따른 모든 도서를 조회(select) 하는 메소드
	@Override
	public List<LoanBookDTO> showAllBooks(String sortChoice) {
		List<LoanBookDTO> loanBookList = null;
	      
		try {
	         String sql = " SELECT LB.BOOK_ID, B.BOOK_NAME, LB.ISBN "
	               + "    , CASE LB.LOAN_STATUS WHEN 0 THEN '대출 가능' ELSE '대출 불가' END AS LOAN_STATUS "
	               + "    , LB.BOOK_STATUS "
	               + " FROM TBL_LOAN_BOOK LB INNER JOIN TBL_BOOK B "
	               + " ON LB.ISBN = B.ISBN ";
	         
	         switch (sortChoice) {
	         case "1":   //1.도서명의 오름차순
	            sql += " order by B.BOOK_NAME asc ";
	            break;
	         case "2":   //2.도서명의 내림차순
	            sql += " order by B.BOOK_NAME desc ";
	            break;
	         case "3":   //3.도서ID의 오름차순
	            sql += " order by LB.BOOK_ID asc ";
	            break;
	         case "4":   //4.도서ID의 내림차순
	            sql += " order by LB.BOOK_ID desc ";
	            break;
	            
	         }   //end of switch (sortChoice)-------------
	               
	         pstmt = conn.prepareStatement(sql);   
	         
	         rs = pstmt.executeQuery(); // sql문 실행
	         
	         int cnt = 0;
	         while(rs.next()) {
	        	 cnt++;
	            
	            if(cnt == 1) {
	               loanBookList = new ArrayList<>();
	            }
	                        
	            LoanBookDTO loanBookDto = new LoanBookDTO();
	            
	            loanBookDto.setBook_id(rs.getInt("BOOK_ID"));
	            loanBookDto.setBook_name(rs.getString("BOOK_NAME"));
	            loanBookDto.setIsbn(rs.getInt("ISBN"));
	            loanBookDto.setLoan_status_kor(rs.getString("LOAN_STATUS"));
	            loanBookDto.setBook_status(rs.getString("BOOK_STATUS"));
	//	            mbrDto.setStatus(rs.getInt("status"));   
	            
	            loanBookList.add(loanBookDto);
	            
	            
	         }   // end of while(rs.next())----------------
	         
	     }catch(SQLException e) {
	         e.printStackTrace();
	     } 
      	 finally {
	       //  close();
	     }
	      
	     return loanBookList;          
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
