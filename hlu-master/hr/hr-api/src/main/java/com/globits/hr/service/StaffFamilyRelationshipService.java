package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.dto.StaffFamilyRelationshipDto;

public interface StaffFamilyRelationshipService extends GenericService<StaffFamilyRelationship, Long>  {

	Page<StaffFamilyRelationshipDto> getPages(int pageIndex, int pageSize);

	List<StaffFamilyRelationshipDto> getAll(Long id);

	StaffFamilyRelationshipDto getFamilyById(Long id);

	StaffFamilyRelationshipDto saveFamily(StaffFamilyRelationshipDto familyDto, Long id);

	StaffFamilyRelationshipDto removeFamily(Long id);

	boolean removeLists(List<Long> ids);

}
