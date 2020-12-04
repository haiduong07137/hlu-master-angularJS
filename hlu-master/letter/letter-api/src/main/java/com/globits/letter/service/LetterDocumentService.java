package com.globits.letter.service;

import org.springframework.data.domain.Page;

import com.globits.letter.dto.LetterDocumentDto;
import com.globits.letter.dto.SearchDocumentDto;

public interface LetterDocumentService {
	public Page<LetterDocumentDto> getListDocument(int pageIndex, int pageSize);
	public LetterDocumentDto getByDocumentById(Long documentId);
	Page<LetterDocumentDto> searchDocument(SearchDocumentDto searchDto, int pageIndex, int pageSize);
}
