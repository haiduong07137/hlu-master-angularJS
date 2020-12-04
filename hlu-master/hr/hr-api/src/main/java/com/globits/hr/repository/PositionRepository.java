package com.globits.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
	@Query("select u from Position u  where u.id = ?1")
	Position findById(Long id);
}
