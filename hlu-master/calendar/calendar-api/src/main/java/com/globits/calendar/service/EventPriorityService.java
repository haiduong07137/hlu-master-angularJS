package com.globits.calendar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.globits.calendar.dto.EventPriorityDto;

public interface EventPriorityService {

	public EventPriorityDto getOne(Long id);

	public EventPriorityDto saveOne(EventPriorityDto dto);

	public Page<EventPriorityDto> getPage(Pageable pageable);

	public List<EventPriorityDto> getAll();

	public boolean deleteMultiple(EventPriorityDto[] dtos);
	public Page<EventPriorityDto> findPage(String textSearch, int pageIndex, int pageSize);

	public Page<EventPriorityDto> getList(int pageSize, int pageIndex);


}
