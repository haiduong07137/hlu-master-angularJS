package com.globits.hr.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
@XmlRootElement
@Table(name = "tbl_staff_reward_history")
@Entity
public class StaffRewardHistory extends BaseObject {
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	
	
}
