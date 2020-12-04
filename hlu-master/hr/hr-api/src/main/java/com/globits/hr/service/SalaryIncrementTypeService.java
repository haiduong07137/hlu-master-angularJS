/*
 * Created by TA2 & Giang on 23/4/2018.
 */

package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.dto.SalaryIncrementTypeDto;


public interface SalaryIncrementTypeService extends GenericService<SalaryIncrementType, Long> {
	public Page<SalaryIncrementTypeDto> getPage(int pageSize, int pageIndex);
	public SalaryIncrementTypeDto saveSalaryIncrementType(SalaryIncrementTypeDto dto);
	public Boolean deleteSalaryIncrementType(Long id);
	public int deleteSalaryIncrementTypes(List<SalaryIncrementTypeDto> dtos);
	public SalaryIncrementTypeDto getSalaryIncrementType(Long id);
			
}
