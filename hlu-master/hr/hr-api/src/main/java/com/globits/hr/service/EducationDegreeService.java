package com.globits.hr.service;
/*
 * author Giang-Tuan Anh
 */
import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.EducationDegree;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.EducationDegreeDto;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;

public interface EducationDegreeService extends GenericService<EducationDegree, Long> {
	public Page<EducationDegreeDto> getPage(int pageSize, int pageIndex);
	public EducationDegreeDto saveEducationDegree(EducationDegreeDto dto);
	public Boolean deleteEducationDegree(Long id);
	public int deleteEducationDegrees(List<EducationDegreeDto> dtos);
	public EducationDegreeDto getEducationDegree(Long id);
	/*
	 * Chi search voi 2 truong name va code thi dung chung search voi SalaryItem
	 */
	public Page<EducationDegreeDto> searchEducationDegree(SearchSalaryItemDto dto, int pageIndex, int pageSize);
}
