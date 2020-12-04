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
import com.globits.hr.domain.CivilServantGrade;
import com.globits.hr.dto.CivilServantGradeDto;
import com.globits.hr.repository.CivilServantGradeRepository;
import com.globits.hr.service.CivilServantGradeService;
import com.globits.security.domain.User;

@Transactional
@Service
public class CivilServantGradeServiceImpl extends GenericServiceImpl<CivilServantGrade, Long> implements CivilServantGradeService {
	@Autowired
	private CivilServantGradeRepository civilServantGradeRepository;	
	
	@Override
	public CivilServantGradeDto saveCivilServantGrade(CivilServantGradeDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		CivilServantGrade civilServantGrade = null;
		if (dto.getId() != null && dto.getId() > 0) {
			civilServantGrade = civilServantGradeRepository.findOne(dto.getId());
		}
		if (civilServantGrade == null) {
			civilServantGrade = new CivilServantGrade();
			civilServantGrade.setCreateDate(currentDate);
			civilServantGrade.setCreatedBy(currentUserName);
		}
		civilServantGrade.setModifiedBy(currentUserName);
		civilServantGrade.setModifyDate(currentDate);
		if (dto.getCode() != null) {
			civilServantGrade.setCode(dto.getCode());
		}
		if (dto.getName() != null) {
			civilServantGrade.setName(dto.getName());
		}
		if (dto.getDescription() != null) {
			civilServantGrade.setDescription(dto.getDescription());
		}
		civilServantGrade.setGrade(dto.getGrade());
		civilServantGrade.setNextGradeId(dto.getNextGradeId());
		civilServantGrade.setMaxGrade(dto.getMaxGrade());
		civilServantGrade.setSalaryCoefficient(dto.getSalaryCoefficient());
//		civilServantGrade.setCivilServantCategory(dto.getCivilServantCategory());
		civilServantGrade = civilServantGradeRepository.save(civilServantGrade);
		return new CivilServantGradeDto(civilServantGrade);
	}

	@Override
	public CivilServantGradeDto getCivilServantGrade(Long id) {
		CivilServantGrade entity = civilServantGradeRepository.findOne(id);

		if (entity == null) {
			return null;
		} else {
			CivilServantGradeDto dto = new CivilServantGradeDto(entity);
			return dto;
		}
	}

	@Override
	public Page<CivilServantGradeDto> getPage(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<CivilServantGrade> _page = civilServantGradeRepository.findAll(pageable);

		List<CivilServantGrade> _content = _page.getContent();
		List<CivilServantGradeDto> content = new ArrayList<CivilServantGradeDto>();

		for (CivilServantGrade entity : _content) {
			CivilServantGradeDto dto = new CivilServantGradeDto(entity);
			content.add(dto);
		}

		return new PageImpl<>(content, pageable, _page.getTotalElements());
	}

	@Override
	public Boolean removeCivilServantGrade(Long id) {
		CivilServantGrade civilServantGrade = civilServantGradeRepository.findOne(id);
		if (civilServantGrade != null) {
			civilServantGradeRepository.delete(civilServantGrade);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean deleteMultiple(CivilServantGradeDto[] dtos) {
		boolean ret = true;

		if (dtos == null || dtos.length <= 0) {
			return ret;
		}
		ArrayList<CivilServantGrade> civilServantGrades = new ArrayList<CivilServantGrade>();
		for (CivilServantGradeDto dto : dtos) {

			CivilServantGrade entity = civilServantGradeRepository.findOne(dto.getId());

			if (entity == null) {
				throw new RuntimeException();
			}
			civilServantGrades.add(entity);
			
		}
		
		civilServantGradeRepository.delete(civilServantGrades);//Sẽ chạy nhanh hơn delete ở trong vòng for

		return ret;
	}

}
