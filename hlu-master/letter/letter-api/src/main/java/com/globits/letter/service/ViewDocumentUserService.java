package com.globits.letter.service;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.letter.domain.ViewDocumentUser;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.dto.ViewDocumentUserDto;
import com.globits.security.dto.UserDto;

public interface ViewDocumentUserService extends GenericService<ViewDocumentUser, Long>{
	public ViewDocumentUserDto addView(Long userId, Long documentId);

	public Page<ViewDocumentUserDto> searchUser(SearchDocumentDto searchDocumentDto, Long documentId, int pageIndex, int pageSize);
}
