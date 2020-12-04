package com.globits.hr.service.impl;



import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.CivilServantCategory;
import com.globits.hr.dto.CivilServantCategoryDto;
import com.globits.hr.repository.CivilServantCategoryRepository;
import com.globits.hr.service.CivilServantCategoryService;
import com.globits.security.domain.User;

@Transactional
@Service
public class CivilServantCategoryServiceImpl extends GenericServiceImpl<CivilServantCategory, Long> implements CivilServantCategoryService {
	@Autowired
	private CivilServantCategoryRepository civilServantCategoryRepository;
	
	@Override
	public CivilServantCategoryDto saveCivilServantCategory(CivilServantCategoryDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		CivilServantCategory civilServantCategory = null;
		if (dto.getId() != null && dto.getId() > 0) {
			civilServantCategory = civilServantCategoryRepository.findOne(dto.getId());
		}
		if (civilServantCategory == null) {
			civilServantCategory = new CivilServantCategory();
			civilServantCategory.setCreateDate(currentDate);
			civilServantCategory.setCreatedBy(currentUserName);
		}
		civilServantCategory.setModifiedBy(currentUserName);
		civilServantCategory.setModifyDate(currentDate);
		if (dto.getCode() != null) {
			civilServantCategory.setCode(dto.getCode());
		}
		if (dto.getName() != null) {
			civilServantCategory.setName(dto.getName());
		}
		civilServantCategory.setVoided(dto.getVoided());
		civilServantCategory = civilServantCategoryRepository.save(civilServantCategory);
		return new CivilServantCategoryDto(civilServantCategory);
	}

	@Override
	public CivilServantCategoryDto getCivilServantCategory(Long id) {
		CivilServantCategory entity = civilServantCategoryRepository.findOne(id);

		if (entity == null) {
			return null;
		} else {
			CivilServantCategoryDto dto = new CivilServantCategoryDto(entity);
			return dto;
		}
	}

	@Override
	public Page<CivilServantCategoryDto> getPage(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<CivilServantCategory> _page = civilServantCategoryRepository.findAll(pageable);

		List<CivilServantCategory> _content = _page.getContent();
		List<CivilServantCategoryDto> content = new ArrayList<CivilServantCategoryDto>();

		for (CivilServantCategory entity : _content) {
			CivilServantCategoryDto dto = new CivilServantCategoryDto(entity);
			content.add(dto);
		}

		return new PageImpl<>(content, pageable, _page.getTotalElements());
	}

	@Override
	public Boolean removeCivilServantCategory(Long id) {
		CivilServantCategory civilServantCategory = civilServantCategoryRepository.findOne(id);
		if (civilServantCategory != null) {
			civilServantCategoryRepository.delete(civilServantCategory);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean deleteMultiple(CivilServantCategoryDto[] dtos) {
		boolean ret = true;

		if (dtos == null || dtos.length <= 0) {
			return ret;
		}
		ArrayList<CivilServantCategory> civilServantCategorys = new ArrayList<CivilServantCategory>();
		for (CivilServantCategoryDto dto : dtos) {

			CivilServantCategory entity = civilServantCategoryRepository.findOne(dto.getId());

			if (entity == null) {
				throw new RuntimeException();
			}
			civilServantCategorys.add(entity);
			
		}
		
		civilServantCategoryRepository.delete(civilServantCategorys);//Sẽ chạy nhanh hơn delete ở trong vòng for

		return ret;
	}
}
