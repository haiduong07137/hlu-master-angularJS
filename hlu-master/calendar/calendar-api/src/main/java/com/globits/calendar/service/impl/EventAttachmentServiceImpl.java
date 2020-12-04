package com.globits.calendar.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.calendar.domain.EventAttachment;
import com.globits.calendar.dto.EventAttachmentDto;
import com.globits.calendar.repository.EventAttachmentRepository;
import com.globits.calendar.service.EventAttachmentService;
import com.globits.core.utils.CommonUtils;

@Service
public class EventAttachmentServiceImpl implements EventAttachmentService {

	@Autowired
	private EventAttachmentRepository repos;

	@Override
	@Transactional(readOnly = true)
	public EventAttachmentDto getOne(Long id, boolean includeContent) {

		if (!CommonUtils.isPositive(id, true)) {
			return null;
		}

		EventAttachment entity = repos.findOne(id);

		if (entity != null) {
			return new EventAttachmentDto(entity, includeContent);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EventAttachmentDto saveOne(EventAttachmentDto dto) {

		if (CommonUtils.isNull(dto)) {
			throw new RuntimeException();
		}

		EventAttachment entity = null;

		if (CommonUtils.isPositive(dto.getId(), true)) {
			entity = repos.findOne(dto.getId());
		}

//		if (entity != null) {
//			entity.setTitle(dto.getTitle());
//		} else {
//			entity = dto.toEntity();
//		}

		entity = repos.save(entity);

		if (entity != null) {
			return new EventAttachmentDto(entity, false);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteMultiple(EventAttachmentDto[] dtos) {
		boolean ret = true;

		if (CommonUtils.isEmpty(dtos)) {
			return ret;
		}

		for (EventAttachmentDto dto : dtos) {

			EventAttachment entity = repos.findOne(dto.getId());

			if (CommonUtils.isNull(entity)) {
				throw new RuntimeException();
			}

			repos.delete(entity);
		}

		return ret;
	}

}
