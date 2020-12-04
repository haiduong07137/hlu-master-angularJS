package com.globits.calendar.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
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

import com.globits.calendar.domain.EventPriority;
import com.globits.calendar.dto.EventPriorityDto;
import com.globits.calendar.repository.EventPriorityRepository;
import com.globits.calendar.service.EventPriorityService;
import com.globits.core.utils.CommonUtils;
import com.globits.security.domain.User;

@Service
public class EventPriorityServiceImpl implements EventPriorityService {

	@Autowired
	private EventPriorityRepository repos;

	@Override
	@Transactional(readOnly = true)
	public EventPriorityDto getOne(Long id) {

		if (!CommonUtils.isPositive(id, true)) {
			return null;
		}

		EventPriority entity = repos.findOne(id);

		if (entity == null) {
			return null;
		} else {
			return new EventPriorityDto(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EventPriorityDto saveOne(EventPriorityDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		if (dto == null) {
			throw new RuntimeException("Cannot save a null object!");
		}

		EventPriority entity = null;

		if (CommonUtils.isPositive(dto.getId(), true)) {
			entity = repos.findOne(dto.getId());
		}

		if (entity == null) {
			entity = dto.toEntity();
			entity.setCreateDate(currentDate);
			entity.setCreatedBy(currentUserName);
		} else {
			entity.setDescription(dto.getDescription());
			entity.setName(dto.getName());
			entity.setPriority(dto.getPriority());
			entity.setModifyDate(currentDate);
			entity.setModifiedBy(currentUserName);
		}

		entity = repos.save(entity);

		if (entity == null) {
			throw new RuntimeException("Persisting an entity failed!");
		} else {
			return new EventPriorityDto(entity);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EventPriorityDto> getPage(Pageable pageable) {
		Iterator<EventPriority> itr = repos.findAll(pageable).iterator();
		List<EventPriorityDto> content = new ArrayList<EventPriorityDto>();

		while (itr.hasNext()) {
			content.add(new EventPriorityDto(itr.next()));
		}

		return new PageImpl<EventPriorityDto>(content, pageable, repos.count());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventPriorityDto> getAll() {

		Iterator<EventPriority> itr = repos.findAll().iterator();
		List<EventPriorityDto> list = new ArrayList<EventPriorityDto>();

		while (itr.hasNext()) {
			list.add(new EventPriorityDto(itr.next()));
		}

		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteMultiple(EventPriorityDto[] dtos) {

		if (CommonUtils.isEmpty(dtos)) {
			return false;
		}

		for (EventPriorityDto dto : dtos) {
			if (!CommonUtils.isPositive(dto.getId(), true)) {
				continue;
			}

			EventPriority entity = repos.findOne(dto.getId());

			if (entity != null) {
				repos.delete(entity);
			}
		}

		return true;
	}
	@Override
	public Page<EventPriorityDto> findPage(String textSearch, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repos.findPage(textSearch, pageable);
	}
	
	@Override
	public Page<EventPriorityDto> getList(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repos.getList(pageable);
	}
	

}
