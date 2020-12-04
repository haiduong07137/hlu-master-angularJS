package com.globits.hr.service;
/*
 * Modify Giang 21/04/2018
 */
import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.dto.SearchShiftWorkDto;
import com.globits.hr.dto.ShiftWorkDto;

public interface ShiftWorkService extends GenericService<ShiftWork, Long> {
	public Page<ShiftWorkDto> getPage(int pageSize, int pageIndex);
	public ShiftWorkDto saveShiftWork(ShiftWorkDto dto);
	public Boolean deleteShiftWork(Long id);
	public int deleteShiftWorks(List<ShiftWorkDto> dtos);
	public ShiftWorkDto getShiftWork(Long id);
	public Page<ShiftWorkDto> searchShiftWork(SearchShiftWorkDto dto, int pageIndex, int pageSize);
}
