package com.globits.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.AcademicTitleDto;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.repository.ShiftWorkRepository;
import com.globits.hr.repository.ShiftWorkTimePeriodRepository;
import com.globits.hr.service.ShiftWorkTimePeriodService;
import com.globits.security.domain.User;



@Transactional
@Service
public class ShiftWorkTimePeriodServiceImpl extends GenericServiceImpl<ShiftWorkTimePeriod, Long> implements ShiftWorkTimePeriodService {
	@Autowired
	private ShiftWorkTimePeriodRepository shiftWorkTimePeriodRepository;
	@Autowired
	private EntityManager manager;
	@Autowired
	private ShiftWorkRepository shiftWorkRepository;
	
	@Override
	public ShiftWorkTimePeriodDto saveShiftWorkTimePeriod(ShiftWorkTimePeriodDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		ShiftWorkTimePeriod time = null;
		if (dto.getId() != null && dto.getId() > 0) {
			time = shiftWorkTimePeriodRepository.findOne(dto.getId());
		}
		if (time == null) {
			time = new ShiftWorkTimePeriod();
			time.setCreateDate(currentDate);
			time.setCreatedBy(currentUserName);
		}
		time.setModifiedBy(currentUserName);
		time.setModifyDate(currentDate);
		if (dto.getStartTime() != null) {
			time.setStartTime(dto.getStartTime());
		}
		if (dto.getEndTime() != null) {
			time.setEndTime(dto.getEndTime());;
		}
		 if(dto.getShiftWork()!=null) {
			   ShiftWork shiftwork = shiftWorkRepository.findOne(dto.getShiftWork().getId());
			   time.setShiftWork(shiftwork);;
		   }
		return new ShiftWorkTimePeriodDto(time);
	}

	@Override
	public ShiftWorkTimePeriodDto getShiftWorkTimePeriod(Long id) {
		ShiftWorkTimePeriod entity = shiftWorkTimePeriodRepository.findOne(id);

		if (entity == null) {
			return null;
		} else {
			ShiftWorkTimePeriodDto dto = new ShiftWorkTimePeriodDto(entity);
			return dto;
		}
	}

	@Override
	public Page<ShiftWorkTimePeriodDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return shiftWorkTimePeriodRepository.getListPage(pageable);
	}

	@Override
	public Boolean deleteShiftWorkTimePeriod(Long id) {
		ShiftWorkTimePeriod time = shiftWorkTimePeriodRepository.findOne(id);
		if (time != null) {
			shiftWorkTimePeriodRepository.delete(time);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int deleteShiftWorkTimePeriods(List<ShiftWorkTimePeriodDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(ShiftWorkTimePeriodDto dto:dtos) {
			if(dto.getId()!=null) {
				shiftWorkTimePeriodRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}
	
	@Override
	public List<ShiftWorkTimePeriodDto> getAllByShiftWorkId(long shiftworkId) {
		String sqlCount ="select count(sw) FROM ShiftWorkTimePeriod as sw WHERE sw.shiftwork.id=:shiftworkId";
		String sql ="select new com.globits.hr.dto.ShiftWorkTimePeriodDto(sw) FROM ShiftWorkTimePeriod as sw WHERE sw.shiftwork.id=:shiftworkId";
		
		Query q = manager.createQuery(sql,ShiftWorkTimePeriodDto.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setParameter("shiftworkId", shiftworkId);
		qCount.setParameter("shiftworkId", shiftworkId);
		List<ShiftWorkTimePeriodDto> entities = q.getResultList();
		
		return entities;
			}
	
	


}
