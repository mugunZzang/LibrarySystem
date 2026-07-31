package book.domain;

public class BookDTO {
   
   //field
   private int isbn;
   private String fk_category_id;
   private String book_name;
   private String pub_year;
   private String contents;
   private int rental_fee;
   private String author;
   private String publisher;
   private String loanStatus;  // 대출상세
   private String resvStatus;  // 예약상세
   private int loan_count; // 대여 횟수 
   
   //method
   public int getIsbn() {
      return isbn;
   }
   public void setIsbn(int isbn) {
      this.isbn = isbn;
   }
   public String getFk_category_id() {
      return fk_category_id;
   }
   public void setFk_category_id(String fk_category_id) {
      this.fk_category_id = fk_category_id;
   }
   public String getBook_name() {
      return book_name;
   }
   public void setBook_name(String book_name) {
      this.book_name = book_name;
   }
   public String getPub_year() {
      return pub_year;
   }
   public void setPub_year(String pub_year) {
      this.pub_year = pub_year;
   }
   public String getContents() {
      return contents;
   }
   public void setContents(String contents) {
      this.contents = contents;
   }
   public int getRental_fee() {
      return rental_fee;
   }
   public void setRental_fee(int rental_fee) {
      this.rental_fee = rental_fee;
   }
   public String getAuthor() {
      return author;
   }
   public void setAuthor(String author) {
      this.author = author;
   }
   public String getPublisher() {
      return publisher;
   }
   public void setPublisher(String publisher) {
      this.publisher = publisher;
   }

	public String getLoanStatus() {
	       return loanStatus;
	}

   public void setLoanStatus(String loanStatus) {
	   this.loanStatus = loanStatus;
   }
  public String getResvStatus() {
	  return resvStatus;
  }
  public void setResvStatus(String resvStatus) {
	  this.resvStatus = resvStatus;
  }
  public int getLoan_count() {
	return loan_count;
  }
  public void setLoan_count(int loan_count) {
	this.loan_count = loan_count;
  }
   
 }
