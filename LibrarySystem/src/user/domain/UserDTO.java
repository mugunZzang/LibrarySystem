package user.domain;

public class UserDTO {
	
	
	// field
	
	private int user_seq;                 // 회원번호
	private String user_id;               // 아이디
	private String user_pw;               // 비밀번호
	private String user_name;             // 이름
	private String user_tel;              // 연락처
	private String user_email;            // 이메일
	private String user_birth;            // 생년월일
	private int loan_stop;                // 대출정지여부
	private int point;                    // 포인트  default 0
	private int overdue_fee;              // 연체료  default 0
 	
 	
 	
 	// method
 	
	public int getUser_seq() {
		return user_seq;
	}
	
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}
	
	public String getUser_id() {
		return user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_pw() {
		return user_pw;
	}
	
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	
	public String getUser_name() {
		return user_name;
	}
	
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getUser_tel() {
		return user_tel;
	}
	
	public void setUser_tel(String user_tel) {
		this.user_tel = user_tel;
	}
	
	public String getUser_email() {
		return user_email;
	}
	
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	
	public String getUser_birth() {
		return user_birth;
	}
	
	public void setUser_birth(String user_birth) {
		this.user_birth = user_birth;
	}
	
	public int getLoan_stop() {
		return loan_stop;
	}
	
	public void setLoan_stop(int loan_stop) {
		this.loan_stop = loan_stop;
	}
	
	public int getPoint() {
		return point;
	}
	
	public void setPoint(int point) {
		this.point = point;
	}
	
	public int getOverdue_fee() {
		return overdue_fee;
	}
	
	public void setOverdue_fee(int overdue_fee) {
		this.overdue_fee = overdue_fee;
	}
	
}
