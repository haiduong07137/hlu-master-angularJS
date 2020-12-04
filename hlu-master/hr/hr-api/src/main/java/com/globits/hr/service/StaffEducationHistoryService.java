package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffEducationHistory;
import com.globits.hr.dto.StaffEducationHistoryDto;

public interface StaffEducationHistoryService extends GenericService<StaffEducationHistory, Long>{

	Page<StaffEducationHistoryDto> getPages(int pageIndex, int pageSize);

	List<StaffEducationHistoryDto> getAll(Long id);

	StaffEducationHistoryDto getEducationById(Long id);

	StaffEducationHistoryDto saveEducation(StaffEducationHistoryDto educationDto, Long id);

	boolean removeLists(List<Long> ids);

	StaffEducationHistoryDto removeEducation(Long id);

}
