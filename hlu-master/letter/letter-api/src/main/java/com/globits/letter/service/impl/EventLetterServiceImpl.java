package com.globits.letter.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.globits.calendar.Constants;
import com.globits.calendar.domain.Event;
import com.globits.calendar.domain.EventAttachment;
import com.globits.calendar.domain.EventAttendee;
import com.globits.calendar.domain.EventPriority;
import com.globits.calendar.dto.EventAttachmentDto;
import com.globits.calendar.dto.EventAttendeeDto;
import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.EventQueryParamDto;
import com.globits.calendar.dto.ViewDateEventDto;
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
import com.globits.letter.LetterConstant;
import com.globits.letter.domain.EventLog;
import com.globits.letter.dto.EventLogDto;
import com.globits.letter.dto.ResultDataEventDto;
import com.globits.letter.repository.EventLogRepository;
import com.globits.letter.service.EventLetterService;
import com.globits.security.domain.User;

@Service
public class EventLetterServiceImpl implements EventLetterService {

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
	private EventService eventService;
	
	@Autowired
	private EventLogRepository eventLogRepository;
	
	@Autowired
	private ActivityLogService activityLogService;

	@Override
	public ResultDataEventDto saveOne(EventDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		ActivityLogDto log = new ActivityLogDto();
		String contentLog = "";
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
		EventAttendee chairPerson = null;
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
		if (modifiedUser != null && modifiedUser.getPerson() != null) {
			entity.setLastUpdatePersonName(modifiedUser.getPerson().getDisplayName());
		} else {
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
		// Kiểm trùng
		List<Event> listDuplicateRoom = getListDuplicateRoomEvent(entity);
		if (listDuplicateRoom != null && listDuplicateRoom.size() > 0) {
			return new ResultDataEventDto(1, "Phòng trong thời gian sự kiện diễn ra đã tổ chức một sự kiện khác");
		}

		List<Event> listDuplicateChairPerson = getListDuplicateChairPersonEvent(dto);
		if (listDuplicateChairPerson != null && listDuplicateChairPerson.size() > 0) {
			return new ResultDataEventDto(2,
					"Người chủ trì trong thời gian sự kiện diễn ra đã chủ trì một sự kiện khác");
		}
		// contentLog = "title:"+entity.getTitle() +";content:"
		// +entity.getDescription();
		entity = repos.save(entity);

		if (entity == null) {
			throw new RuntimeException("Error saving an event!");
		} else {
//			 Ghi log trước khi thoát
//			contentLog = entity.toString();
//			log.setContentLog(contentLog);
//			activityLogService.saveActivityLog(log);
			saveEventLog(entity);
			return new ResultDataEventDto(3, "Thao tác thành công");
		}
	}

	// Tìm ra các phòng họp trùng với 1 sự kiện nào đó
	private List<Event> getListDuplicateRoomEvent(Event event) {
		if (event != null && event.getRoom() != null && event.getRoom().getId() != null) {
			ArrayList<Event> result = new ArrayList<Event>();
			String sql = "from Event e where e.otherLocation=false and e.room.id=:roomId and ((e.startTime<=:startTime and e.endTime>:startTime) or (e.startTime<:endTime and e.endTime>:endTime)"
					+ " or ((:startTime < e.startTime) and (e.startTime < :endTime))) and e.status not in (:listStatus)";
			if (event.getId() != null && event.getId() > 0) {
				sql += " and e.id !=:id";
			}
			Query q = manager.createQuery(sql, Event.class);
			q.setParameter("roomId", event.getRoom().getId());
			q.setParameter("startTime", event.getStartTime());
			q.setParameter("endTime", event.getEndTime());
			List<Integer> listStatus = new ArrayList<Integer>();
			listStatus.add(LetterConstant.EventStatusEnum.IsCancelled.getValue());
			listStatus.add(LetterConstant.EventStatusEnum.IsCancelledBeforPublished.getValue());
			q.setParameter("listStatus", listStatus);
			if (event.getId() != null && event.getId() > 0) {
				q.setParameter("id", event.getId());
			}
			List<Event> list = q.getResultList();
			return list;
		}
		return null;
	}

	// Tìm ra các lịch bị trùng người chủ trì
	private List<Event> getListDuplicateChairPersonEvent(EventDto dto) {
		if (dto != null && dto.getChairPerson() != null && dto.getChairPerson().getStaff() != null
				&& dto.getChairPerson().getStaff().getId() != null) {
			ArrayList<Event> result = new ArrayList<Event>();
			String sqlChairPerson = "from EventAttendee ea where 1=1";
			sqlChairPerson += " and ea.isChairPerson =:isChairPerson";
			sqlChairPerson += " and ea.staff.id =:staffId";
			sqlChairPerson += " and ea.event.id in (select e.id from Event e where ((e.startTime<=:startTime and e.endTime>:startTime) or (e.startTime<:endTime and e.endTime>:endTime) or ((:startTime < e.startTime) and (e.startTime < :endTime))) and e.status not in (:listStatus) ";
			if (dto.getId() != null && dto.getId() > 0) {
				sqlChairPerson += " and e.id !=:id";
			}
			sqlChairPerson += " )";

			Query qChairPerson = manager.createQuery(sqlChairPerson);
			qChairPerson.setParameter("isChairPerson", true);
			qChairPerson.setParameter("staffId", dto.getChairPerson().getStaff().getId());
			qChairPerson.setParameter("startTime", dto.getStartTime());
			qChairPerson.setParameter("endTime", dto.getEndTime());
			List<Integer> listStatus = new ArrayList<Integer>();
			listStatus.add(LetterConstant.EventStatusEnum.IsCancelled.getValue());
			listStatus.add(LetterConstant.EventStatusEnum.IsCancelledBeforPublished.getValue());
			qChairPerson.setParameter("listStatus", listStatus);
			if (dto.getId() != null && dto.getId() > 0) {
				qChairPerson.setParameter("id", dto.getId());
			}
			List<Event> list = qChairPerson.getResultList();
			return list;
		}
		return null;
	}

	@Override
	public ResultDataEventDto cancelEvent(Long eventId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (eventId != null) {
				EventDto dto = eventService.getOne(eventId);
				if (dto.getStatus() == 2) {
					dto.setStatus(LetterConstant.EventStatusEnum.IsCancelledBeforPublished.getValue());
					return saveOne(dto);
				} else {
					dto.setStatus(LetterConstant.EventStatusEnum.IsCancelled.getValue());
					return saveOne(dto);
				}
			}
		}
		return null;
	}

	@Override
	public List<ViewDateEventDto> getEventPublic(DateTime startDate) {
		List<ViewDateEventDto> ret = new ArrayList<ViewDateEventDto>();
		EventQueryParamDto params = new EventQueryParamDto();
		DateTime fromDate = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
		DateTime toDate = fromDate.plusDays(7);
		
		List<Integer> listStatus = new ArrayList<Integer>();
		listStatus.add(LetterConstant.EventStatusEnum.IsPublished.getValue());
		listStatus.add(LetterConstant.EventStatusEnum.IsCancelledBeforPublished.getValue());
		
		params.setFromDate(fromDate);
		params.setToDate(toDate);
		params.setListStatus(listStatus);
		List<EventDto> listEvent = eventService.getEvents(params);

		List<EventDto> list = new ArrayList<EventDto>();
		list.addAll(listEvent);

		Hashtable<Integer, ViewDateEventDto> hashViewEvent = new Hashtable<Integer, ViewDateEventDto>();

		for (int i = 1; i < 8; i++) {
			ViewDateEventDto dto = new ViewDateEventDto();
			dto.setEvents(new ArrayList<EventDto>());
			dto.setId(i);
			if (i < 7) {
				dto.setName("Thứ " + (i + 1));
			} else {
				dto.setName("Chủ nhật");
			}
			DateTime date = fromDate.plusDays(i - 1);
			dto.setDate(date);
			hashViewEvent.put(i, dto);
		}
		for (int i = 0; i < list.size(); i++) {
			EventDto e = list.get(i);
			int dayOfWeek = e.getStartTime().dayOfWeek().get();
			if (dayOfWeek == 0) {
				dayOfWeek = 7;
			}
			ViewDateEventDto dto = hashViewEvent.get(dayOfWeek);

			dto.getEvents().add(e);
		}
		ret.addAll(hashViewEvent.values());
		Collections.sort(ret);
		return ret;
	}

	@Override
	public ResultDataEventDto restoreEvent(Long eventId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (eventId != null) {
				EventDto dto = eventService.getOne(eventId);
				
				String sql = "Select el.status from EventLog el where el.eventId = :eventId order by el.id desc";
				Query q = manager.createQuery(sql);
				
				q.setParameter("eventId", eventId);
				List<Integer> list = q.getResultList();
				
				if(list!=null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						if(dto.getStatus() != list.get(i)) {
							dto.setStatus(list.get(i));
							break;
						}
					}
				}
				return saveOne(dto);
			}
		}
		return null;
	}

	public boolean saveEventLog(Event data) {
		if (data != null) {
			EventLog entity = new EventLog();
			
			entity.setStartTime(data.getStartTime());
			entity.setEndTime(data.getEndTime());
			entity.setCreateDate(data.getModifyDate());
			entity.setCreatedBy(data.getModifiedBy());
			entity.setStatus(data.getStatus());
			entity.setEventId(data.getId());
			Room room = null;
			if (data.getRoom() != null && CommonUtils.isPositive(data.getRoom().getId(), true)) {
				room = roomRepos.findOne(data.getRoom().getId());
			}
			entity.setRoom(room);

			entity = eventLogRepository.save(entity);
			return true;
		}
		return false;
	}

	@Override
	public Integer saveList(List<EventDto> dtos) {
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
	public List<EventLogDto> getEventLog() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			String sql = "Select new com.globits.letter.dto.EventLogDto(el) from EventLog el where 1=1";
			
			Query q = manager.createQuery(sql);
			List<EventLogDto> list = q.getResultList();
			
			return list;
		}
		return null;
	}

}
