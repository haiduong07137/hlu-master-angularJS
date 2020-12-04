package com.globits.calendar.service;

import com.globits.calendar.dto.EventAttachmentDto;

public interface EventAttachmentService {

	public EventAttachmentDto getOne(Long id, boolean includeContent);

	public EventAttachmentDto saveOne(EventAttachmentDto dto);

	public boolean deleteMultiple(EventAttachmentDto[] dtos);

}
