package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;

@Repository
public interface ShiftWorkTimePeriodRepository extends JpaRepository<ShiftWorkTimePeriod, Long>{
	@Query("select new com.globits.hr.dto.ShiftWorkTimePeriodDto(time) from ShiftWorkTimePeriod time")
	Page<ShiftWorkTimePeriodDto> getListPage( Pageable pageable);
	
	
}

