package com.globits.hr.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.CivilServantGrade;
import com.globits.hr.dto.CivilServantGradeDto;

public interface CivilServantGradeService extends GenericService<CivilServantGrade, Long> {
	
	public CivilServantGradeDto saveCivilServantGrade(CivilServantGradeDto dto);

	public CivilServantGradeDto getCivilServantGrade(Long id);
	
	public Page<CivilServantGradeDto> getPage(int pageIndex, int pageSize);

	public Boolean removeCivilServantGrade(Long long1);
	
	public Boolean deleteMultiple(CivilServantGradeDto[] dtos);
}
