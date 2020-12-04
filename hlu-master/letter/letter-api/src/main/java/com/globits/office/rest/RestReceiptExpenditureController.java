package com.globits.office.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.office.domain.ReceiptExpenditure;
import com.globits.office.service.ReceiptExpenditureService;

@RestController
@RequestMapping("/api/office/expenditure")
public class RestReceiptExpenditureController {
	@Autowired
	private ReceiptExpenditureService receiptExpenditureService;
	
	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/{expenditureId}", method = RequestMethod.GET)
	public ReceiptExpenditure getDocBook(@PathVariable("expenditureId") String expenditureId) {
		return receiptExpenditureService.findById(new Long(expenditureId));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" ,"ROLE_STUDENT_MANAGERMENT","ROLE_STUDENT"})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<ReceiptExpenditure> getDocBooks(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return receiptExpenditureService.getList(pageIndex, pageSize);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ReceiptExpenditure saveDocBook(@RequestBody ReceiptExpenditure docField) {
		return receiptExpenditureService.save(docField);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{expenditureId}", method = RequestMethod.DELETE)
	public ReceiptExpenditure removeDocBook(@PathVariable("expenditureId") String expenditureId) {
		ReceiptExpenditure docField = receiptExpenditureService.delete(new Long(expenditureId));
		return docField;
	}
}
