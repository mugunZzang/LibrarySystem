package common;

public class utils {

	// == 입력받은 문자열(비밀번호)이 영문자, 숫자, 특수기호가 혼합된 8글자 이상 30글자 이하인 경우라면 
	//    true 값을 리턴해주고, 그러한 조건에 만족하지 않으면 false 를 리턴해주는 메서드 ==
	public static boolean isUsePasswd(String passwd) {
			
		int len = passwd.length(); // 입력받은 passwd 의 문자열 길이
		
		if( !(8<=len && len<=30) ) { 
			// passwd 의 길이가 8 미만 이거나 또는 30 보다 큰 경우
			return false;
		}
		else {
			// passwd 의 길이가 8 이상 30 이하인 경우
			
			char[] arrChr = passwd.toCharArray();
			
			boolean flagAlphabet = false; // 영문자표시
			boolean flagDigit = false;    // 숫자표시
			boolean flagSpecial = false;  // 특수문자표시
			
			for(int i=0; i<arrChr.length; i++) {
				char ch = arrChr[i];
				
				if(Character.isLowerCase(ch) || 
				   Character.isUpperCase(ch) ) {
					flagAlphabet = true; // 영문자가 존재한다는 표시 
				}
				else if(Character.isDigit(ch)) {
					flagDigit = true; // 숫자가 존재한다는 표시 
				}
				else {
					flagSpecial = true; // 특수문자가 존재한다는 표시 
				}
				
			}// end of for---------------------
			
			return flagAlphabet && flagDigit && flagSpecial;
		}
		
	}// end of public static boolean isUsePasswd(String passwd)----- 

	
}
