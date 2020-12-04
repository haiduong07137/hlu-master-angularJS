package com.globits.document.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.document.domain.DocumentCategory;
import com.globits.document.dto.DocumentCategoryDto;

public interface DocumentCategoryService extends GenericService<DocumentCategory, Long> {

	Page<DocumentCategoryDto> getListDocumentCategory(int pageIndex, int pageSize);

	DocumentCategoryDto getByDocumentCategoryById(Long id);

	DocumentCategoryDto createOrUpdate(DocumentCategoryDto dto);

	Boolean removeDocumentCategory(Long id);
}
