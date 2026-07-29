package librarian.model;

import java.util.Map;

import librarian.domain.LibrarianDTO;

public interface LibrarianDAO {

   // **** 사서 회원가입을 해주는 메서드 **** //
   int libRegister(LibrarianDTO libDto);

   // **** 사서가 입력한 값이 존재하는 아이디인지 확인 **** //
   boolean checkIdExists(String lib_id);
   
   // 사서 로그인
   LibrarianDTO libLogin(Map<String, String> paraMap);

}
