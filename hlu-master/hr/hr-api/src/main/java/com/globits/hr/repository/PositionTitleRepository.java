package com.globits.hr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.PositionTitle;

@Repository
public interface PositionTitleRepository extends JpaRepository<PositionTitle, Long>{
	@Query("select p from PositionTitle p where p.code = ?1")
	List<PositionTitle> findByCode(String code);
	
	@Query("select p from PositionTitle p where p.id = ?1")
	PositionTitle findById(Long positionTitleId);
}
