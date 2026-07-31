package reservation.domain;

import user.domain.UserDTO;

public class ReservationDTO {

	private int resv_id;		// 예약번호
	private int fk_userseq;		// 회원번호
	private String resv_date;	// 등록일
	
	
	private UserDTO userDto;	// 회원DTO	
	
	
	public void setResv_id(int i) {
		resv_id = i;
	}
	
	public int getResv_id() {
		return resv_id;
	}
	
	public void setFk_userseq(int i) {
		fk_userseq = i;
	}
	
	public int getFk_userseq() {
		return fk_userseq;
	}
	
	public void setResv_date(String s) {
		resv_date = s;
	}
	
	public String getResv_date() {
		return resv_date;
	}

	public UserDTO getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDTO userDto) {
		this.userDto = userDto;
	}
	
	
}
