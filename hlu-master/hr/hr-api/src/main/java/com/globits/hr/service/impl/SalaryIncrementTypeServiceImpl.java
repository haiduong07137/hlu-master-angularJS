/*
 * Created by TA2 & Giang on 23/4/2018.
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
import com.globits.hr.domain.SalaryIncrementType;
import com.globits.hr.dto.SalaryIncrementTypeDto;
import com.globits.hr.repository.SalaryIncrementTypeRepository;
import com.globits.hr.service.SalaryIncrementTypeService;
import com.globits.security.domain.User;
@Transactional
@Service
public class SalaryIncrementTypeServiceImpl extends GenericServiceImpl<SalaryIncrementType, Long>  implements SalaryIncrementTypeService{

	@Autowired
	SalaryIncrementTypeRepository salaryIncrementTypeRepository;

	@Override
	public Page<SalaryIncrementTypeDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return salaryIncrementTypeRepository.getListPage(pageable);
	}

	@Override
	public SalaryIncrementTypeDto saveSalaryIncrementType(SalaryIncrementTypeDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		SalaryIncrementType salaryIncrementType = null;
		if(dto!=null) {
			if(dto.getId()!=null)
				salaryIncrementType = salaryIncrementTypeRepository.findOne(dto.getId());
			if(salaryIncrementType==null) {//Nếu không tìm thấy thì tạo mới 1 đối tượng
				salaryIncrementType = new SalaryIncrementType();
				salaryIncrementType.setCreateDate(currentDate);
				salaryIncrementType.setCreatedBy(currentUserName);
			}
			if(dto.getCode()!=null) {
				salaryIncrementType.setCode(dto.getCode());
			}
			salaryIncrementType.setName(dto.getName());
			
			
			salaryIncrementType.setModifyDate(currentDate);
			salaryIncrementType.setModifiedBy(currentUserName);
			
			salaryIncrementType = salaryIncrementTypeRepository.save(salaryIncrementType);
			return new SalaryIncrementTypeDto(salaryIncrementType);
		}
		return null;
	}

	@Override
	public Boolean deleteSalaryIncrementType(Long id) {
		SalaryIncrementType salaryIncrementType = salaryIncrementTypeRepository.findOne(id);
		if(salaryIncrementType!=null) {
			salaryIncrementTypeRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public SalaryIncrementTypeDto getSalaryIncrementType(Long id) {
		SalaryIncrementType salaryIncrementType = salaryIncrementTypeRepository.findOne(id);
		if(salaryIncrementType!=null) {
			return new SalaryIncrementTypeDto(salaryIncrementType);
		}
		return null;
	}

	@Override
	public int deleteSalaryIncrementTypes(List<SalaryIncrementTypeDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(SalaryIncrementTypeDto dto:dtos) {
			if(dto.getId()!=null) {
				salaryIncrementTypeRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

		
}
