package librarian.controller;


import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



import book.domain.BookDTO;
import book.domain.LoanBookDTO;
import book.domain.WishBookDTO;
import book.model.BookDAO;
import book.model.BookDAO_imple;
import book.model.CategoryDAO;
import book.model.CategoryDAO_imple;
import book.model.LoanBookDAO;
import book.model.LoanBookDAO_imple;
import book.model.WishBookDAO;
import book.model.WishBookDAO_imple;
import dbconnection.ProjectDBConnection;
import librarian.domain.LibrarianDTO;
import loan.domain.LoanDAO;
import loan.domain.LoanDAO_imple;
import loan.domain.LoanDetailDAO;
import loan.domain.LoanDetailDAO_imple;

import reservation.model.ReservationDAO;
import reservation.model.ReservationDAO_imple;
import reservation.model.ResvDetailDAO;
import reservation.model.ResvDetailDAO_imple;
import user.domain.UserDTO;
import user.model.UserDAO;

import user.model.UserDAO_imple;





public class LibrarianController {

	ResvDetailDAO resvDetailDao = new ResvDetailDAO_imple();		// 예약상세
	ReservationDAO reservationDao = new ReservationDAO_imple();		// 예약
	LoanDAO loanDao = new LoanDAO_imple();							// 대여
	LoanDetailDAO loanDetailDao = new LoanDetailDAO_imple();		// 대여상세
	LoanBookDAO loanBookDao = new LoanBookDAO_imple();				// 대여도서
	
	WishBookDAO wishBookDao = new WishBookDAO_imple();				// 희망도서 
	UserDAO userDao = new UserDAO_imple();							// 회원
	//UserDAO2 userDao2 = new UserDAO2_imple();		
	
	CategoryDAO categoryDao = new CategoryDAO_imple();				// 카테고리
	BookDAO bookDao = new BookDAO_imple();							// 도서
	
	Connection conn = ProjectDBConnection.getConn();
	// controller
	
	
	
	
	private LibrarianDTO librarianDto;
	
	public boolean startMenu( LibrarianDTO librarian_dto, Scanner sc) {
		
		librarianDto = librarian_dto;
		
		do {
			System.out.println("---------------------------------------------------");
			System.out.println("1.대출/반납   2.회원관리  3.도서관리  4.마이페이지  5.로그아웃");
			System.out.println("---------------------------------------------------");
			
			System.out.print("▶ 메뉴번호 입력 : ");
			String menuNo = sc.nextLine();
			
			
			switch (menuNo) {
			case "1":	// 대출/반납
				rentAndReturn(sc);
				break;
			case "2":	// 회원관리
				userManagement(librarian_dto, sc);
				break;
			case "3":	// 도서관리
				bookManagement(sc);
				break;
			case "4":	// 마이페이지
				// myPage(librarian_dto, sc); 만들어야함
				break;
			case "5":	// 로그아웃
				return false;
			default:
				System.out.println("[경고] 메뉴에 없는 번호입니다. \n");
				break;
			}
		
		} while (true); 
		
	}

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 회원관리를 해주는 메서드
	private void userManagement(LibrarianDTO librarian_dto, Scanner sc) {
		
		do {
			System.out.println("\n회원관리 페이지입니다. 원하시는 메뉴를 입력하세요.");		
			System.out.println("1.회원목록조회        2.대출정지       3.나가기");		
			System.out.print("▶ 메뉴번호 입력: ");
			String menuNo2 = sc.nextLine();
			
			switch (menuNo2) {
				case "1":	//회원목록조회
					System.out.println("▷ 정렬 [1.회원명의 오름차순  /  2.회원명의 내림차순  / \n" 
	                    	+ "       3.가입일자의 오름차순 /  4.가입일자의 내림차순]");						
					System.out.print("정렬번호 선택 : ");
					String sortChoice = sc.nextLine();							
		
					if(sortChoice.isBlank()) { // 그냥 엔터나 공백만을 주면 1.회원명의 오름차순으로 해주겠다.
						sortChoice = "1";
					}
					
					showAllMember(sortChoice); //정렬방식에 따른 모든 회원조회						
					
					break;
					
				case "2":	//대출정지					
					int n = loanStop(librarian_dto, sc);	//특정 회원을 대출정지 하는 메서드.
					
					if(n == 1) {
						System.out.println(">> 대출정지 성공! <<");
					}
					else if(n == 0) {
						System.out.println(">> 대출정지 취소! <<");
					}
					else if(n == -1) {
						System.out.println(">> 대출정지 실패! <<");
					}					
					
					break;
					
				case "3":	//나가기
					return;
	
				default:
					System.out.println("[경고] 메뉴에 없는 번호입니다. \n");
					break;
			}// end of switch (menuNo2)----------------	
			
		}while(true);
		
	}

	
	

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// **** 모든회원조회를 해주는 메서드 **** //
	private void showAllMember(String sortChoice) {
	
		String[] arrNumber = {"1", "2", "3", "4"};
		boolean isFind = false;
		
		for(int i=0; i<arrNumber.length; i++) {
			
			if(arrNumber[i].equals(sortChoice)) {
				isFind = true;
				break;
			}
			
		}	//end of for(int i=0; i<arrNumber.length; i++) --------
		
		if(isFind) {
			
			List<UserDTO> mbrList = userDao.showAllMember(sortChoice);
			
			if(mbrList != null) {
				
				StringBuilder sb = new StringBuilder();
				
				sb.append("-".repeat(50) + "\n");			
				sb.append("회원번호 아이디 회원명 연락처 포인트 가입일자 대출정지여부 연체료\n");
				sb.append("-".repeat(50) + "\n");
				
				mbrList.forEach(mbrDto -> { 
//							String status = mbrDto.getStatus() == 1? "가입중":"탈퇴";
					sb.append(mbrDto.getUser_seq() + " " + 
							mbrDto.getId() + " " +
							mbrDto.getName() + " " + 
							mbrDto.getTel() + " " +
							mbrDto.getPoint() + " " +
							mbrDto.getRegisterday() + " " +
							mbrDto.getLoan_stop() + " " +
							mbrDto.getOverdue_fee() + " " +
							"\n");
				});
				System.out.println(sb);			
			}
			else {
				System.out.println(">> 가입된 회원이 1명도 없습니다. <<");
			}
		}
		else {
			System.out.println(">> 정렬에 없는 번호 입니다!! << \n");
		}		
		
		
	}	// end of private void showAllMember(String sortChoice) ----------------------------
		
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//특정 회원을 대출정지 하는 메서드.
	private int loanStop(LibrarianDTO loginLibDto, Scanner sc) {
		
		int result = 0;
		
		System.out.println("\n>>> 대출정지 <<<");
		
		System.out.println("1.사서명: " + loginLibDto.getName());
		System.out.print("2.대출정지 할 회원번호: " );
		String userSeq = sc.nextLine();	//"5"
										// "12315" -- 존재하지 않는 글번호
										// "fasdfdafdfdsf"
		
		if( userDao.isExistenceUserSeq(userSeq) ) {
			//입력한 회원번호가 존재하는 것이라면 
			//mbrDao.isExistenceUserSeq(userSeq) 은 true 가 된다.			
			
			String yn ="";
			do {
				System.out.print("▷ 정말로 대출정지를 하시겠습니까?[Y/N] : ");
				yn = sc.nextLine();
				
				if("y".equalsIgnoreCase(yn)) {
					
					/*
					    1. 입력받은 회원번호를 가지고 DB에서 회원의 정보중 LOAN_STOP 컬럼의 값을 1 로 변경한다.
					    ==> update 처리 
					    
					    2. update 처리가 성공되어지면 해당회원은 대출정지가 성공했다라는 메시지를 출력한다.
					    3. 마지막으로 result = 1; 로 해준다.
					*/
					
					int stopedUser = userDao.loanStop(userSeq);
					
					if(stopedUser == 1) {
						result = 1;
					}					
					
				}
				else if("n".equalsIgnoreCase(yn)) {
					
				}
				else {
					System.out.println(">> [경고] Y 또는 N만 입력하세요! << \n");
				}
			
			}while(! ("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)) );
			 
		}
		else {
			System.out.println(">> 회원번호 " + userSeq + "인 회원은 DB상에 존재하지 않습니다. << \n");
			result = -1;
		}		
		
		return result;
		
	}	//end of private int loanStop(LibrarianDTO loginLibDto, Scanner sc)-------------
		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//도서관리
	private void bookManagement(Scanner sc) {
		
		do {
			System.out.println("\n도서관리 페이지입니다. 원하시는 메뉴를 입력하세요.");		
			System.out.println("1.도서목록조회     2.도서등록        3.도서수정\n"
							+ "4.대여도서수정        5.도서삭제      6.희망도서조회\n"
							+ "7.나가기\n");	
			
			System.out.print("▶ 메뉴번호 입력: ");
			String menuNo3 = sc.nextLine();
			
			switch (menuNo3) {
				case "1":	//도서목록조회
					System.out.println("▷ 정렬 [1.도서명의 오름차순  /  2.도서명의 내림차순  / \n" 
	                    	+ "       3.도서ID 오름차순 /  4.도서ID의 내림차순]");						
					System.out.print("정렬번호 선택 : ");
					String sortChoice = sc.nextLine();							
		
					if(sortChoice.isBlank()) { // 그냥 엔터나 공백만을 주면 1.도서명의 오름차순으로 해주겠다.
						sortChoice = "1";
					}
					
					showAllBooks(sortChoice); //정렬방식에 따른 모든 도서조회
					
					break;
					
				case "2":	//도서등록
					
					insertBook(sc);
					break;
				case "3":	//도서수정
					
					updateBook(sc);
					break;	
				case "4":	//대여도서수정
					
					updateLoanBooks(sc);
					break;
					
				case "5":	//도서삭제
					
					deleteBook(sc);
					break;
					
				case "6":	//희망도서조회
					
					System.out.println("▷ 정렬 [1.도서명의 오름차순  /  2.도서명의 내림차순  / \n" 
	                    	+ "       3.신청일자의 오름차순 /  4.신청일자의 내림차순]");						
					System.out.print("정렬번호 선택 : ");
					String sortChoice2 = sc.nextLine();							
		
					if(sortChoice2.isBlank()) { // 그냥 엔터나 공백만을 주면 1.도서명의 오름차순으로 해주겠다.
						sortChoice2 = "1";
					}
					
					showAllWishBooks(sortChoice2); //정렬방식에 따른 모든 희망도서조회					
					
					break;
					
				case "7":	//나가기					
					return;
	
				default:
					System.out.println("[경고] 메뉴에 없는 번호입니다. \n");
					break;
			}// end of switch (menuNo3)----------------	
			
		}while(true);
		
	}// end of private void bookManagement(Scanner sc)----------------
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 정렬방식에 따른 모든 도서를 조회(select) 해주는 메서드
		private void showAllBooks(String sortChoice) {
			
			String[] arrNumber = {"1", "2", "3", "4"};
			boolean isFind = false;
			
			for(int i=0; i<arrNumber.length; i++) {
				
				if(arrNumber[i].equals(sortChoice)) {
					isFind = true;
					break;
				}
				
			}	//end of for(int i=0; i<arrNumber.length; i++) --------
			
			if(isFind) {
				
				List<LoanBookDTO> LoanBookList = loanBookDao.showAllBooks(sortChoice);
				
				if(LoanBookList != null) {
					
					StringBuilder sb = new StringBuilder();
					
					sb.append("-".repeat(50) + "\n");			
					sb.append("도서ID\t도서명\tISBN\t대출여부\t상태\n");
					sb.append("-".repeat(50) + "\n");
					
					LoanBookList.forEach(loanBookDto -> { 
//							String status = mbrDto.getStatus() == 1? "가입중":"탈퇴";
						sb.append(loanBookDto.getBook_id() + " " + 
								loanBookDto.getBook_name() + " " +	
								loanBookDto.getIsbn() + " " + 
								loanBookDto.getLoan_status_kor() + " " + 
								loanBookDto.getBook_status() + "\n");
					});
					System.out.println(sb);			
				}
				else {
					System.out.println(">> 도서가 한 권도 없습니다. <<");
				}
			}
			else {
				System.out.println(">> 정렬에 없는 번호 입니다!! << \n");
			}		
			
			
		}	// end of private void showAllBooks(String sortChoice)---------------

	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// 도서수정 메소드
	private void updateBook(Scanner sc) {
		
		// 1. 일단 도서목록 출력해주기
		// 일단 도서목록 가져와서 출력
		List<BookDTO> bookList = bookDao.getAllBooks();
		if (bookList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("-----------------------------------------------------------------------------------------------------\n");
			sb.append("ISBN   카테고리ID   도서명   저자명   출판사   발행년도   대여료\n");
			sb.append("-----------------------------------------------------------------------------------------------------\n");
			
			for (BookDTO book : bookList) {
				sb.append(book.getIsbn() + "   " 
						+ book.getFk_category_id() + "   " 
						+ book.getBook_name() + "   "
						+ book.getAuthor() + "   "
						+ book.getPublisher() + "   "
						+ book.getPub_year() + "   " 
						+ book.getRental_fee() + "\n");
			}
			sb.append("-----------------------------------------------------------------------------------------------------\n");
			
			System.out.println(sb.toString());
			
		}
		// 도서가 없다면
		else {
			System.out.println(">> 현재 등록된 도서가 없습니다.\n");
			return;
		}

		
		int isbn = 0;
		// 2. 변경할 도서의 ISBN을 입력받기.
		do {
			System.out.print("▶ 수정할 도서의 ISBN을 입력 : ");
			String inputIsbn = sc.nextLine();
			
			try {
				isbn = Integer.parseInt(inputIsbn);
				
				if (bookDao.isExistBook(isbn))
					break;
				else
					System.out.println("[경고] : 존재하지 않는 ISBN입니다.\n");
			} catch (NumberFormatException e) {
				System.out.println("[경고] : ISBN은 정수로 입력하십시오.\n");
			}
		} while(true);
		
		
		
		BookDTO bookDto = new BookDTO();
		bookDto = bookDao.getBookInfo(isbn);
		if (bookDto.getBook_name() == null ) {
			System.out.println(">> 해당 도서정보 불러오기에 실패하였습니다.\n");
			return;
		}
		
		System.out.println();
		System.out.println("-------------------------");
		System.out.println("1.도서명 : " + bookDto.getBook_name());
		System.out.println("2.저자명 : " + bookDto.getAuthor());
		System.out.println("3.출판사 : " + bookDto.getPublisher());
		System.out.println("4.발행년도 : " + bookDto.getPub_year());
		System.out.println("5.카테고리ID : " + bookDto.getFk_category_id());
		System.out.println("6.도서내용 : " + bookDto.getContents());
		System.out.println("7.대여료 : " + bookDto.getRental_fee());
		System.out.println("-------------------------\n");
		
		
		
		String bookName = "";		// 도서명
		String author = "";			// 저자명
		String publisher = "";		// 출판사
		String pubYear = "";
		String fkCategoryId = "";	// 카테고리(id)
		String contents = "";		// 도서내용
		String rentalFee = "";		// 대여료
		
		
		// 3. 수정해줄 데이터들 입력받기
		System.out.println("도서 수정을 시작합니다. 수정을 원치않는 정보는 엔터 또는 공백을 입력하십시오.");
		
		//--------------------------------------------------------------//
		
		do {
			System.out.println("▶ 도서명 : ");
			bookName = sc.nextLine();
			if(bookName.isBlank()) {
				bookName = bookDto.getBook_name();
				break;
			}
			
			if(bookName.length() > 50)
				System.out.println("[경고] 도서명은 50자 이내로 작성하세요.\n");
			else
				break;
			
		}while(true);
		
		
		do {
			System.out.println("▶ 저자명 : ");
			author = sc.nextLine();
			if(author.isBlank()) {
				author = bookDto.getAuthor();
				break;
			}
			
			if(author.length() > 30)
				System.out.println("[경고] 저자명은 30자 이내로 작성하세요.\n");
			else
				break;
			
		}while(true);
		
		
		do {
			System.out.println("▶ 출판사 : ");
			publisher = sc.nextLine();
			if(publisher.isBlank()) {
				publisher = bookDto.getPublisher();
				break;
			}
			
			if(publisher.length() > 10)
				System.out.println("[경고] 출판사는 10자 이내로 작성하세요.\n");
			else
				break;
		}while(true);
		
		//--------------------------------------------------------------//
		// 발행년도 입력
		do {
			try {
				
				System.out.println("▶ 발행년도(yyyy-MM-dd) : ");
				pubYear = sc.nextLine();
				if(pubYear.isBlank()) {
					pubYear = bookDto.getPub_year();
					break;
				}
				
				Date today = new Date();
				// 'yyyy-dd-mm'의 형태로 받는다.
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        
			    sdf.setLenient(false); 
		    
				Date inputPubYear = sdf.parse(pubYear);
				
				// 오늘 날짜보다 이전 날짜여야 한다.
				if (inputPubYear.getTime() > today.getTime()) {
					System.out.println("[경고] 발행년도는 현재보다 미래일 수 없습니다.\n");
					continue;
				}
				
				break;
			} catch (ParseException e) {
				System.out.println("[경고] 발행년도는 yyyy-MM-dd 형식으로 입력하세요.\n");
			}
		}while(true);
		
		//--------------------------------------------------------------//
		// 카테고리 입력
		// 카테고리 목록 출력하기
		List<Map<String, String>> categoryList = categoryDao.getCategories();
		if(categoryList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("--------------------------\n");
			sb.append("카테고리ID  카테고리명\n");
			sb.append("--------------------------\n");
			
			for(Map<String, String> map : categoryList) {
				sb.append(map.get("CATEGORY_ID") + "       " + map.get("CATEGORY_NAME") + "\n");
			}
			
			System.out.println(sb.toString());
			System.out.println("");
		}
		do {
			System.out.println("▶ 카테고리ID : ");
			fkCategoryId = sc.nextLine();
			if(fkCategoryId.isBlank()) {
				fkCategoryId = bookDto.getFk_category_id();
				break;
			}
			
			boolean isExistCategoryId = false;
			for(Map<String, String> map : categoryList) {
				if (fkCategoryId.equals(map.get("CATEGORY_ID"))) {
					isExistCategoryId = true;
					break;
				}
			}
			
			if (isExistCategoryId) {
				break;
			} else {
				System.out.println("[경고] 카테고리 목록에 존재하는 카테고리ID만 입력하세요.\n");
			}
			
		} while (true);
		
		
		//--------------------------------------------------------------//
		// 도서내용 입력
		do {
			System.out.println("▶ 도서내용 : ");
			contents = sc.nextLine();
			if(contents.isBlank()) {
				contents = bookDto.getContents();
				break;
			}
			// 도서내용은 100자를 넘지 않도록 한다.
			
			if(contents.length() > 100)
				System.out.println("[경고] 도서내용은 100자 이내로 작성하세요.\n");
			else
				break;
		} while(true);
		
		//--------------------------------------------------------------//
		
		// 대여료 입력
		do {
			System.out.println("▶ 대여료 : ");
			rentalFee = sc.nextLine();
			if (rentalFee.isBlank()) {
				rentalFee = String.valueOf(bookDto.getRental_fee());
				break;
			}
			// 대여료는 양수인 정수로만 입력받는다.
			
			
			try {
				int fee = Integer.parseInt(rentalFee);
				
				if(fee < 0) {
					System.out.println("[경고] 대여료는 음수가 될 수 없습니다.\n");
				} 
				else
					break;
			} catch (NumberFormatException e) {
				System.out.println("[경고] 대여료는 정수로 입력하세요.\n");
			}
		} while (true);
		
		//--------------------------------------------------------------//
		
		
		
		// 도서정보 삽입하기
		bookDto = new BookDTO();
		bookDto.setIsbn(isbn);
		bookDto.setBook_name(bookName);
		bookDto.setAuthor(author);
		bookDto.setPublisher(publisher);
		bookDto.setPub_year(pubYear);
		bookDto.setFk_category_id(fkCategoryId);
		bookDto.setContents(contents);
		bookDto.setRental_fee(Integer.parseInt(rentalFee));
		
		
		// 4. 수정사항 실제 적용하기 or 취소하기
		String yn = "";
		do {
			
			System.out.print("입력하신 사항대로 도서 정보를 수정하시겠습니까? [Y/N] : ");
			yn = sc.nextLine();
			
			if (yn.equalsIgnoreCase("y")) {
			
				if(bookDao.updateBookInfo(bookDto) == -1) {
					System.out.println(">> 도서정보 수정에 실패하였습니다.\n");
					return;
				}
				
				System.out.println(">> 도서정보가 수정되었습니다.\n");
			}
			else if (yn.equalsIgnoreCase("n")) {
				System.out.println(">> 도서정보 수정을 취소하였습니다.\n");

			}
			else {
				System.out.println("[경고] Y 또는 N만 입력하십시오.\n");
			}
			
			
			
		
		} while(!(yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("n")));
		
	}

	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 도서삭제 메소드
	private void deleteBook(Scanner sc) {
		// 해당 도서정보의 ISBN을 갖는 자식 대여도서가 있다면, 삭제하지 못하게 한다.
		
		// 일단 도서목록 가져와서 출력
		List<BookDTO> bookList = bookDao.getAllBooks();
		if (bookList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("-----------------------------------------------------------------------------------------------------\n");
			sb.append("ISBN   카테고리ID   도서명   저자명   출판사   발행년도   대여료\n");
			sb.append("-----------------------------------------------------------------------------------------------------\n");
			
			for (BookDTO book : bookList) {
				sb.append(book.getIsbn() + "   " 
						+ book.getFk_category_id() + "   " 
						+ book.getBook_name() + "   "
						+ book.getAuthor() + "   "
						+ book.getPublisher() + "   "
						+ book.getPub_year() + "   " 
						+ book.getRental_fee() + "\n");
			}
			sb.append("-----------------------------------------------------------------------------------------------------\n");
			
			System.out.println(sb.toString());
			
		}
		// 도서가 없다면
		else {
			System.out.println(">> 현재 등록된 도서가 없습니다.\n");
			return;
		}
		
		// 삭제할 도서의 ISBN 입력받기
		do {
			System.out.print("▶ 삭제할 도서의 ISBN을 입력 : ");
			int isbn = 0;
			try {
				isbn = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("[경고] ISBN은 정수로 입력하십시오.\n");
				continue;
			}
			 
			
			boolean isExist = false;
			for (BookDTO book : bookList) {
				// 입력받은 isbn이 존재한다면
				if (isbn == book.getIsbn()) {
					isExist = true;
					break;
				}
			}
			// 해당 isbn이 존재한다면
			if (isExist) {
				//System.out.println("good.");
				String yn="";
				
				do {
					
					System.out.print(">> 해당 도서를 삭제하시겠습니까? [Y/N] : ");
					yn = sc.nextLine();
					if (yn.equalsIgnoreCase("y")) {
						
						try {
							
							conn.setAutoCommit(false);
							
							if(bookDao.deleteBook(isbn) == -1) {
								System.out.println(">> 도서 삭제에 실패하였습니다.\n");
								conn.rollback();
								return;
							}
							
							System.out.println(">> 도서 삭제가 완료되었습니다.\n");
							conn.commit();
							
						} catch (SQLException e) {
							try {
								conn.rollback();
							} catch (SQLException e1) {

							}
						} finally {
							try {
								conn.setAutoCommit(true);	
							} catch (SQLException e) {

							}
							
						}

							
					}
					// 취소
					else if (yn.equalsIgnoreCase("n")) {
						System.out.println(">> 도서 삭제를 취소하였습니다.\n");
					}
					else {
						System.out.println("[경고] Y 또는 N만 입력하십시오.\n");
					}
				
					
				} while ( !( yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("n")) );
				
				
				
				break;
			}
			// 없다면
			else {
				System.out.println("[경고] 존재하지 않는 ISBN입니다.\n");	
			}
			
		} while(true);
		
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 대여도서 수정 메소드
		private void updateLoanBooks(Scanner sc) {
			// 대여도서 목록을 한번 출력해준다.
			// 이때, 현재 대출중인 도서는 수정 불가하므로 목록에서 제외한다.
			// 도서번호, 도서명, 대출여부, 상태 를 갖고와서 출력
			List<Map<String,String>> loanBookList = loanBookDao.getLoanBooks();
			
			if (loanBookList.size() > 0) {
				
				// 대여도서목록 출력
				StringBuilder sb = new StringBuilder();
				
				sb.append("-----------------------------------------------------------\n");
				sb.append("도서번호   도서명       대출여부  상태\n");
				sb.append("-----------------------------------------------------------\n");
				
				for (Map<String,String> map : loanBookList) {
					sb.append(map.get("BOOK_ID") + "   " + map.get("BOOK_NAME") + "  " + map.get("LOAN_STATUS") + "  " + map.get("BOOK_STATUS") + "\n" );
				}
				sb.append("-----------------------------------------------------------\n\n");
				
				System.out.println(sb.toString());
				
				
				
				boolean isExist = false;
				do {
					// 상태를 수정할 도서의 도서번호를 입력받는다.
					System.out.print("▶ 상태를 변경할 도서의 도서번호 입력 : ");
					String bookId = sc.nextLine();
					
					
					// 해당 도서번호가 존재하는지 검사한다.
					for (Map<String,String> map : loanBookList) {
						if (bookId.equals(map.get("BOOK_ID"))) {
							isExist = true;
							break;
						}
					}
					
					// 존재한다면
					if(isExist) {
						// 목록에서 도서번호를 선택하면, 적용할 도서상태를 고르게 한다.
						// 1.훼손  2.폐기  3.분실
						System.out.println("--------------------");
						System.out.println(" 1.훼손  2.폐기  3.분실");
						System.out.println("--------------------");
						
						boolean isSelected = false;
						String status = "";
						// 적용할 상태 선택하기
						do {
							System.out.print("▶ 적용할 상태를 선택(번호) : ");
							String no = sc.nextLine();
							
							switch (no) {
							case "1":
								status = "훼손";
								isSelected = true;
								break;
							case "2":
								status = "폐기";
								isSelected = true;
								break;
							case "3":
								status = "분실";
								isSelected = true;
								break;
							default:
								System.out.println("[경고] 존재하지 않는 상태번호입니다.\n");
								break;
							}
							
						} while(!isSelected);
						
						
						String yn = "";
						do {
							System.out.print("해당 도서의 상태를 " + status + "로 변경하시겠습니까? [Y/N] : " );
							yn = sc.nextLine();
							
							
							if (yn.equalsIgnoreCase("y")) {
								
								try {
									conn.setAutoCommit(false);
									// 적용하기
									if (loanBookDao.updateLoanBookStatus(bookId, status) == -1) {
										System.out.println(">> 상태 적용에 실패하였습니다.\n");
										conn.rollback();
									}
									
									System.out.println(">> 상태 적용이 완료되었습니다.\n");
									conn.commit();
									
								} catch (SQLException e) {
									try {
										conn.rollback();
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
								} finally {
									try {
										conn.setAutoCommit(true);
									} catch (SQLException e) {
									}
								}
								
								
							}
							else if (yn.equalsIgnoreCase("n")) {
								System.out.println(">> 대여도서 상태 수정을 취소하였습니다.\n");
							}
							else {
								System.out.println("[경고] Y 또는 N만 입력하십시오.\n");
							}
						} while(!(yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("n")));
						
						
						return;
						
					}
					// 없다면
					else {
						System.out.println("[경고] 존재하지 않는 도서번호입니다.\n");
					}
					
				} while(true);
			
				
			}
			else {
				System.out.println(">> 현재 도서관 내 수정가능한 도서가 없습니다.\n");
			}

			// 
			
			
			
		}
		
		
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// 도서등록 메소드
	private void insertBook(Scanner sc) {
		
		String bookName = "";		// 도서명
		String author = "";			// 저자명
		String publisher = "";		// 출판사
		String pubYear = "";
		String fkCategoryId = "";	// 카테고리(id)
		String contents = "";		// 도서내용
		String rentalFee = "";		// 대여료
		String bookCnt = "";		// 수량
		
		//--------------------------------------------------------------//
		
		// 도서명
		do {
			System.out.println("▶ 도서명 : ");
			bookName = sc.nextLine();
			
			if(bookName.isBlank()) {
				System.out.println("[경고] 도서명은 공백일 수 없습니다.\n");
			}
				
			if(bookName.length() > 50)
				System.out.println("[경고] 도서명은 50자 이내로 작성하세요.\n");
			else
				break;
			
		}while(true);
		
		// 저자명
		do {
			System.out.println("▶ 저자명 : ");
			author = sc.nextLine();
			
			if(author.isBlank()) {
				System.out.println("[경고] 저자명은 공백일 수 없습니다.\n");
			}
			
			if(author.length() > 30)
				System.out.println("[경고] 저자명은 30자 이내로 작성하세요.\n");
			else
				break;
			
		}while(true);
		
		// 출판사
		do {
			System.out.println("▶ 출판사 : ");
			publisher = sc.nextLine();
			
			if(publisher.isBlank()) {
				System.out.println("[경고] 출판사는 공백일 수 없습니다.\n");
			}
			
			if(publisher.length() > 10)
				System.out.println("[경고] 출판사는 10자 이내로 작성하세요.\n");
			else
				break;
			
		}while(true);

		
		//--------------------------------------------------------------//
		// 발행년도 입력
		do {
			try {
				
				System.out.println("▶ 발행년도(yyyy-MM-dd) : ");
				pubYear = sc.nextLine();
				
				Date today = new Date();
				// 'yyyy-dd-mm'의 형태로 받는다.
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        
			    sdf.setLenient(false); 
		    
				Date inputPubYear = sdf.parse(pubYear);
				
				// 오늘 날짜보다 이전 날짜여야 한다.
				if (inputPubYear.getTime() > today.getTime()) {
					System.out.println("[경고] 발행년도는 현재보다 미래일 수 없습니다.\n");
					continue;
				}
				
				break;
			} catch (ParseException e) {
				System.out.println("[경고] 발행년도는 yyyy-MM-dd 형식으로 입력하세요.\n");
			}
		}while(true);
		
		//--------------------------------------------------------------//
		// 카테고리 입력
		// 카테고리 목록 출력하기
		List<Map<String, String>> categoryList = categoryDao.getCategories();
		if(categoryList.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("--------------------------\n");
			sb.append("카테고리ID  카테고리명\n");
			sb.append("--------------------------\n");
			
			for(Map<String, String> map : categoryList) {
				sb.append(map.get("CATEGORY_ID") + "       " + map.get("CATEGORY_NAME") + "\n");
			}
			
			System.out.println(sb.toString());
			System.out.println("");
		}
		do {
			System.out.println("▶ 카테고리ID : ");
			fkCategoryId = sc.nextLine();
			
			boolean isExistCategoryId = false;
			for(Map<String, String> map : categoryList) {
				if (fkCategoryId.equals(map.get("CATEGORY_ID"))) {
					isExistCategoryId = true;
					break;
				}
			}
			
			if (isExistCategoryId) {
				break;
			} else {
				System.out.println("[경고] 카테고리 목록에 존재하는 카테고리ID만 입력하세요.\n");
			}
			
		} while (true);
		
		
		//--------------------------------------------------------------//
		// 도서내용 입력
		do {
			System.out.println("▶ 도서내용 : ");
			contents = sc.nextLine();
			// 도서내용은 100자를 넘지 않도록 한다.
			
			if(contents.isBlank()) {
				System.out.println("[경고] 도서내용은 공백일 수 없습니다.\n");
			}
			else if(contents.length() > 100)
				System.out.println("[경고] 도서내용은 100자 이내로 작성하세요.\n");
			else
				break;
		} while(true);
		
		//--------------------------------------------------------------//
		
		// 대여료 입력
		do {
			System.out.println("▶ 대여료 : ");
			rentalFee = sc.nextLine();
			// 대여료는 양수인 정수로만 입력받는다.
			
			if(rentalFee.isBlank()) {
				System.out.println("[경고] 대여료는 공백일 수 없습니다.\n");
				continue;
			}
			
			try {
				int fee = Integer.parseInt(rentalFee);
				
				if(fee < 0) {
					System.out.println("[경고] 대여료는 음수가 될 수 없습니다.\n");
				} 
				else
					break;
			} catch (NumberFormatException e) {
				System.out.println("[경고] 대여료는 정수로 입력하세요.\n");
			}
		} while (true);
		
		//--------------------------------------------------------------//
		
		
		// 수량 입력
		do {
			System.out.println("▶ 도서수량 : ");
			bookCnt = sc.nextLine();
			// 수량은 양수인 정수만 입력받고, 수량만큼 대여도서에 삽입한다.
			if(bookCnt.isBlank()) {
				bookCnt = "0";
			}
			
			try {
				int cnt = Integer.parseInt(bookCnt);
				
				if(cnt < 0) {
					System.out.println("[경고] 도서 수량은 음수가 될 수 없습니다.\n");
				}
				else 
					break;
			} catch (NumberFormatException e) {
				System.out.println("[경고] 도서 수량은 정수로 입력하세요.\n");
			}
		} while (true);
		
		//--------------------------------------------------------------//
		
		// 1. 도서정보 삽입하기
		BookDTO bookDTO = new BookDTO();
		bookDTO.setBook_name(bookName);
		bookDTO.setAuthor(author);
		bookDTO.setPublisher(publisher);
		bookDTO.setPub_year(pubYear);
		bookDTO.setFk_category_id(fkCategoryId);
		bookDTO.setContents(contents);
		bookDTO.setRental_fee(Integer.parseInt(rentalFee));
		
		try {
			conn.setAutoCommit(false);
			
			// 도서테이블에 삽입
			int n = bookDao.insertBook(bookDTO);
			if(n == -1) {
				System.out.println(">> 도서정보 등록에 실패하였습니다.\n");
				return;
			}
			
			
			
			// 2. 수량이 0 이상이라면, 대여도서에도 삽입하기
			if (Integer.parseInt(bookCnt) > 0) {
				// 해당하는 ISBN 가져오기
				int isbn = bookDao.getIsbn();
				if(isbn == -1) {
					System.out.println(">> isbn 가져오기에 실패하였습니다.\n");
					conn.rollback();
					return;
				}
	
				
				// 수량만큼 대여도서에 삽입
				n = loanBookDao.insertLoanBook(isbn, Integer.parseInt(bookCnt));
				if(n == -1) {
					System.out.println(">> 대여도서 등록에 실패하였습니다.\n");
					conn.rollback();
					return;
				}
			}
			
			System.out.println(">> 도서등록을 완료하였습니다.\n");
			conn.commit();
			
		
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				
			}
		} finally {
			try {
				conn.setAutoCommit(true);	
			} catch (SQLException e2) {
			}
			
		}
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 정렬방식에 따른 모든 희망도서를 조회(select) 해주는 메서드
	private void showAllWishBooks(String sortChoice) {
		
		String[] arrNumber = {"1", "2", "3", "4"};
		boolean isFind = false;
		
		for(int i=0; i<arrNumber.length; i++) {
			
			if(arrNumber[i].equals(sortChoice)) {
				isFind = true;
				break;
			}
			
		}	//end of for(int i=0; i<arrNumber.length; i++) --------
		
		if(isFind) {
			
			List<WishBookDTO> wishBookList = wishBookDao.showAllWishBooks(sortChoice);
			
			if(wishBookList != null) {
				
				StringBuilder sb = new StringBuilder();
				
				sb.append("-".repeat(50) + "\n");			
				sb.append("희망도서번호 회원번호 도서명 저자명 출판사 신청일\n");
				sb.append("-".repeat(50) + "\n");
				
				wishBookList.forEach(wishbookDto -> { 
//							String status = mbrDto.getStatus() == 1? "가입중":"탈퇴";
					sb.append(wishbookDto.getWish_book_no() + " " + 
							wishbookDto.getUser_seq() + " " +
							wishbookDto.getWish_book_name() + " " + 
							wishbookDto.getWish_book_author() + " " +
							wishbookDto.getWish_book_publisher() + " " +
							wishbookDto.getRequest_date() + " " +
							"\n");
				});
				System.out.println(sb);			
			}
			else {
				System.out.println(">> 희망도서가 한 권도 없습니다. <<");
			}
		}
		else {
			System.out.println(">> 정렬에 없는 번호 입니다!! << \n");
		}		
		
	
	}	// end of private void showAllHopeBooks(String sortChoice)--------------
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// === 대출/반납 메소드 ===
	private void rentAndReturn(Scanner sc) {


		
		do {
			
			System.out.println("----------------------------------");
			System.out.println("1.예약대출  2.일반대출  3.반납  4.뒤로가기");
			System.out.println("----------------------------------\n");
			
			
			System.out.print("▶ 메뉴번호 입력 : ");
			String menuNo = sc.nextLine();
	             
	
			switch (menuNo) {
			case "1":	// 예약대출
				reservedRent(sc);
				break;
			case "2":	// 일반대출
				normalRent(sc);
				break;
			case "3":	// 반납
				returnBooks(sc);
				break;
			case "4" :	// 뒤로가기
				return;
			default:
				System.out.println("[경고] 메뉴에 없는 번호입니다. \n");
				break;
			}
		
		} while (true);
		
		
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 반납
	private void returnBooks(Scanner sc) {	
		
		System.out.println(">>> 현재 대여중인 목록 <<<");
		

		// DB로부터 대여상세번호, 회원id, 회원명, 도서id, 도서명을 가져오는 메소드
		List<Map<String, String>> notReturnedList = loanDetailDao.getNoReturnedList();

		// 대여상세테이블에서 반납 상태가 아닌 행이 있다면
		if(notReturnedList != null) {
			
			System.out.println("--------------------------------------------");
			System.out.println("대여상세번호   회원명   도서명");
			System.out.println("--------------------------------------------");

			StringBuilder sb = new StringBuilder();
			
			// 대여상세번호, 회원명, 도서명 나타내기
			for (Map<String, String> map : notReturnedList) {
				sb.append(map.get("LOAN_DETAIL_NO") + "    " + map.get("USER_NAME") + "   " + map.get("BOOK_NAME") + "\n");
			}
			sb.append("--------------------------------------------\n\n");
			System.out.println(sb.toString());
			
			
			// 반납 처리하기
			do {
				// 반납의 경우 사용자가 원하는 만큼 받도록 한다.
				
				// 반납할 대여상세번호 입력받기
				System.out.print("▶ 반납처리할 대여상세번호 입력 : ");
				String no = sc.nextLine();
				
				String bookId = "";
				String loanDetailNo = "";
				// 입력받은 번호에 해당하는 대여상세가 있는지 검사한다.
				boolean isExist = false; 
				for (Map<String, String> map : notReturnedList) {
					if (map.get("LOAN_DETAIL_NO").equals(no)) {
						isExist = true;
						bookId = map.get("BOOK_ID");
						loanDetailNo = no;
						break;
					}
				}
				
				
				// 해당하는 대여상세가 있다면
				if (isExist) {
					String yn = "";
					
					// 반납처리할지 최종으로 묻기
					do {
						
						System.out.print(">> 해당 대여목록을 반납처리 하시겠습니까? [Y/N] : ");
						yn = sc.nextLine();
						
						// y를 입력한다면
						if (yn.equalsIgnoreCase("y")) {
							
							try {
								conn.setAutoCommit(false);
								
								// 반납처리하기 = 해당 도서id와 대여상세번호을 참조하여 대여상세테이블 및 대여도서테이블 수정하기
								
								// 1. 해당 대여의 주체인 회원의 회원번호를 가져옴.
								int userSeq = loanDetailDao.getUserSeqFromLoanDetail(loanDetailNo);
								
								// 2. 해당 대여상세가 반납기한일로부터 얼마나 지났는지 갖고옴.
								int delayedDays = loanDetailDao.getReturnDelayedDays(loanDetailNo);
								
								// 3. 해당 대여상세 업데이트 - 반납완료
								int n = loanDetailDao.setReturnLoanDetail(loanDetailNo);
								if (n == -1) {
									System.out.println(">> 대여상세 업데이트에 실패하였습니다.\n");
									conn.rollback();
									return;
								}
								
								// 4. 연체되었다면 연체료 부과
								if (delayedDays > 0) {
									if (userDao.setUserOverDueFee(userSeq ,delayedDays) == -1) {
										System.out.println(">> 회원 연체료 업데이트에 실패하였습니다.\n");
										conn.rollback();
										return;
									}
								}
								
								// 5. 대여도서 테이블도 업데이트 - 상태 : 정상, 대출여부 : 0
								n = loanBookDao.updateBookStatus2(bookId);
								if (n == -1) {
									System.out.println(">> 대여도서 테이블 업데이트에 실패하였습니다. \n");
									conn.rollback();
									return;
								}
								
								
								conn.commit();
								
								System.out.println(">> 반납처리가 완료되었습니다.\n");
								
							} catch (SQLException e) {
								e.printStackTrace();
								System.out.println(">> 반납처리 실패...\n");
								try {
									conn.rollback();	
								} catch (SQLException e2) {

								}
								
							} finally {
								try {
									conn.setAutoCommit(true);	
								} catch (SQLException e) {

								}
								
							}
							
	
							
							
						}
						// n 을 입력한다면
						else if (yn.equalsIgnoreCase("n")) {
							System.out.println(">> 반납을 취소하였습니다.\n");
						}
						else {
							System.out.println("[경고] Y 또는 N만 입력하십시오.\n");
						}
						
						
						
					} while(!( yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("n")) );
					break;
					
				}
				// 해당하는 대여상세가 없다면
				else {
					System.out.println("[경고] 입력하신 번호에 해당하는 대여목록이 없습니다.\n");
					break;
				}
				
				
				
			} while(true);
			
			
			
		}
		// 대여상세테이블에서 반납 상태가 아닌 행이 없다면(대출 없음) 
		else {
			System.out.println(">> 현재 반납처리할 대여목록이 없습니다.\n");
			return;
		}


		

		
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 일반대출
	private void normalRent(Scanner sc) {
		
		String userId = "";
		String bookId = "";
		int remainLoanCnt = 0;
		String yn = "";
		int userSeq = 0;
		
		do {
			// 회원id 입력
			System.out.print("▶ 회원id 입력 : ");
			userId = sc.nextLine();
			
			// 해당 회원이 존재하지 않다면
			if (!userDao.checkIdExists(userId)) {
				System.out.println("[경고] 존재하지 않는 회원id입니다.\n");
				continue;
			}
			
			// 회원번호 알아오기
			userSeq = userDao.getUserSeqById(userId);
			
			// 해당 회원이 연체료미납 또는 대출정지 상태라면
			if (!userDao.userIsBanned(userId)) {
				System.out.println("[경고] 해당 회원은 현재 대출이 불가합니다.\n");
				return;
			}
			
			// 해당 회원의 현재 대여권수를 알아온다.
			int loanCnt = loanDetailDao.getLoanDetailCnt(userId);
			if (loanCnt == -1) {
				System.out.println(">>> 회원의 대여권수 가져오기에 실패하였습니다. \n");
				return;
			}
			
			// 해당 회원의 현재 예약권수를 알아온다.
			// 그 전에 예약테이블에서 예약기한을 지난 예약은 삭제한다.
			if (reservationDao.deleteEndedReservation() == -1) {
				System.out.println(">> 예약테이블의 기한만기 예약 삭제에 실패하였습니다.\n");
				return;
				// 애초에 삭제 실패하면 바뀐게 없으니 롤백은 따로 안함
			}
			// 진짜 예약권수 알아오기
			int resvCnt = resvDetailDao.getResvDetailCnt(userSeq);
			if (loanCnt == -1) {
				System.out.println(">>> 회원의 예약건수 가져오기에 실패하였습니다. \n");
				return;
			}
			
			// 해당 회원이 대여할 수 있는 권수를 구한다.(3 - 현재대여권수 - 현재예약권수)
			remainLoanCnt = 3 - loanCnt - resvCnt;
			
			// 남은 대여가능권수가 0 이하라면
			if(remainLoanCnt < 0) {
				System.out.println("[경고] 해당 회원의 남은 대여가능권수가 없습니다. \n");
				return;
			}
			
			// 모두 만족시 탈출
			break;
			
		} while(true); 
		//-----------------------------------------------------
		
		do {
			// 대여할 도서Id 입력받기
			System.out.print("▶ 대여할 도서ID 입력 : ");
			bookId = sc.nextLine();
		
			// 해당 도서id가 존재하지 않다면
			if (!loanBookDao.bookIdExist(bookId)) {
				System.out.println("[경고] 해당 도서는 존재하지 않습니다.\n");
				continue;
			}
		
			// 해당 도서id가 대출불가 상태라면
			if (!loanBookDao.isEnableToLoan(bookId)) {
				System.out.println("[경고] 해당 도서는 현재 대출이 불가합니다.\n");
				continue;
			}
			
			// 해당 도서를 예약중인 사람이 있다면 (자신이 이 도서를 예약한 경우에도 true가 됩니다.)
			if (resvDetailDao.isReserved(bookId)) {
				System.out.println("[경고] 해당 도서는 현재 다른 회원이 예약중인 도서입니다.\n");
				continue;
			}

			// 모든 조건 만족 시 탈출
			break;
		} while(true);
		
		
		do {
			System.out.print("▶ 대출처리 하시겠습니까? [Y/N] : ");
			yn = sc.nextLine();
			
			
			if (yn.equalsIgnoreCase("y")) {
				int bookRentFee = 0;	// 대여료
				int userPoint = 0;		// 포인트
				
				try {
					conn.setAutoCommit(false);	// 여기부터
					

					// 1. 해당 책의 대여료 가져오기
					bookRentFee = bookDao.getRentFee(bookId);
					// 2. 회원의 포인트를 가져오기
					userPoint = userDao.getUserPoint(userSeq);
					// 3. 포인트 < 대여료 라면 대출취소시키기
					if (userPoint < bookRentFee) {
						System.out.println("[경고] 보유중인 포인트가 부족합니다.");
						conn.rollback();
						return;
						
					}
					
					// 4. 포인트가 충분하다면 회원의 포인트를 대여료만큼 감소시키기
					if(userDao.minusToPoint(bookRentFee, userSeq) == -1) {
						System.out.println(">> 회원의 포인트 차감에 실패하였습니다.\n");
						conn.rollback();
						return;
					}
					
					
					// 이미 오늘 날짜로 생성된 대여목록이 있는지 검사한다.
					String loanNoTemp = loanDao.isExistTodayLoan();
					// 아직 없다면
					if (loanNoTemp.isBlank()) {

						
						// 대여목록 생성하기
						
						int n = loanDao.insertLoan(userSeq, librarianDto.getLib_seq());		// 생성 성공하면 1, 실패시 -1
						if (n < 1) {
							System.out.println(">>> 대여목록 생성에 실패하였습니다... <<<\n");
							conn.rollback();
							return;
						}
						
					}
					
					// 당일 날짜의 대여목록의 대여번호를 가져오고, 
					int loanNo = loanDao.getRecentLoanNo();

					// 그 번호를 부모삼는 대여상세를 생성한다.
					int n = loanDetailDao.insertResvDetail(loanNo, bookId);		// 생성 성공하면 1, 실패시 -1
					if (n == -1) {
						System.out.println(">>> 대여상세목록 생성에 실패하였습니다... <<<\n");
						conn.rollback();
						return;
					}
					
					// 이후 해당 도서의 대여도서를 업데이트한다.
					n = loanBookDao.updateBookStatus(bookId);		// 생성 성공하면 1, 실패시 -1
					if (n == -1) {
						System.out.println(">>> 대여도서정보 수정에 실패하였습니다... <<<\n");
						conn.rollback();
						return;
					}
					
					
					System.out.println(">>> 대출처리가 완료되었습니다. \n");
					conn.commit();
					
					
				} catch (SQLException e) {
					e.printStackTrace();
					try {
						conn.rollback();	
					} catch (SQLException e1) {

					}
					
				} finally {
					try {
						conn.setAutoCommit(true);
					} catch (SQLException e) {
					
					}
					
				}
				
			
			} 
			else if (yn.equalsIgnoreCase("n")) {
				System.out.println(">> 대출을 취소하였습니다.\n");
				return;
			}
			else {
				System.out.println("[경고] Y 또는 N만 입력하십시오.\n");
			}
		
		} while (! (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("n")));
		
		
		
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// 예약대출
	private void reservedRent(Scanner sc) {


		
		
		
		do {
			
			System.out.println(">>> 예약대출 메뉴 <<<");
			System.out.println("----------------------------------");
			System.out.println("1.예약조회 및 대출    2.뒤로가기");
			System.out.println("----------------------------------\n");
				
			
			System.out.print("▶ 메뉴번호 입력 : ");
			String menuNo = sc.nextLine();
			
			switch (menuNo) {
			case "1":	// 예약조회 및 대출
				
				
				// 예약기한이 지난 예약 지우는 메소드
				
				int n = reservationDao.deleteEndedReservation();
				if (n == -1) {
					System.out.println(">> 예약테이블의 기한만기 예약 삭제에 실패하였습니다.\n");
					return;
				}
				
				
				// 예약상세목록 갖고오기( 예약번호, 예약상세번호, 회원id, 회원명, 도서id, 도서명 )
				// 단, 해당 예약의 도서 상태가 대출가능인 경우만을 갖고온다.
				// 또한, 각 도서별로 가장 먼저 예약된 건만 갖고온다.
				List<Map<String, String>> resvDetailList = resvDetailDao.getResvDetailList();
				
				
				// 가져온 List 에 예약상세가 있다면
				if(resvDetailList.size() > 0) {
					
					StringBuilder sb = new StringBuilder();
					
					sb.append("-----------------------------------------------------------\n");
					sb.append("예약상세번호   회원명   도서명\n");
					sb.append("-----------------------------------------------------------\n");
					
					// List 에서 예약상세번호, 회원명, 도서명 만 뽑아오기
					for (Map<String, String> map : resvDetailList) {
						sb.append(map.get("RESV_DETAIL_ID") + "    " + map.get("USER_NAME") + "    " + map.get("BOOK_NAME") + "\n" );
						
					}
					sb.append("\n");
					// 뽑아온 목록 출력하기
					System.out.println(sb.toString());
					
					
					String resvDetailId = "";	// 예약상세번호
					String userSeq = "";			// 회원번호
					String bookId = "";			// 도서번호
					
					do {
						// 대여처리할 예약상세번호 입력받기
						System.out.print("▶ 대여처리할 예약상세번호 입력 : ");
						String no = sc.nextLine();
						
						// 입력받은 번호가 List 에 존재하는지 검사
						boolean isExist = false;
						for (Map<String, String> map : resvDetailList) {
							if (no.equals(map.get("RESV_DETAIL_ID"))) {
								isExist = true;
								resvDetailId = map.get("RESV_DETAIL_ID");
								userSeq = map.get("USER_SEQ");
								bookId = map.get("BOOK_ID");
							}
						}
						
						
						// 입력받은 예약상세번호가 List 에 존재한다면
						if (isExist) {
						
							
							// 회원id 갖고오기
							String userid = userDao.getUserIdBySeq(userSeq);
							
							// 해당 예약의 회원이 현재 연체료를 갖고 있는지, 현재 대출정지 상태인지 검사
							if (!userDao.userIsBanned(userid)) {
								System.out.println("[경고] 해당 회원은 현재 대출이 불가합니다.\n");
								return;
							}
						

							// 정말로 대여처리할건지 묻기
							String yn = "";
							do {
								System.out.print("선택한 예약상세를 대여처리 하시겠습니까? [Y/N] : ");
								yn = sc.nextLine();
								
								
								// y 입력시
								if (yn.equalsIgnoreCase("y")) {
									int bookRentFee = 0;	// 대여료
									int userPoint = 0;		// 포인트
									
									try {
										
										conn.setAutoCommit(false);
										
										// 1. 해당 책의 대여료 가져오기
										bookRentFee = bookDao.getRentFee(bookId);
										// 2. 회원의 포인트를 가져오기
										userPoint = userDao.getUserPoint(Integer.parseInt(userSeq));
										// 3. 포인트 < 대여료 라면 대출취소시키기
										if (userPoint < bookRentFee) {
											System.out.println("[경고] 보유중인 포인트가 부족합니다.");
											conn.rollback();
											return;
											
										}
										
										// 4. 포인트가 충분하다면 회원의 포인트를 대여료만큼 감소시키기
										if(userDao.minusToPoint(bookRentFee, Integer.parseInt(userSeq)) == -1) {
											System.out.println(">> 회원의 포인트 차감에 실패하였습니다.\n");
											conn.rollback();
											return;
										}

										
										// 해당 예약상세를 삭제한다.
										if (resvDetailDao.deleteResvDetail(resvDetailId) == -1) {
											System.out.println(">> 예약상세 삭제에 실패하였습니다.\n");
											conn.rollback();
											return;
										}
										
										
										// 이미 오늘 날짜로 생성된 대여목록이 있는지 검사한다.
										String loanNoTemp = loanDao.isExistTodayLoan();
										// 아직 없다면
										if (loanNoTemp.isBlank()) {

											
											// 대여목록 생성하기
											
											n = loanDao.insertLoan(Integer.parseInt(userSeq), librarianDto.getLib_seq());		// 생성 성공하면 1, 실패시 -1
											if (n < 1) {
												System.out.println(">>> 대여목록 생성에 실패하였습니다... <<<\n");
												conn.rollback();
												return;
											}
											
										}
										
										// 당일 날짜의 대여목록의 대여번호를 가져오고, 
										int loanNo = loanDao.getRecentLoanNo();

										// 그 번호를 부모삼는 대여상세를 생성한다.
										n = loanDetailDao.insertResvDetail(loanNo, bookId);		// 생성 성공하면 1, 실패시 -1
										if (n == -1) {
											System.out.println(">>> 대여상세목록 생성에 실패하였습니다... <<<\n");
											conn.rollback();
											return;
										}
										
										// 이후 해당 도서의 대여도서를 업데이트한다.
										n = loanBookDao.updateBookStatus(bookId);		// 생성 성공하면 1, 실패시 -1
										if (n == -1) {
											System.out.println(">>> 대여도서정보 수정에 실패하였습니다... <<<\n");
											conn.rollback();
											return;
										}
										
										
										conn.commit();
										System.out.println(">> 대출처리가 완료되었습니다.\n");
										
									
									} catch (SQLException e) {
										e.printStackTrace();
										try {
											conn.rollback();
										} catch (SQLException e2) {

										}
										
									} finally {

									}
									
									
									
									
									
									
									
									
								}
								// n 입력시
								else if (yn.equalsIgnoreCase("n")) {
									System.out.println(">> 대여처리를 취소하였습니다.\n");
								}
								// 잘못된 입력시
								else {
									System.out.println("[경고] Y 또는 N만 입력하십시오.\n");
								}
								
								
							} while ( !(yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("n")));
							
							break;
							
							
						}
						// 입력받은 예약상세번호가 List 에 없다면
						else {
							System.out.println("[경고] 입력하신 번호는 존재하지 않는 예약상세번호입니다.\n");
						}
						
						
					
					} while(true);
					
					
				}
				
				// 가져온 List 에 예약상세가 없다면
				else {
					System.out.println(">> 현재 예약된 목록이 존재하지 않습니다...\n");
				}
				
				
				break;
			
			
			
			case "2":	// 뒤로가기
				
				return;
			default:
				System.out.println("[경고] 메뉴에 없는 번호입니다.\n");
			}
			

		} while(true);
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	

	
	
	
	
	
	
	
}





