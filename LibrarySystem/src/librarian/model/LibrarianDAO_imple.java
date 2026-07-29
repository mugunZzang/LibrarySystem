package librarian.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbconnection.ProjectDBConnection;
import librarian.domain.LibrarianDTO;

public class LibrarianDAO_imple implements LibrarianDAO {

   
   // field
   private Connection conn = ProjectDBConnection.getConn(); 
   
   private PreparedStatement pstmt;
   
   private ResultSet rs;
   
   // method
   
   // === 자원반납을 해주는 메서드 === //
   private void close() {
      
      // >>> 사용하였던 자원을 반납하기 <<<
      try {
         if(rs != null) { rs.close();        rs = null;     }
         
         if(pstmt != null) { pstmt.close();  pstmt = null;  }
         
      } catch (SQLException e) {
         e.printStackTrace();
      }
      
   } // end of private void close()------------



   // **** 사서 회원가입을 해주는 메서드 **** //
   @Override
   public int libRegister(LibrarianDTO libDto) {
      
      int result = 0;
      
      try {
         
         // SQL 문 작성
         String sql = " INSERT INTO TBL_LIBRARIAN(lib_seq, lib_id, lib_passwd, lib_name, lib_tel, lib_email) " 
                  + " values(lib_seq.nextval, ?, ?, ?, ?, ?) " ;

         // 연결한 DB에 SQL문 제작 후 전달
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, libDto.getId());
         pstmt.setString(2, libDto.getPw());
         pstmt.setString(3, libDto.getName());
         pstmt.setString(4, libDto.getTel());
         pstmt.setString(5, libDto.getEmail());
         

         result = pstmt.executeUpdate();    // SQL 문 실행
         
         sql = " INSERT INTO tbl_lib_login(lib_seq, lib_id, lib_passwd, lib_name) " 
             + " values(lib_seq.CURRVAL, ?, ?, ?) " ; 
         
         // 연결한 DB에 SQL문 제작 후 전달
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, libDto.getId());
         pstmt.setString(2, libDto.getPw());
         pstmt.setString(3, libDto.getName());
         
         result = pstmt.executeUpdate();    // SQL 문 실행
         
      } catch(SQLException e) {
         e.printStackTrace();
         
      } finally {
         close();
         
      }
      return result;
      

   } // end of public int libRegister(Librarian_DTO libDto)----------

   
   // **** 사서가 입력한 값이 존재하는 아이디인지 확인 **** //
   @Override
   public boolean checkIdExists(String lib_id) {
      
      boolean result = false;
      
      try {      
         String sql = " SELECT * "
                   + " FROM tbl_lib_login "
                   + " WHERE lib_id = ? ";
      

         // 연결한 오라클 서버에 우편배달부 생성 후 내 SQL 문 전달
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, lib_id);
            
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


   } // end of public boolean checkIdExists(String lib_id)---------
   

}
