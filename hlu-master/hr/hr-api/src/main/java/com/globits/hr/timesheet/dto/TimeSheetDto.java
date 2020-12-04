package com.globits.hr.timesheet.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.domain.TimeSheetDetail;

public class TimeSheetDto {
	
	private Long id;
	
	private Date workingDate;
	
	private double 	totalHours;
	
	private WorkingStatusDto workingStatus;
	
	private Date startTime;
	
	private Date endTime;
	
	private StaffDto employee;

	private List<TimeSheetDetailDto> details;
	
	private ShiftWorkDto shiftWork;
	
	private Integer approveStatus;
	
	public ShiftWorkDto getShiftWork() {
		return shiftWork;
	}

	public void setShiftWork(ShiftWorkDto shiftWork) {
		this.shiftWork = shiftWork;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public StaffDto getEmployee() {
		return employee;
	}

	public void setEmployee(StaffDto employee) {
		this.employee = employee;
	}
	
	public List<TimeSheetDetailDto> getDetails() {
		return details;
	}

	public void setDetails(List<TimeSheetDetailDto> details) {
		this.details = details;
	}
	
	public WorkingStatusDto getWorkingStatus() {
		return workingStatus;
	}

	public void setWorkingStatus(WorkingStatusDto workingStatus) {
		this.workingStatus = workingStatus;
	}
	
	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public TimeSheetDto() {
		
	}
	
	public TimeSheetDto(TimeSheet timeSheet) {
		if(timeSheet!=null) {
			this.id = timeSheet.getId();
			this.workingDate = timeSheet.getWorkingDate();
			this.startTime = timeSheet.getStartTime();
			this.endTime = timeSheet.getEndTime();
			this.totalHours = timeSheet.getTotalHours();
			this.approveStatus = timeSheet.getApproveStatus();
			if(timeSheet.getWorkingStatus()!=null) {
				this.workingStatus=new WorkingStatusDto();
				this.workingStatus.setId(timeSheet.getWorkingStatus().getId());
				this.workingStatus.setName(timeSheet.getWorkingStatus().getName());
				this.workingStatus.setCode(timeSheet.getWorkingStatus().getCode());
				this.workingStatus.setStatusValue(timeSheet.getWorkingStatus().getStatusValue());
			}
			if (timeSheet.getEmployee()!=null) {
				this.employee = new StaffDto();
				this.employee.setId(timeSheet.getEmployee().getId());
				this.employee.setLastName(timeSheet.getEmployee().getLastName());
				this.employee.setFirstName(timeSheet.getEmployee().getFirstName());
				this.employee.setDisplayName(timeSheet.getEmployee().getDisplayName());
				//Thêm các trường khác vào đây
			}
			
			if(timeSheet.getDetails()!=null && timeSheet.getDetails().size()>0) {
				this.details = new ArrayList<TimeSheetDetailDto>();
				Iterator<TimeSheetDetail> iters= timeSheet.getDetails().iterator();
				while(iters.hasNext()) {
					TimeSheetDetail detail = iters.next();
					TimeSheetDetailDto detailDto = new TimeSheetDetailDto();
					detailDto.setId(detail.getId());
					detailDto.setDuration(detail.getDuration());
					detailDto.setEndTime(detail.getEndTime());
					detailDto.setStartTime(detail.getStartTime());
					detailDto.setWorkingItemTitle(detail.getWorkingItemTitle());
					this.details.add(detailDto);
				}
			}
			
			if(timeSheet.getShiftWork()!=null ) {
				this.shiftWork=new ShiftWorkDto();
				this.shiftWork.setName(timeSheet.getShiftWork().getName());
				this.shiftWork.setId(timeSheet.getShiftWork().getId());
				this.shiftWork.setCode(timeSheet.getShiftWork().getCode());
				this.shiftWork.setTotalHours(timeSheet.getShiftWork().getTotalHours());
				if(timeSheet.getShiftWork().getTimePeriods()!=null) {
//					shiftWork.getTimePeriods() = new ArrayList<ShiftWorkTimePeriodDto>();
					this.shiftWork.setTimePeriods(new ArrayList<ShiftWorkTimePeriodDto>());
					for(ShiftWorkTimePeriod swt: timeSheet.getShiftWork().getTimePeriods()) {
						ShiftWorkTimePeriodDto stdDto = new ShiftWorkTimePeriodDto();
						stdDto.setId(swt.getId());
						stdDto.setStartTime(swt.getStartTime());
						stdDto.setEndTime(swt.getEndTime());
						this.shiftWork.getTimePeriods().add(stdDto);
					}
				}
			}
		}
	}

	
	
}
