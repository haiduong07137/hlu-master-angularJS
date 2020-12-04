package com.globits.hr.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.dto.StaffFamilyRelationshipDto;

@Repository
public interface StaffFamilyRelationshipRepository extends JpaRepository<StaffFamilyRelationship, Long> {

	@Query("select new com.globits.hr.dto.StaffFamilyRelationshipDto(family) from StaffFamilyRelationship family")
	Page<StaffFamilyRelationshipDto> getPages(Pageable pageable);
	
	@Query("select new com.globits.hr.dto.StaffFamilyRelationshipDto(family) from StaffFamilyRelationship family where family.staff.id = ?1")
	List<StaffFamilyRelationshipDto> getAll(Long id);
	
	@Query("select new com.globits.hr.dto.StaffFamilyRelationshipDto(family) from StaffFamilyRelationship family where family.id = ?1")
	StaffFamilyRelationshipDto getFamilyById(Long id);

}
	