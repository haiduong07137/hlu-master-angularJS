package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.StaffSalaryPropertyDto;

public interface StaffSalaryPropertyService extends GenericService<SalaryItem, Long> {
	public Page<StaffSalaryPropertyDto> getPage(int pageSize, int pageIndex);	
	public Boolean deleteStaffSalaryProperty(Long id);
	public StaffSalaryPropertyDto saveStaffSalaryProperty(StaffSalaryPropertyDto dto);
	public StaffSalaryPropertyDto getStaffSalaryProperty(Long id);
	public int deleteStaffSalaryPropertys(List<StaffSalaryPropertyDto> dtos);
}
