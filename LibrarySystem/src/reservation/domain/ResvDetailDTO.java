package reservation.domain;

import book.domain.LoanBookDTO;

public class ResvDetailDTO {
	
	private	int resvDetailId;	// 예약상세번호
	private int fkResvId;		// 예약번호
	private int bookId;			// 도서번호
	
	private LoanBookDTO loanBookDto;		// 대여도서DTO
	private ReservationDTO reservationDto;	// 예약DTO
	
	
	
	public int getResvDetailId() {
		return resvDetailId;
	}
	public void setResvDetailId(int resvDetailId) {
		this.resvDetailId = resvDetailId;
	}
	public int getFkResvId() {
		return fkResvId;
	}
	public void setFkResvId(int fkResvId) {
		this.fkResvId = fkResvId;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public LoanBookDTO getLoanBookDto() {
		return loanBookDto;
	}
	public void setLoanBookDto(LoanBookDTO loanBookDto) {
		this.loanBookDto = loanBookDto;
	}
	public ReservationDTO getReservationDto() {
		return reservationDto;
	}
	public void setReservationDto(ReservationDTO reservationDto) {
		this.reservationDto = reservationDto;
	}
	
	
	
}
