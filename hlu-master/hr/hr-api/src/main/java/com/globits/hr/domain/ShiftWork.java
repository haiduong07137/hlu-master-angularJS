package com.globits.hr.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.globits.core.domain.BaseObject;

/*
 * Ca làm việc
 */
@Table(name = "tbl_shift_work")
@Entity
public class ShiftWork extends BaseObject {

	/**
	 * Ca làm việc
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "code", unique = true)
	private String code;

	@Column(name = "total_hours" ,nullable = true)
	private Double totalHours;
	
	public Double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}

	@OneToMany(mappedBy = "shiftWork", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ShiftWorkTimePeriod> timePeriods = new HashSet<>();

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

	public Set<ShiftWorkTimePeriod> getTimePeriods() {
		return timePeriods;
	}

	public void setTimePeriods(Set<ShiftWorkTimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}

}
