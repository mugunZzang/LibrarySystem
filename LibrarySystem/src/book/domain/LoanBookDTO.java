package book.domain;

public class LoanBookDTO {

   //field
   private int book_id;
   private int isbn;
   private int loan_status;
   private String loan_status_kor;
   private String book_status;
   
   
   // TBL_BOOK 테이블 JOIN 시 SELECT 용
   private String book_name;   
   
   
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
   public int getLoan_status() {
      return loan_status;
   }
   public void setLoan_status(int loan_status) {
      this.loan_status = loan_status;
   }
   public String getLoan_status_kor() {
      return loan_status_kor;
   }
   public void setLoan_status_kor(String loan_status_kor) {
      this.loan_status_kor = loan_status_kor;
   }
   public String getBook_status() {
      return book_status;
   }
   public void setBook_status(String book_status) {
      this.book_status = book_status;
   }
   public String getBook_name() {
      return book_name;
   }
   public void setBook_name(String book_name) {
      this.book_name = book_name;
   }
      
}
