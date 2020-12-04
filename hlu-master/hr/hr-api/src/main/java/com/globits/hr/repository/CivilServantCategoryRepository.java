package com.globits.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.CivilServantCategory;

@Repository
public interface CivilServantCategoryRepository extends JpaRepository<CivilServantCategory, Long>{
		
}
