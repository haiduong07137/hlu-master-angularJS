package com.globits.hr.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffCivilServantCategoryGrade;
import com.globits.hr.dto.StaffCivilServantGradeDto;

public interface StaffCivilServantGradeService extends GenericService<StaffCivilServantCategoryGrade, Long> {
	public StaffCivilServantGradeDto saveStaffCivilServantGrade(StaffCivilServantGradeDto dto);

	public StaffCivilServantGradeDto getStaffCivilServantGrade(Long id);
	
	public Page<StaffCivilServantGradeDto> getPage(int pageIndex, int pageSize);

	public Boolean removeStaffCivilServantGrade(Long long1);
	
	public Boolean deleteMultiple(StaffCivilServantGradeDto[] dtos);

}
