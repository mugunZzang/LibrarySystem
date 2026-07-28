package book.domain;

public class LoanBookDTO {

	//field
	private int book_id;
	private int isbn;
	private boolean loan_status;
	private String book_status;
	
	//method
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public int getIsbn() {
		return isbn;
	}
	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}
	public boolean isLoan_status() {
		return loan_status;
	}
	public void setLoan_status(boolean loan_status) {
		this.loan_status = loan_status;
	}
	public String getBook_status() {
		return book_status;
	}
	public void setBook_status(String book_status) {
		this.book_status = book_status;
	}

	
}
