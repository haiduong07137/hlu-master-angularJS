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
import com.globits.hr.domain.StaffFamilyRelationship;
import com.globits.hr.dto.StaffEducationHistoryDto;
import com.globits.hr.dto.StaffFamilyRelationshipDto;
import com.globits.hr.service.StaffEducationHistoryService;
import com.globits.hr.service.StaffFamilyRelationshipService;

@RestController
@RequestMapping("/api/relationship")
public class RestStaffFamilyRelationshipController {
	
	@Autowired
	private StaffFamilyRelationshipService familyRelationshipService;
	
//	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<StaffFamilyRelationshipDto> getPages(@PathVariable int pageIndex, @PathVariable int pageSize){
		return this.familyRelationshipService.getPages(pageIndex, pageSize);
	} 
//	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/getall/{staffId}", method = RequestMethod.GET)
	public List<StaffFamilyRelationshipDto> getAll(@PathVariable ("staffId") Long id){
		return this.familyRelationshipService.getAll(id);
	} 
//	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{familyId}", method = RequestMethod.GET)
	public StaffFamilyRelationshipDto getFamilyById(@PathVariable("familyId") Long id) {
		 return this.familyRelationshipService.getFamilyById(id);
		  
	}
	
	//create
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public StaffFamilyRelationshipDto saveFamily(@RequestBody StaffFamilyRelationshipDto familyDto) {
		return this.familyRelationshipService.saveFamily(familyDto,null);
	}
	//update
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public StaffFamilyRelationshipDto saveFamily(@PathVariable("id") Long id ,@RequestBody StaffFamilyRelationshipDto familyDto) {
		return this.familyRelationshipService.saveFamily(familyDto,id);
	}
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public StaffFamilyRelationshipDto removeFamily(@PathVariable Long id) {
		 return this.familyRelationshipService.removeFamily(id);
	}
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/deleteLists", method = RequestMethod.DELETE)
	public boolean removeLists(@RequestBody List<Long> ids) {
		  this.familyRelationshipService.removeLists(ids);
		  return false;
	}
}
