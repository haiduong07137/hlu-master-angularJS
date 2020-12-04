package com.globits.hr.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.PositionTitle;
import com.globits.hr.dto.PositionTitleDto;

public interface PositionTitleService extends GenericService<PositionTitle, Long> {
	
	public PositionTitleDto saveTitle(PositionTitleDto dto);

	public PositionTitleDto getTitle(Long id);
	
	public Page<PositionTitleDto> getPage(int pageIndex, int pageSize);

	public Boolean removeTitle(Long id);
	
	public Boolean deleteMultiple(PositionTitleDto[] dtos);
}
