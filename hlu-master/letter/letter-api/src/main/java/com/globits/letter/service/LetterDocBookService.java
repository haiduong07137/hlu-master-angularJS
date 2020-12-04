package com.globits.letter.service;

import java.util.List;

import com.globits.core.service.GenericService;
import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.dto.LetterDocBookDto;

public interface LetterDocBookService extends GenericService<LetterDocBook, Long> {
	LetterDocBookDto checkDuplicateCode(String code);

	List<LetterDocBook> getDocBooksByGroupId(Long groupId);
}
