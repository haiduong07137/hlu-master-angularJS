package com.globits.letter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.letter.domain.LetterDocField;
import com.globits.letter.service.LetterDocFieldService;
@Service
@Transactional
public class LetterDocFieldServiceImpl extends GenericServiceImpl<LetterDocField, Long> implements LetterDocFieldService{
}
