package user_controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import book.domain.BookDTO;
import book.domain.WishBookDTO;
import book.model.BookDAO;
import book.model.BookDAO_imple;
import common.utils;
import librarian.controller.LibrarianController;
import librarian.domain.LibrarianDTO;
import librarian.model.LibrarianDAO;
import librarian.model.LibrarianDAO_imple;
import loan.domain.LoanDAO;
import loan.domain.LoanDAO_imple;
import reservation.model.ReservationDAO;
import reservation.model.ReservationDAO_imple;
import user.domain.UserDTO;
import user.model.UserDAO;
import user.model.UserDAO_imple;


public class UserController {
	
	UserDAO userDao = new UserDAO_imple();
	LibrarianDAO libDao = new LibrarianDAO_imple();
	LoanDAO loanDao =new LoanDAO_imple();
	BookDAO bookDao = new BookDAO_imple();
	ReservationDAO resvDao = new ReservationDAO_imple();   
	
	// *** 시작메뉴를 보여주는 메서드 ***
	public void mainstart(Scanner sc) {
		
		boolean isLoginSuccess = false;
		UserDTO loginUserDto = null;
		LibrarianDTO loginLibDto = null;
		
		do {
			// 로그인 실패시
			if(isLoginSuccess == false) {
				System.out.println("\n----------------------- 도서관리 프로그램 ------------------------\n"									
									+ "1.베스트셀러   2.도서 검색   3.로그인   4.회원가입   5.프로그램 종료 \n");
		
				System.out.print("▷ 메뉴번호를 선택하세요 : ");
				String menuNo = sc.nextLine();
				
				switch (menuNo) {
				
					case "1":
						bestSeller(sc); // 베스트셀러 검색
						break;
					case "2":
						searchBook(sc,0); // 도서 검색
						break;
					case "3":
							System.out.println("\n------- 로그인 할 계정 유형 선택 -------");
							System.out.println("1.회원로그인     2.사서로그인");
							
							System.out.print("▷ 메뉴번호 선택 : ");
							String loginNo = sc.nextLine();
							
							switch (loginNo) {
								case "1":
									loginUserDto = member_login(sc);
									
									if(loginUserDto != null) {
										isLoginSuccess = true;
									}
									break;
								case "2":
									loginLibDto = librarian_login(sc);
									
									if(loginLibDto != null) {
										isLoginSuccess = true;
									}
									break;
						
								default:
									System.out.println("[경고] 1이나 2만 입력해주세요!!");
									break;
								}
						break;
					case "4":
			            do {
			               System.out.print("1.일반회원가입   2.사서회원가입");
			               String choice = sc.nextLine();
			               if(choice.equals("1")) {
			                  // 회원으로 회원가입
			                  userRegister(sc);
			                  break;     // 회원가입 메서드 완료되면 do~while 탈출
			               } else if(choice.equals("2")) {
			                  // 사서로 회원가입
			                  librarianRegister(sc);
			                  break;     // 회원가입 메서드 완료되면 do~while 탈출
			               } else {
			                  System.out.println("[경고] 1번과 2번 중에서 선택하세요.");
			               }
			            } while(true);
			            break;
					case "5" :
						return;
					default:
						break;
				}// end of switch (menuNo)
				
			} // end of if(isLoginSuccess == false)
			
			// 로그인 성공시 
			if(isLoginSuccess == true) {
				
				// 사서 로그인시
				if(loginLibDto != null) {
               LibrarianController libCtrl = new LibrarianController();
					
					// 사서 Controller Menu method 호출
					isLoginSuccess = libCtrl.startMenu(loginLibDto, sc);
					if(!isLoginSuccess) {
						loginLibDto = null;
					}

				}
				
				// 회원 로그인시
				else if(loginUserDto != null) {
					
					// loginUserDto.getUserid() 로 tbl_user 테이블에서 정보 가져오기
				    loginUserDto = userDao.myInfo(loginUserDto.getId());
				     
					System.out.println("\n>>> ---------- 일반회원 메뉴 ["+ loginUserDto.getName() +"님 로그인중..] ---------- <<<\n"
									+"1.베스트셀러  2.도서 검색  3.희망도서 신청  4.연체료 납부  5.관심도서  6.마이페이지  7.로그아웃\n");
			
					System.out.print("▷ 메뉴번호 선택 : ");
					String menuNo = sc.nextLine();
					switch (menuNo) {
					case "1":
						bestSeller(sc);  // 베스트셀러 조회
						break;
					case "2":
						searchBook(sc,loginUserDto.getUser_seq());  // 도서 검색
						break;
					case "3":
						requestWishBook(sc,loginUserDto.getUser_seq());  // 희망도서 신청
						break;
					case "4":
						payOverdueFee(sc,loginUserDto);   // 연체료 납부
						break;
					case "5": // 관심도서
					    
		                String favNo;
		                  
	                     
	                    do {
		                    System.out.println("1.관심도서 조회  2.관심도서 신청  3.관심도서 삭제  4.이전");
		                    System.out.print("▷ 메뉴번호 선택 : ");
		                    favNo = sc.nextLine();
		                    
			                List<BookDTO> favBookList = new ArrayList<>();
		                    favBookList = bookDao.bringBookList(favBookList, loginUserDto);
		                    
		                     
		                    switch (favNo) {
		                    case "1": // 관심도서 조회
		                        
		                       if(favBookList.size() != 0) {
		                          System.out.println(loginUserDto.getName()+"의 관심도서 목록");
		                          StringBuilder sb = new StringBuilder();
		                                 
		                          for(BookDTO book : favBookList) {
		                                
		                               sb.append(book.getIsbn() + "\t");
		                               sb.append(book.getBook_name() + "\t");
		                               sb.append(book.getAuthor() + "\t");
		                               sb.append((book.getPublisher() + "\t"));
		                               sb.append(book.getRental_fee() + "\t");
		                               sb.append(book.getPub_year() + "\n");
		                          }
		                           
		                          System.out.println(sb.toString());
		                       }else {
		                          System.out.println(loginUserDto.getName()+"님의 관심도서 목록이 없습니다.!");
		                       }
		                        
		                      break;
		                     case "2": // 관심도서 신청
		                        favBookApply(loginUserDto, favBookList, sc);
		                        break;
		                     case "3": // 관심도서 삭제
		                        favBookcancle(sc, loginUserDto, favBookList);
		                        break;
		                        
		                     case "4": // 이전
			                        
			                        break;
			            
			                     default:
			                        System.out.println("존재하지 않는 메뉴입니다.");
			                        break;
			                     }
		                     } while(!"4".equals(favNo));
		                  
		                  
		                  break;
		           
						
					case "6": // 마이페이지
					    
						userMyPage(sc, loginUserDto); // 로그인된 회원의 마이페이지
						break;
					case "7": // 로그아웃
						loginUserDto = null;
						isLoginSuccess = false;
						System.out.println(">>> 로그아웃 되었습니다. <<<\n");
						break;
					default:
						System.out.println("메뉴에 있는 번호만 입력해주세요!!");
						break;
					}
					
				}
			
				
			}// end of if(isLoginSuccess == true)
			
			
			
		}while(true); // end of do ~ while문---------------------------
		
		
		
		
	} // end of public void mainstart(Scanner sc)----------


	
     
     //관심도서 취소 메서드
    private void favBookcancle(Scanner sc, UserDTO loginUserDto, List<BookDTO> favBookList) {
    	System.out.println(loginUserDto.getName()+"의 관심도서 목록");
        StringBuilder sb = new StringBuilder();
        
        boolean exist = false;
              
        for(BookDTO book : favBookList) {
             
             sb.append(book.getIsbn() + "\t");
             sb.append(book.getBook_name() + "\t");
             sb.append(book.getAuthor() + "\t");
             sb.append((book.getPublisher() + "\t"));
             sb.append(book.getRental_fee() + "\t");
             sb.append(book.getPub_year() + "\n");
        }
        
        System.out.println(sb.toString());
        
        System.out.print("삭제하실 책 번호를 입력해주세요 : ");
        String isbnNo = sc.nextLine();
        
        for(int i = 0; i < favBookList.size(); i++) {
            
           // bookDTO 안에 있는 리스트의 인덱스를 읽어옴
           BookDTO book = favBookList.get(i);

            if(isbnNo.equals(String.valueOf(book.getIsbn()))) {

                int n = bookDao.cancleFav(isbnNo, loginUserDto);

                if(n == 1) {
                    System.out.println("관심도서가 취소되었습니다.");

                    favBookList.remove(i);
                    
                    exist = true;
                }

                break;
            }// end of if(isbnNo.equals(String.valueOf(book.getIsbn())))
            
        }// end of for(int i = 0; i < favBookList.size(); i++)
        if(!exist) {
            System.out.println("삭제하실 책 번호가 아닙니다.!!");
         }


        
		
	}





	//관심도서 신청 메서드
	private void favBookApply(UserDTO loginUserDto, List<BookDTO> favBookList, Scanner sc) {
		// 현재 존재하는 도서 목록 보여주기
		/*
		        --------------------------------------------------------------
		        ISBN    카테고리    책이름    발행년도    저자명    출판사    대여료
		        --------------------------------------------------------------
		*/
		       List<BookDTO> selectBook = bookDao.selectAllBook();
		       
		       System.out.println("\n" + "-".repeat(30) + "현재 도서 목록" + "-".repeat(30));
		       System.out.println("ISBN    카테고리    책이름    발행년도    저자명    출판사    대여료    \n");
		       System.out.println("-".repeat(80));
		       
		       StringBuilder sb = new StringBuilder();
		       
		       for(BookDTO allBook : selectBook) {
		          sb.append(allBook.getIsbn() + "    "
		                + allBook.getFk_category_id() + "    "
		                + allBook.getBook_name() + "    "
		                + allBook.getPub_year() + "    "
		                + allBook.getAuthor() + "    "
		                + allBook.getPublisher() + "    "
		                + allBook.getRental_fee() + "    "
		                + allBook.getContents() + "\n"
		                
		                );
		       } // end of for()--------------------------------
		       
		       System.out.println(sb.toString());
		       
		       do {
		       ///////////////////////////////////////////////////////////////
		          // 존재하는 도서에서 사용자가 입력해서 원하는 책 관심도서 신청
		          System.out.print("관심도서 신청할 ISBN을 입력하세요. : ");
		          int choice = 0;
		          
		          do {
		             try {
		                String userChoiceIsbn = sc.nextLine();
		                choice = Integer.parseInt(userChoiceIsbn);
		             
		                break;
		             } catch(NumberFormatException e) {
		                System.out.println("문자가 아닌 숫자로만 입력하세요.");
		             }
		          } while(true);
		          
		          // 사용자가 입력한 값이 존재하는 ISBN인지 확인하는 용도
		          boolean isHave = false;
		         
		          for(BookDTO allBook : selectBook) {
		             if(choice == allBook.getIsbn()) {
		                isHave = true;
		             }
		          } // end of for---------------------------
		          
		          if(isHave == true) {
		             // 사용자가 입력한 ISBN이 실제로 존재하는 ISBN
		             
		             // 만약 관심도서 신청을 이미 했다면 반영하지 않는다.
		             // 관심도서 테이블의 ISBN 과 일치하는 ISBN 을 입력한 경우
		             // 해당 관심도서는 이미 신청을 했다는 소리이므로 
		             // 신청이 불가능하다.
		             for(BookDTO checkBookList : favBookList) {
		                if(checkBookList.getIsbn() == choice) {
		                   System.out.println("이미 신청한 도서입니다.");
		                   return;   // favBookApply 메서드 종료
		                }
		             } // end of for-----------------------------
		             
		             // 관심도서 신청을 하지 않았다면 반영한다.
		             int n = bookDao.favBookApply(loginUserDto.getUser_seq(), choice);
		          
		             if(n==1) {
		                System.out.println("관심도서 신청이 완료되었습니다.");
		                return ;  // favBookApply 메서드 종료
		             } else {
		                System.out.println("신청이 실패하였습니다.");
		                return ;  // favBookApply 메서드 종료
		             }
		          
		          } else {
		             // 사용자가 입력한 ISBN이 실제로 존재하지 않는 ISBN
		             System.out.println(">>> [경고]존재하지 않는 ISBN 입니다. 올바른 ISBN을 입력하세요. <<<");
		             
		          }
		       /////////////////////////////////////////////////////////////
		       } while(true);
		    
		
	}






	// 회원 로그인 메서드
	private UserDTO member_login(Scanner sc) {
		
		System.out.println("\n >>> --- 회원 로그인 --- <<<");
		
		System.out.print("▷ 아이디 : ");
		String userid = sc.nextLine();
		
		System.out.print("▷ 비밀번호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		UserDTO loginUserDto = userDao.userLogin(paraMap);
		
		if(loginUserDto != null) {
			System.out.println("\n >>> 로그인 성공!! <<< \n");
		}
		else {
			System.out.println("\n >>> 로그인 실패!! <<< \n");
		}
		
		return loginUserDto;
	} // end of private UserDTO member_login(Scanner sc)
	
	
	// 사서 로그인 메서드
	private LibrarianDTO librarian_login(Scanner sc) {
		
		System.out.println("\n >>> --- 사서 로그인 --- <<<");
		
		System.out.print("▷ 아이디 : ");
		String userid = sc.nextLine();
		
		System.out.print("▷ 비밀번호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		LibrarianDTO loginLibDto = libDao.libLogin(paraMap);
		
		if(loginLibDto != null) {
			System.out.println("\n >>> 로그인 성공!! <<< \n");
		}
		else {
			System.out.println("\n >>> 로그인 실패!! <<< \n");
		}
		
		return loginLibDto;
		
	}// end of private Librarian_DTO librarian_login(Scanner sc)
	
	private void userMyPage(Scanner sc, UserDTO loginUserDto) {
		  
	     
	     
	     
	      System.out.println("\n>>> ----- 마이페이지 ["+ loginUserDto.getName() +"님 로그인중..] ----- <<<\n"
	              +"1.내 정보 조회  2.내 정보 변경  3.도서 예약 정보 확인  4.대출 조회  5.반납 연기  6.반납 이력\n"
	              +"7.포인트 충전  8.포인트 조회  9.뒤로가기 \n");
	      
	      
	      do {
	         
	         System.out.print("▷ 메뉴번호 선택 : ");
	         String menuNo = sc.nextLine();
	   
	         switch (menuNo) {
	         case "1":   // 내 정보 조회
	            System.out.println(loginUserDto.toString());
	            
	            break;
	            
	         case "2":   // 내 정보 변경
	            updateMyInfo(loginUserDto , sc);
	            
	            break;
	         case "3":   // 도서 예약 정보 확인
	            selectMyResvInfo(loginUserDto.getUser_seq());
	            break; 
	         case "4":   // 대출 조회
	            selectMyLoan(loginUserDto.getUser_seq());
	            
	            break;
	         case "5":   // 반납 연기
	        	 returnDate(sc, loginUserDto);
	            break;
	         case "6":   // 반납 이력
	        	 returnHistory(loginUserDto);
	               break;
	         
	         case "7":   // 포인트 충전
	            pointRecharge(sc, loginUserDto);
	               break;
	         case "8":   // 포인트 조회
	             System.out.println("내 포인트 는 " + loginUserDto.getPoint()); 
	             break;
	         case "9":   // 포인트 조회
	             
	        	 
	        	 return;
	         default:
	            System.out.println("메뉴에 있는 번호만 입력해주세요!!");
	            break;
	         }
	      } while(true);
	      
	      

		
	}
	
	
	// 포인트 충전
	   private void pointRecharge(Scanner sc, UserDTO loginUserDto) {
	      
	      do {
	         try {
	            System.out.print("얼마를 충전하시겠습니까?");
	            
	            
	            int addPoint = sc.nextInt();
	            sc.nextLine();
	            
	            if(addPoint > 0) {
	               loginUserDto.setPoint(loginUserDto.getPoint() + addPoint);
	               
	               int n = userDao.addPoint(loginUserDto);
	               
	               if( n == 1) {
	                  System.out.println("포인트가 충전되었습니다.");
	               }
	               else {
	                  System.err.println("포인트 충전에 실패했습니다. ㅠㅠ");
	               }// end of if(n == 1)
	               break;
	            }
	            else {
	               System.out.println("0 이상의 숫자만 입력해주세요!");
	            }
	         }catch(InputMismatchException e) {
	            System.out.println("숫자만 입력해주세요.");
	            sc.nextLine();
	         }
	        
	      }while(true);
	   }// end of private void pointRecharge(Scanner sc, UserDTO loginUserDto)




	   // **** 일반 회원가입을 해주는 메서드 **** //
	   private void userRegister(Scanner sc) {
	      
	      System.out.println("\n >>> ---- 일반 회원가입 ---- <<<");

	      // 정보를 입력받기 위한 DTO 1 ROW
	      UserDTO userDto = new UserDTO();
	      
	      // 회원은 사용가능한 아이디값을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 아이디 입력
	         System.out.print("1.아이디 : ");
	         String user_id = sc.nextLine();
	         
	         // 사용자가 입력한 값이 존재하는 아이디인지 확인
	         // isIdDuplicated가 true일 경우 중복되는 아이디 존재
	         if(isUserIdDuplicated(user_id)) {
	            // 존재하는 경우 불가능
	            System.out.println("[경고] 존재하는 아이디입니다.");
	         
	         } else {
	            // 존재하지 않는 경우 해당 값을 사용
	            // setter 에서 진행한 유효성 검사도 같이 한다.
	            userDto.setId(user_id);
	         }
	         
	   
	      } while(userDto.getId() == null); // 회원의 아이디가 null 이라면 값이 들어가지 않았으므로 계속 반복
	   
	      
	      // 회원은 사용가능한 비밀번호값을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 비밀번호 입력
	         System.out.print("2.비밀번호 : ");
	         String user_pw = sc.nextLine();
	         
	         // 사용자가 입력한 비밀번호가 우리가 만든 비밀번호 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setPw(user_pw);
	         
	      } while(userDto.getPw() == null); // 회원의 패스워드가 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 회원은 사용가능한 이름을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이름 입력
	         System.out.print("3.회원명 : ");
	         String user_name = sc.nextLine();
	         
	         // 사용자가 입력한 이름이 우리가 만든 이름 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setName(user_name);
	         
	      } while(userDto.getName() == null); // 회원의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 회원은 사용가능한 연락처를 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 연락처 입력
	         System.out.print("4.연락처(휴대폰) : ");
	         String user_tel = sc.nextLine();
	         
	         // 사용자가 입력한 연락처가 우리가 만든 연락처 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setTel(user_tel);
	         
	      } while(userDto.getTel() == null); // 회원의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	      // 회원은 사용가능한 이메일을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이메일 입력
	         System.out.print("5.이메일 : ");
	         String user_email = sc.nextLine();
	         
	         // 사용자가 입력한 이메일이 우리가 만든 이메일 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setEmail(user_email);
	         
	      } while(userDto.getEmail() == null); // 회원의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.   
	      
	      int n = userDao.userRegister(userDto);
	      
	      if(n == 1) {
	         System.out.println("회원가입에 성공하셨습니다.");
	      } else {
	         System.out.println("회원가입에 실패하셨습니다.");
	      }
	   } // end of private void userRegister(Scanner sc)----------
	   

	   
	   
	   // **** 사서 회원가입을 해주는 메서드 **** //
	   private void librarianRegister(Scanner sc) {
	      
	      System.out.println("\n >>> ---- 사서 회원가입 ---- <<<");

	      // 정보를 입력받기 위한 DTO 1 ROW
	      LibrarianDTO libDto = new LibrarianDTO();
	      
	      // 사서는 사용가능한 아이디값을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 아이디 입력
	         System.out.print("1.아이디 : ");
	         String lib_id = sc.nextLine();
	         
	         // 사용자가 입력한 값이 존재하는 아이디인지 확인
	         // isIdDuplicated가 true일 경우 중복되는 아이디 존재
	         if(isLibIdDuplicated(lib_id)) {
	            // 존재하는 경우 불가능
	            System.out.println("[경고] 존재하는 아이디입니다.");
	         
	         } else {
	            // 존재하지 않는 경우 해당 값을 사용
	            // setter 에서 진행한 유효성 검사도 같이 한다.
	            libDto.setId(lib_id);
	         }
	         
	   
	      } while(libDto.getId() == null); // 사서의 아이디가 null 이라면 값이 들어가지 않았으므로 계속 반복
	   
	      
	      // 사서는 사용가능한 비밀번호값을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 비밀번호 입력
	         System.out.print("2.비밀번호 : ");
	         String lib_pw = sc.nextLine();
	         
	         // 사용자가 입력한 비밀번호가 우리가 만든 비밀번호 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setPw(lib_pw);
	         
	      } while(libDto.getPw() == null); // 사서의 패스워드가 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 사서는 사용가능한 이름을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이름 입력
	         System.out.print("3.사서명 : ");
	         String lib_name = sc.nextLine();
	         
	         // 사용자가 입력한 이름이 우리가 만든 이름 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setName(lib_name);
	         
	      } while(libDto.getName() == null); // 사서의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 사서는 사용가능한 연락처를 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 연락처 입력
	         System.out.print("4.연락처(휴대폰) : ");
	         String lib_tel = sc.nextLine();
	         
	         // 사용자가 입력한 연락처가 우리가 만든 연락처 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setTel(lib_tel);
	         
	      } while(libDto.getTel() == null); // 사서의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	      // 사서는 사용가능한 이메일을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이메일 입력
	         System.out.print("5.이메일 : ");
	         String lib_email = sc.nextLine();
	         
	         // 사용자가 입력한 이메일이 우리가 만든 이메일 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setEmail(lib_email);
	         
	      } while(libDto.getEmail() == null); // 사서의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.   
	      
	      int n = libDao.libRegister(libDto);
	      
	      if(n == 1) {
	         System.out.println("사서 회원가입에 성공하셨습니다.");
	      } else {
	         System.out.println("회원가입에 실패하셨습니다.");
	      }
	      
	   } // end of private void librarianRegister(Scanner sc)----------
	   

	   // 회원이 입력한 값이 존재하는 아이디인지 확인
	   private boolean isUserIdDuplicated(String user_id) {
	      // true 가 return 되면 중복되는 아이디 존재
	      // false 가 return 되면 중복되는 아이디 없음
	      return userDao.checkIdExists(user_id);
	      
	   } // end of private void isIdDuplicated(String user_id)----------


	   // 사서가 입력한 값이 존재하는 아이디인지 확인
	   private boolean isLibIdDuplicated(String lib_id) {
	      // true 가 return 되면 중복되는 아이디 존재
	      // false 가 return 되면 중복되는 아이디 없음
	      return libDao.checkIdExists(lib_id);
	      
	   } // end of private boolean isLibIdDuplicated(String lib_id)----------

	
	   
	   
	   // === 베스트 셀러 조회 메서드 === //
    
       public static void bestSeller(Scanner sc) {
    	
    	UserDAO dao =new UserDAO_imple();
    	
    	while(true) {
    	List<BookDTO> list = dao.bestSeller();
    	
    	System.out.println("=".repeat(40));
    	System.out.println(">>> 베스트셀러 <<<");
    	System.out.println("=".repeat(40));
    	System.out.println("순위\t도서명\t저자\t출판사\t출판일\t대여횟수");
    	System.out.println("-".repeat(40)+"\n");
    	
    	int rank = 1;
    	
    	for(BookDTO dto : list) {
    		
    		System.out.println(rank++ + "\t"
    				           +dto.getBook_name()+"\t"
    				           +dto.getAuthor()+"\t" 
    				           +dto.getPublisher()+"\t"
    				           +dto.getPub_year()+"\t"
    				           +dto.getLoan_count()
    				           );
    	}//end of for--
    	
    	System.out.print("뒤로가기(엔터) : ");

        String menu = sc.nextLine();

        if (menu.isEmpty()) {
            return;
        } else {
            System.out.println("엔터만 눌러주세요!");
        } 

    	}
    	
    }//end of public static void bestSeller--- 

    
    
      //=== 도서 검색하는 메서드 ===//
    
      public static void searchBook(Scanner sc,int user_seq) {
    	
        UserDAO dao = new UserDAO_imple();

    	System.out.println("=".repeat(30));
    	System.out.println(">>>도서 검색<<<");
    	System.out.println("=".repeat(30));
    	
        
        System.out.println("1.도서명 검색");
        System.out.println("2.저자 검색");
        System.out.println("3.출판사 검색");
        System.out.println("4.카테고리 검색");
        System.out.println("0.이전 메뉴");
        
        
        
        while(true) {
        	
	       String type = "";
	       String keyword = "";
	           
	        System.out.print("번호 선택 : ");
	        
	        
	        String menu = sc.nextLine();
	        
	    	switch(menu) {
	    	 case "1":
	    		   type="book_name";
	    	       System.out.print("▷ 검색어 입력: ");    // 도서명 검색
	    	       keyword=sc.nextLine();
	    	        break;
	
	    	    case "2":
	    	    	type="author";
	    	       System.out.print("▷ 검색어 입력: ");    // 저자명 검색
	    	       keyword=sc.nextLine();
	    	        break;
	    	        
	    	    case "3":
	    	    	type="publisher";
	    	    	System.out.print("▷ 검색어 입력: ");  // 출판사 검색
	    	    	keyword=sc.nextLine();
	    	    	break;
	    	    case "4":
	    	    	type="fk_category_id";
	    	    	System.out.print("▷ 검색어 입력: ");  // 카테고리 검색
	    	    	keyword=sc.nextLine();
	                break;
	
	    	    case "0":
	    	        return;  // 이전 메뉴(회원 메뉴)로 돌아감
	    	        
	    	     default:
					System.out.println("[경고] 메뉴에 있는 번호만 입력해주세요!!");
					continue;
	
	    	} //end of switch---
	    	

        
    	// 여기서 DB 검색
    	List<BookDTO> list = dao.searchBook(type, keyword);
    	if(list.isEmpty()) {
    	    System.out.println("찾으시는 검색 내용이 없습니다.");
    	    continue;   // 다시 검색 메뉴로
    	}


       // 도서 검색 결과 출력
	   System.out.println("=".repeat(30));
	   System.out.println(">>> 도서 검색 결과 <<<");
	   System.out.println("=".repeat(30));

       System.out.println("번호\tISBN\t출판사\t도서명\t저자\t대출상태\t예약상태");
       
        int no = 1;
         for(BookDTO dto : list) {      //도서 검색 결과 출력
              System.out.println( no +"\t"
                +dto.getIsbn()+ "\t"
                +dto.getPublisher()+"\t"
                +dto.getBook_name()+"\t"
                +dto.getAuthor()+"\t"
                +dto.getFk_category_id()+"\t"
                +dto.getLoanStatus()+"\t"
                +dto.getResvStatus()
             );
              no++; // 도서 조회 목록에 순서대로 번호 추가
                
         } //end of for---
           
        
        
        
        System.out.println("=".repeat(30));
        System.out.println("1. 도서 예약");
        System.out.println("0. 이전 메뉴");
        
	     
        
        	
        	 
        System.out.print("▷ 메뉴 선택 : ");

        menu = sc.nextLine();

        switch(menu) {
        case "1":
            if(user_seq == 0) {
                System.out.println("로그인 후 예약 가능합니다.");
                break;
            }

            reservation(sc,list,user_seq);
            break;

            

            case "0":
                return;
                
   	         default:
				System.out.println("메뉴에 있는 번호만 입력해주세요!!");
				continue;
				
            } //end of switch---
        break;
        } // end of while---
    	

    }// end of  public static void searchBook()---

      
	      //===도서예약 메서드===//
		  private static void reservation(Scanner sc, List<BookDTO> list,int user_seq) {
			  UserDAO dao =new UserDAO_imple();
			  
			    
			    System.out.println("=".repeat(30));
		    	System.out.println(">>>도서 예약<<<");
		    	System.out.println("=".repeat(30)+"\n");
		    	
		    	int num;
		    	
		    	while(true) {

		    	    try {

		    	        System.out.print("예약할 도서 번호 입력(0: 이전 메뉴) : ");
		    	        num = Integer.parseInt(sc.nextLine());
		    	        if(num == 0) {
		    	            return;
		    	        }
		    	       
		    	        
		    	        
 
		    	        BookDTO book = list.get(num - 1);
		    	        
		    	       

		    	        if("예약불가".equals(book.getResvStatus())) {
		    	            System.out.println("현재 예약 불가능한 도서입니다.");
		    	            continue;
		    	        }

		    	      
		    	        
		    	      
		    	        
		    	      //대여횟수와 예약 횟수 구해서 예약 가능 권수 확인
		    	     int loanCnt = dao.getLoanCount(user_seq);
		    	     int resvCnt = dao.getReservationCount(user_seq);

		    	     if(loanCnt + resvCnt >= 3 ) {
		    	         System.out.println("회원님의 예약횟수가 초과되어 예약이 불가능합니다.");
		    	        continue;
		    	     }
		    	        
		    	        
		    	        while(true) {
		    	        System.out.print(">> 정말로 예약을 하시겠습니까?[Y/N] => ");
		    			String yn = sc.nextLine();
		    			
		    	       
		    	        if("y".equalsIgnoreCase(yn)) {
		    	        	
                           int result=dao.reservation(user_seq, book.getIsbn());
                           if(result==1) {
			    	        System.out.println("선택 도서 : " + book.getBook_name());
			    	        System.out.println("ISBN : " + book.getIsbn());
			    	        System.out.println("예약이 완료되었습니다.!!");
			    	        // 여기서 예약 처리
                           }else {
                        	   System.out.println("예약에 실패하였습니다.");
                           }
			    	        break;
		    	        }else if("n".equalsIgnoreCase(yn)) {
		    	        	System.out.println("예약이 취소되었습니다.");
		    				return;
		    	        }else {
		    	        	System.out.println("Y또는N만 입력해주세요."); //Y와N을 입력안했을 경우
		    	        } 
		    	        }

		    	        

		    	    } catch(NumberFormatException e) {
		    	        System.out.println("숫자만 입력해주세요.");
		    	        
		    	    } catch(IndexOutOfBoundsException e) {
		    	        System.out.println("검색하신 번호가 존재하지 않습니다.");
		    	    } 
		    	   
		    	}//end of while--
		  }//end of  private static void reservation(Scanner sc)---
	
		  
	     

    
			//===희망도서 요청 메서드===//
		 private void requestWishBook(Scanner sc,int user_seq) {
			    UserDAO dao = new UserDAO_imple();
			    
                while(true) {
                	
		    	System.out.println("=".repeat(30));
		    	System.out.println(">>>희망도서 요청 <<<");
		    	System.out.println("=".repeat(30));
		    	
		    	System.out.println("1.희망도서 상세 입력 ");
		    	System.out.println("0.이전 메뉴 ");

				
		    	System.out.print("▷ 메뉴번호 선택 : ");
		    	
		    	String menu = sc.nextLine(); 
		    	switch (menu) {
		    	
		    	
				
		    	case "1":
		    		
		    		String wish_book_name;

	                   while(true) {
	                       System.out.print("도서명 입력: ");
	                       wish_book_name = sc.nextLine();

	                       if(wish_book_name.length() <= 50) {
	                           break;
	                       }

	                       System.out.println("50자 이하로 입력해주세요!");
	                   }

	                   String wish_book_author;
	                   while(true) {
	                       System.out.print("저자명 입력: ");
	                       wish_book_author = sc.nextLine();

	                       if(wish_book_author.length() <= 30) {
	                           break;
	                       }

	                       System.out.println("30자 이하로 입력해주세요!");
	                   }
	                   String wish_book_publisher;
	                   while(true) {
	                       System.out.print("출판사 입력: ");
	                       wish_book_publisher = sc.nextLine();

	                       if(wish_book_publisher.length() <= 10) {
	                           break;
	                       }

	                       System.out.println("10자 이하로 입력해주세요!");
	                   }             

		    	
		    	
		    	  WishBookDTO dto=new WishBookDTO();
		    	  
		    	  dto.setWish_book_name(wish_book_name);
		    	  dto.setWish_book_author(wish_book_author);
		    	  dto.setWish_book_publisher(wish_book_publisher);
		    	  while(true){
		    	  System.out.print(">> 정말로 신청을 하시겠습니까?[Y/N] => ");
	    			String yn = sc.nextLine();
	    			if("y".equalsIgnoreCase(yn)) {
	    			     int result=dao.requestWishBook(user_seq,dto);
	                     if(result==1) {
			    	      
			    	        System.out.println("희망도서 신청이 완료되었습니다.!!");
			    	      
	                     }else {
	                  	   System.out.println("희망도서 신청에 실패하였습니다.");
	                     }
			    	        break;
		    	        }else if("n".equalsIgnoreCase(yn)) {
		    	        	System.out.println("희망도서 신청이 취소되었습니다.");
		    				return;
		    	        }else {
		    	        	System.out.println("[경고] Y또는N만 입력해주세요."); //Y와N을 입력안했을 경우
		    	        }
		    	  }
		    	  break;
		    	
		    	case "0":
		    		return; //이전메뉴
		    		
		    	 default:
		             System.out.println("[경고] 메뉴에 있는 번호만 입력해주세요.");
		             break;
		    	}//end of switch---
		    	
		    	  
		    	  
		    	  
    	       
		    	
                }//end of while ---
		    	
		    	
		    	
		    	
			 
			}//end of private void requestWishBook(Scanner sc,List<WishBookDTO> list)---
	

		//===연체료 납부 메서드===//
         private void payOverdueFee(Scanner sc, UserDTO loginUserDto) {
            int fee = 0;
                System.out.println("회원님의 연체료는 " + loginUserDto.getOverdue_fee() + "원입니다.");
               
                if(loginUserDto.getOverdue_fee() != 0) {
                do {
                   try {
                      System.out.print("얼마 납부하시겠습니까? : ");
                      
                      fee = sc.nextInt();
                      sc.nextLine();
             
                      if (fee <= 0) {
                          System.out.println("0원보다 큰 금액을 입력해주세요.");
                          continue;
                      }// end of if (fee <= 0)
             
                      if (fee > loginUserDto.getOverdue_fee()) {
                          System.out.println("연체료보다 많은 금액은 납부할 수 없습니다.");
                          continue;    
                      } // end of if (fee > loginUserDto.getOverdue_fee())
                      
                      break;
                      
                      }catch(InputMismatchException e){
                          System.out.println("숫자만 입력해주세요."); 
                          sc.nextLine();
                      }
                }while(true);
                

                Map<String, Object> map = new HashMap<>();
                map.put("user_id", loginUserDto.getId());
                map.put("fee", fee);

                int n = userDao.payOverdueFee(map);
                
                if(n == 1) {
                   loginUserDto.setOverdue_fee(loginUserDto.getOverdue_fee() - fee);
                   
                   System.out.println("납부가 완료되었습니다.");
                   System.out.println("남은 연체료 : " + loginUserDto.getOverdue_fee() + "원");
                }//end of if(n == 1)
                  else {
                     System.out.println("납부가 완료되지 않았습니다.");
                  }
                }
                else
                   System.out.println("납부 안하셔도됩니다.");
                
                
          
          }// end of private void payOverdueFee(Scanner sc, UserDTO loginUserDto)

		  
		  
		  
      // 5. 반납 연기
         private void returnDate(Scanner sc, UserDTO loginUserDto) {
            
            boolean exist = false;
            
            // returnDate는 그 회원의 대출권수를 불러온다.
            List<Map<String,String>> returnList = loanDao.returnDate(loginUserDto.getUser_seq());
              
              StringBuilder sb = new StringBuilder();
              
              sb.append("-".repeat(40)+"\n");
              sb.append(" 대여상세번호     도서명      대여일자     반납기한일 \n");
              sb.append("-".repeat(40)+"\n");
              
              for( Map<String, String> map : returnList) {
                 
                 sb.append(map.get("LOAN_DETAIL_NO")+ "\t" +map.get("book_name")+"\t"+map.get("LOAN_DATE")+"\t"+map.get("RETURN_DUE_DATE")+"\n");
                 
              } // end of for( Map<String, String> map : returnList)
              
              System.out.println(sb.toString());
              
              System.out.print("연기할 도서의 대여번호를 입력하세요. : ");
              
              String menuNo = sc.nextLine();
              
              // 대출권수가 1권일때
              if(returnList.size() == 1) {
        
                 for(Map<String, String> map : returnList) {
  
                     if(map.get("LOAN_DETAIL_NO").equals(menuNo)) {
                        
                       exist = true;
                        
                        LocalDate loan_date = LocalDate.parse(map.get("LOAN_DATE"));
                        LocalDate RETURN_DUE_DATE = LocalDate.parse(map.get("RETURN_DUE_DATE"));
                        
                        long days = ChronoUnit.DAYS.between(loan_date, RETURN_DUE_DATE);
                        
                        // 연기 할것인지
                        if(days != 10) {
                           
                           int n = loanDao.returnAdd(menuNo);
                           
                           if(n == 1) {
                              System.out.println("연기 되셨습니다");
                           }
                           else {
                              System.out.println("연기 되지않았습니다");
                           }
                           
                        }
                        else {
                           System.out.println("더이상 연기 할 수 없습니다.");
                        }
                        
                         break;
                     }// end of if(map.get("LOAN_DETAIL_NO").equals(menuNo))
              
                
                 }// end of for(Map<String, String> map : returnList)
                 
                 if(!exist) {
                    System.out.println("해당 도서 상세 번호는 없는 번호입니다.");
                 }
  
               }// end of if(returnList.size() == 1)
              
              //대출권수가 2권일때
              else if(returnList.size() == 2) {
                
                 for(Map<String, String> map : returnList) {

                     if(map.get("LOAN_DETAIL_NO").equals(menuNo)) {
                        
                       int loanCnt = Integer.parseInt(map.get("cnt"));
                        
                       exist = true;
                       
                        LocalDate loan_date = LocalDate.parse(map.get("LOAN_DATE"));
                        LocalDate RETURN_DUE_DATE = LocalDate.parse(map.get("RETURN_DUE_DATE"));
                        
                        long days = ChronoUnit.DAYS.between(loan_date, RETURN_DUE_DATE);
                        
                        // 연기 할것인지
                        if(days != 10) {
                           
                           //같은 loan_no 값이 2개일때
                          if(loanCnt == 2) {
                              int n = loanDao.returnAdd2(menuNo);
                              
                              if(n == 1) {
                                 System.out.println("연기 되셨습니다");
                              }
                              else {
                                 System.out.println("연기 되지않았습니다");
                              }
                          }// end of if(loanCnt == 2)
                          //같은 loan_no 값이 1개일때
                          else {
                             int n = loanDao.returnAdd(menuNo);
                              
                              if(n == 1) {
                                 System.out.println("연기 되셨습니다");
                              }
                              else {
                                 System.out.println("연기 되지않았습니다");
                              }
                           
                          }
                        }
                        else {
                           System.out.println("더이상 연기 할 수 없습니다.");
                        }
                        
                         break;
                     }// end of if(map.get("LOAN_DETAIL_NO").equals(menuNo))
              
                     
                 }// end of for(Map<String, String> map : returnList)
                 
                 if(!exist) {
                    System.out.println("해당 도서 상세 번호는 없는 번호입니다.");
                 }
          

               }// end of else if(returnList.size() == 2)
              //대출권수가 3권일때
              else if(returnList.size() == 3) {
                // 대출권수의 loan_no이 3개가 같을때 
                 for(Map<String, String> map : returnList) {

                     if(map.get("LOAN_DETAIL_NO").equals(menuNo)) {
                        
                       int loanCnt = Integer.parseInt(map.get("cnt"));
                       
                       exist = true;
                       
                        LocalDate loan_date = LocalDate.parse(map.get("LOAN_DATE"));
                        LocalDate RETURN_DUE_DATE = LocalDate.parse(map.get("RETURN_DUE_DATE"));
                        
                        long days = ChronoUnit.DAYS.between(loan_date, RETURN_DUE_DATE);
                        
                        // 연기 할것인지
                        if(days != 10) {
                          //같은 loan_no 값이 3개일때
                          if (loanCnt == 3 || loanCnt == 2) {
                              int n = loanDao.returnAdd2(menuNo);
                              
                              if(n == 1) {
                                 System.out.println("연기 되셨습니다");
                              }
                              else {
                                 System.out.println("연기 되지않았습니다");
                              }
                          }// end of if (loanCnt == 3 || loanCnt == 2)
                          
                          //같은 loan_no 값이 1개일때
                          else {
                             int n = loanDao.returnAdd(menuNo);
                              
                              if(n == 1) {
                                 System.out.println("연기 되셨습니다");
                              }
                              else {
                                 System.out.println("연기 되지않았습니다");
                              }
                          }
                           
                        }
                        else {
                           System.out.println("더이상 연기 할 수 없습니다.");
                        }
                        
                         break;
                     }// end of if(map.get("LOAN_DETAIL_NO").equals(menuNo))
              
                     
                 }// end of for(Map<String, String> map : returnList)
                 
                 if(!exist) {
                    System.out.println("해당 도서 상세 번호는 없는 번호입니다.");
                 }
                 // 대출권수의 loan_no이 2개가 같을때
                 
                 
                 
                 // 대출권수의 loan_no이 다를때

               }// end of else if(returnList.size() == 3)
              
              else if(returnList.size() == 0){
                 System.out.println(" 반납할 도서가 없습니다.");
              }
               
         } // end of private void returnDate(UserDTO loginUserDto)

   
      // 6. 반납 이력
         private void returnHistory(UserDTO loginUserDto) {
           
            List<Map<String,String>> HistoryList = loanDao.returnHistory(loginUserDto.getUser_seq());
            
            if(HistoryList.size() > 0) {
              
              StringBuilder sb = new StringBuilder();
              
              sb.append("-".repeat(30)+"\n");
              sb.append(" 대여상세번호   도서명  \n");
              sb.append("-".repeat(30)+"\n");
              
              for( Map<String, String> map : HistoryList) {
                 
                 sb.append(map.get("LOAN_DETAIL_NO")+ "\t" +map.get("book_name")+"\n");
                 
              } // end of for( Map<String, String> map : returnList)
              
              System.out.println(sb.toString());
           
            }
            else {
               System.out.println(" 반납 이력이 없습니다.");
            }
         }// end of private void returnHistory(UserDTO loginUserDto)
	

         // 내 정보 변경 메서드
         private void updateMyInfo(UserDTO loginUserDto, Scanner sc) {
         
            System.out.println(loginUserDto.toString());
            
            System.out.println("== [주의사항] 변경하지 않으려면 그냥 엔터하세요!! ==");
            
            // 1. 비밀번호 입력 및 유효성 검사
            String newPasswd;
            while (true) {
                System.out.print("▷ 비밀번호 : ");
                newPasswd = sc.nextLine();

                // [경우 A] 변경 없이 그냥 엔터를 친 경우 -> 기존 비밀번호 유지
                if (newPasswd.isBlank()) {
                    newPasswd = loginUserDto.getPw();
                    break;
                }

                // [경우 B] 새로운 비밀번호 입력 -> 유효성 검사 수행
                if (utils.isUsePasswd(newPasswd)) {
                    break; // 통과 시 루프 탈출 (loginUserDto에 아직 set 하지 않음!)
                } else {
                    System.out.println("[경고] 비밀번호는 영문자, 숫자, 특수기호가 혼합된 8~30자이어야 합니다. 다시 입력하세요.\n");
                }
            }
            
            // 2. 성명 입력 및 유효성 검사
            String newName;
            while (true) {
                System.out.print("▷ 성명 : ");
                newName = sc.nextLine();

                // 1) 엔터만 입력한 경우 -> 기존 로그인 유저의 이름 유지하고 탈출
                if (newName.isBlank()) {
                    newName = loginUserDto.getName();
                    break;
                }

                // 2) loginUserDto에 직접 set 시도
                loginUserDto.setName(newName);

                // 3) set 결과 DTO 내부의 이름이 내가 입력한 newName과 같아졌는지 검사
                if (newName.equals(loginUserDto.getName())) {
                    break; // 정규식 통과 -> 값이 저장되었으므로 루프 탈출
                }

                // 통과하지 못한 경우:
                // setName() 내부에서 [경고] 문구가 출력되고,
                // newName과 달라 루프를 계속 돈다 정규식에 맞지 않아 계속 반복한다.
            }          
            
            // 3. 연락처 입력 및 유효성 검사
            String newMobile;
            while (true) {
                System.out.print("▷ 연락처 : ");
                newMobile = sc.nextLine();

                // 1) 엔터(공백)만 입력한 경우 -> 기존 로그인 유저의 연락처 유지하고 탈출
                if (newMobile.isBlank()) {
                    newMobile = loginUserDto.getTel();
                    break;
                }

                // 2) loginUserDto에 직접 set 시도!
                loginUserDto.setTel(newMobile);

                // 3) set 후의 값(getTel)이 내가 입력한 newMobile과 동일한지 확인
                if (newMobile.equals(loginUserDto.getTel())) {
                    break; // 정규식 통과 -> 값이 저장되었으므로 루프 탈출
                }

                // 통과하지 못한 경우:
                // setTel() 내부에서 [경고] 문구가 출력되고
                // newMobile과 달라 루프를 계속 돈다. 정규식에 맞지 않아 계속 반복한다.
            }

            // 이렇게 복수개의 값은 Spring 에서 지원하지 않음
            // Java에서는 가능하지만 Spring 을 위해서 Map 으로 변환해서 전달
            Map<String, String> paraMap = new HashMap<>();

            paraMap.put("userseq", String.valueOf(loginUserDto.getUser_seq()));  // valueOf 는 String 을 int 로 형변환 해주는 함수
            paraMap.put("newPasswd", newPasswd);
            paraMap.put("newName", newName);
            paraMap.put("newMobile", newMobile);
            
            int n = userDao.updateMyinfo(paraMap);
            
            if(n == 1) {
               System.out.println("\n>>> 수정 완료 !!! <<< \n");
               
               System.out.println("===> 수정된 나의 정보 <===");

               
               System.out.println(loginUserDto);
            } else {
               System.out.println("변경 실패");
            }

         }// end of private void updateMyInfo(UserDTO loginUserDto, Scanner sc)----------



		    
		    
		    // 대출 조회
		    private void selectMyLoan(int userSeq) {
		       
		       /*
		       -----------------------------------------
		       대여상세번호    도서명      반납기한일  
		       -----------------------------------------
		       */
		       
		       
		       System.out.println("\n" + "-".repeat(30) + "대출 이력 조회" + "-".repeat(30));
		       System.out.println("대여상세번호    도서명    반납기한일    ");
		       System.out.println("\n" + "-".repeat(80));
		       
		       StringBuilder sb = new StringBuilder();
		       
		       List<Map<String, String>> loanList = loanDao.getloanList(userSeq);
		       
		       if(loanList.isEmpty()) {
		          System.out.println("\n" + "-".repeat(80));
		          System.out.println("대출 이력이 존재하지 않습니다.");
		          System.out.println("\n" + "-".repeat(80));
		          return;
		       }
		       
		       for(Map<String, String> loanMap : loanList) {
		          sb.append(loanMap.get("loan_detail_no") + "    ");
		          sb.append(loanMap.get("book_name") + "    ");
		          sb.append(loanMap.get("RETURN_DUE_DATE") + "\n");
		       } // end of for---------------------------------
		       
		       System.out.println(sb.toString());
		    
		    } // end of private void selectMyLoan()
		    
		    
		    // 관심 도서 조회
		    private void selectFavBook(String id) {
		       
		    /*
		    --------------------------------------------------------------------------
		    도서명    저자명     출판사     발행년도       대여료    도서내용
		    --------------------------------------------------------------------------
		    */
		       // 관심 도서 목록 조회
		       List<Map<String, String>> favList = bookDao.getFavList(id);
		       
		       // 없다면 존재하지 않음을 보여주고 끝내기
		       if(favList.isEmpty()) {
		          System.out.println("\n" + "-".repeat(80));
		          System.out.println("관심도서가 존재하지 않습니다.");
		          System.out.println("\n" + "-".repeat(80));
		          return;
		       }
		       
		       // 관심도서가 있다면 사용자에게 출력
		       System.out.println("\n" + "-".repeat(50) + "관심 도서 조회" + "-".repeat(50));
		       System.out.println("도서명    저자명    출판사    발행년도    대여료    도서내용    ");
		       System.out.println("\n" + "-".repeat(100));
		       
		       StringBuilder sb = new StringBuilder();
		       
		       for(Map<String, String> favMap : favList) {
		          sb.append(favMap.get("book_name") + "    ");
		          sb.append(favMap.get("author") + "    ");
		          sb.append(favMap.get("publisher") + "    ");
		          sb.append(favMap.get("pub_year") + "    ");
		          sb.append(favMap.get("rental_fee") + "원    ");
		          sb.append(favMap.get("contents") + "\n");
		       } // end of for-------------------------
		       
		       System.out.println(sb.toString());

		    } // end of private void selectFavBook(String id)----------
		    
		    

		       // 도서 예약 정보 확인
		       private void selectMyResvInfo(int user_seq) {
		          
		          /*
		             -------------------------------------------------------
		             예약번호    도서명   예약순번   예약등록일     예약상태   대출가능일자
		             -------------------------------------------------------
		          */
		    	   
				  // 로그인한 회원의 예약 목록 보기 메서드
		          List<Map<String, String>> resvList = resvDao.selectMyReservationList(user_seq);
		       
		          // 회원이 예약한 목록이 없는 경우
		          if(resvList.isEmpty()) {
		             System.out.println(" 현재 예약된 도서 내역이 없습니다. ");
		             return;
		          }
		          
		          System.out.println("\n"+"=".repeat(100));
		          System.out.println("[내가 예약한 도서 목록]");
		          System.out.println("예약번호    도서명   예약순번   예약등록일     예약상태   대출가능일자");
		          System.out.println("=".repeat(100));
		       
		          StringBuilder sb = new StringBuilder();
		          
		          // for문을 통해 존재하는 데이터는 sb에 담기
		          for(Map<String, String> resvMap : resvList) {
		             sb.append(resvMap.get("resv_detail_id") + "    ");
		             sb.append(resvMap.get("book_name") + "    ");
		             sb.append(resvMap.get("resv_rank") + "순위    ");
		             sb.append(resvMap.get("resv_date") + "    ");
		             
		             String status = "";
		             if(resvMap.get("loan_status").equals("0")) {
		                status = "대출가능";
		             } else if(resvMap.get("loan_status").equals("1")) {
		                status = "대출 중";
		             }
		             
		             sb.append(status + "    ");
		             sb.append(resvMap.get("possible_date") + "    " + "\n");
		          }
		          
		          System.out.println(sb.toString());
		       } // end of private void selectMyResvInfo(String id)----------
		       
		       		       
}
