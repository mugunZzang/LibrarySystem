package main;

import java.util.Scanner;

import dbconnection.ProjectDBConnection;
import user_controller.UserController;

public class Main {

	public static void main(String[] args) {
		
		UserController userCtrl = new UserController();
		
		Scanner sc = new Scanner(System.in);
		
		
		
		userCtrl.mainstart(sc);
		
		
		
		ProjectDBConnection.closeConnection();
		System.out.println("~~~~ 프로그램 종료 ~~~~");
		
	} // end of public static void main(String[] args)---------

}
