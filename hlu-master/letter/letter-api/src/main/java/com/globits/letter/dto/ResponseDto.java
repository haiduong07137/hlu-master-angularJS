package com.globits.letter.dto;

public class ResponseDto {
	private Boolean isSuccess;
	private String message;
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ResponseDto() {
		this.isSuccess=false;
	}
}
