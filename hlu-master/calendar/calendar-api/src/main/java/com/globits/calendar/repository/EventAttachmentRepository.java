package com.globits.calendar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.globits.calendar.domain.EventAttachment;

@Repository
public interface EventAttachmentRepository
		extends PagingAndSortingRepository<EventAttachment, Long>{

}
