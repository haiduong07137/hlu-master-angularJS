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
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.repository.SalaryItemRepository;
import com.globits.hr.service.SalaryItemService;
import com.globits.security.domain.User;
@Transactional
@Service
public class SalaryItemServiceImpl extends GenericServiceImpl<SalaryItem, Long>  implements SalaryItemService{

	@Autowired
	SalaryItemRepository salaryItemRepository;

	@Override
	public Page<SalaryItemDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return salaryItemRepository.getListPage(pageable);
	}

	@Override
	public SalaryItemDto saveSalaryItem(SalaryItemDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		SalaryItem salaryItem = null;
		if(dto!=null) {
			if(dto.getId()!=null)
				salaryItem = salaryItemRepository.findOne(dto.getId());
			if(salaryItem==null) {//Nếu không tìm thấy thì tạo mới 1 đối tượng
				salaryItem = new SalaryItem();
				salaryItem.setCreateDate(currentDate);
				salaryItem.setCreatedBy(currentUserName);
			}
			if(dto.getCode()!=null) {
				salaryItem.setCode(dto.getCode());
			}
			salaryItem.setName(dto.getName());
			salaryItem.setFormula(dto.getFormula());
			salaryItem.setIsDefault(dto.getIsDefault());
			salaryItem.setIsActive(dto.getIsActive());
			salaryItem.setType(dto.getType()); // Author Giang 04/06/2018
			
			salaryItem.setModifyDate(currentDate);
			salaryItem.setModifiedBy(currentUserName);
			
			salaryItem = salaryItemRepository.save(salaryItem);
			return new SalaryItemDto(salaryItem);
		}
		return null;
	}

	@Override
	public Boolean deleteSalaryItem(Long id) {
		SalaryItem salaryItem = salaryItemRepository.findOne(id);
		if(salaryItem!=null) {
			salaryItemRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public SalaryItemDto getSalaryItem(Long id) {
		SalaryItem salaryItem = salaryItemRepository.findOne(id);
		if(salaryItem!=null) {
			return new SalaryItemDto(salaryItem);
		}
		return null;
	}

	@Override
	public int deleteSalaryItems(List<SalaryItemDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(SalaryItemDto dto:dtos) {
			if(dto.getId()!=null) {
				salaryItemRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

	@Override
	public Page<SalaryItemDto> searchSalaryItem(SearchSalaryItemDto dto, int pageIndex, int pageSize) {
		String name = '%'+dto.getName()+'%';
		String code = '%'+dto.getCode()+'%';
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return salaryItemRepository.searchByPage(name, code, pageable);
	}
	

}
