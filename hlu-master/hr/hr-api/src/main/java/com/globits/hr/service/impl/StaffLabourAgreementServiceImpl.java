package com.globits.hr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffCivilServantCategoryGrade;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffLabourAgreementDto;
import com.globits.hr.repository.LabourAgreementTypeRepository;
import com.globits.hr.repository.StaffLabourAgreementRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffLabourAgreementService;
import com.globits.security.domain.User;

@Transactional
@Service
public class StaffLabourAgreementServiceImpl extends GenericServiceImpl<StaffLabourAgreement, Long>	implements StaffLabourAgreementService {
	@Autowired
	private StaffLabourAgreementRepository agreementRepository;
	@Autowired 
	private StaffRepository staffRepository;
	@Autowired
	private LabourAgreementTypeRepository agreementTypeRepository;
	
	@Override
	
	public List<StaffLabourAgreementDto> getAll(Long id) {
		// TODO Auto-generated method stub
		return this.agreementRepository.getAll(id);
	}

	@Override
	public StaffLabourAgreementDto getAgreementById(Long id) {
		return this.agreementRepository.getAgreementById(id);
	}

	@Override
	public StaffLabourAgreementDto saveAgreement(StaffLabourAgreementDto agreementDto, Long id) {
		Staff staff = null;
		LabourAgreementType agreementType = null;
		if (agreementDto != null && agreementDto.getStaff() != null && agreementDto.getStaff().getId() != null) {
			staff = this.staffRepository.findOne(agreementDto.getStaff().getId());
		}
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User modifiedUser = null;
			LocalDateTime currentDate = LocalDateTime.now();
			String currentUserName = "Unknown User";
			if (authentication != null) {
				modifiedUser = (User) authentication.getPrincipal();
				currentUserName = modifiedUser.getUsername();
			}

			StaffLabourAgreement agreement = null;
			if (id != null) {// trường hợp edit
				agreement = this.agreementRepository.findOne(id);
			} else if (agreementDto.getId() != null) {
				agreement = this.agreementRepository.findOne(agreementDto.getId());
			}

			if (agreement == null) {// trường hợp thêm mới
				agreement = new StaffLabourAgreement();
				agreement.setCreateDate(currentDate);
				agreement.setCreatedBy(currentUserName);
			}
			agreement.setModifyDate(currentDate);
			agreement.setModifiedBy(currentUserName);
			
			if (agreementDto.getStartDate() != null)
				agreement.setStartDate(agreementDto.getStartDate());

			if (agreementDto.getEndDate() != null)
				agreement.setEndDate(agreementDto.getEndDate());

			if (agreementDto.getSignedDate() != null)
				agreement.setSignedDate(agreementDto.getSignedDate());
			if (agreementDto.getAgreementStatus() != null)
				agreement.setAgreementStatus(agreementDto.getAgreementStatus());

			agreement.setStaff(staff);
			
			if(agreementDto != null && agreementDto.getLabourAgreementType() != null && agreementDto.getLabourAgreementType().getId() != null) {
				agreementType = this.agreementTypeRepository.findOne(agreementDto.getLabourAgreementType().getId());
			}
			
			agreement.setLabourAgreementType(agreementType);
			agreement = this.agreementRepository.save(agreement);
			agreementDto.setId(agreement.getId());
			agreementDto.getLabourAgreementType().setId(agreementType.getId());
			agreementDto.getLabourAgreementType().setName(agreementType.getName());
			
			
			return agreementDto;

	}

	@Override
	public StaffLabourAgreementDto removeAgreement(Long id) {
		StaffLabourAgreementDto agreementDto = null;
		if (agreementRepository != null && this.agreementRepository.exists(id)) {
			agreementDto = new StaffLabourAgreementDto(this.agreementRepository.findOne(id));
			this.agreementRepository.delete(id);

		}
		return agreementDto;
	}

	@Override
	public boolean removeLists(List<Long> ids) {
		// TODO Auto-generated method stub
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				this.agreementRepository.delete(id);
			}
		}
		return false;

	}

	@Override
	public Page<StaffLabourAgreementDto> getPages(int pageIndex, int pageSize) {
		if (pageIndex > 1) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return agreementRepository.getPages(pageable);
	}

	




}
