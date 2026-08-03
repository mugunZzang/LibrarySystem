package user.model;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import book.domain.BookDTO;
import book.domain.WishBookDTO;
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
			        		+ " to_char(b.pub_year,'yyyy-mm-dd') AS pub_year, "
			        		+ " COUNT(*) AS loan_count "
			        		+ " FROM tbl_loan_detail a "
			        		+ " JOIN tbl_loan_book c "
			        		+ " ON a.book_id = c.book_id "
			        		+ " JOIN tbl_book b "
			        		+ " ON c.isbn = b.isbn "
			        		+ " GROUP BY "
			        		+ " b.isbn, "
			        		+ " b.book_name, "
			        		+ " b.author, "
			        		+ " b.publisher, "
			        		+ " b.pub_year "
			        		+ " ) "
			        		+ " WHERE ranking <= 20 "
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
		            dto.setLoan_count(rs.getInt("loan_count"));
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
						+ "    , CASE "
						+ "       WHEN LOAN_STOP = 0 THEN '대출 가능' "
						+ "       WHEN LOAN_STOP = 1 THEN '대출 정지' "
						+ "       END AS LOAN_STOP "
						+ "		, OVERDUE_FEE "
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
									
					UserDTO mbrDto = new UserDTO();	//회원 한 명을 저장할 객체
					
					mbrDto.setUser_seq(rs.getInt("USER_SEQ"));
					mbrDto.setId(rs.getString("USER_ID"));
					mbrDto.setName(rs.getString("USER_NAME"));
					mbrDto.setTel(rs.getString("USER_TEL"));
					mbrDto.setPoint(rs.getInt("POINT"));
					mbrDto.setRegisterday(rs.getString("USER_REGISTERDAY"));
					mbrDto.setLoan_stop_kor(rs.getString("LOAN_STOP"));
					mbrDto.setOverdue_fee(rs.getInt("OVERDUE_FEE"));
//							mbrDto.setStatus(rs.getInt("status"));	
					
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
		
		// **** 260801 회원을 대출승인 하는 메서드 ****
		@Override
		public int loanAllow(String userSeq) {
			int result = 0;
			
			try {
				String sql = " UPDATE TBL_USER SET LOAN_STOP = 0 "
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
		
		// 260801 회원번호와 일치하는 회원명을 가져오는 메서드
		@Override
		public String getUserName(String userSeq) {
			
			String userName ="";
			
			String sql = " SELECT USER_NAME "
					+ " FROM TBL_USER "
					+ " WHERE USER_SEQ = ? ";
			
			try {				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(userSeq));
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					userName = rs.getString("USER_NAME");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			
			return userName;
			
		}

		// 260801 회원번호와 일치하는 대출권한을 가져오는 메서드
		@Override
		public int getloanStop(String userSeq) {
			
			int loanApproval = 0;
			
			String sql = " SELECT LOAN_STOP "
					+ " FROM TBL_USER "
					+ " WHERE USER_SEQ = ? ";
			
			try {				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(userSeq));
				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					loanApproval = rs.getInt("LOAN_STOP");
				}
				
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					close();
				}
			
			return loanApproval;
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
                             + " CASE WHEN EXISTS( SELECT 1 FROM tbl_loan_book a WHERE a.isbn=b.isbn "
                             + " AND a.loan_status =0 AND a.book_status = '정상' ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
                             + " THEN '대출가능' "
                             + " ELSE '대출불가' "
                             + " END AS 대출상세, " // 대출상세 조회
                             + " CASE WHEN EXISTS ( SELECT 1 FROM tbl_loan_book a WHERE a.isbn = b.isbn AND a.book_status = '정상' "
                             + " AND ( SELECT COUNT(*) "
                             + " FROM tbl_resv_detail c "
                             + " WHERE c.book_id = a.book_id "
                             + " ) < 2 "
                             + " ) "
                                + " THEN '예약가능' "
                                + " ELSE '예약불가' "
                                + " END AS 예약상세 " //예약상세 조회
                             + " FROM tbl_book b "
                             + " WHERE b.book_name LIKE ?";
               }
               else if (type.equals("author")) {
                  
                  sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
                             + " CASE WHEN EXISTS( SELECT 1 FROM tbl_loan_book a WHERE a.isbn=b.isbn "
                             + " AND a.loan_status =0 AND a.book_status = '정상' ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
                             + " THEN '대출가능' "
                             + " ELSE '대출불가' "
                             + " END AS 대출상세, " // 대출상세 조회
                             + " CASE WHEN EXISTS ( SELECT 1 FROM tbl_loan_book a WHERE a.isbn = b.isbn AND a.book_status = '정상' "
                             + " AND ( SELECT COUNT(*) "
                             + " FROM tbl_resv_detail c "
                             + " WHERE c.book_id = a.book_id "
                             + " ) < 2 "
                             + " ) "
                                + " THEN '예약가능' "
                                + " ELSE '예약불가' "
                                + " END AS 예약상세 " //예약상세 조회
                             + " FROM tbl_book b "
                             + " WHERE b.author LIKE ?";
               }
               else if (type.equals("publisher")) {
                  sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
                             + " CASE WHEN EXISTS( SELECT 1 FROM tbl_loan_book a WHERE a.isbn=b.isbn "
                             + " AND a.loan_status =0 AND a.book_status = '정상' ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
                             + " THEN '대출가능' "
                             + " ELSE '대출불가' "
                             + " END AS 대출상세, " // 대출상세 조회
                             + " CASE WHEN EXISTS ( SELECT 1 FROM tbl_loan_book a WHERE a.isbn = b.isbn AND a.book_status = '정상' "
                             + " AND ( SELECT COUNT(*) "
                             + " FROM tbl_resv_detail c "
                             + " WHERE c.book_id = a.book_id "
                             + " ) < 2 "
                             + " ) "
                                + " THEN '예약가능' "
                                + " ELSE '예약불가' "
                                + " END AS 예약상세 " //예약상세 조회
                             + " FROM tbl_book b "
                             + " WHERE b.publisher LIKE ?";
               }
               else if (type.equals("fk_category_id")) {
                  sql=  " SELECT b.isbn, b.book_name, b.author, b.publisher, b.fk_category_id, "
                             + " CASE WHEN EXISTS( SELECT 1 FROM tbl_loan_book a WHERE a.isbn=b.isbn "
                             + " AND a.loan_status =0 AND a.book_status = '정상' ) "  // 대여상태가 0인 행이 한개라도 존재하는지 확인
                             + " THEN '대출가능' "
                             + " ELSE '대출불가' "
                             + " END AS 대출상세, " // 대출상세 조회
                             + " CASE WHEN EXISTS ( SELECT 1 FROM tbl_loan_book a WHERE a.isbn = b.isbn AND a.book_status = '정상' "
                             + " AND ( SELECT COUNT(*) "
                             + " FROM tbl_resv_detail c "
                             + " WHERE c.book_id = a.book_id "
                             + " ) < 2 "
                             + " ) "
                                + " THEN '예약가능' "
                                + " ELSE '예약불가' "
                                + " END AS 예약상세 " //예약상세 조회
                             + " FROM tbl_book b "
                             + " WHERE b.fk_category_id LIKE ?";
               }
               
               pstmt = conn.prepareStatement(sql);
               pstmt.setString(1, "%" + keyword + "%");
                   rs = pstmt.executeQuery();

                   while(rs.next()) {

                       BookDTO dto = new BookDTO();
                       dto.setIsbn(rs.getInt("isbn"));
                       dto.setBook_name(rs.getString("book_name"));
                       dto.setAuthor(rs.getString("author"));
                       dto.setPublisher(rs.getString("publisher"));
                       dto.setFk_category_id(rs.getString("fk_category_id"));
                       dto.setLoanStatus(rs.getString("대출상세"));
                       dto.setResvStatus(rs.getString("예약상세"));
                       list.add(dto);
                   } //end of while ----------------
               
               
            } catch(SQLException e)  {
               e.printStackTrace();
            }finally {
               close();
            }  
         

           return list;
     } //end of public List<BookDTO> searchBook(String type, String keyword)---     
		
		
      //== 예약 신청 메서드 ==//
        @Override
        public int reservation(int user_seq, int isbn) {

            int result = 0;

            try {

                conn.setAutoCommit(false);


                int resv_id = 0;



                // 1. 기존 회원 예약번호 조회
                String sql =
                      " SELECT resv_id "
                    + " FROM tbl_reservation "
                    + " WHERE fk_user_seq = ? ";


                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, user_seq);


                rs = pstmt.executeQuery();



                if(rs.next()) {

                    // 기존 예약번호 사용
                    resv_id = rs.getInt("resv_id");

                }



                // 2. 예약번호가 없으면 생성
                if(resv_id == 0) {


                    sql =
                          " INSERT INTO tbl_reservation "
                        + " (resv_id, fk_user_seq, resv_date) "
                        + " VALUES(resv_id.nextval, ?, SYSDATE) ";


                    pstmt = conn.prepareStatement(sql);

                    pstmt.setInt(1, user_seq);


                    int n1 = pstmt.executeUpdate();


                    if(n1 != 1) {

                        conn.rollback();
                        return 0;

                    }



                    // 방금 생성한 예약번호 가져오기
                    sql =
                          " SELECT resv_id.currval AS resv_id "
                        + " FROM dual ";


                    pstmt = conn.prepareStatement(sql);

                    rs = pstmt.executeQuery();



                    if(rs.next()) {

                        resv_id = rs.getInt("resv_id");

                    }

                }


                // 3. 예약 가능한 실제 도서 ID 찾기
               sql=" SELECT a.book_id "
                + " FROM tbl_loan_book a "
                + " WHERE a.isbn = ? "
                + "  AND a.book_status = '정상' "
                + "  AND ( SELECT COUNT(*) "
                + " FROM tbl_resv_detail c "
                + "  WHERE c.book_id = a.book_id ) < 2 "
                + " ORDER BY a.book_id "
                +" FETCH FIRST 1 ROW ONLY ";


                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, isbn);


                rs = pstmt.executeQuery();



                int book_id = 0;


                if(rs.next()) {

                    book_id = rs.getInt("book_id");

                }
                else {

                    conn.rollback();
                    return 0;

                }



                // 4. 예약 상세 추가
                sql =
                      " INSERT INTO tbl_resv_detail "
                    + " (resv_detail_id, fk_resv_id, book_id) "
                    + " VALUES(resv_detail_id.nextval, ?, ?) ";


                pstmt = conn.prepareStatement(sql);


                pstmt.setInt(1, resv_id);
                pstmt.setInt(2, book_id);



                int n2 = pstmt.executeUpdate();



                if(n2 == 1) {

                    conn.commit();
                    result = 1;

                }
                else {

                    conn.rollback();

                }



                conn.setAutoCommit(true);



            } catch(SQLException e) {

                e.printStackTrace();

                try {
                    conn.rollback();
                } catch(SQLException e2) {
                    e2.printStackTrace();
                }


            } finally {

                close();

            }


            return result;
        }
		
		
				
				

		//===관심 도서 등록 메서드===//
		@Override
		public int favorite(int user_seq, int isbn) {
			
			return 0;
		}

		//=== 회원이 현재 대여중인 책 권수 구하기 메서드===//
		@Override
		public int getLoanCount(int user_seq) {
			int count =0;
			
			  try {
				  String sql=" SELECT COUNT(*) "
						     +" FROM tbl_loan_detail a "
						     +" JOIN  tbl_loan b "
						     +" ON a.loan_no=b.loan_no "
						     +" WHERE b.user_seq =? "
						     +" AND a.return =0 "; //반납여부가 0인 책 권수 구하는 sql문
				  
				    pstmt = conn.prepareStatement(sql);	
				    pstmt.setInt(1,user_seq);
				  
					rs = pstmt.executeQuery(); // sql문 실행
					
					if(rs.next()) {
					   count=rs.getInt(1);
					}
			  } catch(SQLException e) {
			        e.printStackTrace();
			    } finally {
			    	close();
			    }

			    return count;
		}


		 //===회원이 현재 예약중인 책 권수 구하기 메서드===//
		@Override
		public int getReservationCount(int user_seq) {
			int count =0;
			
			  try {
				  String sql=" SELECT COUNT(*) "
						     +" FROM tbl_resv_detail a "
						     +" JOIN  tbl_reservation b "
						     +" ON a.fk_resv_id=b.resv_id "
						     +" WHERE b.fk_user_seq =? ";
						      //예약중인 책 권수 구하는 sql문
				  
				    pstmt = conn.prepareStatement(sql);	
				    pstmt.setInt(1,user_seq);
					rs = pstmt.executeQuery(); // sql문 실행
					
					if(rs.next()) {
					   count=rs.getInt(1);
					}
			  } catch(SQLException e) {
			        e.printStackTrace();
			    }finally {
			    	close();
			    }

			    return count;
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
		
		if(rs.getInt("OVERDUE_FEE") == 0 && rs.getInt("LOAN_STOP") == 0) {
		result = true;
		}
		
		
		} catch (SQLException e) {
		e.printStackTrace();
		}finally {
	    	close();
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
		close();
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
		
		pstmt.setInt(1, delayedDays);
		pstmt.setInt(2, userSeq);
		
		
		result = pstmt.executeUpdate();
		
		} catch (SQLException e) {
		e.printStackTrace();
		result = -1;
		}finally {
	    	close();
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
		close();
		}
		
		
		
		return result;
		}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// 
/// 
/// 	//===희망도서 요청 메서드===//
		@Override
		public int requestWishBook(int user_seq, WishBookDTO dto) {
			int result=0;
			try {
			String sql= " INSERT INTO tbl_wish_book(wish_book_no,user_seq, "
					+ " wish_book_name,wish_book_author,wish_book_publisher,request_date) " 
					+ " VALUES(wish_book_no.nextval,?,?,?,?,SYSDATE) ";
				
				  pstmt = conn.prepareStatement(sql);
				  pstmt.setInt(1, user_seq);
				  pstmt.setString(2, dto.getWish_book_name());
				  pstmt.setString(3, dto.getWish_book_author());
				  pstmt.setString(4, dto.getWish_book_publisher());
				
				 
				  result += pstmt.executeUpdate();
				
			}catch(SQLException e) {
		        e.printStackTrace();
		    }finally {
		    	close();
		    }

		    return result;
		}


		//연체료 납입
		@Override
		public int payOverdueFee(Map<String, Object> map) {
			int result = 0;
	           
	           String sql = " update tbl_user "
	                       + " set overdue_fee = overdue_fee - ? "
	                       + " where user_id = ?";

	            try {
	                PreparedStatement ps = conn.prepareStatement(sql);
	                ps.setInt(1, (Integer) map.get("fee"));
	                ps.setString(2, (String) map.get("user_id"));

	                result = ps.executeUpdate();

	            } catch (SQLException e) {
	                e.printStackTrace();
	            } finally {
	                 close();
	                 
	            }
	           
	           
	           return result;
		}


		//포인트 충전 
		@Override
		public int addPoint(UserDTO loginUserDto) {
			int result = 0;
            
            try {
               
               // SQL 문 작성
               String sql = " update tbl_user set point = point + ? " 
                        + " where user_id = ? " ;

               // 연결한 DB에 SQL문 제작 후 전달
               pstmt = conn.prepareStatement(sql);
               pstmt.setInt(1, loginUserDto.getPoint());
               pstmt.setString(2, loginUserDto.getId());
               
               // 5. 실행
               result = pstmt.executeUpdate();    // SQL 문 실행
         
            } catch(SQLException e) {
                  e.printStackTrace();
                  
               } finally {
                  close();
                  
               }
               return result;
		}

		// === 내 정보 조회 메서드 ===
		@Override
		public UserDTO myInfo(String id) {
			UserDTO loginUserDto = new UserDTO();
	           
            try {      
               String sql = " SELECT * "
                           + " FROM tbl_user "
                           + " WHERE user_id = ? ";
              
        
                 // 연결한 오라클 서버에 우편배달부 생성 후 내 SQL 문 전달
                 pstmt = conn.prepareStatement(sql);
                 pstmt.setString(1, id);
                    
                 // 실행
                 rs = pstmt.executeQuery();    // SQL 문 실행
                 
                 // 만약 해당 아이디가 존재한다면 
                 if(rs.next()) {
                   loginUserDto.setUser_seq(rs.getInt("user_seq"));
                   loginUserDto.setId(rs.getString("user_id"));
                   loginUserDto.setPw(rs.getString("user_pw"));;
                   loginUserDto.setName(rs.getString("user_name"));
                   loginUserDto.setTel(rs.getString("user_tel"));
                   loginUserDto.setEmail(rs.getString("user_email"));
                   loginUserDto.setRegisterday(rs.getString("user_registerday"));
                   loginUserDto.setLoan_stop(rs.getInt("loan_stop"));
                   loginUserDto.setPoint(rs.getInt("point"));
                   loginUserDto.setOverdue_fee(rs.getInt("overdue_fee"));
                   
                   
                 }
                 
              }  catch(SQLException e) {
                 e.printStackTrace();
                 
              } finally {
                 // >>> 사용하였던 자원을 반납하기 <<<
                 close();
              }

           return loginUserDto;
		}


        // ****  내 정보를 수정(UPDATE)해주는 메서드 **** //
		@Override
		public int updateMyinfo(Map<String, String> paraMap) {
			int result = 0;
            
            try {
                // 1. 자동 커밋 해제 (트랜잭션 시작)
                conn.setAutoCommit(false);

                // --- 첫 번째 UPDATE: tbl_user 테이블 수정 ---
                String sql = " UPDATE tbl_user SET user_pw = ?, "
                            + "                    user_name = ?, "
                            + "                    user_tel = ? "
                            + " WHERE user_seq = ? ";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, paraMap.get("newPasswd"));
                pstmt.setString(2, paraMap.get("newName"));
                pstmt.setString(3, paraMap.get("newMobile"));
                pstmt.setInt(4, Integer.parseInt(paraMap.get("userseq")));
                
                int n1 = pstmt.executeUpdate();
                pstmt.close(); // 사용한 PreparedStatement 닫기

                // --- 두 번째 UPDATE: tbl_user_login 테이블 수정 ---
                sql = " UPDATE tbl_user_login SET user_pw = ? "
                            + " WHERE user_seq = ? ";
                
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, paraMap.get("newPasswd"));
                pstmt.setInt(2, Integer.parseInt(paraMap.get("userseq")));
                
                int n2 = pstmt.executeUpdate();

                // 두 테이블 모두 정상적으로 수정된 경우에만 Commit
                if (n1 == 1 && n2 == 1) {
                    conn.commit();
                    result = 1;
                } else {
                    conn.rollback();
                }
                
            } catch(SQLException e) {
                // 예외 발생 시 트랜잭션 롤백
                try {
                    if (conn != null) conn.rollback();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                // 수동 커밋 모드 원복 및 자원 해제
                try {
                    if (conn != null) conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                close();
            }
            
            return result;
		}


	


		// 특정 회원이 포인트를 가져오기
		@Override
		public int getUserPoint(int userSeq) {
			int result = 0;
				
			String sql = " SELECT POINT FROM TBL_USER WHERE USER_SEQ = ? ";
			
			try {
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userSeq);
						
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = rs.getInt("POINT");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				result = -1;
			}finally {
				close();
			}
			
			return result;
		}


		
		
		// 대출처리시 포인트 차감시키기
		@Override
		public int minusToPoint(int bookRentFee, int userSeq) {
			int result = 0;
			
			String sql = " UPDATE TBL_USER"
					+ " SET POINT = POINT - ? "
					+ " WHERE USER_SEQ = ? ";
			
			try {
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bookRentFee);
				pstmt.setInt(2, userSeq);
						
				result = pstmt.executeUpdate();
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				result = -1;
			} finally {
				close();
			}
			
			return 0;
		}
		
		
				
				
		
		
		
		
	
	
} //end of public class UserDAO_imple implements UserDAO
