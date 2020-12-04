package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.dto.StaffLabourAgreementDto;


public interface StaffLabourAgreementService extends GenericService<StaffLabourAgreement, Long>{

	List<StaffLabourAgreementDto> getAll(Long id);

	StaffLabourAgreementDto getAgreementById(Long id);

	StaffLabourAgreementDto saveAgreement(StaffLabourAgreementDto agreementDto, Long id);

	StaffLabourAgreementDto removeAgreement(Long id);

	boolean removeLists(List<Long> ids);

	Page<StaffLabourAgreementDto> getPages(int pageIndex, int pageSize);

}
