package com.globits.letter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.letter.domain.LetterDocumentType;
import com.globits.letter.service.LetterDocumentTypeService;
@Service
@Transactional
public class LetterDocumentTypeServiceImpl extends GenericServiceImpl<LetterDocumentType, Long> implements LetterDocumentTypeService{
}
