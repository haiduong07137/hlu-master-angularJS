package com.globits.letter.service;

import java.util.List;


import com.globits.core.dto.OrganizationDto;
import com.globits.core.dto.PersonDto;
import com.globits.letter.dto.OrganizationTreeDto;

public interface LetterCoreService {
	public List<OrganizationDto> getListOrganization(int pageIndex, int pageSize);
	public List<OrganizationTreeDto> getTreeData();
	public OrganizationDto getOrganizationById(Long organizationId);
	public PersonDto getPersonByStaff(Long staffId);
}
