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
import com.globits.hr.dto.StaffEducationHistoryDto;
import com.globits.hr.repository.StaffEducationHistoryRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.StaffEducationHistoryService;
import com.globits.security.domain.User;

@Transactional
@Service
public class StaffEducationHistoryServiceImpl extends GenericServiceImpl<StaffEducationHistory, Long>
		implements StaffEducationHistoryService {

	@Autowired
	private StaffRepository staffRepository;

	@Autowired
	private StaffEducationHistoryRepository educationHistoryRepository;

	@Override
	public Page<StaffEducationHistoryDto> getPages(int pageIndex, int pageSize) {
		if (pageIndex > 1) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return this.educationHistoryRepository.getPages(pageable);
	}

	@Override
	public List<StaffEducationHistoryDto> getAll(Long id) {
		// TODO Auto-generated method stub
		return this.educationHistoryRepository.getAll(id);
	}

	@Override
	public StaffEducationHistoryDto getEducationById(Long id) {
		return this.educationHistoryRepository.getEducationById(id);
	}

	@Override
	public StaffEducationHistoryDto saveEducation(StaffEducationHistoryDto educationDto, Long id) {

		Staff staff = null;
		if (educationDto != null && educationDto.getStaff() != null && educationDto.getStaff().getId() != null) {
			staff = this.staffRepository.findOne(educationDto.getStaff().getId());
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		StaffEducationHistory educationHistory = new StaffEducationHistory();
		if (id != null) {
			educationHistory = this.educationHistoryRepository.findOne(id);
		} else if (educationDto.getId() != null) {
			educationHistory = this.educationHistoryRepository.findOne(educationDto.getId());
		}

		if (educationHistory == null) {// trường hợp thêm mới
			educationHistory = new StaffEducationHistory();
			educationHistory.setCreateDate(currentDate);
			educationHistory.setCreatedBy(currentUserName);
		}
		educationHistory.setModifyDate(currentDate);
		educationHistory.setModifiedBy(currentUserName);

		if (educationDto.getSchoolName() != null)
			educationHistory.setSchoolName(educationDto.getSchoolName());
		if (educationDto.getStartDate() != null)
			educationHistory.setStartDate(educationDto.getStartDate());
		if (educationDto.getEndDate() != null)
			educationHistory.setEndDate(educationDto.getEndDate());
		if(educationDto.getDescription() != null)
			educationHistory.setDescription(educationDto.getDescription());
		if (educationDto.getStatus() != null)
			educationHistory.setStatus(educationDto.getStatus());

		educationHistory.setStaff(staff);

		educationHistory = this.educationHistoryRepository.save(educationHistory);
		educationDto.setId(educationHistory.getId());
		return educationDto;
	}

	@Override
	public boolean removeLists(List<Long> ids) {
		if (ids != null && ids.size() > 0) {
			for (Long id : ids) {
				this.educationHistoryRepository.delete(id);
			}
		}
		return false;

	}

	@Override
	public StaffEducationHistoryDto removeEducation(Long id) {
		StaffEducationHistoryDto educationDto = null;
		if (educationHistoryRepository != null && this.educationHistoryRepository.exists(id)) {
			educationDto = new StaffEducationHistoryDto(this.educationHistoryRepository.findOne(id));
			this.educationHistoryRepository.delete(id);

		}
		return educationDto;

	}

}
