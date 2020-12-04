package com.globits.office.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@Entity
@Table(name = "tbl_receipt_expenditure")
@XmlRootElement
public class ReceiptExpenditure extends BaseObject {
	private static final long serialVersionUID = 7548497663720390681L;
	@Column(name="is_resceipt")
	private int isReceipt;//Là khoản thu =1, là khoản chi = -1
	@Column(name="amount")
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

}
