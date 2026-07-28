package user_controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import common.CommonMember;
import librarian.domain.Librarian_DTO;
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
		Librarian_DTO loginLibDto = null;
		
		do {
			// 로그인 실패시
			if(isLoginSuccess == false) {
				System.out.println("\n--------------- 로그인전 ---------------\n"
				         + "1.베스트셀러    2.도서 검색    3. 로그인   4. 회원가입   \n");
		
				System.out.print("▷ 메뉴번호 선택 : ");
				String menuNo = sc.nextLine();
				
				switch (menuNo) {
				
					case "1":
						bestSeller();
						break;
					case "2":
						searchBook();
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
						
						break;
		
					default:
						break;
				}// end of switch (menuNo)
				
			} // end of if(isLoginSuccess == false)
			
			// 로그인 성공시 
			if(isLoginSuccess == true) {
				
				// 사서 로그인시
				if(loginLibDto != null) {
					
				}
				
				// 회원 로그인시
				else if(loginUserDto != null) {
					
					System.out.println("\n>>> ----- 시작메뉴 ["+ loginUserDto.getName() +"님 로그인중..] ----- <<<\n"
					                   + "1~~~~~~~~~~~~~~~~~~(나중에 고쳐야함)      5.마이페이지\n");
			
					System.out.print("▷ 메뉴번호 선택 : ");
					String menuNo = sc.nextLine();
					switch (menuNo) {
					case "1":
						
						break;
					case "2":
						
						break;
					case "3":
						
						break;
					case "4":
						
						break;
					case "5": // 마이페이지
					    
						userMyPage(sc, loginUserDto); // 로그인된 회원의 마이페이지
						break;
			
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
		
		UserDTO loginUserDto = userDao.UserLogin(paraMap);
		
		if(loginUserDto != null) {
			System.out.println("\n >>> 로그인 성공!! <<< \n");
		}
		else {
			System.out.println("\n >>> 로그인 실패!! <<< \n");
		}
		
		return loginUserDto;
	} // end of private UserDTO member_login(Scanner sc)
	
	
	// 사서 로그인 메서드
	private Librarian_DTO librarian_login(Scanner sc) {
		
		System.out.println("\n >>> --- 사서 로그인 --- <<<");
		
		System.out.print("▷ 아이디 : ");
		String userid = sc.nextLine();
		
		System.out.print("▷ 비밀번호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		Librarian_DTO loginLibDto = libDao.LibLogin(paraMap);
		
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
                + "1~~~~~~~~~~~~~~~~~~(나중에 고쳐야함)      5.마이페이지\n");

		System.out.print("▷ 메뉴번호 선택 : ");
		String menuNo = sc.nextLine();
		
	}
	
	// === 베스트 셀러 조회 메서드 === //
    
    public static void bestSeller() {
    	
    	BookDAO dao =new BookDAO_imple();
    	List<BookDTO> list = dao.bestSeller();
    	
    	System.out.println("=".repeat(30));
    	System.out.println("\n>>>베스트셀러<<<");
    	System.out.println("=".repeat(30));
    	System.out.println("순위\t도서명\t저자\t출판사\t출판일");
    	System.out.println("-".repeat(30));
    	
    	int rank = 1;
    	
    	for(BookDTO dto : list) {
    		
    		System.out.println(rank++ + "\t"
    				           +dto.getBook_name()+"\t"
    				           +dto.getAuthor()+"\t"
    				           +dto.getPub_year()+"\t"
    				           +dto.getPublisher() 
    				           );
    	}
    	
    	System.out.println( );
    	System.out.println(0.ㅇ );
    	
    }//end of public static void bestSeller 

    
    
    //=== 도서 검색하는 메서드 ===//
    
    public static void searchBook() {
    	
    	
    	
    	

    }

	
	
	

}
