package com.globits.hr.timesheet.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.globits.core.domain.Person;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.SearchReportDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffShiftWorkDto;
import com.globits.hr.dto.SynthesisReportOfStaffDto;
import com.globits.hr.repository.ShiftWorkRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.repository.WorkingStatusRepository;
import com.globits.hr.timesheet.domain.TimeSheet;
import com.globits.hr.timesheet.domain.TimeSheetDetail;
import com.globits.hr.timesheet.dto.SearchTimeSheetDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.repository.TimeSheetDetailRepository;
import com.globits.hr.timesheet.repository.TimeSheetRepository;
import com.globits.hr.timesheet.service.TimeSheetService;
import com.globits.security.domain.User;


@Transactional
@Service
public class TimeSheetServiceImpl extends GenericServiceImpl<TimeSheet, Long> implements TimeSheetService{
	
	@Autowired
	EntityManager manager;
	
	@Autowired
	TimeSheetRepository timeSheetRepository;
	
	@Autowired
	ShiftWorkRepository shiftworkRepository;
	@Autowired
	WorkingStatusRepository workingStatusRepository;
	@Autowired
	TimeSheetDetailRepository timeDetailSheetRepository;
	@Autowired
	WorkingStatusRepository workingstatusRepository;
	@Autowired
	StaffRepository staffRepository;
	public TimeSheet setEntityValue(TimeSheetDto dto, TimeSheet entity, User modifiedUser) {
		 String currentUserName ="Unknow User"; 
		 LocalDateTime currentDate = LocalDateTime.now();
		 if(modifiedUser!=null) {
			 currentUserName = modifiedUser.getUsername();
		 }
				 
		 
		if(entity==null) {
			entity=new TimeSheet();
		}
		entity.setEndTime(dto.getEndTime());
		entity.setStartTime(dto.getStartTime());
		entity.setTotalHours(dto.getTotalHours());
		entity.setWorkingDate(dto.getWorkingDate());
		if(dto.getApproveStatus()==null) {
			dto.setApproveStatus(0);
		}
		entity.setApproveStatus(dto.getApproveStatus());
		if(dto.getWorkingStatus()!=null) {
			WorkingStatus workingStatus = workingStatusRepository.findOne(dto.getWorkingStatus().getId());
			entity.setWorkingStatus(workingStatus);	
		}
		
		if(dto.getEmployee()!=null) {
			Staff staff = staffRepository.findOne(dto.getEmployee().getId());
			entity.setEmployee(staff);
		}else {//nếu không sẽ thêm cho chính nhân viên đăng nhập
			if(modifiedUser!=null) {
				Person person  = modifiedUser.getPerson();
				if(person!=null) {
					Staff staff = staffRepository.findOne(person.getId());
					entity.setEmployee(staff);
				}
			}
		}
		if(dto.getShiftWork()!=null) {
			ShiftWork shiftwork = shiftworkRepository.getOne(dto.getShiftWork().getId());
			entity.setShiftWork(shiftwork);
		}
		Set<TimeSheetDetail> details =new HashSet<TimeSheetDetail>(); 
		
		if(dto.getDetails()!=null && dto.getDetails().size()>0) {
			
			for(TimeSheetDetailDto detailDto:dto.getDetails()) {
				TimeSheetDetail detail =null;
				if(detailDto!=null && detailDto.getId()!=null) {
					detail = timeDetailSheetRepository.findOne(detailDto.getId());
				}
				if(detail==null) {
					detail=new TimeSheetDetail();
					detail.setTimeSheet(entity);
					detail.setCreateDate(currentDate);
					detail.setCreatedBy(currentUserName);
				}
				detail = detailDto.toEntity(detailDto, detail);
				details.add(detail);
			}
		}	
		if(entity.getDetails()!=null) {
			entity.getDetails().clear();
			entity.getDetails().addAll(details);
		}else {
			entity.setDetails(details);
		}
		return entity;
	}
	@Override
	public Page<TimeSheetDto> getPage(int pageSize, int pageIndex){
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return timeSheetRepository.getListPage(pageable);
	}
	
	@Override
	public TimeSheetDto saveTimeSheet(TimeSheetDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String ip = request.getRemoteAddr();
		TimeSheet timeSheet =null;
		if(dto.getId()!=null) {
			timeSheet = timeSheetRepository.findOne(dto.getId());
		}
		if(timeSheet==null) {
			timeSheet = new TimeSheet();
			timeSheet.setCreateDate(currentDate);
			timeSheet.setCreatedBy(currentUserName);
		}
		timeSheet = setEntityValue(dto,timeSheet, modifiedUser);
//		double totalHours = DateTimeUtil.getDateDiff( timeSheet.getStartTime(),timeSheet.getEndTime(), TimeUnit.MINUTES)/60;
//		timeSheet.setTotalHours(totalHours);
		timeSheet = repository.save(timeSheet);
		return new TimeSheetDto(timeSheet);
	}
	
	public TimeSheetDto findTimeSheetByDate(Date date) {
		TimeSheet timeSheet = timeSheetRepository.findByDate(date);
		return new TimeSheetDto(timeSheet);
	}
	@Override
	public TimeSheetDto findTimeSheetById(Long id) {
		return timeSheetRepository.findTimeSheetById(id);
	}
	@Override
	public Boolean deleteTimeSheetById(Long id) {
		try {
			timeSheetRepository.delete(id);
			return true;
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;		
	}	
	
	@Override
	public Boolean deleteTimeSheets(List<TimeSheetDto> list) {
		try {
		ArrayList<TimeSheet> entities = new ArrayList<TimeSheet>();
		if(list!=null) {
			for(int i=0;i<list.size();i++) {
				if(list.get(i)!=null && list.get(i).getId()!=null) {
					TimeSheet ts = timeSheetRepository.getOne(list.get(i).getId());
					entities.add(ts);		
				}
					
			}
		}
		timeSheetRepository.delete(entities);
		return true;
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}	
	
	@Override
	public Page<StaffDto> findPageByName(String textSearch, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		String keyword ='%' + textSearch +'%';
		String sql = "from Staff s where s.displayName like :keyword";
		String sqlCount = "select count (s.id) from Staff s where s.displayName like :keyword";
		Query q = manager.createQuery(sql);
		Query qCount = manager.createQuery(sqlCount);
		q.setParameter("keyword", keyword);
		qCount.setParameter("keyword", keyword);
		List<StaffDto> content = new ArrayList<StaffDto>();
		int startPosition = (pageIndex-1)*pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<Staff> list = q.getResultList();
		
		for(int i=0;i<list.size();i++) {
			StaffDto dto = new StaffDto(list.get(i));
			content.add(dto);
		}
		long total = (long)qCount.getSingleResult();
		Page<StaffDto> page = new PageImpl<StaffDto>(content, pageable, total);
		
		return page;
	}
	@Override
	public Page<TimeSheetDto>  getAllByWorkingDate(Date workDate,int pageIndex, int pageSize) {
		String sqlCount ="select count(sw) FROM TimeSheet as sw WHERE sw.workingDate=:workDate";
		String sql ="select new com.globits.hr.timesheet.dto.TimeSheetDto(sw) FROM TimeSheet as sw WHERE sw.workingDate=:workDate";
		
		Query q = manager.createQuery(sql,TimeSheetDto.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setParameter("workDate", workDate);
		qCount.setParameter("workDate", workDate);
		int startPosition = (pageIndex-1)* pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<TimeSheetDto> entities = q.getResultList();		
		long count = (long)qCount.getSingleResult();
		Pageable pageable = new PageRequest(pageIndex-1,pageSize);
		Page<TimeSheetDto> result = new PageImpl<TimeSheetDto>(entities, pageable,count);
		
		return result;
			}
	@Override
	public Page<TimeSheetDetailDto> getTimeSheetDetailByTimeSheetID(Long id, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return timeSheetRepository.findTimeSheetDetailByTimeSheetId(id,pageable);
	}
	@Override
	public Boolean confirmTimeSheets(List<TimeSheetDto> list) {
		ArrayList<TimeSheet> entities = new ArrayList<TimeSheet>();
		try {
			for(int i=0; i<list.size();i++) {
				if(list.get(i)!=null && list.get(i).getId()!=null) {
					TimeSheet entity= timeSheetRepository.getOne(list.get(i).getId());
					entity.setApproveStatus(1);
					entities.add(entity);
				}
			}
			timeSheetRepository.save(entities);
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
	}
	@Override
	public Page<TimeSheetDto> findPageByStaff(String textSearch, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return timeSheetRepository.findPageByCodeOrName(textSearch, pageable);
	}
	@Override
	public Boolean createTimeSheets(StaffShiftWorkDto staffShiftworkDto) {
		Date mindate= new Date();
		Date maxdate= new Date();
		ArrayList<TimeSheet> entities = new ArrayList<TimeSheet>();
		try {
			Calendar start = Calendar.getInstance();
			start.setTime(staffShiftworkDto.getFromDate());
			Calendar end = Calendar.getInstance();
			end.setTime(staffShiftworkDto.getToDate());
			
//			 String dayOfWeek = new SimpleDateFormat("EEEE").format(start.getTime());
//	         // in ra output
//	         System.out.println(dayOfWeek);
	         
			if(staffShiftworkDto.getShiftWork()!=null && staffShiftworkDto.getShiftWork().getId()!=null) {
				
				mindate=staffShiftworkDto.getShiftWork().getTimePeriods().get(0).getStartTime();
				maxdate=staffShiftworkDto.getShiftWork().getTimePeriods().get(0).getEndTime();
				
				for(int i=0;i<staffShiftworkDto.getShiftWork().getTimePeriods().size();i++) {
					if(staffShiftworkDto.getShiftWork().getTimePeriods().get(i).getStartTime().before(mindate)) {
						 mindate=staffShiftworkDto.getShiftWork().getTimePeriods().get(i).getStartTime();
					}
					if(staffShiftworkDto.getShiftWork().getTimePeriods().get(i).getStartTime().after(maxdate)) {
						maxdate=staffShiftworkDto.getShiftWork().getTimePeriods().get(i).getStartTime();
					}
				}
			}	
			if(staffShiftworkDto.getWorkingOnSunday()==1) {
				for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
					for(int i=0; i<staffShiftworkDto.getStaffs().size();i++) {
						
						TimeSheet timesheet = new TimeSheet();
						
						if(staffShiftworkDto.getStaffs().get(i)!=null && staffShiftworkDto.getStaffs().get(i).getId()!=null) { 
							Staff staff= staffRepository.getOne(staffShiftworkDto.getStaffs().get(i).getId());
							timesheet.setEmployee(staff);
						}
						if(staffShiftworkDto.getShiftWork()!=null && staffShiftworkDto.getShiftWork().getId()!=null) {
							ShiftWork shiftwork = shiftworkRepository.getOne(staffShiftworkDto.getShiftWork().getId());
							timesheet.setShiftWork(shiftwork);
							timesheet.setTotalHours(shiftwork.getTotalHours());
						}
						if(staffShiftworkDto.getWorkingStatus()!=null && staffShiftworkDto.getWorkingStatus().getId()!=null) {
							WorkingStatus workingStatus = workingstatusRepository.getOne(staffShiftworkDto.getWorkingStatus().getId());
							timesheet.setWorkingStatus(workingStatus);
						}
						
							timesheet.setWorkingDate(date);
							timesheet.setStartTime(mindate);   //set giờ vào starttime
							timesheet.setEndTime(maxdate);
							//timesheet.setWorkingStatus(0); // chưa làm việc
							entities.add(timesheet);
						
					}
				}
			}
			else {
				for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
//					 String dayOfWeek = new SimpleDateFormat("EEEE").format(date);
//					 System.out.println(dayOfWeek);
					 Calendar dayOfWeek = Calendar.getInstance();
					 dayOfWeek.setTime(date);
				        System.out.println(dayOfWeek.get(Calendar.DAY_OF_WEEK));
					 if(dayOfWeek.get(Calendar.DAY_OF_WEEK) != 1) {
						 for(int i=0; i<staffShiftworkDto.getStaffs().size();i++) {
								
								TimeSheet timesheet = new TimeSheet();
								
								if(staffShiftworkDto.getStaffs().get(i)!=null && staffShiftworkDto.getStaffs().get(i).getId()!=null) { 
									Staff staff= staffRepository.getOne(staffShiftworkDto.getStaffs().get(i).getId());
									timesheet.setEmployee(staff);
								}
								if(staffShiftworkDto.getShiftWork()!=null && staffShiftworkDto.getShiftWork().getId()!=null) {
									ShiftWork shiftwork = shiftworkRepository.getOne(staffShiftworkDto.getShiftWork().getId());
									timesheet.setShiftWork(shiftwork);
									timesheet.setTotalHours(shiftwork.getTotalHours());
								}
								if(staffShiftworkDto.getWorkingStatus()!=null && staffShiftworkDto.getWorkingStatus().getId()!=null) {
									WorkingStatus workingStatus = workingstatusRepository.getOne(staffShiftworkDto.getWorkingStatus().getId());
									timesheet.setWorkingStatus(workingStatus);
								}
								
									timesheet.setWorkingDate(date);
									timesheet.setStartTime(mindate);   //set giờ vào starttime
									timesheet.setEndTime(maxdate);
									//timesheet.setWorkingStatus(0); // chưa làm việc
									entities.add(timesheet);
								
							}
					 }
					 else {
						 
					 }
					
				}
			}
			
			timeSheetRepository.save(entities);
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	@Override
	public Page<TimeSheetDto> searchByDto(SearchTimeSheetDto searchTimeSheetDto, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		if (searchTimeSheetDto != null ) {
			if (searchTimeSheetDto.getCodeAndName() != null) {
				if (searchTimeSheetDto.getWorkingDate() != null) {
					return timeSheetRepository.findPageByCodeAndNameAndDate(searchTimeSheetDto.getCodeAndName(), searchTimeSheetDto.getWorkingDate(), pageable);
				}else {
					return timeSheetRepository.findPageByCodeOrName(searchTimeSheetDto.getCodeAndName(), pageable);
				}
			}
			else if (searchTimeSheetDto.getWorkingDate() != null) {
				return timeSheetRepository.findPageByDate(searchTimeSheetDto.getWorkingDate(), pageable);
			}
			else {
				return timeSheetRepository.getListPage(pageable);
			}
		}
		
		return null;
	}
	@Override
	public Page<SynthesisReportOfStaffDto> reportWorkingStatus(SearchReportDto searchReportDto, int pageIndex,
			int pageSize) {
		
		return null;
	}
	
}
