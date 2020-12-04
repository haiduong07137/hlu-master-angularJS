package com.globits.document.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.document.domain.Document;
import com.globits.document.dto.DocumentDto;
import com.globits.document.dto.SearchDto;

public interface DocumentService extends GenericService<Document, Long>{

	DocumentDto getByDocumentById(Long id);

	Page<DocumentDto> getListDocument(int pageIndex, int pageSize);

	Boolean removeDocument(Long id);

	DocumentDto createOrUpdate(DocumentDto dto);

	Page<DocumentDto> searchDocumentBySearchDto(SearchDto search);
}

