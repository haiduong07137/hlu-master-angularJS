package com.globits.hr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.CivilServantCategory;
import com.globits.hr.domain.CivilServantGrade;

@Repository
public interface CivilServantGradeRepository extends JpaRepository<CivilServantGrade, Long>{
		
}
