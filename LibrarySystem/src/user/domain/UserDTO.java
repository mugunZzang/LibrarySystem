package user.domain;

import common.CommonMember;

public class UserDTO extends CommonMember {
	
	
	// field
	
	private int user_seq;                 // 회원번호
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
