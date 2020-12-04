package com.globits.hr.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.ShiftWorkTimePeriod;

public class ShiftWorkDto implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Double totalHours;
	private String code;
	private List<ShiftWorkTimePeriodDto> timePeriods;

	public Double getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public List<ShiftWorkTimePeriodDto> getTimePeriods() {
		return timePeriods;
	}
	public void setTimePeriods(List<ShiftWorkTimePeriodDto> timePeriods) {
		this.timePeriods = timePeriods;
	}
	public ShiftWorkDto() {}

	public ShiftWorkDto(ShiftWork shiftWork) {
		if (shiftWork!=null) {
			this.setId(shiftWork.getId());
			this.setName(shiftWork.getName());
			this.setCode(shiftWork.getCode());
			this.setTotalHours(shiftWork.getTotalHours());
			if(shiftWork.getTimePeriods()!=null) {
				timePeriods = new ArrayList<ShiftWorkTimePeriodDto>();
				for(ShiftWorkTimePeriod swt: shiftWork.getTimePeriods()) {
					ShiftWorkTimePeriodDto stdDto = new ShiftWorkTimePeriodDto();
					stdDto.setId(swt.getId());
					stdDto.setStartTime(swt.getStartTime());
					stdDto.setEndTime(swt.getEndTime());
					timePeriods.add(stdDto);
				}
			}
		}
	}
	
	
}
