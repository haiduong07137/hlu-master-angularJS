package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;

public interface SalaryItemService extends GenericService<SalaryItem, Long> {
	public Page<SalaryItemDto> getPage(int pageSize, int pageIndex);
	public SalaryItemDto saveSalaryItem(SalaryItemDto dto);
	public Boolean deleteSalaryItem(Long id);
	public int deleteSalaryItems(List<SalaryItemDto> dtos);
	public SalaryItemDto getSalaryItem(Long id);
	public Page<SalaryItemDto> searchSalaryItem(SearchSalaryItemDto dto, int pageIndex, int pageSize);
}
