package com.globits.letter.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.letter.domain.LetterDocPriority;
import com.globits.letter.service.LetterDocPriorityService;
@Service
@Transactional
public class LetterDocPriorityServiceImpl extends GenericServiceImpl<LetterDocPriority, Long> implements LetterDocPriorityService{
}
