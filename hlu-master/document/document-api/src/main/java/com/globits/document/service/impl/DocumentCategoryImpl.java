package com.globits.document.service.impl;

import javax.persistence.EntityManager;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.document.domain.DocumentCategory;
import com.globits.document.dto.DocumentCategoryDto;
import com.globits.document.repository.DocumentCategoryRepository;
import com.globits.document.service.DocumentCategoryService;
import com.globits.security.domain.User;

@Service
public class DocumentCategoryImpl extends GenericServiceImpl<DocumentCategory, Long> implements DocumentCategoryService {
	@Autowired
	DocumentCategoryService documentCategoryService;
	@Autowired
	DocumentCategoryRepository documentCategoryRepository;
	@Autowired
	EntityManager manager;
	
	@Override
	public Page<DocumentCategoryDto> getListDocumentCategory(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return documentCategoryRepository.getAllDocumentCategory(pageable);
	}

	@Override
	public DocumentCategoryDto getByDocumentCategoryById(Long id) {
		return documentCategoryRepository.getDocumentCategoryById(id);
	}

	@Override
	public Boolean removeDocumentCategory(Long id) {
		if (id != null) {
			try {
				documentCategoryRepository.delete(id);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return null;
	}

	@Override
	public DocumentCategoryDto createOrUpdate(DocumentCategoryDto dto) {
		Boolean isAdd=false;
		DocumentCategory st = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}		
		if (dto.getId() != null) {
			st = documentCategoryRepository.findOne(dto.getId());
		}
		
		if (st == null) {
			isAdd=true;
			st = new DocumentCategory();
			st.setCreateDate(currentDate);
			st.setCreatedBy(currentUserName);
		}
		if(dto.getCode()!=null) {
			st.setCode(dto.getCode());
		}
		if(dto.getName()!=null) {
			st.setName(dto.getName());
		}
		st = documentCategoryRepository.save(st);
			
		
		return new DocumentCategoryDto(st);
	}

}
