package com.globits.letter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.domain.Organization;
import com.globits.core.domain.Person;
import com.globits.core.dto.OrganizationDto;
import com.globits.core.dto.PersonDto;
import com.globits.core.repository.OrganizationRepository;
import com.globits.letter.dto.OrganizationTreeDto;
import com.globits.letter.service.LetterCoreService;
@Service
public class LetterCoreServiceImpl implements LetterCoreService {
	@Autowired
	EntityManager manager;
	@Autowired
	OrganizationRepository orgRepository;
	@Override
	public List<OrganizationDto> getListOrganization(int pageIndex, int pageSize){
		List<Organization> list=  orgRepository.getListOrganizationByTree();
		ArrayList<OrganizationDto> ret = new ArrayList<OrganizationDto>();
		for(Organization org : list) {
			ret.add(new OrganizationDto(org));
		}
		return ret;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<OrganizationTreeDto> getTreeData() {
		String sql = "Select o from Organization o";
		Query q = manager.createQuery(sql,Organization.class);
		List<Organization> list = q.getResultList();
		List<OrganizationTreeDto> dtos = new ArrayList<OrganizationTreeDto>();
		for (Organization organization : list) {
			dtos.add(new OrganizationTreeDto(organization));
		}
		return dtos;
	}
	
	@Override
	public OrganizationDto getOrganizationById(Long organizationId) {
		String sql = "Select o from Organization o where o.id = :organizationId";
		Query q = manager.createQuery(sql,Organization.class);
		OrganizationDto ret = null;
		if(organizationId != null && organizationId > 0) {
			q.setParameter("organizationId", organizationId);
			ret = new OrganizationDto((Organization) q.getSingleResult());
		}
		return ret;
	}

	@Override
	public PersonDto getPersonByStaff(Long staffId) {
		String sql = "Select p from Person p where p.id = :staffId";
		Query q = manager.createQuery(sql,Person.class);
		PersonDto ret = null;
		if(staffId != null && staffId > 0) {
			q.setParameter("staffId", staffId);
			Person p = (Person) q.getSingleResult();
			ret = new PersonDto(p);
		}
		return ret;
	}
}
