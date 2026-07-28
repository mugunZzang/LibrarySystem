// 사서와 회원에게 정보를 전달하는 상속 클래스
package common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMember {

	// field 
	
	private String id;               // 아이디
	private String pw;               // 비밀번호
	private String name;             // 이름
	private String tel;              // 연락처
	private String email;            // 이메일
	private String registerday;      // 가입일자 
	
	
	// method
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		
		/* 
		   id 는 첫글자는 반드시 영문(대,소문자)자로 시작해야 하고, 
		   그 나머지 글자는 영문자 또는 숫자로만 이루어져야 한다.
		   그리고 id 의 길이는 최소 3글자, 최대 30글자로 제한한다.
		*/
		
		// 정규표현식 패턴 작성
		Pattern p = Pattern.compile("^[A-Za-z][A-Za-z0-9]{2,29}$");
		
		// 문자열이 주어진 정규식 패턴과 일치하는지 판별
		Matcher m = p.matcher(id);
		
		// 판별
		if(m.matches()) {
			this.id = id;
		}
		else {
			System.out.println("[경고] 아이디는 첫글자는 영문자 이어야 하고, 나머지는 영문 또는 숫자로만 이루어진 글자길이는 3~30 글자만 가능합니다.\n"); 
		}	

		
	} // end of public void setId(String id)----------
	
	public String getPw() {
		return pw;
	}
	
	public void setPw(String pw) {
		// 비밀번호는 영문자, 숫자, 특수문자 혼합된 최소 8글자 이상 최대 30글자 이하이어야 한다. 
	 	
		if(utils.isUsePasswd(pw) ) {
 			this.pw = pw;
 		}
 		else {
 			System.out.println("[경고] 비밀번호는 영문자, 숫자, 특수문자 혼합된 최소 8글자 이상 최대 30글자 이하이어야 합니다.\n"); 
 		}
	} // end of public void setPw(String pw)----------
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		
		// 성명은 공백이 없는 한글로만 이루어져야 하며, 최소 2글자 이상 최대 10글자로 되어져야 한다. 

		// 정규표현식 패턴 작성
		Pattern p = Pattern.compile("^[가-힣]{2,10}$");
	
		// 문자열이 주어진 정규식 패턴과 일치하는지 판별
		Matcher m = p.matcher(name);
		
		// 판별
		if(m.matches()) {
 			this.name = name;
 	 	}
 	 	else {
 			System.out.println("[경고] 성명은 공백이 없는 한글로만 이루어져야 하며, 최소 2글자 이상 최대 6글자로만 되어져야 합니다.\n"); 
 		}
	
	} // end of public void setName(String name)----------
	
	public String getTel() {
		return tel;
	}
	
	
	public void setTel(String tel) {
		
		/*
			조건: 포함 총 13자리 (예: 010-1111-2222)
		*/
		
		// 정규 표현식: 숫자3자리-숫자4자리-숫자4자리 (총 13자리)
		Pattern p = Pattern.compile("^010-[0-9]{4}-[0-9]{4}$");
		
		// 문자열이 주어진 정규식 패턴과 일치하는지 판별
		Matcher m = p.matcher(tel);
		
		if(m.matches()) {
			this.tel = tel;
		} else {
			System.out.println("[경고] 올바른 휴대폰 형식이 아닙니다. (예: 010-1234-1234)");
		}
	
	} // end of public void setTel(String tel)----------
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		/*
			조건: 이메일은 총 50자까지 가능하며 가운데 @와 뒤에 .이 들어가야 한다.
			예시: abcd@gmail.com,   abcd@sist.ac.kr
		*/
		
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,50}$";
		
		if(!email.matches(regex)) {
			System.out.println("[경고] 올바른 이메일 형식이 아닙니다. (예: abcd@gami.com, abcd@sist.ac.kr)");
			return ;
		}
		
		this.email = email;
		
	} // end of public void setEmail(String email)----------
	
	public String getRegisterday() {
		return registerday;
	}
	
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	

	
}
