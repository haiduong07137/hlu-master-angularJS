package com.globits.hr.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.StaffLabourAgreementDto;
import com.globits.hr.service.StaffLabourAgreementService;

@RestController
@RequestMapping("api/agreement")
public class RestStaffLabourAgreementController {
	@Autowired 
	private StaffLabourAgreementService agreement;
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<StaffLabourAgreementDto> getPages(@PathVariable int pageIndex, @PathVariable int pageSize){
		return this.agreement.getPages(pageIndex, pageSize);
	} 
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/getall/{staffId}", method = RequestMethod.GET)
	public List<StaffLabourAgreementDto> getAll(@PathVariable ("staffId") Long id){
		return this.agreement.getAll(id);
	} 
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{agreementId}", method = RequestMethod.GET)
	public StaffLabourAgreementDto getAgreementById(@PathVariable("agreementId") Long id) {
		 return this.agreement.getAgreementById(id);
		  
	}
	
	//create
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public StaffLabourAgreementDto saveAgreement(@RequestBody StaffLabourAgreementDto agreementDto) {
		return this.agreement.saveAgreement(agreementDto,null);
	}
	//update
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public StaffLabourAgreementDto saveAgreement(@PathVariable("id") Long id ,@RequestBody StaffLabourAgreementDto agreementDto) {
		return this.agreement.saveAgreement(agreementDto,id);
	}
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public StaffLabourAgreementDto removeAgreement(@PathVariable Long id) {
		 return this.agreement.removeAgreement(id);
	}
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/deleteLists", method = RequestMethod.DELETE)
	public boolean removeLists(@RequestBody List<Long> ids) {
		  this.agreement.removeLists(ids);
		  return false;
	}
}
