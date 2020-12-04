package com.globits.letter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.dto.LetterDocBookDto;
import com.globits.letter.repository.LetterDocBookRepository;
import com.globits.letter.service.LetterDocBookService;
@Service
@Transactional
public class LetterDocBookServiceImpl extends GenericServiceImpl<LetterDocBook, Long> implements LetterDocBookService{
	@Autowired
	LetterDocBookRepository letterDocBookRepository;

	@Override
	public LetterDocBookDto checkDuplicateCode(String code) {
		LetterDocBookDto viewCheckDuplicateCodeDto = new LetterDocBookDto();
		LetterDocBook letterDocBook = null;
		List<LetterDocBook> list = letterDocBookRepository.findListByCode(code);
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

	@Override
	public List<LetterDocBook> getDocBooksByGroupId(Long groupId) {
		// TODO Auto-generated method stub
		return letterDocBookRepository.getAllByGroupId(groupId);
	}
}
