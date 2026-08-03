package book.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.domain.BookDTO;
import dbconnection.ProjectDBConnection;
import user.domain.UserDTO;

public class BookDAO_imple implements BookDAO {

	
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
	   
	   
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 도서정보 삽입하기
	@Override
	public int insertBook(BookDTO bookDTO) {
		int result = 0;
		
		String sql = "INSERT INTO TBL_BOOK (ISBN, FK_CATEGORY_ID, BOOK_NAME, PUB_YEAR, CONTENTS, RENTAL_FEE, AUTHOR, PUBLISHER) "
				+ " VALUES (ISBN.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?) ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, bookDTO.getFk_category_id());
			pstmt.setString(2, bookDTO.getBook_name());
			pstmt.setString(3, bookDTO.getPub_year());
			pstmt.setString(4, bookDTO.getContents());
			pstmt.setInt( 5, bookDTO.getRental_fee());
			pstmt.setString(6, bookDTO.getAuthor());
			pstmt.setString(7, bookDTO.getPublisher());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ISBN 가져오기
	@Override
	public int getIsbn() {
		int result = 0;
		
		String sql = " SELECT ISBN "
				+ " FROM TBL_BOOK "
				+ " ORDER BY ISBN DESC "
				+ " FETCH FIRST 1 ROWS ONLY ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			result = rs.getInt("ISBN");
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		
		
		return result;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 
	@Override
	public List<BookDTO> getAllBooks() {
		List<BookDTO> result = new ArrayList<>();
		
		String sql = " SELECT ISBN, FK_CATEGORY_ID, BOOK_NAME, TO_CHAR(PUB_YEAR, 'YYYY-MM-DD') AS PUB_YEAR, CONTENTS, RENTAL_FEE, AUTHOR, PUBLISHER "
				+ " FROM TBL_BOOK ORDER BY ISBN ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BookDTO book = new BookDTO();
				
				book.setIsbn(rs.getInt("ISBN"));
				book.setFk_category_id(rs.getString("FK_CATEGORY_ID"));
				book.setBook_name(rs.getString("BOOK_NAME"));
				book.setPub_year(rs.getString("PUB_YEAR"));
				book.setContents(rs.getString("CONTENTS"));
				book.setRental_fee(rs.getInt("RENTAL_FEE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setPublisher(rs.getString("PUBLISHER"));
				
				result.add(book);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}finally {
			close();
		}
		
		
		return result;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 해당도서 삭제하기
	@Override
	public int deleteBook(int isbn) {
		int result = 0;
		
		String sql = " DELETE FROM TBL_BOOK WHERE ISBN = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, isbn);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			if (e.getErrorCode() == 2292) {
				System.out.println("[경고] 대여도서가 존재하는 도서는 삭제할 수 없습니다.\n");
			}
			result = -1;
		}finally {
			close();
		}
		
		return result;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// isbn에 해당하는 도서가 있는지 검사
	@Override
	public boolean isExistBook(int isbn) {
		boolean result = false;
		
		String sql = " SELECT * FROM TBL_BOOK "
				+ " WHERE ISBN = ? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, isbn);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = true;
					
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return result;
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	// isbn에 해당하는 도서정보 갖고오기
	@Override
	public BookDTO getBookInfo(int isbn) {
		
		BookDTO result = new BookDTO();
		
		String sql = " SELECT ISBN, FK_CATEGORY_ID, BOOK_NAME, TO_CHAR(PUB_YEAR, 'YYYY-MM-DD') AS PUB_YEAR, CONTENTS, RENTAL_FEE, AUTHOR, PUBLISHER "
				+ " FROM TBL_BOOK "
				+ " WHERE ISBN = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, isbn);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				result.setIsbn(rs.getInt("ISBN"));
				result.setFk_category_id(rs.getString("FK_CATEGORY_ID"));
				result.setBook_name(rs.getString("BOOK_NAME"));
				result.setPub_year(rs.getString("PUB_YEAR"));
				result.setContents(rs.getString("CONTENTS"));
				result.setRental_fee(rs.getInt("RENTAL_FEE"));
				result.setAuthor(rs.getString("AUTHOR"));
				result.setPublisher(rs.getString("PUBLISHER"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return result;
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 특정 도서의 정보 수정하기
	@Override
	public int updateBookInfo(BookDTO bookDto) {
		int result = 0;
		
		String sql = " UPDATE TBL_BOOK SET "
				+ " FK_CATEGORY_ID = ?, "
				+ " BOOK_NAME = ?, "
				+ " PUB_YEAR = TO_DATE(?, 'YYYY-MM-DD'), "
				+ " CONTENTS = ?, "
				+ " RENTAL_FEE = ?, "
				+ " AUTHOR = ?, "
				+ " PUBLISHER = ? "
				+ " WHERE ISBN = ? ";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, bookDto.getFk_category_id());
			pstmt.setString(2, bookDto.getBook_name());
			pstmt.setString(3, bookDto.getPub_year());
			pstmt.setString(4, bookDto.getContents());
			pstmt.setInt(5, bookDto.getRental_fee());
			pstmt.setString(6, bookDto.getAuthor());
			pstmt.setString(7, bookDto.getPublisher());
			pstmt.setInt(8, bookDto.getIsbn());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		}finally {
			close();
		}
		
		return result;
	}

	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// 관심도서 조회
	@Override
	public List<BookDTO> bringBookList(List<BookDTO> favBookList, UserDTO loginUserDto) {
		 try {
	            String sql = " select f.isbn, FK_CATEGORY_ID, BOOK_NAME, to_char(PUB_YEAR,'yyyy-mm-dd') as PUB_YEAR, RENTAL_FEE, AUTHOR, PUBLISHER "
	                     + " from TBL_BOOK B join tbl_favorite f "
	                     + " on b.isbn = f.isbn "
	                      + " where fk_user_seq = ? ";
	            
	            pstmt = conn.prepareStatement(sql);
	            
	            pstmt.setInt(1, loginUserDto.getUser_seq());
	            
	            rs = pstmt.executeQuery();
	            
	            while(rs.next()) {
	               
	               BookDTO dto = new BookDTO();
	               
	               dto.setIsbn(rs.getInt("isbn"));
	               dto.setFk_category_id(rs.getString("FK_CATEGORY_ID"));
	               dto.setBook_name(rs.getString("BOOK_NAME"));
	               dto.setPub_year(rs.getString("PUB_YEAR"));
	               dto.setRental_fee(rs.getInt("RENTAL_FEE"));
	               dto.setAuthor(rs.getString("AUTHOR"));
	               dto.setPublisher(rs.getString("PUBLISHER"));
	               
	               favBookList.add(dto);
	            }
	            
	            
	            }catch(SQLException e) {
	                  e.printStackTrace();
	                  
	             }finally {
	                  
	                  close();
	             }
	         
	      return favBookList;
	}

	
	// 관심도서 취소
	@Override
	public int cancleFav(String isbnNo, UserDTO loginUserDto) {
		int result = 0;
	      
	      try {
	         String sql = " delete from tbl_favorite "
	                  + " where isbn = to_number(?) and "
	                  + " Fk_user_seq = ? ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, isbnNo);
	         pstmt.setInt(2, loginUserDto.getUser_seq());
	         
	         result = pstmt.executeUpdate(); // sql문 실행 
	         
	      } catch (SQLException e) {
	         if(e.getErrorCode() == 1722) {
	            System.out.println(">> [경고] 글번호는 정수만 가능합니다. << \n");
	         }
	         else {
	            e.printStackTrace();
	         }
	      } finally {
	         close();
	      }
	      
	      return result;
	}

	
	//  **** 모든 도서 조회 메서드 ****
	@Override
	public List<BookDTO> selectAllBook() {
		List<BookDTO> selectBook = new ArrayList<>();
        
        BookDTO bookDto = null;
        
        try {
              
              String sql = " SELECT isbn, fk_category_id, book_name, pub_year, author, publisher, rental_fee "
                         + " FROM tbl_book ";
             
              pstmt = conn.prepareStatement(sql);
             
             // sql문 실행
             rs = pstmt.executeQuery();
            
             while(rs.next()) {
           
              bookDto = new BookDTO();
                
              // 1. 존재하는 행들을 DTO에 저장
              bookDto.setIsbn(rs.getInt("isbn"));
              bookDto.setFk_category_id(rs.getString("fk_category_id"));
              bookDto.setBook_name(rs.getString("book_name"));
              bookDto.setPub_year(rs.getString("pub_year"));
              bookDto.setAuthor(rs.getString("author"));
              bookDto.setPublisher(rs.getString("publisher"));
              bookDto.setRental_fee(rs.getInt("rental_fee"));
              // Contents의 내용은 null로 초기화 되지만 결과를 보여줄 때 아무런 
              // 결과도 보여주지 않으려고 "" 값을 넣는다.
              bookDto.setContents("");    
              
                 // 2. list에 Map 추가
              selectBook.add(bookDto);
                 
               }// end of while(rs.next())------------
            
      } catch (SQLException e) {
              e.printStackTrace();
              
        } finally {
           close();
           
        }
           
        return selectBook;
	}

	
	// **** 관심도서 신청 메소드 ****
	@Override
	public int favBookApply(int user_seq, int choice) {
		 int result = 0;
         
         try {
            
            String sql = " INSERT INTO tbl_favorite(FK_USER_SEQ, ISBN) "
                     + " VALUES (?, ?) " ;
            
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user_seq);
            pstmt.setInt(2, choice);
            
            int n = pstmt.executeUpdate();
            
            if(n == 1) {
               result = 1;
            }
         } catch(SQLException e) {
            result = -1;
            
         } finally {
            close();

         }
         return result;
	}

	// 관심도서조회
	@Override
	public List<Map<String, String>> getFavList(String id) {
		List<Map<String, String>> favList = new ArrayList<>();
        try {
              
              String sql = " WITH F AS "
                         + " ( "
                         + " SELECT fk_user_seq, isbn "
                         + " FROM tbl_favorite "
                         + " ), B AS "
                         + " (SELECT isbn, book_name, pub_year, author, publisher, fk_category_id, rental_fee, contents "
                         + " FROM tbl_book "
                         + " ) "
                         + " SELECT book_name, author, publisher, pub_year, rental_fee, contents "
                         + " FROM F JOIN B "
                         + " ON F.isbn = B.isbn "
                         + " WHERE F.fk_user_seq = TO_NUMBER(?)";
             
              
              pstmt = conn.prepareStatement(sql);
             pstmt.setString(1, id);
             
             // sql문 실행
             rs = pstmt.executeQuery();
            
             while(rs.next()) {
               
              // 1. 행마다 새로운 Map 객체 생성
              Map<String, String> map = new HashMap<>();    
           
              // 2. RS에서 6개 컬럼 값을 가져와 Map 에 저장
                 map.put("book_name", rs.getString("book_name"));
                 map.put("author", rs.getString("AUTHOR"));
                 map.put("publisher", rs.getString("publisher"));   
                 map.put("pub_year", rs.getString("pub_year"));
                 map.put("rental_fee", String.valueOf(rs.getInt("rental_fee")));
                 map.put("contents", rs.getString("contents"));   
                 
                 
                 // 3. list에 Map 추가
                 favList.add(map);
                 
               }// end of while(rs.next())------------
            
      } catch (SQLException e) {
              e.printStackTrace();
              
        } finally {
           close();
           
        }
           

        return favList;
           

        
	}

	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 특정 도서의 대여료 가져오기
	@Override
	public int getRentFee(String bookId) {
		int result = 0;
		
		String sql = " SELECT A.RENTAL_FEE, B.BOOK_ID "
				+ " FROM TBL_BOOK A JOIN TBL_LOAN_BOOK B "
				+ " ON A.ISBN = B.ISBN "
				+ " WHERE B.BOOK_ID = ? ";
		
		int book_id = Integer.parseInt(bookId);
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt("RENTAL_FEE");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = -1;
		} finally {
			close();
		}
		
		return result;
	}






}
