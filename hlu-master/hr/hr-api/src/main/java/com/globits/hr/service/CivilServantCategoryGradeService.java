package com.globits.hr.service;


import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.CivilServantCategory;
import com.globits.hr.domain.CivilServantCategoryGrade;
import com.globits.hr.dto.CivilServantCategoryDto;
import com.globits.hr.dto.CivilServantCategoryGradeDto;

public interface CivilServantCategoryGradeService extends GenericService<CivilServantCategoryGrade, Long> {
	public CivilServantCategoryGradeDto saveCivilServantCategoryGrade(CivilServantCategoryGradeDto dto);

	public CivilServantCategoryGradeDto getCivilServantCategoryGrade(Long id);
	
	public Page<CivilServantCategoryGradeDto> getPage(int pageIndex, int pageSize);

	public Boolean removeCivilServantCategoryGrade(Long long1);
	
	public Boolean deleteMultiple(CivilServantCategoryGradeDto[] dtos);
}
