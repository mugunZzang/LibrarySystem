package user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import book.domain.BookDTO;
import dbconnection.ProjectDBConnection;
import user.domain.UserDTO;


public class UserDAO_imple implements UserDAO {

	
	    // field
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
	
	
		// **** 회원 회원가입을 해주는 메서드 **** //
		   @Override
		   public int userRegister(UserDTO userDto) {
		      int result = 0;
		      
		      try {
		         
		         // SQL 문 작성
		         String sql = " INSERT INTO tbl_user(user_seq, user_id, user_pw, user_name, user_tel, user_email) " 
		                  + " values(user_seq.nextval, ?, ?, ?, ?, ?) " ;

		         // 연결한 DB에 SQL문 제작 후 전달
		         pstmt = conn.prepareStatement(sql);
		         pstmt.setString(1, userDto.getId());
		         pstmt.setString(2, userDto.getPw());
		         pstmt.setString(3, userDto.getName());
		         pstmt.setString(4, userDto.getTel());
		         pstmt.setString(5, userDto.getEmail());
		         
		         // 5. 실행
		         result = pstmt.executeUpdate();    // SQL 문 실행
		         
		         
		         sql = " INSERT INTO tbl_user_login(user_seq, user_id, user_pw, user_name) " 
		            + " values(user_seq.CURRVAL, ?, ?, ?) " ; 
		            
		            // 연결한 DB에 SQL문 제작 후 전달
		            pstmt = conn.prepareStatement(sql);
		            pstmt.setString(1, userDto.getId());
		            pstmt.setString(2, userDto.getPw());
		            pstmt.setString(3, userDto.getName());
		            
		            result = pstmt.executeUpdate();    // SQL 문 실행
		            
		      } catch(SQLException e) {
		         e.printStackTrace();
		         
		      } finally {
		         close();
		         
		      }
		      return result;
		      
		   } // end of public int userRegister(UserDTO userDto)---------

		   
		   
		   // **** 사용자가 입력한 값이 존재하는 아이디인지 확인하는 메서드 **** //
		   @Override
		   public boolean checkIdExists(String user_id) {
		      boolean result = false;
		      
		      try {      
		         String sql = " SELECT * "
		                   + " FROM tbl_user_login "
		                   + " WHERE user_id = ? ";
		      

		         // 연결한 오라클 서버에 우편배달부 생성 후 내 SQL 문 전달
		         pstmt = conn.prepareStatement(sql);
		         pstmt.setString(1, user_id);
		            
		         // 실행
		         rs = pstmt.executeQuery();    // SQL 문 실행
		         
		         // 만약 해당 아이디가 존재한다면 
		         if(rs.next()) {
		            result = true;
		         }
		      }  catch(SQLException e) {
		         e.printStackTrace();
		         
		      } finally {
		         // >>> 사용하였던 자원을 반납하기 <<<
		         close();
		      }
		      
		      return result;

		   } // end of public boolean checkIdExists(String user_id)


		

         
		
		// === 베스트셀러 조회 메서드=== //
		
		@Override
		public List<BookDTO> bestSeller() {

		    List<BookDTO> list = new ArrayList<>();

		    try {  // 베스트셀러 조회 sql문 
		        String sql = " SELECT * "
			        		+ " FROM ( "
			        		+ " SELECT "
			        		+ " DENSE_RANK() OVER(ORDER BY COUNT(*) DESC) AS ranking , "
			        		+ " b.isbn,"
			        		+ " b.book_name, "
			        		+ " b.author, "
			        		+ " b.publisher, "
			        		+ " b.pub_year, "
			        		+ " COUNT(*) AS loan_count "
			        		+ " FROM loan_detail a "
			        		+ " JOIN loan_book c "
			        		+ " ON a.book_id = c.book_id "
			        		+ " JOIN book b "
			        		+ " ON c.isbn = b.isbn "
			        		+ " GROUP BY "
			        		+ " b.isbn, "
			        		+ " b.book_name, "
			        		+ " b.author, "
			        		+ " b.publisher, "
			        		+ " b.pub_year "
			        		+ " ) "
			        		+ " WHERE rank <= 20 "
			        		+ " ORDER BY ranking ";
		        		
		        pstmt = conn.prepareStatement(sql);
		        rs = pstmt.executeQuery();

		        while(rs.next()) {

		            BookDTO dto = new BookDTO();
		            
		            dto.setIsbn(rs.getInt("isbn"));
		            dto.setBook_name(rs.getString("book_name"));
		            dto.setAuthor(rs.getString("author"));
		            dto.setPublisher(rs.getString("publisher"));
		            dto.setPub_year(rs.getString("pub_year"));

		            list.add(dto);
		        } //end of while ----------------

		    } catch(SQLException e) {
		        e.printStackTrace();
		    } finally {
		        close();
		    }

		    return list;
		}// end of public List<BookDTO> bestSeller() ------------------
		
		
		
	
	
} //end of public class UserDAO_imple implements UserDAO
