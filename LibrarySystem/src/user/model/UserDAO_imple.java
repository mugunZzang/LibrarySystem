package user.model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


		   // 회원로그인처리 메서드
	        @Override
	        public UserDTO userLogin(Map<String, String> paraMap) {
	               
	               UserDTO loginUserDto = null;
	               
	               
	               try {
	                  
	                  String sql = " select * "
	                           + " from tbl_user_login "
	                           + " where user_id = ? and user_pw = ? ";
	                  
	                  pstmt = conn.prepareStatement(sql);
	                  pstmt.setString(1, paraMap.get("userid"));
	                  pstmt.setString(2, paraMap.get("passwd"));
	                  
	                  rs = pstmt.executeQuery(); // sql문 실행
	                  
	                  if(rs.next()) {
	                      
	                	  loginUserDto = new UserDTO();
	                     
	                      loginUserDto.setId(rs.getString("user_id"));
	                      loginUserDto.setUser_seq(rs.getInt("user_seq"));
	                      loginUserDto.setPw(rs.getString("user_pw"));
	                      loginUserDto.setName(rs.getString("user_name"));
	                  }
	               }catch(SQLException e) {
	                  e.printStackTrace();

	               }finally {
	                  close();
	               }
	               
	               return loginUserDto;
	            }// public UserDTO UserLogin(Map<String, String> paraMap)


         
		
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

		
		
		

		// 정렬방식에 따른 모든 회원을 조회(select) 해주는 메서드
		@Override
		public List<UserDTO> showAllMember(String sortChoice) {
			
			List<UserDTO> memberList = null;
			
			try {
				String sql = " select USER_SEQ, USER_ID, USER_PW, USER_NAME, USER_TEL, POINT "
						+ "    , to_char(USER_REGISTERDAY, 'yyyy-mm-dd') AS USER_REGISTERDAY "
						+ "    , LOAN_STOP, OVERDUE_FEE "
						+ " from TBL_USER "; 
				
				switch (sortChoice) {
				case "1":	//1.회원명의 오름차순
					sql += " order by USER_NAME asc ";
					break;
				case "2":	//2.회원명의 내림차순
					sql += " order by USER_NAME desc ";
					break;
				case "3":	//3.가입일자의 오름차순
					sql += " order by USER_REGISTERDAY asc ";
					break;
				case "4":	//4.가입일자의 내림차순
					sql += " order by USER_REGISTERDAY desc ";
					break;
					
				}	//end of switch (sortChoice)-------------
						
				pstmt = conn.prepareStatement(sql);	
				
				rs = pstmt.executeQuery(); // sql문 실행
				
				int cnt = 0;
				while(rs.next()) {
					cnt++;
					
					if(cnt == 1) {
						memberList = new ArrayList<>();
					}
									
					UserDTO mbrDto = new UserDTO();
					
					mbrDto.setUser_seq(rs.getInt("USER_SEQ"));
					mbrDto.setId(rs.getString("USER_ID"));
					mbrDto.setName(rs.getString("USER_NAME"));
					mbrDto.setTel(rs.getString("USER_TEL"));
					mbrDto.setPoint(rs.getInt("POINT"));
					mbrDto.setRegisterday(rs.getString("USER_REGISTERDAY"));
					mbrDto.setLoan_stop(rs.getInt("LOAN_STOP"));
					mbrDto.setOverdue_fee(rs.getInt("OVERDUE_FEE"));
//					mbrDto.setStatus(rs.getInt("status"));	
					
					memberList.add(mbrDto);
					
					
				}	// end of while(rs.next())----------------
				
			}catch(SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			return memberList;		
			
		}	//end of public List<UserDTO> showAllMember(String sortChoice)--------------


		// **** 회원번호가 존재하는지 확인하는 메서드 ****
		@Override
		public boolean isExistenceUserSeq(String userSeq) {
			
			boolean result = false;
			
			try {
				String sql = " SELECT * " 
							+ " FROM TBL_USER "
							+ " WHERE USER_SEQ = TO_NUMBER(?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,  userSeq);
				
				rs = pstmt.executeQuery();	//sql문 실행하기
				
				if(rs.next()) {
					result = true;
				}
				
			} catch (SQLException e) {
				
			} finally {
				close();
			}
			
			return result;
			
		}	// end of public boolean isExistenceUserSeq(String userSeq)-------


		
		// **** 회원을 대출정지 하는 메서드 ****	
		@Override
		public int loanStop(String userSeq) {
			int result = 0;
			
			try {
				String sql = " UPDATE TBL_USER SET LOAN_STOP = 1 "
						   + " WHERE USER_SEQ = TO_NUMBER(?) ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, userSeq);
				
				result = pstmt.executeUpdate();	//sql문 실행하기
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			
			return result;			
		}	
		
		
		
		
		
		
	         //===도서 검색 메서드===//
			@Override
			public List<BookDTO> searchBook(String type, String keyword) {
				List<BookDTO> list = new ArrayList<>();
				
			
			 try { 
				 String sql = " " ;
				 
				  // 도서검색 후 조회 sql문
				 
				 if (type.equals("book_name")) { 
			         sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
			                  + " CASE WHEN EXISTS( SELECT 1 FROM loan_book a WHERE a.isbn=b.isbn "
			                  + " AND a.loan_status =0 ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
			                  + " THEN '대출가능' "
			                  + " ELSE '대출불가' "
			                  + " END AS 대출상세, " // 대출상세 조회
			                  + " CASE WHEN ( SELECT COUNT (c.book_id)  FROM resv_detail c " //예약권수 구해서 2보다 작은지 확인
			                  + " JOIN loan_book a ON c.book_id=a.book_id  WHERE a.isbn=b.isbn ) <2 "
			                  + " THEN '예약가능' "
			                  + " ELSE '예약불가' "
			                  + " END AS 예약상세 "  //예약상세 조회
			                  + " FROM book b "
			                  + " WHERE b.book_name LIKE ?";
				 }
				 else if (type.equals("author")) {
					 
					 sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
			                  + " CASE WHEN EXISTS( SELECT 1 FROM loan_book a WHERE a.isbn=b.isbn "
			                  + " AND a.loan_status =0 ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
			                  + " THEN '대출가능' "
			                  + " ELSE '대출불가' "
			                  + " END AS 대출상세, " // 대출상세 조회
			                  + " CASE WHEN ( SELECT COUNT (c.book_id)  FROM resv_detail c " //예약권수 구해서 2보다 작은지 확인
			                  + " JOIN loan_book a ON c.book_id=a.book_id  WHERE a.isbn=b.isbn ) <2 "
			                  + " THEN '예약가능' "
			                  + " ELSE '예약불가' "
			                  + " END AS 예약상세 "  //예약상세 조회
			                  + " FROM book b "
			                  + " WHERE b.author LIKE ?";
				 }
				 else if (type.equals("publisher")) {
					 sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
			                  + " CASE WHEN EXISTS( SELECT 1 FROM loan_book a WHERE a.isbn=b.isbn "
			                  + " AND a.loan_status =0 ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
			                  + " THEN '대출가능' "
			                  + " ELSE '대출불가' "
			                  + " END AS 대출상세, " // 대출상세 조회
			                  + " CASE WHEN ( SELECT COUNT (c.book_id)  FROM resv_detail c " //예약권수 구해서 2보다 작은지 확인
			                  + " JOIN loan_book a ON c.book_id=a.book_id  WHERE a.isbn=b.isbn ) <2 "
			                  + " THEN '예약가능' "
			                  + " ELSE '예약불가' "
			                  + " END AS 예약상세 "  //예약상세 조회
			                  + " FROM book b "
			                  + " WHERE b.publisher LIKE ?";
				 }
				 else if (type.equals("fk_category_id")) {
					 sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
			                  + " CASE WHEN EXISTS( SELECT 1 FROM loan_book a WHERE a.isbn=b.isbn "
			                  + " AND a.loan_status =0 ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
			                  + " THEN '대출가능' "
			                  + " ELSE '대출불가' "
			                  + " END AS 대출상세, " // 대출상세 조회
			                  + " CASE WHEN ( SELECT COUNT (c.book_id)  FROM resv_detail c " //예약권수 구해서 2보다 작은지 확인
			                  + " JOIN loan_book a ON c.book_id=a.book_id  WHERE a.isbn=b.isbn ) <2 "
			                  + " THEN '예약가능' "
			                  + " ELSE '예약불가' "
			                  + " END AS 예약상세 "  //예약상세 조회
			                  + " FROM book b "
			                  + " WHERE b.fk_category_id LIKE ?";
				 }
				 
				 pstmt = conn.prepareStatement(sql);
			        rs = pstmt.executeQuery();

			        while(rs.next()) {

			            BookDTO dto = new BookDTO();
			            dto.setIsbn(rs.getInt("isbn"));
			            dto.setBook_name(rs.getString("book_name"));
			            dto.setAuthor(rs.getString("author"));
			            dto.setPublisher(rs.getString("publisher"));
			            dto.setFk_category_id(rs.getString("fk_category_id"));
			            dto.setLoanStatus(rs.getString("대여상세"));
			            dto.setResvStatus(rs.getString("예약상세"));
			            list.add(dto);
			        } //end of while ----------------
				 
				 
			 } catch(SQLException e)  {
				 e.printStackTrace();
			 }finally {
				 
			 }  close();
	    

			return list;
		} //end of public List<BookDTO> searchBook(String type, String keyword)---

		
		
		//===도서 예약 메서드===//
		@Override
		public int reservation(int user_seq, int isbn) {
			
			return 0;
		}
		
		
				
				

		//===관심 도서 등록 메서드===//
		@Override
		public int favorite(int user_seq, int isbn) {
			
			return 0;
		}


		@Override
		public int getLoanCount(int user_seq) {
			// TODO Auto-generated method stub
			return 0;
		}


		@Override
		public int getReservationCount(int user_seq) {
			// TODO Auto-generated method stub
			return 0;
		}

		

		
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
		
		
				
				
		
		
		
		
	
	
} //end of public class UserDAO_imple implements UserDAO
