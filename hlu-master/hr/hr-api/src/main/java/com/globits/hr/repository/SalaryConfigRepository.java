package com.globits.hr.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.SalaryConfig;
import com.globits.hr.dto.SalaryConfigDto;
import com.globits.hr.dto.SalaryConfigItemDto;

@Repository
public interface SalaryConfigRepository extends JpaRepository<SalaryConfig, Long>{
	@Query("select new com.globits.hr.dto.SalaryConfigDto(sc) from SalaryConfig sc")
	Page<SalaryConfigDto> getListPage( Pageable pageable);
	

}
