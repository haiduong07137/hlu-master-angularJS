package com.globits.hr.service.impl;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.EducationDegree;
import com.globits.hr.dto.EducationDegreeDto;
import com.globits.hr.dto.SearchSalaryItemDto;
import com.globits.hr.repository.EducationDegreeRepository;
import com.globits.hr.service.EducationDegreeService;
import com.globits.security.domain.User;
@Transactional
@Service
public class EducationDegreeServiceImpl extends GenericServiceImpl<EducationDegree, Long>  implements EducationDegreeService{

	@Autowired
	EducationDegreeRepository educationDegreeRepository;

	@Override
	public Page<EducationDegreeDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return educationDegreeRepository.getListPage(pageable);
	}

	@Override
	public EducationDegreeDto saveEducationDegree(EducationDegreeDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		EducationDegree educationDegree = null;
		if(dto!=null) {
			if(dto.getId()!=null)
				educationDegree = educationDegreeRepository.findOne(dto.getId());
			if(educationDegree==null) {//Nếu không tìm thấy thì tạo mới 1 đối tượng
				educationDegree = new EducationDegree();
				educationDegree.setCreateDate(currentDate);
				educationDegree.setCreatedBy(currentUserName);
			}
			if(dto.getCode()!=null) {
				educationDegree.setCode(dto.getCode());
			}
			educationDegree.setName(dto.getName());
			
			educationDegree.setModifyDate(currentDate);
			educationDegree.setModifiedBy(currentUserName);
			
			educationDegree = educationDegreeRepository.save(educationDegree);
			return new EducationDegreeDto(educationDegree);
		}
		return null;
	}

	@Override
	public Boolean deleteEducationDegree(Long id) {
		EducationDegree educationDegree = educationDegreeRepository.findOne(id);
		if(educationDegree!=null) {
			educationDegreeRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public EducationDegreeDto getEducationDegree(Long id) {
		EducationDegree educationDegree = educationDegreeRepository.findOne(id);
		if(educationDegree!=null) {
			return new EducationDegreeDto(educationDegree);
		}
		return null;
	}

	@Override
	public int deleteEducationDegrees(List<EducationDegreeDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(EducationDegreeDto dto:dtos) {
			if(dto.getId()!=null) {
				educationDegreeRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

	@Override
	public Page<EducationDegreeDto> searchEducationDegree(SearchSalaryItemDto dto, int pageIndex, int pageSize) {
		String name = '%'+dto.getName()+'%';
		String code = '%'+dto.getCode()+'%';
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return educationDegreeRepository.searchByPage(name, code, pageable);
	}
	

}
