package com.globits.hr.timesheet.service.impl;

import java.util.ArrayList;
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
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.repository.TimeSheetDetailRepository;
import com.globits.hr.timesheet.repository.TimeSheetRepository;
import com.globits.hr.timesheet.service.TimeSheetDetailService;
import com.globits.security.domain.User;

@Transactional
@Service
public class TimeSheetDetailServiceImpl extends GenericServiceImpl<TimeSheetDetail, Long> implements TimeSheetDetailService{
	
	@Autowired
	TimeSheetDetailRepository timeSheetDetailRepository;
	
	@Autowired
	TimeSheetRepository timeSheetRepository;
	private TimeSheetDetail setTimeSheetDetailValue(TimeSheetDetailDto dto, TimeSheetDetail entity) {
		if(dto.getTimeSheet()!=null) {
			Long timeSheetId = dto.getTimeSheet().getId();
			TimeSheet timeSheet = timeSheetRepository.findOne(timeSheetId);
			entity.setTimeSheet(timeSheet);
		}
		entity.setWorkingItemTitle(dto.getWorkingItemTitle());
		entity.setDuration(dto.getDuration());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
		return entity;
	}
	
	@Override
	public TimeSheetDetailDto saveTimeSheetDetail(TimeSheetDetailDto dto) {
		TimeSheetDetailDto ret = new TimeSheetDetailDto();
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }		
		TimeSheetDetail entity =null;
		if(dto!=null && dto.getId()!=null) {
			entity = timeSheetDetailRepository.findOne(dto.getId());
		}
		
		if(entity==null) {
			entity = new TimeSheetDetail();
			entity.setCreateDate(currentDate);
			entity.setCreatedBy(currentUserName);
		}else {
			entity.setModifyDate(currentDate);
			entity.setModifiedBy(currentUserName);
		}
		entity = setTimeSheetDetailValue(dto,entity);
		entity = timeSheetDetailRepository.save(entity);
		return new TimeSheetDetailDto(entity);
	}
	
	@Override
	public Page<TimeSheetDetailDto> getPage(int pageSize, int pageIndex){
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return timeSheetDetailRepository.getListPage(pageable);
	}
	
	@Override
	public Boolean deleteTimeSheetDetails(List<TimeSheetDetailDto> list) {
		try {
		ArrayList<TimeSheetDetail> entities = new ArrayList<TimeSheetDetail>();
		if(list!=null) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i)!=null && list.get(i).getId()!=null) {
					TimeSheetDetail ts = timeSheetDetailRepository.getOne(list.get(i).getId());
					entities.add(ts);		
				}
					
			}
		}
		timeSheetDetailRepository.delete(entities);
		return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	@Override
	public TimeSheetDetailDto findTimeSheetDetailById(Long id) {
		return timeSheetDetailRepository.findTimeSheetDetailById(id);
	}
}
