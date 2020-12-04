package com.globits.calendar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.globits.calendar.domain.EventAttendee;

@Repository
public interface EventAttendeeRepository
		extends PagingAndSortingRepository<EventAttendee, Long>{

}
