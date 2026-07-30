package user_controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import book.domain.BookDTO;
import common.CommonMember;
import librarian.controller.LibrarianController;
import librarian.domain.LibrarianDTO;
import librarian.model.LibrarianDAO;
import librarian.model.LibrarianDAO_imple;
import user.domain.UserDTO;
import user.model.UserDAO;
import user.model.UserDAO_imple;


public class UserController {
	
	UserDAO userDao = new UserDAO_imple();
	LibrarianDAO libDao = new LibrarianDAO_imple();

	
	// *** 시작메뉴를 보여주는 메서드 ***
	public void mainstart(Scanner sc) {
		
		boolean isLoginSuccess = false;
		UserDTO loginUserDto = null;
		LibrarianDTO loginLibDto = null;
		
		do {
			// 로그인 실패시
			if(isLoginSuccess == false) {
				System.out.println("\n--------------- 로그인전 ---------------\n"
				         + "1.베스트셀러    2.도서 검색    3. 로그인   4. 회원가입   \n");
		
				System.out.print("▷ 메뉴번호 선택 : ");
				String menuNo = sc.nextLine();
				
				switch (menuNo) {
				
					case "1":
						bestSeller(sc); // 베스트셀러 검색
						break;
					case "2":
						searchBook(sc,loginUserDto.getUser_seq()); // 도서 검색
						break;
					case "3":
						
							System.out.println("1. 회원로그인     2. 사서로그인");
							
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
									System.out.println("1이나 2만 입력해주세요.!!");
									break;
								}
						break;
					case "4":
			            do {
			               System.out.print("1. 회원회원가입   2. 사서회원가입");
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
		
					default:
						break;
				}// end of switch (menuNo)
				
			} // end of if(isLoginSuccess == false)
			
			// 로그인 성공시 
			if(isLoginSuccess == true) {
				
				// 사서 로그인시
				if(loginLibDto != null) {
					// 사서 Controller Menu method 호출
					//LibrarianController.startMenu(loginLibDto, sc);
				}
				
				// 회원 로그인시
				else if(loginUserDto != null) {
					
					System.out.println("\n>>> ----- 시작메뉴 ["+ loginUserDto.getName() +"님 로그인중..] ----- <<<\n"
					     +"1.베스트셀러 2.도서 검색 3.희망도서 신청 4.연체료 납부 5.마이페이지 6.로그아웃\n");
			
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
						requestWishBook(sc);  // 희망도서 신청
						break;
					case "4":
						payOverdueFee(sc);   // 연체료 납부
						break;
					case "5": // 마이페이지
					    
						userMyPage(sc, loginUserDto); // 로그인된 회원의 마이페이지
						break;
					case "6": // 로그아웃
						loginUserDto = null;
						isLoginSuccess = false;
						System.out.println(">>> 로그아웃 되었습니다. <<<\n");
			
					default:
						System.out.println("메뉴에 있는 번호만 입력해주세요!!");
						break;
					}
					
				}
			
				
			}// end of if(isLoginSuccess == true)
			
			
			
		}while(true); // end of do ~ while문---------------------------
		
		
		
		
	} // end of public void mainstart(Scanner sc)----------


	
     


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
		
		System.out.println("\n>>> ----- 시작메뉴 ["+ loginUserDto.getName() +"님 로그인중..] ----- <<<\n"
             + "1~~~~~~~~~~~~~~~~~~(나중에 고쳐야함)         5.마이페이지 \n");

		System.out.print("▷ 메뉴번호 선택 : ");
		String menuNo = sc.nextLine();
		
	}
	
	// **** 회원 회원가입을 해주는 메서드 **** //
	   private void userRegister(Scanner sc) {
	      
	      System.out.println("\n >>> ---- 회원 회원가입 ---- <<<");

	      // 정보를 입력받기 위한 DTO 1 ROW
	      UserDTO userDto = new UserDTO();
	      
	      // 회원은 사용가능한 아이디값을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 아이디 입력
	         System.out.print("1. 아이디 : ");
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
	         System.out.print("2. 비밀번호 : ");
	         String user_pw = sc.nextLine();
	         
	         // 사용자가 입력한 비밀번호가 우리가 만든 비밀번호 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setPw(user_pw);
	         
	      } while(userDto.getPw() == null); // 회원의 패스워드가 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 회원은 사용가능한 이름을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이름 입력
	         System.out.print("3. 회원명 : ");
	         String user_name = sc.nextLine();
	         
	         // 사용자가 입력한 이름이 우리가 만든 이름 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setName(user_name);
	         
	      } while(userDto.getName() == null); // 회원의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 회원은 사용가능한 연락처를 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 연락처 입력
	         System.out.print("4. 연락처(휴대폰) : ");
	         String user_tel = sc.nextLine();
	         
	         // 사용자가 입력한 연락처가 우리가 만든 연락처 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setTel(user_tel);
	         
	      } while(userDto.getTel() == null); // 회원의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	      // 회원은 사용가능한 이메일을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이메일 입력
	         System.out.print("5. 이메일 : ");
	         String user_email = sc.nextLine();
	         
	         // 사용자가 입력한 이메일이 우리가 만든 이메일 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         userDto.setEmail(user_email);
	         
	      } while(userDto.getEmail() == null); // 회원의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.   
	      
	      int n = userDao.userRegister(userDto);
	      
	      if(n == 1) {
	         System.out.println("회원 회원가입에 성공하셨습니다.");
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
	         System.out.print("1. 아이디 : ");
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
	         System.out.print("2. 비밀번호 : ");
	         String lib_pw = sc.nextLine();
	         
	         // 사용자가 입력한 비밀번호가 우리가 만든 비밀번호 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setPw(lib_pw);
	         
	      } while(libDto.getPw() == null); // 사서의 패스워드가 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 사서는 사용가능한 이름을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이름 입력
	         System.out.print("3. 사서명 : ");
	         String lib_name = sc.nextLine();
	         
	         // 사용자가 입력한 이름이 우리가 만든 이름 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setName(lib_name);
	         
	      } while(libDto.getName() == null); // 사서의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	   
	      // 사서는 사용가능한 연락처를 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 연락처 입력
	         System.out.print("4. 연락처(휴대폰) : ");
	         String lib_tel = sc.nextLine();
	         
	         // 사용자가 입력한 연락처가 우리가 만든 연락처 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setTel(lib_tel);
	         
	      } while(libDto.getTel() == null); // 사서의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.
	      
	      // 사서는 사용가능한 이메일을 입력할 때까지 반복해야 한다.
	      do {
	         // 사용자 이메일 입력
	         System.out.print("5. 이메일 : ");
	         String lib_email = sc.nextLine();
	         
	         // 사용자가 입력한 이메일이 우리가 만든 이메일 규칙과 일치하는지 봐야한다.
	         // 만약 일치하지 않는다면 사용 불가능하다는 메시지가 나온다.
	         libDto.setEmail(lib_email);
	         
	      } while(libDto.getEmail() == null); // 사서의 이름이 null 이면 값이 들어가지 않았으므로 계속 반복한다.   
	      
	      int n = libDao.libRegister(libDto);
	      
	      if(n == 1) {
	         System.out.println("회원 회원가입에 성공하셨습니다.");
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
    	List<BookDTO> list = dao.bestSeller();
    	
    	System.out.println("=".repeat(30)+"\n");
    	System.out.println(">>>베스트셀러<<<\n");
    	System.out.println("=".repeat(30)+"\n");
    	System.out.println("순위\t도서명\t저자\t출판사\t출판일\n");
    	System.out.println("-".repeat(30)+"\n");
    	
    	int rank = 1;
    	
    	for(BookDTO dto : list) {
    		
    		System.out.println(rank++ + "\t"
    				           +dto.getBook_name()+"\t"
    				           +dto.getAuthor()+"\t"
    				           +dto.getPub_year()+"\t"
    				           +dto.getPublisher() 
    				           );
    	}
    	
    	System.out.println("0.이전 메뉴");  //이전 메뉴(회원메뉴)로 돌아감 
    	System.out.print("번호 선택 : ");

    	String menu = sc.nextLine();

    	if(menu.equals("0")) {
    	    return;
    	    
    	} else{
    		System.out.println("메뉴에 있는 번호만 입력해주세요!!");
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
	    	       System.out.println(" 검색어 입력: ");    // 도서명 검색
	    	       keyword=sc.nextLine();
	    	        break;
	
	    	    case "2":
	    	    	type="author";
	    	       System.out.println(" 검색어 입력: ");    // 저자명 검색
	    	       keyword=sc.nextLine();
	    	        break;
	    	        
	    	    case "3":
	    	    	type="publisher";
	    	    	System.out.println(" 검색어 입력: ");  // 출판사 검색
	    	    	keyword=sc.nextLine();
	    	    	break;
	    	    case "4":
	    	    	type="fk_category_id";
	    	    	System.out.println(" 검색어 입력: ");  // 카테고리 검색
	    	    	keyword=sc.nextLine();
	                break;
	
	    	    case "0":
	    	        return;  // 이전 메뉴(회원 메뉴)로 돌아감
	    	        
	    	     default:
					System.out.println("메뉴에 있는 번호만 입력해주세요!!");
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
        System.out.println("2. 관심도서 등록");
        System.out.println("0. 이전 메뉴");
        
	     
        
        	
        	 
        System.out.print("메뉴 선택 : ");

        menu = sc.nextLine();

        switch(menu) {
            case "1":
                reservation(sc,list,user_seq);   // 예약 
                break;

            case "2":
                favorite(sc,list);  // 관심 도서 등록
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
		    	        
		    	        

		    	      //대여횟수와 예약 횟수 구해서 예약 가능 권수 확인
		    	     int loanCnt = dao.getLoanCount(user_seq);
		    	     int resvCnt = dao.getReservationCount(user_seq);

		    	     if(loanCnt + resvCnt >= 3) {
		    	         System.out.println("회원님의 예약횟수가 초과되어 예약이 불가능합니다.");
		    	         return;
		    	     }
		    	        
		    	        
		    	        
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
		    				return;
		    	        }else {
		    	        	System.out.println("Y또는N만 입력해주세요."); //Y와N을 입력안했을 경우
		    	        }

		    	        

		    	    } catch(NumberFormatException e) {
		    	        System.out.println("숫자만 입력해주세요.");
		    	        
		    	    } catch(IndexOutOfBoundsException e) {
		    	        System.out.println("검색하신 번호가 존재하지 않습니다.");
		    	    }
		    	}//end of while--
		  }//end of  private static void reservation(Scanner sc)---
	
		  
	      //===관심 도서 등록===//
		  private static void favorite(Scanner sc, List<BookDTO> list) {
			
			
		 }

    
			//===희망도서 요청 메서드===//
		 private void requestWishBook(Scanner sc) {
				
				
			}
	

		   //===연체료 납부 메서드===//
		  private void payOverdueFee(Scanner sc) {
				
				
			}
	
	

}
