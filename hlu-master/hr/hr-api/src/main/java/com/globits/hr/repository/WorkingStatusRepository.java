package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.Position;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.WorkingStatusDto;

@Repository
public interface WorkingStatusRepository extends JpaRepository<WorkingStatus, Long> {
	@Query("select new com.globits.hr.dto.WorkingStatusDto(status) from WorkingStatus status")
	Page<WorkingStatusDto> getListPage( Pageable pageable);
}
