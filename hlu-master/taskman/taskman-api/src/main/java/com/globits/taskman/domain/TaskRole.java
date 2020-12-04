package com.globits.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

/*
 * Vai trò trong công việc
 * Ví dụ : Chủ trì, tham gia, phối hợp
 */
@Entity
@Table(name = "tbl_task_role")
@XmlRootElement
public class TaskRole  extends BaseObject{
	private String name;
	private String code;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
