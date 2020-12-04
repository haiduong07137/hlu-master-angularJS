package com.globits.hr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.dto.LabourAgreementTypeDto;

	@Repository
	public interface LabourAgreementTypeRepository extends JpaRepository<LabourAgreementType, Long>{
		@Query("select new com.globits.hr.dto.LabourAgreementTypeDto(labour) from LabourAgreementType labour")
		Page<LabourAgreementTypeDto> getListPage( Pageable pageable);
		
		@Query("select new com.globits.hr.dto.LabourAgreementTypeDto(labour) from LabourAgreementType labour where labour.name like ?1 or labour.code like ?2")
		Page<LabourAgreementTypeDto> searchByPage(String name, String code, Pageable pageable);
	}

