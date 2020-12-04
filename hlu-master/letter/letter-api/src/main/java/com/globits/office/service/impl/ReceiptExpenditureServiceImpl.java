package com.globits.office.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.office.domain.ReceiptExpenditure;
import com.globits.office.service.ReceiptExpenditureService;
@Service
@Transactional
public class ReceiptExpenditureServiceImpl extends GenericServiceImpl<ReceiptExpenditure, Long> implements ReceiptExpenditureService
{
}
