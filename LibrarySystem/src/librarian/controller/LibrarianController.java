package librarian.controller;

import java.util.Scanner;

public class LibrarianController {

	
	public void startMenu(Scanner sc) {
		
		
		do {
			System.out.print("▶ 메뉴번호 입력 : ");
			String menuNo = sc.nextLine();
			
			
			switch (menuNo) {
			case "1":	// 대출/반납
				
				break;
			case "2":	// 회원관리
				
				break;
			case "3":	// 도서관리
				
				break;
			case "4":	// 마이페이지
				
				break;
			case "5":	// 로그아웃
				return;
			default:
				System.out.println("[경고] 메뉴에 없는 번호입니다. \n");
				break;
			}
		
		} while (true); 
		
	}
	
}
