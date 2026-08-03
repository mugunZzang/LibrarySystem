package librarian.domain;

import common.CommonMember;

public class LibrarianDTO extends CommonMember{

	// field
	 private int lib_seq;         // 사서번호
	 
	 
	 // method
	 public int getLib_seq() {
		 return lib_seq;
	 }
	 public void setLib_seq(int lib_seq) {
		 this.lib_seq = lib_seq;
	 }
	 
	 //사서 정보 불러오기
	 @Override
	    public String toString() {
	      
	       return "=== " + super.getName() + "님의 정보 ===" + "\n"
	           + "◇ 성명 : " + super.getName() + "\n"
	           + "◇ 연락처 : " + super.getTel() + "\n"
	           + "◇ 이메일 : " + super.getEmail() + "\n"
	           ;
	    }// end of public String toString()
	
	 
}
