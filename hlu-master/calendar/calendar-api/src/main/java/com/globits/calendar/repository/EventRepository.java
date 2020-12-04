package com.globits.calendar.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.globits.calendar.domain.Event;

@Repository
public interface EventRepository
		extends PagingAndSortingRepository<Event, Long>{

	// @Query("select event from Event event where event.startTime<= ?1 and
	// event.startTime>=?2")
	// public List<Event> listEventByDate(DateTime fromDate, DateTime toDate);
	//
	// @Query("select event from Event event where event.scope=?1 and
	// event.startTime>= ?2 and event.startTime<=?3")
	// public List<Event> listEventByScopeAndDate(Integer scope, DateTime fromDate,
	// DateTime toDate);
	//
	// @Query("select event from Event event join fetch event.attendees att where
	// (att.department.id=?1 or event.ownerDepartment.id=?1) and event.startTime>=
	// ?2 and event.startTime<=?3")
	// public List<Event> listEventByDepartmentAndDate(Long depId, DateTime
	// fromDate, DateTime toDate);

}
