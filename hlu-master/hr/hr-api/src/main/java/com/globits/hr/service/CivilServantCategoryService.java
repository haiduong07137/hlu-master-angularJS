package com.globits.hr.service;


import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.CivilServantCategory;
import com.globits.hr.dto.CivilServantCategoryDto;

public interface CivilServantCategoryService extends GenericService<CivilServantCategory, Long> {
	public CivilServantCategoryDto saveCivilServantCategory(CivilServantCategoryDto dto);

	public CivilServantCategoryDto getCivilServantCategory(Long id);
	
	public Page<CivilServantCategoryDto> getPage(int pageIndex, int pageSize);

	public Boolean removeCivilServantCategory(Long long1);
	
	public Boolean deleteMultiple(CivilServantCategoryDto[] dtos);
}
