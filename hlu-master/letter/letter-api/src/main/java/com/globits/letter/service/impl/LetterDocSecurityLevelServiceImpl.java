package com.globits.letter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.letter.domain.LetterDocSecurityLevel;
import com.globits.letter.domain.LetterDocumentType;
import com.globits.letter.service.LetterDocSecurityLevelService;
import com.globits.letter.service.LetterDocumentTypeService;
@Service
@Transactional
public class LetterDocSecurityLevelServiceImpl extends GenericServiceImpl<LetterDocSecurityLevel, Long> implements LetterDocSecurityLevelService{
}
