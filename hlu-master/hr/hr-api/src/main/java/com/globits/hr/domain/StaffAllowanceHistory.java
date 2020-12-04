package com.globits.hr.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@XmlRootElement
@Table(name = "tbl_staff_allowance_history")
@Entity
public class StaffAllowanceHistory extends BaseObject {
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
}
