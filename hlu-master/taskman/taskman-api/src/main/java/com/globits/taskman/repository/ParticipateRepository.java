package com.globits.taskman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.Participate;
import com.globits.taskman.dto.ParticipateDto;
@Repository
public interface ParticipateRepository  extends JpaRepository<Participate, Long> {
	@Query("select new com.globits.taskman.dto.ParticipateDto(d) from Participate d")
	public Page<ParticipateDto> getListParticipate(Pageable pageable);
}
