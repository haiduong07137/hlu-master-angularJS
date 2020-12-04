package com.globits.calendar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.globits.calendar.domain.EventPriority;
import com.globits.calendar.dto.EventPriorityDto;

@Repository
public interface EventPriorityRepository
		extends PagingAndSortingRepository<EventPriority, Long>{
	@Query("select new com.globits.calendar.dto.EventPriorityDto(d) from EventPriority d where d.name like %?1% or d.description like %?1% or d.priority like %?1%")
	public Page<EventPriorityDto> findPage(String name,Pageable pageable);
	
	@Query("select new com.globits.calendar.dto.EventPriorityDto(d) from EventPriority d")
	public Page<EventPriorityDto> getList(Pageable pageable);
}
