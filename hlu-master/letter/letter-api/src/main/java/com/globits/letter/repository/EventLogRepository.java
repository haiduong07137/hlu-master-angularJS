package com.globits.letter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.EventLog;
@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
	
}
