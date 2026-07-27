package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProjectDBConnection {
	   // ================= singleton 패턴 만들기 시작 =================== //   
	   /*
	       !!! === singleton 패턴에서 중요한 것은 다음의 3가지 이다  === !!!
	       
	       == 첫번째,
	          private 변수로 자기 자신의 클래스 인스턴스를 가지도록 해야 한다.
	          접근제한자가 private 이므로 외부 클래스에서는 직접적으로 접근이 불가하다.
	          또한 static 변수로 지정하여 MyDBConnection 클래스를 사용할 때 
	          객체생성은 딱 1번만 생성되도록 해야 한다.  
	   */

		// >>> field (첫번째로 작동) <<< //
		// static 변수 
		private static Connection conn = null;
		
		
		// >>> static 초기화 블럭 (두번째로 작동) <<< //
		static {
			// 중요한 사실은 static 초기화 블럭은 해당 클래스가 객체로 생성되기전에 먼저 실행되어지며,
		    // 딱 1번만 호출되어지고 다음번에 새로운 객체(인스턴스)를 매번 생성하더라도 
		    // static 초기화 블럭은 호출이 안되어진다.
		
			try {
				
				// 1. 오라클 Driver 로딩
				Class.forName("oracle.jdbc.driver.OracleDriver");
			
				// 2. 어떤 오라클 서버 연결할지 설정
				conn = DriverManager.getConnection("jdbc:oracle:thin:@211.238.142.54:1521/XEPDB1", "JDBC_PROJECT_USER", "bclass");
			
			} catch(ClassNotFoundException e) {
				System.out.println(">>> ojdbc11.jar 파일이 없습니다. <<<");
				
			} catch(SQLException e) {
				e.printStackTrace();
				
			}
			
		}
		// == 두번째,
	    // 생성자에 접근제한자를 private 으로 지정하여, 외부에서 절대로 인스턴스를 생성하지 못하도록 막아버린다. 
		private ProjectDBConnection() {}
		
		// == 세번째,
	    // static 메소드를 생성[ 지금은 getConn() ] 하여 외부에서 해당 클래스의 객체를 사용할 수 있도록 해준다.
		public static Connection getConn() {
			return conn;   // static 변수를 반환하여 사용할 수 있게한다.
		}
		
		// =========================== singleton 패턴 만들기 끝 ====================================== // 
		
		// ==== Connection conn 객체 자원 반납하기 ==== //
        public static void closeConnection() {
	         
			 try {
			     if(conn != null) {conn.close(); conn=null;}
			     
			 } catch (SQLException e) {
			    e.printStackTrace();
			    
			 }
	         
        }// end of public static void closeConnection()--------------
		
		
		
		
}
