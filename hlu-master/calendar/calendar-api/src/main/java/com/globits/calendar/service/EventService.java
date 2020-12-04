package com.globits.calendar.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.globits.calendar.dto.CalendarPermissionDto;
import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.EventQueryParamDto;
import com.globits.calendar.dto.SearchEventDto;
import com.globits.calendar.dto.ViewDateEventDto;

public interface EventService {

	public EventDto getOne(Long id);

	public Page<EventDto> getPage(Pageable pageable);

	public EventDto saveOne(EventDto dto);
	
	public EventDto saveAttachments(EventDto dto);

	public Boolean deleteMultiple(EventDto[] dtos);

	public List<EventDto> getEvents(EventQueryParamDto params);
	
	public EventDto changeStatus(EventDto dto);

	public EventDto changeTime(EventDto dto);
	
	public EventDto changeLocation(EventDto dto);

	List<ViewDateEventDto> getListEventByWeek(DateTime startDate);

	public int setEventStatus(List<EventDto> dtos, Integer status);

	public int approveEvents(List<EventDto> dtos);

	public int publishEvents(List<EventDto> dtos);

	public int saveListEvents(List<EventDto> dtos);

	List<ViewDateEventDto> getListEventByWeekAndStatusPublish(DateTime startDate);

	CalendarPermissionDto getPermission();
	
	List<EventDto> getEventBySearchDto(SearchEventDto searchEventDto);

}
