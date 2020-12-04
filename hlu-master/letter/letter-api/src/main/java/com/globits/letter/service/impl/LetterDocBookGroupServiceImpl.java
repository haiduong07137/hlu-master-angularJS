package com.globits.letter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.domain.LetterDocBookGroup;
import com.globits.letter.dto.LetterDocBookDto;
import com.globits.letter.dto.LetterDocBookGroupDto;
import com.globits.letter.repository.LetterDocBookGroupRepository;
import com.globits.letter.repository.LetterDocBookRepository;
import com.globits.letter.service.LetterDocBookGroupService;

@Service
@Transactional
public class LetterDocBookGroupServiceImpl extends GenericServiceImpl<LetterDocBookGroup, Long> implements LetterDocBookGroupService {
	@Autowired
	LetterDocBookGroupRepository letterDocBookGroupRepository;

	
	@Override
	public LetterDocBookGroupDto checkDuplicateCode(String code) {
		LetterDocBookGroupDto viewCheckDuplicateCodeDto = new LetterDocBookGroupDto();
		LetterDocBookGroup letterDocBook = null;
		List<LetterDocBookGroup> list = letterDocBookGroupRepository.findListByCode(code);
		if(list != null && list.size() > 0) {
			letterDocBook = list.get(0);
		}
		if(list == null) {
			viewCheckDuplicateCodeDto.setDuplicate(false);
		}else if(list != null && list.size() > 0) {
			viewCheckDuplicateCodeDto.setDuplicate(true);
			viewCheckDuplicateCodeDto.setDupCode(letterDocBook.getCode());
			viewCheckDuplicateCodeDto.setDupName(letterDocBook.getName());
		}
		return viewCheckDuplicateCodeDto;
	}

}
