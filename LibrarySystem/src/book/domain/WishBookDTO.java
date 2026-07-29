package book.domain;

public class WishBookDTO {

	//field
	private int wish_book_no;
	private int user_seq;
    private String wish_book_name;
    private String wish_book_author;
    private String wish_book_publisher;
    private String request_date;
    
    
    //method
	public int getWish_book_no() {
		return wish_book_no;
	}
	public void setWish_book_no(int wish_book_no) {
		this.wish_book_no = wish_book_no;
	}
	public int getUser_seq() {
		return user_seq;
	}
	public void setUser_seq(int user_seq) {
		this.user_seq = user_seq;
	}
	public String getWish_book_name() {
		return wish_book_name;
	}
	public void setWish_book_name(String wish_book_name) {
		this.wish_book_name = wish_book_name;
	}
	public String getWish_book_author() {
		return wish_book_author;
	}
	public void setWish_book_author(String wish_book_author) {
		this.wish_book_author = wish_book_author;
	}
	public String getWish_book_publisher() {
		return wish_book_publisher;
	}
	public void setWish_book_publisher(String wish_book_publisher) {
		this.wish_book_publisher = wish_book_publisher;
	}
	public String getRequest_date() {
		return request_date;
	}
	public void setRequest_date(String request_date) {
		this.request_date = request_date;
	}
}
