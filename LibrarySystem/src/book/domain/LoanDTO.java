package book.domain;

public class LoanDTO {

	//field
	private int loan_no;
	private int lib_seq;
	private int user_seq;
	private String loan_date;
	private String return_due_date;
	
	
	//method
	public int getLoan_no() {
		return loan_no;
	}
	public void setLoan_no(int loan_no) {
		this.loan_no = loan_no;
	}
	public int getLib_seq() {
		return lib_seq;
	}
	public void setLib_seq(int lib_seq) {
		this.lib_seq = lib_seq;
	}
	public int getUser_seq() {
		return user_seq;
	}
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}
	public String getLoan_date() {
		return loan_date;
	}
	public void setLoan_date(String loan_date) {
		this.loan_date = loan_date;
	}
	public String getReturn_due_date() {
		return return_due_date;
	}
	public void setReturn_due_date(String return_due_date) {
		this.return_due_date = return_due_date;
	}
	
	
}
