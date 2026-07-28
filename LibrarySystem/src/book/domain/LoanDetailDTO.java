package book.domain;

public class LoanDetailDTO {
	
	//field
	private int loan_detail_no;
	private int loan_no;
	private String book_id;
	private boolean return_stat;
	
	
	//method
	public int getLoan_detail_no() {
		return loan_detail_no;
	}
	public void setLoan_detail_no(int loan_detail_no) {
		this.loan_detail_no = loan_detail_no;
	}
	public int getLoan_no() {
		return loan_no;
	}
	public void setLoan_no(int loan_no) {
		this.loan_no = loan_no;
	}
	public String getBook_id() {
		return book_id;
	}
	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}
	public boolean isReturn_stat() {
		return return_stat;
	}
	public void setReturn_stat(boolean return_stat) {
		this.return_stat = return_stat;
	}	
	
	
	
}
