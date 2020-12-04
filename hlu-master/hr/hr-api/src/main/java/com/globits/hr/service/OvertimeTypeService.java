/*
 * Created by TA & Giang on 22/4/2018.
 */

package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.OvertimeType;
import com.globits.hr.dto.OvertimeTypeDto;

public interface OvertimeTypeService extends GenericService<OvertimeType, Long> {
	public Page<OvertimeTypeDto> getPage(int pageSize, int pageIndex);
	public OvertimeTypeDto saveOvertimeType(OvertimeTypeDto dto);
	public Boolean deleteOvertimeType(Long id);
	public int deleteOvertimeTypes(List<OvertimeTypeDto> dtos);
	public OvertimeTypeDto getOvertimeType(Long id);		
}
