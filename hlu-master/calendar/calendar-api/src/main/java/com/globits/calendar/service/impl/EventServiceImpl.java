package com.globits.calendar.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.calendar.Constants;
import com.globits.calendar.Constants.EventDuplicateEnum;
import com.globits.calendar.Constants.EventStatusEnum;
import com.globits.calendar.domain.Event;
import com.globits.calendar.domain.EventAttachment;
import com.globits.calendar.domain.EventAttendee;
import com.globits.calendar.domain.EventPriority;
import com.globits.calendar.dto.CalendarPermissionDto;
import com.globits.calendar.dto.EventAttachmentDto;
import com.globits.calendar.dto.EventAttendeeDto;
import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.EventQueryParamDto;
import com.globits.calendar.dto.SearchEventDto;
import com.globits.calendar.dto.ViewDateEventDto;
import com.globits.calendar.dto.WeekDto;
import com.globits.calendar.repository.EventAttachmentRepository;
import com.globits.calendar.repository.EventAttendeeRepository;
import com.globits.calendar.repository.EventPriorityRepository;
import com.globits.calendar.repository.EventRepository;
import com.globits.calendar.service.EventService;
import com.globits.core.Constants.ActionLogTypeEnum;
import com.globits.core.domain.Department;
import com.globits.core.domain.Person;
import com.globits.core.domain.Room;
import com.globits.core.dto.ActivityLogDto;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.repository.PersonRepository;
import com.globits.core.repository.RoomRepository;
import com.globits.core.service.ActivityLogService;
import com.globits.core.utils.CommonUtils;
import com.globits.hr.domain.Staff;
import com.globits.hr.repository.StaffRepository;
import com.globits.security.domain.Role;
import com.globits.security.domain.User;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	EntityManager manager;
	
	@Autowired
	private EventRepository repos;

	@Autowired
	private PersonRepository personRepos;

	@Autowired
	private StaffRepository staffRepos;

	@Autowired
	private DepartmentRepository departmentRepos;

	@Autowired
	private EventPriorityRepository eventPriorityRepos;

	@Autowired
	private RoomRepository roomRepos;

	@Autowired
	private EventAttendeeRepository eventAttendeeRepos;
	
	@Autowired
	private EventAttachmentRepository eventAttachmentRepos;

	@Autowired
	private ActivityLogService activityLogService;
	private List<WeekDto> generateWeekList(int year) {
		List<WeekDto> listAllWeek = new ArrayList<WeekDto>();
		Calendar cal = new GregorianCalendar(year, 1, 1);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		Date fromDate = cal.getTime(); // get back a Date object
		
		cal.add(Calendar.DATE, 6); 
		Date toDate = cal.getTime(); // get back a Date object
		
		for(int i=0;i<52;i++) {
			WeekDto dto = new WeekDto();
			dto.setFromDate(fromDate);
			toDate = cal.getTime();
			dto.setToDate(toDate);
			dto.setId(i);
			
			cal.add(Calendar.DATE, 1); 
			fromDate = cal.getTime();
			cal.add(Calendar.DATE, 6); 
			fromDate = cal.getTime();
		}
		return listAllWeek;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ViewDateEventDto> getListEventByWeek(DateTime startDate){
		List<ViewDateEventDto> ret = new ArrayList<ViewDateEventDto>();
		EventQueryParamDto params = new EventQueryParamDto();
		DateTime fromDate = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
		DateTime toDate = fromDate.plusDays(7);
		
		params.setFromDate(fromDate);
		params.setToDate(toDate);
		List<EventDto> list = getEvents(params);
		Hashtable<Integer,ViewDateEventDto> hashViewEvent = new Hashtable<Integer,ViewDateEventDto>();

		for(int i=1;i<8;i++) {
			ViewDateEventDto dto = new ViewDateEventDto();
			dto.setEvents(new ArrayList<EventDto>());
			dto.setId(i);
			if(i<7) {
				dto.setName("Thứ " + (i+1));
			}else {
				dto.setName("Chủ nhật");
			}
			DateTime date = fromDate.plusDays(i-1);
			dto.setDate(date);
			hashViewEvent.put(i, dto);
		}
		
		 String userName = "";
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime.now();
		 if (authentication != null) {
		 modifiedUser = (User) authentication.getPrincipal();
			 if (modifiedUser != null && modifiedUser.getUsername() != null) {
				 userName = modifiedUser.getUsername();
			 }
		 }
		
		for(int i=0;i<list.size();i++) {
			EventDto e = list.get(i);
			int dayOfWeek = e.getStartTime().dayOfWeek().get();
			if(dayOfWeek==0) {
				dayOfWeek=7;
			}
			ViewDateEventDto dto = hashViewEvent.get(dayOfWeek);
			
			if (userName != "" && e.getCreatedBy() != null) {
				String createBy = e.getCreatedBy();
				if (userName.equals(createBy)) {
					e.setIsPermissionEdit(true);
				}else {
					e.setIsPermissionEdit(false);
				}
			}else {
				e.setIsPermissionEdit(false);
			}
			
			dto.getEvents().add(e);
		}
		ret.addAll(hashViewEvent.values());
		Collections.sort(ret);
		return ret;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ViewDateEventDto> getListEventByWeekAndStatusPublish(DateTime startDate){
		List<ViewDateEventDto> ret = new ArrayList<ViewDateEventDto>();
		EventQueryParamDto params = new EventQueryParamDto();
		DateTime fromDate = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
		DateTime toDate = fromDate.plusDays(7);
		
		params.setFromDate(fromDate);
		params.setToDate(toDate);
		params.setStatus(EventStatusEnum.IsPublished.getValue());
		List<EventDto> list = getEvents(params);
		Hashtable<Integer,ViewDateEventDto> hashViewEvent = new Hashtable<Integer,ViewDateEventDto>();

		for(int i=1;i<8;i++) {
			ViewDateEventDto dto = new ViewDateEventDto();
			dto.setEvents(new ArrayList<EventDto>());
			dto.setId(i);
			if(i<7) {
				dto.setName("Thứ " + (i+1));
			}else {
				dto.setName("Chủ nhật");
			}
			DateTime date = fromDate.plusDays(i-1);
			dto.setDate(date);
			hashViewEvent.put(i, dto);
		}
		for(int i=0;i<list.size();i++) {
			EventDto e = list.get(i);
			int dayOfWeek = e.getStartTime().dayOfWeek().get();
			if(dayOfWeek==0) {
				dayOfWeek=7;
			}
			ViewDateEventDto dto = hashViewEvent.get(dayOfWeek);
			
			dto.getEvents().add(e);
		}
		ret.addAll(hashViewEvent.values());
		Collections.sort(ret);
		return ret;
	}
	@Override
	@Transactional(readOnly = true)
	public EventDto getOne(Long id) {

		if (id == null || id < 0) {
			return null;
		}

		Event entity = repos.findOne(id);

		if (entity == null) {
			return null;
		} else {
			return new EventDto(entity);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EventDto> getPage(Pageable pageable) {

		Iterator<Event> itr = repos.findAll(pageable).iterator();
		List<EventDto> content = new ArrayList<EventDto>();

		while (itr.hasNext()) {
			content.add(new EventDto(itr.next()));
		}

		return new PageImpl<EventDto>(content, pageable, repos.count());
	}
	//Tìm ra các phòng họp trùng với 1 sự kiện nào đó
	private List<Event> getListDuplicateRoomEvent(Event event){
		if (event != null && event.getRoom() != null && event.getRoom().getId() != null) {
			new ArrayList<Event>();
			String sql ="from Event e where e.otherLocation=false and e.room.id=:roomId and ((e.startTime>=:startTime and e.startTime<:endTime) or (e.startTime>:startTime and e.startTime<=:endTime)";
			sql += " or (e.endTime>:startTime and e.endTime<=:endTime) or (e.endTime>=:startTime and e.endTime<:endTime)";
			sql += " or (e.endTime<:startTime and e.endTime<=:endTime) or (e.endTime<=:startTime and e.endTime<:endTime)";
			sql +=")";
			if(event.getId()!=null && event.getId()>0) {
				sql +=" and e.id !=:id";
			}
			Query q = manager.createQuery(sql, Event.class);
			q.setParameter("roomId", event.getRoom().getId());
			q.setParameter("startTime", event.getStartTime());
			q.setParameter("endTime", event.getEndTime());
			if(event.getId()!=null && event.getId()>0) {
				q.setParameter("id", event.getId());
			}
			List<Event> list = q.getResultList();
			return list;
		}
		return null;
	}
	
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public EventDto saveOne(EventDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		ActivityLogDto log = new ActivityLogDto();
		String contentLog ="";
		log.setModuleLog(Constants.EventApplication);
		log.setEntityObjectType(Event.class.getName());

		if (dto == null) {
			throw new RuntimeException("Invalid EventDTO object passed in for persisting!");
		}

		Event entity = null;

		if (CommonUtils.isPositive(dto.getId(), true)) {
			entity = repos.findOne(dto.getId());
		}

		if (entity == null) {
			entity = dto.toEntity();
			entity.setCreateDate(currentDate);
			entity.setCreatedBy(currentUserName);
			log.setLogType(ActionLogTypeEnum.Create.getValue());
		} else {
			entity.setTitle(dto.getTitle());
			entity.setDescription(dto.getDescription());
			entity.setStartTime(dto.getStartTime());
			entity.setEndTime(dto.getEndTime());
			entity.setLocation(dto.getLocation());
			entity.setOtherLocation(dto.getOtherLocation());
			entity.setIsOtherChairPerson(dto.getIsOtherChairPerson());
			entity.setChairPersonName(dto.getChairPersonName());
			entity.setCarRegistered(dto.getCarRegistered());
			entity.setPersonsRequireTravelling(dto.getPersonsRequireTravelling());
			entity.setEventType(dto.getEventType());
			entity.setScope(dto.getScope());
			entity.setStatus(dto.getStatus());
			entity.setOtherAttendees(dto.getOtherAttendees());
			entity.setModifyDate(currentDate);
			entity.setModifiedBy(currentUserName);
			
			log.setLogType(ActionLogTypeEnum.Update.getValue());
		}

		Department ownerDepartment = null;
		Person ownerPerson = null;
		EventPriority priority = null;
		Department depCreator = null;
		Person creator = null;
		Room room = null;
		List<EventAttendee> attendees = new ArrayList<EventAttendee>();
		List<EventAttachment> attachments = new ArrayList<EventAttachment>();

		if (dto.getOwnerDepartment() != null && CommonUtils.isPositive(dto.getOwnerDepartment().getId(), true)) {
			ownerDepartment = departmentRepos.findById(dto.getOwnerDepartment().getId());
		}

		if (dto.getOwnerPerson() != null && CommonUtils.isPositive(dto.getOwnerPerson().getId(), true)) {
			ownerPerson = personRepos.findOne(dto.getOwnerPerson().getId());
		}

		if (dto.getPriority() != null && CommonUtils.isPositive(dto.getPriority().getId(), true)) {
			priority = eventPriorityRepos.findOne(dto.getPriority().getId());
		}

		if (dto.getDepCreator() != null && CommonUtils.isPositive(dto.getDepCreator().getId(), true)) {
			depCreator = departmentRepos.findById(dto.getDepCreator().getId());
		}

		if (dto.getCreator() != null && CommonUtils.isPositive(dto.getCreator().getId(), true)) {
			creator = personRepos.findOne(dto.getCreator().getId());
		}

		if (dto.getRoom() != null && CommonUtils.isPositive(dto.getRoom().getId(), true)) {
			room = roomRepos.findOne(dto.getRoom().getId());
		}
		
		List<EventAttachmentDto> attachmentDto = new ArrayList<>();
		if (dto.getAttachments() != null) {
			attachmentDto.addAll(dto.getAttachments());
		}

		List<EventAttendeeDto> attendeeDtos = new ArrayList<>();

		if (dto.getChairPerson() != null) {
			attendeeDtos.add(dto.getChairPerson());
		}

		if (dto.getsAttendees() != null && dto.getsAttendees().size() > 0) {
			attendeeDtos.addAll(dto.getsAttendees());
		}

		if (dto.getdAttendees() != null && dto.getdAttendees().size() > 0) {
			attendeeDtos.addAll(dto.getdAttendees());
		}
		
		if (attachmentDto.size() > 0) {
			for (EventAttachmentDto _a : attachmentDto) {

				EventAttachment a = null;
				if (CommonUtils.isPositive(_a.getId(), true)) {
					a = eventAttachmentRepos.findOne(_a.getId());
				}
				
				if (a == null) {
					a = _a.toEntity();
					a.setCreateDate(currentDate);
					a.setCreatedBy(currentUserName);
				} else {
					a.setModifyDate(currentDate);
					a.setModifiedBy(currentUserName);
				}
				a.setEvent(entity);
				attachments.add(a);
			}
		}
		if (attendeeDtos.size() > 0) {
			for (EventAttendeeDto _a : attendeeDtos) {

				EventAttendee a = null;
				if (CommonUtils.isPositive(_a.getId(), true)) {
					a = eventAttendeeRepos.findOne(_a.getId());
				}

				if (a == null) {
					a = _a.toEntity();
					a.setCreateDate(currentDate);
					a.setCreatedBy(currentUserName);
				} else {
					a.setModifyDate(currentDate);
					a.setModifiedBy(currentUserName);
					
					a.setAttendeeType(_a.getAttendeeType());
					a.setDisplayName(_a.getDisplayName());
					a.setEmail(_a.getEmail());
					a.setIsChairPerson(_a.getIsChairPerson());
					a.setIsOptional(_a.getIsOptional());
					a.setVisibility(_a.getVisibility());
					Department d = null;
					if (_a.getDepartment() != null && CommonUtils.isPositive(_a.getDepartment().getId(), true)) {
						d = departmentRepos.findById(_a.getDepartment().getId());
					}

					a.setDepartment(d);

					Staff s = null;
					if (_a.getStaff() != null && CommonUtils.isPositive(_a.getStaff().getId(), true)) {
						s = staffRepos.findById(_a.getStaff().getId());
					}

					a.setStaff(s);
				}

				a.setEvent(entity);

				attendees.add(a);
			}
		}
		if(modifiedUser!=null && modifiedUser.getPerson()!=null) {
			entity.setLastUpdatePersonName(modifiedUser.getPerson().getDisplayName());	
		}else {
			entity.setLastUpdatePersonName(currentUserName);
		}
		
		entity.setOwnerDepartment(ownerDepartment);
		entity.setOwnerPerson(ownerPerson);
		entity.setPriority(priority);
		entity.setDepCreator(depCreator);
		entity.setCreator(creator);
		entity.setRoom(room);

		entity.getAttendees().clear();
		if (attendees.size() > 0) {
			entity.getAttendees().addAll(attendees);
		}
		
		entity.getAttachments().clear();
		if (attachments.size() > 0) {
			entity.getAttachments().addAll(attachments);
		}
		//Kiểm trùng phòng họp 
		List<Event> listDuplicateRoom = getListDuplicateRoomEvent(entity);
		if(listDuplicateRoom!=null && listDuplicateRoom.size()>0) {
			entity.setDuplicate(EventDuplicateEnum.DuplicateRoom.getValue());
		}
		//contentLog = "title:"+entity.getTitle() +";content:" +entity.getDescription();
		entity = repos.save(entity);
		

		if (entity == null) {
			throw new RuntimeException("Error saving an event!");
		} else {
			//Ghi log trước khi thoát
			contentLog = entity.toString();
			log.setContentLog(contentLog);
			activityLogService.saveActivityLog(log);
			return new EventDto(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EventDto saveAttachments(EventDto dto) {

		if (dto == null) {
			throw new RuntimeException("Invalid EventDTO object passed in for persisting!");
		}

		Event entity = null;

		if (CommonUtils.isPositive(dto.getId(), true)) {
			entity = repos.findOne(dto.getId());
		}

		if (entity == null || CommonUtils.isEmpty(dto.getAttachments())) {
			throw new RuntimeException("Invalid EventDTO object passed in for persisting!");
		}

		List<EventAttachment> attachments = new ArrayList<>();

		for (EventAttachmentDto _a : dto.getAttachments()) {
			if (!CommonUtils.isPositive(_a.getId(), true)) {
				attachments.add(_a.toEntity());
			}
		}

		// entity.getAttachments().clear();
		entity.getAttachments().addAll(attachments);

		entity = repos.save(entity);

		if (entity != null) {
			return new EventDto(entity);
		} else {
			throw new RuntimeException("An error occurred while persisting an event!");
		}
	}
	
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public int approveEvents(List<EventDto> dtos) {
		if (CommonUtils.isEmpty(dtos)) {
			return 0;
		}
		int result = 0;
		for (EventDto d : dtos) {
			if (!CommonUtils.isPositive(d.getId(), true)) {
				throw new RuntimeException("Invalid event ID found!");
			}

			Event event = repos.findOne(d.getId());

			if (event != null) {
				if(event.getStatus()<EventStatusEnum.IsApproved.getValue()) {
					result++;
					event.setStatus(EventStatusEnum.IsApproved.getValue()); //Đặt trạng thái là phê duyệt lịch
					repos.save(event);
				}
			}
		}
		return result;
	}
	
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public int saveListEvents(List<EventDto> dtos) {
		if (CommonUtils.isEmpty(dtos)) {
			return 0;
		}
		int result = 0;
		for (EventDto d : dtos) {
			
			if (!CommonUtils.isPositive(d.getId(), true)) {
				throw new RuntimeException("Invalid event ID found!");
			}
			this.saveOne(d);
		}
		return result;
	}
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public int publishEvents(List<EventDto> dtos) {
		if (CommonUtils.isEmpty(dtos)) {
			return 0;
		}
		int result = 0;
		for (EventDto d : dtos) {
			if (!CommonUtils.isPositive(d.getId(), true)) {
				throw new RuntimeException("Invalid event ID found!");
			}

			Event event = repos.findOne(d.getId());

			if (event != null) {
				if(event.getStatus()<EventStatusEnum.IsPublished.getValue()) {
					result++;
					event.setStatus(EventStatusEnum.IsPublished.getValue()); //Đặt trạng thái là phê duyệt lịch
					repos.save(event);
				}
			}
		}
		return result;
	}
	@Override
	@Transactional(rollbackFor = { Exception.class })
	public int setEventStatus(List<EventDto> dtos, Integer status) {

		if (CommonUtils.isEmpty(dtos)) {
			return 0;
		}
		int retValue = 0;
		for (EventDto d : dtos) {
			if (!CommonUtils.isPositive(d.getId(), true)) {
				throw new RuntimeException("Invalid event ID found!");
			}
			Event event = repos.findOne(d.getId());
			if (event != null) {
				if(event.getStatus()<=status) {
					event.setStatus(status); //Đặt trạng thái là phê duyệt lịch
					repos.save(event);
					retValue++;
				}
			}
		}
		return retValue;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Boolean deleteMultiple(EventDto[] dtos) {

		if (CommonUtils.isEmpty(dtos)) {
			return false;
		}

		for (EventDto d : dtos) {
			if (!CommonUtils.isPositive(d.getId(), true)) {
				throw new RuntimeException("Invalid event ID found!");
			}

			Event event = repos.findOne(d.getId());

			if (event != null) {
				repos.delete(event);
			}
		}

		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDto> getEvents(EventQueryParamDto params) {
		if (params == null || params.getScope() < 0 || params.getType() < 0 || params.getFromDate() == null
				|| params.getToDate() == null) {
			return new ArrayList<EventDto>();
		}
		if (params.getFromDate().isAfter(params.getToDate().getMillis())) {
			params.setToDate(params.getFromDate()); // TODO double check the logic
		}
		String sql = "from Event e where e.startTime>=:startDate and  e.startTime<=:toDate";
		if(params.getVoided()!=null) {
			sql +=" and e.voided=:voided";
		}
		if(params.getStatus()!=null && params.getStatus()>0) {
			sql +=" and e.status=:status";
		}
		if(params.getListStatus()!=null && params.getListStatus().size() > 0) {
			sql +=" and e.status in (:listStatus)";
		}
		sql+= " order by e.startTime ASC";
		Query q= manager.createQuery(sql);
		q.setParameter("startDate", params.getFromDate());
		q.setParameter("toDate", params.getToDate());
		if(params.getVoided()!=null) {
			q.setParameter("voided", params.getVoided());
		}
		
		if(params.getStatus()!=null && params.getStatus()>0) {
			q.setParameter("status", params.getStatus());
		}
		if(params.getListStatus()!=null && params.getListStatus().size() > 0) {
			q.setParameter("listStatus", params.getListStatus());
		}	
		List<Event> list = q.getResultList();
		List<EventDto> ret = new ArrayList<EventDto>();
		if(list!=null  && list.size()>0) {
			for(Event e : list) {
				ret.add(new EventDto(e));
			}
		}
		return ret;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EventDto changeStatus(EventDto dto) {
		if (dto == null || !CommonUtils.isPositive(dto.getId(), true)) {
			return null;
		}

		Event event = repos.findOne(dto.getId());

		if (event == null) {
			return null;
		}

		if (event.getStatus().intValue() != dto.getStatus()) {
			event.setStatus(dto.getStatus());

			event = repos.save(event);

			if (event != null) {
				return new EventDto(event);
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EventDto changeTime(EventDto dto) {
		if (dto == null || !CommonUtils.isPositive(dto.getId(), true)) {
			return null;
		}

		Event event = repos.findOne(dto.getId());

		if (event == null) {
			return null;
		}

		if (!event.getStartTime().isEqual(dto.getStartTime()) || !event.getEndTime().isEqual(dto.getEndTime())) {
			event.setStartTime(dto.getStartTime());
			event.setEndTime(dto.getEndTime());

			event = repos.save(event);

			if (event != null) {
				return new EventDto(event);
			} else {
				return null;
			}
		}

		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public EventDto changeLocation(EventDto dto) {
		if (dto == null || !CommonUtils.isPositive(dto.getId(), true)) {
			return null;
		}

		Event event = repos.findOne(dto.getId());

		if (event == null) {
			return null;
		}

		boolean otherLocation = false;
		String location = null;
		Room room = null;

		if (dto.getOtherLocation()) {
			if (!CommonUtils.isEmpty(dto.getLocation())) {
				otherLocation = dto.getOtherLocation();
				location = dto.getLocation();
			}
		} else if (dto.getRoom() != null && CommonUtils.isPositive(dto.getRoom().getId(), true)) {
			room = roomRepos.findOne(dto.getRoom().getId());
		}

		if ((otherLocation && !CommonUtils.isEmpty(location)) || room != null) {
			event.setOtherLocation(dto.getOtherLocation());
			event.setLocation(dto.getLocation());
			event.setRoom(room);

			event = repos.save(event);

			if (event != null) {
				return new EventDto(event);
			} else {
				return null;
			}
		}

		return null;
	}
	@Override
	public CalendarPermissionDto getPermission() {
		 CalendarPermissionDto ret = new CalendarPermissionDto();
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime.now();
		 if (authentication != null) {
		 modifiedUser = (User) authentication.getPrincipal();
			 if (modifiedUser!=null && modifiedUser.getRoles()!=null) {
				 for(Role role:modifiedUser.getRoles()) {
					 if(role.getName()!=null && role.getName().equals(Constants.CALENDAR_EDITOR_ROLE)) {
						 ret.setHasCalendarEditorPermission(true);
					 }
					 if(role.getName()!=null && role.getName().equals(Constants.CALENDAR_APPROVER_ROLE)) {
						 ret.setHasCalendarApproverPermission(true);
						 ret.setHasCalendarEditorPermission(true);
					 }
					 
					 if(role.getName()!=null && role.getName().equals(Constants.CALENDAR_PUBLISHER_ROLE)) {
						 ret.setHasCalendarPublisherPermission(true);
						 ret.setHasCalendarApproverPermission(true);
						 ret.setHasCalendarEditorPermission(true);
					 }
					 if(role.getName()!=null && role.getName().equals(Constants.ROLE_CALENDAR_MANAGEMENT)) {
						 ret.setHasCalendarPublisherPermission(true);
						 ret.setHasCalendarApproverPermission(true);
						 ret.setHasCalendarEditorPermission(true);
					 }
					 if(role.getName()!=null && role.getName().equals(com.globits.core.Constants.ROLE_ADMIN)) {
						 ret.setHasCalendarPublisherPermission(true);
						 ret.setHasCalendarApproverPermission(true);
						 ret.setHasCalendarEditorPermission(true);
						 ret.setHasCalendarAdminPermission(true);
					 }
				 }
			 }
		 }
		 return ret;
	}
	/**
	 * For sorting in listing events
	 * 
	 * @return
	 */
//	private OrderSpecifier<DateTime> orderByStartTimeAsc() {
//		return QEvent.event.startTime.asc();
//	}
	public static Date getEndOfDay(Date date) {
		if(date!=null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 999);
		    return calendar.getTime();
		}
	    return null;
	}
	
	public static Date getStartOfDay(Date date) {
		if(date!=null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.set(Calendar.HOUR_OF_DAY, 00);
		    calendar.set(Calendar.MINUTE, 00);
		    calendar.set(Calendar.SECOND, 00);
		    calendar.set(Calendar.MILLISECOND, 000);
		    return calendar.getTime();
		}
	    return null;
	}
	@Override
	public List<EventDto> getEventBySearchDto(SearchEventDto searchEventDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = null;
		if (authentication != null) {
			currentUser = (User) authentication.getPrincipal();
		}
		if(currentUser != null) {
			String sql = " select new com.globits.calendar.dto.EventDto(e) from Event e where (1=1) and e.status IN (2) ";//lấy tất cả lịch đã xuất bản
			String whereClause ="";
			if(searchEventDto != null) {
				if (StringUtils.hasText(searchEventDto.getTextSearch())) {
					whereClause += " and (e.title like :text or e.description like :text or e.chairPersonName like :text)";
				}
				if(searchEventDto.getEventType()!=null) {//lọc theo loại sự kiện
					whereClause += " and e.eventType = :eventType";
				}
				if(searchEventDto.getStartTime()!=null) {
					whereClause += " and e.startTime >= :startTime";
				}
				if(searchEventDto.getEndTime()!=null) {
					whereClause += " and e.endTime <= :endTime";
				}
				if(searchEventDto.getStartTime() != null && searchEventDto.getEndTime() != null) {
					whereClause += " and e.startTime >=:startTime and e.endTime <=:endTime";
				}
			}
			
			sql +=whereClause;
			sql +=" order by e.startTime asc ";		
	
			Query q = manager.createQuery(sql, EventDto.class);	
			
			if(searchEventDto != null) {
				if (StringUtils.hasText(searchEventDto.getTextSearch())) {
					String converSearch = "%" + searchEventDto.getTextSearch() + "%";
					q.setParameter("text", converSearch);
				}
				if(searchEventDto.getEventType()!=null) {			
					q.setParameter("eventType", searchEventDto.getEventType());			
				}
				if(searchEventDto.getStartTime()!=null) {
					q.setParameter("startTime", searchEventDto.getStartTime());			
				}
				if(searchEventDto.getEndTime()!=null) {
					q.setParameter("endTime", searchEventDto.getEndTime());			
				}
				
			}
			List list = q.getResultList();
			return list;
		}else {
			return null;
		}
	}
}
