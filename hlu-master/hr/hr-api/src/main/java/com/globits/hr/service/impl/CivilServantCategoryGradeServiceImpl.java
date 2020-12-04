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
import com.globits.hr.domain.CivilServantCategoryGrade;
import com.globits.hr.dto.CivilServantCategoryDto;
import com.globits.hr.dto.CivilServantCategoryGradeDto;
import com.globits.hr.repository.CivilServantCategoryGradeRepository;
import com.globits.hr.repository.CivilServantCategoryRepository;
import com.globits.hr.service.CivilServantCategoryGradeService;
import com.globits.hr.service.CivilServantCategoryService;
import com.globits.security.domain.User;

@Transactional
@Service
public class CivilServantCategoryGradeServiceImpl extends GenericServiceImpl<CivilServantCategoryGrade, Long> implements CivilServantCategoryGradeService {
	@Autowired
	private CivilServantCategoryGradeRepository civilServantCategoryGradeRepository;
	
	@Override
	public CivilServantCategoryGradeDto saveCivilServantCategoryGrade(CivilServantCategoryGradeDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		CivilServantCategoryGrade civilServantCategoryGrade = null;
		if (dto.getId() != null && dto.getId() > 0) {
			civilServantCategoryGrade = civilServantCategoryGradeRepository.findOne(dto.getId());
		}
		if (civilServantCategoryGrade == null) {
			civilServantCategoryGrade = new CivilServantCategoryGrade();
			civilServantCategoryGrade.setCreateDate(currentDate);
			civilServantCategoryGrade.setCreatedBy(currentUserName);
		}
		civilServantCategoryGrade.setModifiedBy(currentUserName);
		civilServantCategoryGrade.setModifyDate(currentDate);
		if (dto.getCode() != null) {
			civilServantCategoryGrade.setCode(dto.getCode());
		}
		if (dto.getName() != null) {
			civilServantCategoryGrade.setName(dto.getName());
		}
		civilServantCategoryGrade.setSalaryCoefficient(dto.getSalaryCoefficient());
		if(dto.getCivilServantCategory()!=null) {
			civilServantCategoryGrade.setCivilServantCategory(dto.getCivilServantCategory());
		}
		if(dto.getCivilServantGrade()!=null) {
			civilServantCategoryGrade.setCivilServantGrade(dto.getCivilServantGrade());
		}
		civilServantCategoryGrade.setVoided(dto.getVoided());
		civilServantCategoryGrade = civilServantCategoryGradeRepository.save(civilServantCategoryGrade);
		return new CivilServantCategoryGradeDto(civilServantCategoryGrade);
	}

	@Override
	public CivilServantCategoryGradeDto getCivilServantCategoryGrade(Long id) {
		CivilServantCategoryGrade entity = civilServantCategoryGradeRepository.findOne(id);

		if (entity == null) {
			return null;
		} else {
			CivilServantCategoryGradeDto dto = new CivilServantCategoryGradeDto(entity);
			return dto;
		}
	}

	@Override
	public Page<CivilServantCategoryGradeDto> getPage(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<CivilServantCategoryGrade> _page = civilServantCategoryGradeRepository.findAll(pageable);

		List<CivilServantCategoryGrade> _content = _page.getContent();
		List<CivilServantCategoryGradeDto> content = new ArrayList<CivilServantCategoryGradeDto>();

		for (CivilServantCategoryGrade entity : _content) {
			CivilServantCategoryGradeDto dto = new CivilServantCategoryGradeDto(entity);
			content.add(dto);
		}

		return new PageImpl<>(content, pageable, _page.getTotalElements());
	}

	@Override
	public Boolean removeCivilServantCategoryGrade(Long id) {
		CivilServantCategoryGrade civilServantCategory = civilServantCategoryGradeRepository.findOne(id);
		if (civilServantCategory != null) {
			civilServantCategoryGradeRepository.delete(civilServantCategory);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean deleteMultiple(CivilServantCategoryGradeDto[] dtos) {
		boolean ret = true;

		if (dtos == null || dtos.length <= 0) {
			return ret;
		}
		ArrayList<CivilServantCategoryGrade> civilServantCategorys = new ArrayList<CivilServantCategoryGrade>();
		for (CivilServantCategoryGradeDto dto : dtos) {

			CivilServantCategoryGrade entity = civilServantCategoryGradeRepository.findOne(dto.getId());

			if (entity == null) {
				throw new RuntimeException();
			}
			civilServantCategorys.add(entity);
			
		}
		
		civilServantCategoryGradeRepository.delete(civilServantCategorys);//Sẽ chạy nhanh hơn delete ở trong vòng for

		return ret;
	}
}
