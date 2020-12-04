/*
 * TA va Giang làm
 */

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
import com.globits.hr.domain.AcademicTitle;
import com.globits.hr.dto.AcademicTitleDto;
import com.globits.hr.repository.AcademicTitleRepository;
import com.globits.hr.service.AcademicTitleService;
import com.globits.security.domain.User;
@Transactional
@Service
public class AcademicTitleServiceImpl extends GenericServiceImpl<AcademicTitle, Long>  implements AcademicTitleService{

	@Autowired
	AcademicTitleRepository academicTitleRepository;

	@Override
	public Page<AcademicTitleDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return academicTitleRepository.getListPage(pageable);
	}

	@Override
	public AcademicTitleDto saveAcademicTitle(AcademicTitleDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		AcademicTitle academicTitle = null;
		if(dto!=null) {
			if(dto.getId()!=null)
				academicTitle = academicTitleRepository.findOne(dto.getId());
			if(academicTitle==null) {//Nếu không tìm thấy thì tạo mới 1 đối tượng
				academicTitle = new AcademicTitle();
				academicTitle.setCreateDate(currentDate);
				academicTitle.setCreatedBy(currentUserName);
			}
			if(dto.getCode()!=null) {
				academicTitle.setCode(dto.getCode());
			}
			academicTitle.setName(dto.getName());
			
			
			academicTitle.setModifyDate(currentDate);
			academicTitle.setModifiedBy(currentUserName);
			
			academicTitle = academicTitleRepository.save(academicTitle);
			return new AcademicTitleDto(academicTitle);
		}
		return null;
	}

	@Override
	public Boolean deleteAcademicTitle(Long id) {
		AcademicTitle academicTitle = academicTitleRepository.findOne(id);
		if(academicTitle!=null) {
			academicTitleRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public AcademicTitleDto getAcademicTitle(Long id) {
		AcademicTitle academicTitle = academicTitleRepository.findOne(id);
		if(academicTitle!=null) {
			return new AcademicTitleDto(academicTitle);
		}
		return null;
	}

	@Override
	public int deleteAcademicTitles(List<AcademicTitleDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(AcademicTitleDto dto:dtos) {
			if(dto.getId()!=null) {
				academicTitleRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

		
}
