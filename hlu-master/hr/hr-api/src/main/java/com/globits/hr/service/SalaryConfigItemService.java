package com.globits.hr.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryConfigItem;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.SalaryConfigItemDto;

public interface SalaryConfigItemService extends GenericService<SalaryConfigItem, Long> {
	public Page<SalaryConfigItemDto> getPageBySalaryConfigId(Long salaryConfigId, int pageSize, int pageIndex);

	public SalaryConfigItemDto saveSalaryConfigItem(SalaryConfigItemDto dto);

	public SalaryConfigItemDto getSalaryConfigItem(Long id);

}
