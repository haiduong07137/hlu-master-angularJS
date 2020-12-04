package com.globits.hr.service.impl;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffEducationHistory;
import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.dto.StaffEducationHistoryDto;
import com.globits.hr.dto.StaffFamilyRelationshipDto;
import com.globits.hr.repository.StaffFamilyRelationshipRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffFamilyRelationshipService;
import com.globits.security.domain.User;

@Transactional
@Service
public class StaffFamilyRelationshipServiceImpl extends GenericServiceImpl<StaffFamilyRelationship, Long>
		implements StaffFamilyRelationshipService {

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private StaffFamilyRelationshipRepository familyRelationshipRepository;

	@Override
	public Page<StaffFamilyRelationshipDto> getPages(int pageIndex, int pageSize) {
		if (pageIndex > 1) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return this.familyRelationshipRepository.getPages(pageable);
	}

	@Override
	public List<StaffFamilyRelationshipDto> getAll(Long id) {
		return this.familyRelationshipRepository.getAll(id);
	}

	@Override
	public StaffFamilyRelationshipDto getFamilyById(Long id) {
		return this.familyRelationshipRepository.getFamilyById(id);
	}

	@Override
	public StaffFamilyRelationshipDto saveFamily(StaffFamilyRelationshipDto familyDto, Long id) {
		Staff staff = null;
		if (familyDto != null && familyDto.getStaff() != null && familyDto.getStaff().getId() != null) {
			staff = this.staffRepository.findOne(familyDto.getStaff().getId());
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		StaffFamilyRelationship familyRelationship = new StaffFamilyRelationship();
		if (id != null) {
			familyRelationship = this.familyRelationshipRepository.findOne(id);
		} else if (familyDto.getId() != null) {
			familyRelationship = this.familyRelationshipRepository.findOne(familyDto.getId());
		}

		if (familyRelationship == null) {// trường hợp thêm mới
			familyRelationship = new StaffFamilyRelationship();
			familyRelationship.setCreateDate(currentDate);
			familyRelationship.setCreatedBy(currentUserName);
		}
		familyRelationship.setModifyDate(currentDate);
		familyRelationship.setModifiedBy(currentUserName);

		if (familyDto.getFullName() != null)
			familyRelationship.setFullName(familyDto.getFullName());
		if (familyDto.getProfession() != null)
			familyRelationship.setProfession(familyDto.getProfession());
		if (familyDto.getAddress() != null)
			familyRelationship.setAddress(familyDto.getAddress());
		if (familyDto.getBirthDate() != null)
			familyRelationship.setBirthDate(familyDto.getBirthDate());
		if(familyDto.getDescription() != null)
			familyRelationship.setDescription(familyDto.getDescription());

		familyRelationship.setStaff(staff);

		familyRelationship = this.familyRelationshipRepository.save(familyRelationship);
		familyDto.setId(familyRelationship.getId());
		return familyDto;
	}

	@Override
	public StaffFamilyRelationshipDto removeFamily(Long id) {
		StaffFamilyRelationshipDto familyRelationshipDto = null;
		if (familyRelationshipRepository != null && this.familyRelationshipRepository.exists(id)) {
			familyRelationshipDto = new StaffFamilyRelationshipDto(this.familyRelationshipRepository.findOne(id));
			this.familyRelationshipRepository.delete(id);

		}
		return familyRelationshipDto;

	}

	@Override
	public boolean removeLists(List<Long> ids) {
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				this.familyRelationshipRepository.delete(id);
			}
		}
		return false;
	}

}
