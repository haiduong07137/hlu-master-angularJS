package com.globits.document.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;
@Entity
@Table(name="tbl_document_category")
public class DocumentCategory extends BaseObject{
	private String code;
	private String name;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
