package com.globits.hr.service.impl;

import java.util.HashSet;
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
import com.globits.hr.domain.SalaryConfig;
import com.globits.hr.domain.SalaryConfigItem;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.SalaryConfigItemDto;
import com.globits.hr.dto.SalaryItemDto;
import com.globits.hr.repository.SalaryConfigItemRepository;
import com.globits.hr.repository.SalaryConfigRepository;
import com.globits.hr.repository.SalaryItemRepository;
import com.globits.hr.service.SalaryConfigService;
import com.globits.hr.service.SalaryItemService;
import com.globits.security.domain.User;
@Transactional
@Service
public class SalaryConfigServiceImpl extends GenericServiceImpl<SalaryConfig, Long>  implements SalaryConfigService{

	@Autowired
	SalaryConfigRepository salaryConfigRepository;
	
	@Autowired
	SalaryConfigItemRepository salaryConfigItemRepository;
	
	@Autowired
	SalaryItemRepository salaryItemRepository;

	@Override
	public Page<SalaryConfigDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return salaryConfigRepository.getListPage(pageable);
	}

	@Override
	public SalaryConfigDto saveSalaryConfig(SalaryConfigDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		SalaryConfig salaryConfig = null;
		if(dto!=null) {
			if(dto.getId()!=null)
				salaryConfig = salaryConfigRepository.findOne(dto.getId());
			if(salaryConfig==null) {//Nếu không tìm thấy thì tạo mới 1 đối tượng
				salaryConfig = new SalaryConfig();
				salaryConfig.setCreateDate(currentDate);
				salaryConfig.setCreatedBy(currentUserName);
			}
			if(dto.getCode()!=null) {
				salaryConfig.setCode(dto.getCode());
			}
			salaryConfig.setName(dto.getName());
			salaryConfig.setCode(dto.getCode());
			
			salaryConfig.setModifyDate(currentDate);
			salaryConfig.setModifiedBy(currentUserName);
			//Cần thêm đoạn code để tạo ra và thêm danh sách SalaryConfiItem vào Entity salaryConfig
			if(dto.getSalaryConfigItems()!=null && dto.getSalaryConfigItems().size()>0) {
				HashSet<SalaryConfigItem> salaryConfigItems = new HashSet<SalaryConfigItem>();
				for(SalaryConfigItemDto sDto:dto.getSalaryConfigItems()) {
					SalaryConfigItem salaryConfigItem = null;
					if(sDto.getId()!=null) {
						salaryConfigItem = salaryConfigItemRepository.findOne(sDto.getId());
					}
					if(salaryConfigItem==null) {
						salaryConfigItem = new SalaryConfigItem();
						salaryConfigItem.setCreateDate(currentDate);
						salaryConfigItem.setCreatedBy(currentUserName);
					}
					if(salaryConfig != null) {
						salaryConfigItem.setSalaryConfig(salaryConfig);
					}
					if(sDto.getSalaryItem()!=null && sDto.getSalaryItem().getId()!=null) {
						SalaryItem salaryItem= salaryItemRepository.findOne(sDto.getSalaryItem().getId());
						salaryConfigItem.setSalaryItem(salaryItem);
					}
					salaryConfigItem.setFormula(sDto.getFormula());
					salaryConfigItem.setItemOrder(sDto.getItemOrder());
					salaryConfigItems.add(salaryConfigItem);
				}
				if(salaryConfig.getSalaryConfigItems()!=null) {
					salaryConfig.getSalaryConfigItems().clear();
					salaryConfig.getSalaryConfigItems().addAll(salaryConfigItems);
				}else {
					salaryConfig.setSalaryConfigItems(salaryConfigItems);
				}
			}
			salaryConfig = salaryConfigRepository.save(salaryConfig);
			return new SalaryConfigDto(salaryConfig);
		}
		return null;
	}
//
	@Override
	public Boolean deleteSalaryConfig(Long id) {
		SalaryConfig salaryConfig = salaryConfigRepository.findOne(id);
		if(salaryConfig!=null) {
			salaryConfigRepository.delete(id);
			return true;
		}
		return false;
	}
//
	@Override
	public SalaryConfigDto getSalaryConfig(Long id) {
		SalaryConfig salaryConfig = salaryConfigRepository.findOne(id);
		if(salaryConfig!=null) {
			return new SalaryConfigDto(salaryConfig);
		}
		return null;
	}
//
	@Override
	public int deleteSalaryConfig(List<SalaryConfigDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(SalaryConfigDto dto:dtos) {
			if(dto.getId()!=null) {
				salaryConfigRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}



}
