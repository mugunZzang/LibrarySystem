package book.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import book.domain.WishBookDTO;
import dbconnection.ProjectDBConnection;

public class WishBookDAO_imple implements WishBookDAO {
	
	
	// field
	private Connection conn = ProjectDBConnection.getConn();
	private PreparedStatement pstmt;
	private ResultSet rs;
	

	//method
	// === 자원반납을 해주는 메서드 === //
	private void close() {
		try {
			if(rs != null)    {rs.close();    rs = null;}
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}// end of private void close()--------------

	
	// 정렬방식에 따른 모든 희망도서를 조회(select) 해주는 메서드
	@Override
	public List<WishBookDTO> showAllWishBooks(String sortChoice) {
		
		List<WishBookDTO> wishBookList = null;
		
		try {
			String sql = " select WISH_BOOK_NO, USER_SEQ, WISH_BOOK_NAME, WISH_BOOK_AUTHOR, WISH_BOOK_PUBLISHER "
					+ "    , to_char(REQUEST_DATE, 'yyyy-mm-dd') AS REQUEST_DATE "
					+ " from TBL_WISH_BOOK "; 
			
			switch (sortChoice) {
			case "1":	//1.도서명의 오름차순
				sql += " order by WISH_BOOK_NAME asc ";
				break;
			case "2":	//2.도서명의 내림차순
				sql += " order by WISH_BOOK_NAME desc ";
				break;
			case "3":	//3.신청일자의 오름차순
				sql += " order by REQUEST_DATE asc ";
				break;
			case "4":	//4.신청일자의 내림차순
				sql += " order by REQUEST_DATE desc ";
				break;
				
			}	//end of switch (sortChoice)-------------
					
			pstmt = conn.prepareStatement(sql);	
			
			rs = pstmt.executeQuery(); // sql문 실행
			
			int cnt = 0;
			while(rs.next()) {
				cnt++;
				
				if(cnt == 1) {
					wishBookList = new ArrayList<>();
				}
								
				WishBookDTO wishBookDto = new WishBookDTO();
				
				wishBookDto.setWish_book_no(rs.getInt("WISH_BOOK_NO"));
				wishBookDto.setUser_seq(rs.getInt("USER_SEQ"));
				wishBookDto.setWish_book_name(rs.getString("WISH_BOOK_NAME"));
				wishBookDto.setWish_book_author(rs.getString("WISH_BOOK_AUTHOR"));
				wishBookDto.setWish_book_publisher(rs.getString("WISH_BOOK_PUBLISHER"));
				wishBookDto.setRequest_date(rs.getString("REQUEST_DATE"));
//				mbrDto.setStatus(rs.getInt("status"));	
				
				wishBookList.add(wishBookDto);
				
				
			}	// end of while(rs.next())----------------
			
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return wishBookList;		
		
	}

}
