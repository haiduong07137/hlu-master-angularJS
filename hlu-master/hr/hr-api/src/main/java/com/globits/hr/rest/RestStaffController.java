package com.globits.hr.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.Constants;
import com.globits.core.dto.DepartmentTreeDto;
import com.globits.core.service.DepartmentService;
import com.globits.hr.HrConstants;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.StaffService;

@RestController
@RequestMapping("/api/staff")
public class RestStaffController {
	@Autowired
	private StaffService staffService;
	@Autowired
	private DepartmentService departmentService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/departmenttree", method = RequestMethod.GET)
	public List<DepartmentTreeDto> getTreeData() {
		return departmentService.getTreeData();
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/{staffId}", method = RequestMethod.GET)
	public Staff getStaff(@PathVariable("staffId") String staffId) {
		// Staff staff = staffService.findById(new Long(staffId));
		//
		// Staff retStaff = new Staff(staff);
		// HashSet<PositionStaff> positions = new HashSet<PositionStaff>();
		// if(staff.getPositions()!=null && staff.getPositions().size()>0){
		// Iterator<PositionStaff> iter = staff.getPositions().iterator();
		// while(iter.hasNext()){
		// PositionStaff ps = iter.next();
		// ps = new PositionStaff(ps.getStaff(), ps.getPosition());
		// positions.add(ps);
		// }
		// }
		// retStaff.setPositions(positions);
		// return retStaff;
		return staffService.getStaff(new Long(staffId));
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteStaffs(@RequestBody Staff[] staffs) {
		Boolean deleted = staffService.deleteMultiple(staffs);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<StaffDto> getStaffs(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return staffService.findByPageBasicInfo(pageIndex, pageSize);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public Staff saveStaff(@RequestBody Staff staff) {
		staff = staffService.createStaffAndAccountByCode(staff);
		staff = new Staff(staff);
		return staff;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{staffId}", method = RequestMethod.DELETE)
	public StaffDto removeStaff(@PathVariable("staffId") Long staffId) {
		StaffDto staff = staffService.deleteStaff(staffId);
		return staff;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{staffId}", method = RequestMethod.PUT)
	public Staff updateStaff(@RequestBody Staff staff, @PathVariable("staffId") Long staffId) {
		// return staffService.save(staff);
		staff = staffService.updateStaff(staff, staffId);
		staff = new Staff(staff);
		return staff;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/department/{departmentId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<PositionStaff> findTeacherByDepartment(@PathVariable("departmentId") Long departmentId,
			@PathVariable int pageIndex, @PathVariable int pageSize) {
		return staffService.findTeacherByDepartment(departmentId, pageIndex, pageSize);
	}

	/**
	 * Get a list of staffs
	 * 
	 * @author Tuan Anh for Calendar
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<StaffDto>> getAllStaffs() {
		List<StaffDto> staffs = staffService.getAll();

		return new ResponseEntity<List<StaffDto>>(staffs, HttpStatus.OK);
	}

	/**
	 * Search staffs
	 * 
	 * @author Tuanh Anh for Calendar
	 * @param dto
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/find/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<StaffDto>> findStaffs(@RequestBody StaffSearchDto dto, @PathVariable int pageIndex,
			@PathVariable int pageSize) {

		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<StaffDto> page = staffService.searchStaff(dto, pageSize, pageIndex);

		return new ResponseEntity<Page<StaffDto>>(page, HttpStatus.OK);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
	@RequestMapping(value = "/staffcode/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public Page<StaffDto> getStaffsByCode(@RequestBody StaffSearchDto searchStaffDto, @PathVariable int pageIndex,
			@PathVariable int pageSize) {
		return staffService.findPageByCode(searchStaffDto, pageIndex, pageSize);
	}

//	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN,"ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT","ROLE_FINANCIAL_MANAGERMENT","ROLE_EXAM_MANAGERMENT" })
//	@RequestMapping(method = RequestMethod.POST)
//	public List<StaffDto> getSimpleSearch(@RequestBody StaffSearchDto searchStaffDto) {
//		int pageIndex = 1, pageSize = 10;
//		Page<StaffDto> page = staffService.findPageByCode(searchStaffDto, pageIndex, pageSize);
//		return page.getContent();
//	}
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST, value="/validatestaffcode/{staffId}")
	public Boolean validateStaffCode(@RequestParam String staffCode, @PathVariable Long staffId) {
		return staffService.validateStaffCode(staffCode, staffId);
	}
	
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST, value="/validateusername/{userId}")
	public Boolean validateUserName(@RequestParam String userName, @PathVariable("userId") Long userId) {
		return staffService.validateUserName(userName, userId);
	}
}
