package com.globits.hr.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.SalaryItem;
import com.globits.hr.domain.Staff;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;

public interface StaffService extends GenericService<Staff, Long> {

	public Page<StaffDto> findByPageBasicInfo(int pageIndex, int pageSize);

	public Staff updateStaff(Staff staff, Long staffId);

	public Staff createStaff(Staff staff);

	public Staff createStaffAndAccountByCode(Staff staff);

	public Staff getStaff(Long staffId);

	public Page<PositionStaff> findTeacherByDepartment(Long departmentId, int pageIndex, int pageSize);
	
	public StaffDto createStaffFromDto(StaffDto staffDto);
	
	public Page<StaffDto> findPageByCode(StaffSearchDto searchStaffDto, int pageIndex, int pageSize);

	/**
	 * @author Nguyen Tuan Anh
	 */
	public StaffDto deleteStaff(Long id);

	/**
	 * Get list of staffs
	 * 
	 * @author Nguyen Tuan Anh for Calendar
	 */
	public List<StaffDto> getAll();

	/**
	 * delete Multiple staffs
	 * 
	 * @author DangNH for HR
	 */
	public Boolean deleteMultiple(Staff[] staffs);

	public Page<StaffDto> searchStaff(StaffSearchDto dto, int pageSize, int pageIndex);

	public int saveListStaff(List<StaffDto> dtos);

	public Boolean validateStaffCode(String staffCode, Long staffId);

	public Boolean validateUserName(String userName, Long userId);
}
