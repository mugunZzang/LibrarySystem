package user.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.day04.dbconnection.MyDBConnection;

public class UserDAO_imple implements UserDAO {

	
	    // field
		private Connection conn = MyDBConnection.getConn();
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
	
	
	
		
		// === 베스트셀러 조회 메서드=== //
		
		@Override
		public List<BookDTO> bestSeller() {

		    List<BookDTO> list = new ArrayList<>();

		    try {
		        String sql = " SELECT * "
			        		+ " FROM ( "
			        		+ " SELECT "
			        		+ " DENSE_RANK() OVER(ORDER BY COUNT(*) DESC) AS rank, "
			        		+ " b.isbn,"
			        		+ " b.book_name, "
			        		+ " b.author, "
			        		+ " b.publisher, "
			        		+ " b.pub_year "
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
			        		+ " b.publisher "
			        		+ " b.pub_year "
			        		+ " ) "
			        		+ " WHERE rank <= 20 " ;
		        		
		        pstmt = conn.prepareStatement(sql);
		        rs = pstmt.executeQuery();

		        while(rs.next()) {

		            BookDTO dto = new BookDTO();
		            
		            dto.setISBN(rs.getInt("isbn"));
		            dto.setBook_name(rs.getString("book_name"));
		            dto.setAuthor(rs.getString("author"));
		            dto.setPublisher(rs.getString("publisher"));
		            dto.setPub_year(rs.getString("pub_year"));

		            list.add(dto);
		        }

		    } catch(SQLException e) {
		        e.printStackTrace();
		    } finally {
		        close();
		    }

		    return list;
		}
		
		
		
		
		
		
		
	
	
	
	
	
} //end of public class UserDAO_imple implements UserDAO
