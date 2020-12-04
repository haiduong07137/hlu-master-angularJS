package com.globits.letter.service;

import com.globits.core.service.GenericService;
import com.globits.letter.domain.LetterDocBookGroup;
import com.globits.letter.dto.LetterDocBookDto;
import com.globits.letter.dto.LetterDocBookGroupDto;

public interface LetterDocBookGroupService  extends GenericService<LetterDocBookGroup, Long>{
	LetterDocBookGroupDto checkDuplicateCode(String code);
}
