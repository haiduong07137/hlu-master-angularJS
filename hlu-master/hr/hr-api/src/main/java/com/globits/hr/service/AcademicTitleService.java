/*
 * TA va Giang làm
 */

package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.AcademicTitle;
import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.AcademicTitleDto;
import com.globits.hr.dto.SalaryIncrementTypeDto;
import com.globits.hr.dto.SalaryItemDto;

public interface AcademicTitleService extends GenericService<AcademicTitle, Long> {
	public Page<AcademicTitleDto> getPage(int pageSize, int pageIndex);
	public AcademicTitleDto saveAcademicTitle(AcademicTitleDto dto);
	public Boolean deleteAcademicTitle(Long id);
	public int deleteAcademicTitles(List<AcademicTitleDto> dtos);
	public AcademicTitleDto getAcademicTitle(Long id);		
}
