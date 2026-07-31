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
	
	 
}
