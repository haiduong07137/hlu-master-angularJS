package com.globits.letter.dto;

public class ResultDataEventDto {
	private int status; //1 là trùng phòng, 2 là trùng chủ trì, 3 là thành công
	private String messenger;
	
	public ResultDataEventDto(int b, String string) {
		this.status = b;
		this.messenger = string;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessenger() {
		return messenger;
	}
	public void setMessenger(String messenger) {
		this.messenger = messenger;
	}
}
