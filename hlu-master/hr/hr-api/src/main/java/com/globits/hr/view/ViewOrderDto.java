package com.globits.hr.view;

public class ViewOrderDto {
	
	private String field;
	private String title;
	private String cellStyle;
	private String formatter;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCellStyle() {
		return cellStyle;
	}
	public void setCellStyle(String cellStyle) {
		this.cellStyle = cellStyle;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}
}
