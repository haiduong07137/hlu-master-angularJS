package com.globits.letter.dto;

import java.util.Date;

public class SearchDocumentDto {
	private String text;
	
	private Long documentTypeId;//Loại văn bản
	private Long letterDocFieldId;//Lĩnh vực văn bản
	private Long issueOrgan;
	private String otherIssueOrgan;
	private Date dateFrom;
	private Date dateTo;
	private String fullname; //tìm user theo tên
		
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Long getLetterDocFieldId() {
		return letterDocFieldId;
	}

	public void setLetterDocFieldId(Long letterDocFieldId) {
		this.letterDocFieldId = letterDocFieldId;
	}

	public Long getIssueOrgan() {
		return issueOrgan;
	}

	public void setIssueOrgan(Long issueOrgan) {
		this.issueOrgan = issueOrgan;
	}

	public String getOtherIssueOrgan() {
		return otherIssueOrgan;
	}

	public void setOtherIssueOrgan(String otherIssueOrgan) {
		this.otherIssueOrgan = otherIssueOrgan;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	
}
