/*
 * Created by TA & Giang on 22/4/2018.
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
import com.globits.hr.domain.OvertimeType;
import com.globits.hr.dto.OvertimeTypeDto;
import com.globits.hr.repository.OvertimeTypeRepository;
import com.globits.hr.service.OvertimeTypeService;
import com.globits.security.domain.User;
@Transactional
@Service
public class OvertimeTypeServiceImpl extends GenericServiceImpl<OvertimeType, Long>  implements OvertimeTypeService{

	@Autowired
	OvertimeTypeRepository overtimeTypeRepository;

	@Override
	public Page<OvertimeTypeDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return overtimeTypeRepository.getListPage(pageable);
	}

	@Override
	public OvertimeTypeDto saveOvertimeType(OvertimeTypeDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		 OvertimeType overtimeType = null;
		if(dto!=null) {
			if(dto.getId()!=null)
				overtimeType = overtimeTypeRepository.findOne(dto.getId());
			if(overtimeType==null) {//Nếu không tìm thấy thì tạo mới 1 đối tượng
				overtimeType = new OvertimeType();
				overtimeType.setCreateDate(currentDate);
				overtimeType.setCreatedBy(currentUserName);
			}
			if(dto.getCode()!=null) {
				overtimeType.setCode(dto.getCode());
			}
			overtimeType.setName(dto.getName());
			
			
			overtimeType.setModifyDate(currentDate);
			overtimeType.setModifiedBy(currentUserName);
			
			overtimeType = overtimeTypeRepository.save(overtimeType);
			return new OvertimeTypeDto(overtimeType);
		}
		return null;
	}

	@Override
	public Boolean deleteOvertimeType(Long id) {
		OvertimeType overtimeType = overtimeTypeRepository.findOne(id);
		if(overtimeType!=null) {
			overtimeTypeRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public OvertimeTypeDto getOvertimeType(Long id) {
		OvertimeType overtimeType = overtimeTypeRepository.findOne(id);
		if(overtimeType!=null) {
			return new OvertimeTypeDto(overtimeType);
		}
		return null;
	}

	@Override
	public int deleteOvertimeTypes(List<OvertimeTypeDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(OvertimeTypeDto dto:dtos) {
			if(dto.getId()!=null) {
				overtimeTypeRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

			
}
