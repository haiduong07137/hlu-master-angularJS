package com.globits.hr.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.globits.core.dto.PersonDto;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.security.dto.UserDto;

public class StaffDto extends PersonDto {

	private String staffCode;

	private Set<PositionStaffDto> positions = new HashSet<PositionStaffDto>();
	
	private Set<StaffLabourAgreementDto> agreements = new HashSet<StaffLabourAgreementDto>();

	private UserDto user;
	
	private String currentCell; // for import excel
	
	public StaffDto() {
	}

	public StaffDto(Long id, String staffCode, String displayName, String gender) {
			this.setId(id);
			this.setStaffCode(staffCode);
			this.setDisplayName(displayName);
			this.setGender(gender);
	}
	
	public StaffDto(Staff entity) {
		super(entity);
		if (entity == null) {
			return;
		}
		
		id = entity.getId();
		staffCode = entity.getStaffCode();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		displayName = entity.getDisplayName();
		gender = entity.getGender();
		
		if (entity.getPositions() != null) {
			List<PositionStaffDto> list = new ArrayList<PositionStaffDto>();

			for (PositionStaff _e : entity.getPositions()) {
				list.add(new PositionStaffDto(_e));
			}

			if (list.size() > 0) {
				positions.addAll(list);
			}
		}
		
		if(entity.getAgreements() != null) {
			List<StaffLabourAgreementDto> list = new ArrayList<StaffLabourAgreementDto>();
			
			for(StaffLabourAgreement agreement : entity.getAgreements()) {
				list.add(new StaffLabourAgreementDto(agreement));
			}
			
			if(list.size() > 0) {
				agreements.addAll(list);
			}
		}
		
		
		if (entity.getUser() != null) {
			user = new UserDto(entity.getUser());
		}
	}

	@Override
	public Staff toEntity() {
		Staff staff = new Staff();

		staff.setId(id);
		staff.setStaffCode(staffCode);
		staff.setFirstName(staffCode);
		staff.setLastName(staffCode);
		staff.setGender(gender);

		if (positions.size() > 0) {
			List<PositionStaff> list = new ArrayList<PositionStaff>();

			for (PositionStaffDto _d : positions) {
				list.add(_d.toEntity());
			}

			staff.getPositions().addAll(list);
		}

		return staff;
	}

	
	
	public Set<StaffLabourAgreementDto> getAgreements() {
		return agreements;
	}

	public void setAgreements(Set<StaffLabourAgreementDto> agreements) {
		this.agreements = agreements;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public Set<PositionStaffDto> getPositions() {
		return positions;
	}

	public void setPositions(Set<PositionStaffDto> positions) {
		this.positions = positions;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getCurrentCell() {
		return currentCell;
	}

	public void setCurrentCell(String currentCell) {
		this.currentCell = currentCell;
	}
	
}
