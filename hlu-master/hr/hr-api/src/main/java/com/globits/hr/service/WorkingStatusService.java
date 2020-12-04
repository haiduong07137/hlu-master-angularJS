package com.globits.hr.service;
/*
 * Modify Giang 21/04/2018
 */
import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.WorkingStatusDto;

public interface WorkingStatusService extends GenericService<WorkingStatus, Long> {
	public Page<WorkingStatusDto> getPage(int pageSize, int pageIndex);
	public WorkingStatusDto saveWorkingStatus(WorkingStatusDto dto);
	public Boolean deleteWorkingStatus(Long id);
	public int deleteWorkingStatuses(List<WorkingStatusDto> dtos);
	public WorkingStatusDto getWorkingStatus(Long id);
}
