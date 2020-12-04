package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryConfig;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.SalaryConfigItemDto;
import com.globits.hr.dto.SalaryItemDto;

public interface SalaryConfigService extends GenericService<SalaryConfig, Long> {
	public Page<SalaryConfigDto> getPage(int pageSize, int pageIndex);
	public SalaryConfigDto saveSalaryConfig(SalaryConfigDto dto);
	public Boolean deleteSalaryConfig(Long id);
	public int deleteSalaryConfig(List<SalaryConfigDto> dtos);
	public SalaryConfigDto getSalaryConfig(Long id);
}
