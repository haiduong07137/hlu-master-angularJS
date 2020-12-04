package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.dto.LabourAgreementTypeDto;
import com.globits.hr.dto.SearchLabourAgreementTypeDto;

public interface LabourAgreementTypeService extends GenericService<LabourAgreementType, Long>{
	public Page<LabourAgreementTypeDto> getPage(int pageSize, int pageIndex);
	public LabourAgreementTypeDto saveLabourAgreementType(LabourAgreementTypeDto dto);
	public Boolean deleteLabourAgreementType(Long id);
	public int deleteLabourAgreementTypes(List<LabourAgreementTypeDto> dtos);
	public LabourAgreementTypeDto getLabourAgreementType(Long id);
	public Page<LabourAgreementTypeDto> searchLabourAgreementType(SearchLabourAgreementTypeDto dto, int pageIndex, int pageSize);
}
