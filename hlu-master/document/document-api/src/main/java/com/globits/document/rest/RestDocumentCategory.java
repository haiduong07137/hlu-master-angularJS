package com.globits.document.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.Constants;
import com.globits.document.DocumentConstant;
import com.globits.document.dto.DocumentCategoryDto;
import com.globits.document.service.DocumentCategoryService;

@RestController
@RequestMapping("/api/document_category")
public class RestDocumentCategory {
	@Autowired
	DocumentCategoryService service;
		
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public Page<DocumentCategoryDto> getList(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return service.getListDocumentCategory(pageIndex, pageSize);
	}
	
	@RequestMapping(path="/{id}",method = RequestMethod.GET)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public DocumentCategoryDto findById( @PathVariable Long id) {
		return service.getByDocumentCategoryById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public DocumentCategoryDto createOrUpdate(@RequestBody DocumentCategoryDto dto) {
		return service.createOrUpdate(dto);
	}
	
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public Boolean removeDocumentCategory(@PathVariable("Id") String Id) {
		return service.removeDocumentCategory(new Long(Id));
	}
}
