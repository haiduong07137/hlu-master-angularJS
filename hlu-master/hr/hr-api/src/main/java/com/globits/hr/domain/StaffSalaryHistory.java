package com.globits.hr.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
/*
 * Bảng lịch sử lương - dùng cho lương ngạch bậc hiện tại
 */
@XmlRootElement
@Table(name = "tbl_staff_salary_history")
@Entity
public class StaffSalaryHistory extends BaseObject {
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	
}
