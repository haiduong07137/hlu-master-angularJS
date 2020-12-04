package com.globits.office.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.letter.domain.LetterDocBook;
import com.globits.office.domain.ReceiptExpenditure;

public class ReceiptExpenditureDto  extends BaseObjectDto{
	private int isReceipt;
	private Double amount;
	
	public int getIsReceipt() {
		return isReceipt;
	}
	public void setIsReceipt(int isReceipt) {
		this.isReceipt = isReceipt;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public ReceiptExpenditureDto() {
		
	}
	public ReceiptExpenditureDto(ReceiptExpenditure entity) {
		this.isReceipt = entity.getIsReceipt();
		this.amount = entity.getAmount();
	}
}
