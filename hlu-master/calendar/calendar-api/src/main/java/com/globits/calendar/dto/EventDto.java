package com.globits.calendar.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.globits.calendar.domain.Event;
import com.globits.calendar.domain.EventAttachment;
import com.globits.calendar.domain.EventAttendee;
import com.globits.core.dto.AuditableEntityDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.PersonDto;
import com.globits.core.dto.RoomDto;
import com.globits.core.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
public class EventDto extends AuditableEntityDto implements Serializable {

	private static final long serialVersionUID = 4344046558272888186L;

	private Long id;

	private String title;

	private String description;
	
	private DateTime originalStartTime;
	private DateTime originalEndTime;
	private DateTime startTime;
	private DateTime endTime;

	private String location;

	private Boolean otherLocation = false;

	private Boolean isOtherChairPerson = false;

	private String chairPersonName;

	private Boolean carRegistered = false;

	private Integer personsRequireTravelling = 0;

	private String contentBy;

	private Integer eventType = 0;

	private Integer scope = 0;

	private DepartmentDto ownerDepartment;

	private PersonDto ownerPerson;

	private Integer status = 0;

	private String otherAttendees;

	private String lastUpdatePersonName;
	
	private EventPriorityDto priority;

	private DepartmentDto depCreator;

	private PersonDto creator;

	private RoomDto room;

	private Boolean isDuplicate;
	
	private Boolean isPermissionEdit;
	
	private Integer duplicateType;//Sẽ cho vào 1 Enum. 0 = không trùng, 1= trùng phòng họp, 2= trùng người chủ trì, 3 = trùng cả thời gian và người chủ trì
	
	// Added in for ease of view manipulation...
	private EventAttendeeDto chairPerson;

	private Set<EventAttendeeDto> sAttendees = new HashSet<EventAttendeeDto>(); // Staff attendees

	private Set<EventAttendeeDto> dAttendees = new HashSet<EventAttendeeDto>(); // Department attendees

	private Set<EventAttachmentDto> attachments = new HashSet<EventAttachmentDto>();

	
	public EventDto() {

	}

	public EventDto(Event entity) {
		if (entity == null) {
			return;
		}

		id = entity.getId();
		title = entity.getTitle();
		description = entity.getDescription();
		originalStartTime = entity.getOriginalStartTime();
		originalEndTime = entity.getOriginalEndTime();
		startTime = entity.getStartTime();
		endTime = entity.getEndTime();
		location = entity.getLocation();
		otherLocation = entity.getOtherLocation();
		isOtherChairPerson = entity.getIsOtherChairPerson();
		chairPersonName = entity.getChairPersonName();
		carRegistered = entity.getCarRegistered();
		personsRequireTravelling = entity.getPersonsRequireTravelling();
		contentBy = entity.getContentBy();
		eventType = entity.getEventType();
		scope = entity.getScope();
		status = entity.getStatus();
		otherAttendees = entity.getOtherAttendees();
		duplicateType = entity.getDuplicate();
		
		this.lastUpdatePersonName = entity.getLastUpdatePersonName();
		if (entity.getOwnerDepartment() != null) {
			ownerDepartment = new DepartmentDto(entity.getOwnerDepartment());
		}

		if (entity.getOwnerPerson() != null) {
			ownerPerson = new PersonDto(entity.getOwnerPerson());
		}

		if (entity.getPriority() != null) {
			priority = new EventPriorityDto(entity.getPriority());
		}

		if (entity.getDepCreator() != null) {
			depCreator = new DepartmentDto(entity.getDepCreator());
		}

		if (entity.getCreator() != null) {
			creator = new PersonDto(entity.getCreator());
		}

		if (entity.getRoom() != null) {
			room = new RoomDto(entity.getRoom());
		}

		if (entity.getAttendees() != null) {
			List<EventAttendeeDto> sAtts = new ArrayList<>();
			List<EventAttendeeDto> dAtts = new ArrayList<>();

			for (EventAttendee _a : entity.getAttendees()) {
				if (_a!=null &&_a.getIsChairPerson()!=null && _a.getIsChairPerson()) {
					chairPerson = new EventAttendeeDto(_a);
				} else {
					if (_a.getDepartment() != null && CommonUtils.isPositive(_a.getDepartment().getId(), true)) {
						dAtts.add(new EventAttendeeDto(_a));
					} else {
						sAtts.add(new EventAttendeeDto(_a));
					}

				}
			}

			sAttendees.addAll(sAtts);
			dAttendees.addAll(dAtts);
		}

		if (entity.getAttachments() != null) {
			List<EventAttachmentDto> atts = new ArrayList<>();
			for (EventAttachment _a : entity.getAttachments()) {
				attachments.add(new EventAttachmentDto(_a, false));
			}

			attachments.addAll(atts);
		}

		setCreateDate(entity.getCreateDate());
		setCreatedBy(entity.getCreatedBy());
		setModifyDate(entity.getModifyDate());
		setModifiedBy(entity.getModifiedBy());
	}

	public Event toEntity() {

		Event entity = new Event();

		entity.setId(id);
		entity.setTitle(title);
		entity.setDescription(description);
		entity.setOriginalStartTime(originalStartTime);
		entity.setOriginalEndTime(originalEndTime);
		entity.setStartTime(startTime);
		entity.setEndTime(endTime);
		entity.setLocation(location);
		entity.setOtherLocation(otherLocation);
		entity.setIsOtherChairPerson(isOtherChairPerson);
		entity.setChairPersonName(chairPersonName);
		entity.setCarRegistered(carRegistered);
		entity.setPersonsRequireTravelling(personsRequireTravelling);
		entity.setContentBy(contentBy);
		entity.setEventType(eventType);
		entity.setScope(scope);
		entity.setStatus(status);
		entity.setOtherAttendees(otherAttendees);
		entity.setLastUpdatePersonName(lastUpdatePersonName);
		entity.setDuplicate(duplicateType);
		if (ownerDepartment != null) {
			entity.setOwnerDepartment(ownerDepartment.toEntity());
		}

		if (ownerPerson != null) {
			entity.setOwnerPerson(ownerPerson.toEntity());
		}

		if (priority != null) {
			entity.setPriority(priority.toEntity());
		}

		if (depCreator != null) {
			entity.setDepCreator(depCreator.toEntity());
		}

		if (creator != null) {
			entity.setCreator(creator.toEntity());
		}

		if (room != null) {
			entity.setRoom(room.toEntity());
		}

		if (sAttendees != null || dAttendees != null || chairPerson != null) {
			List<EventAttendee> atts = new ArrayList<>();

			if (chairPerson != null) {
				atts.add(chairPerson.toEntity());
			}

			for (EventAttendeeDto _a : sAttendees) {
				atts.add(_a.toEntity());
			}

			for (EventAttendeeDto _a : dAttendees) {
				atts.add(_a.toEntity());
			}

			entity.getAttendees().addAll(atts);
		}

		if (attachments != null) {
			List<EventAttachment> atts = new ArrayList<>();

			for (EventAttachmentDto _a : attachments) {
				atts.add(_a.toEntity());
			}

			entity.getAttachments().addAll(atts);
		}
		
		entity.setCreateDate(getCreateDate());
		entity.setCreatedBy(getCreatedBy());

		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getOriginalStartTime() {
		return originalStartTime;
	}

	public void setOriginalStartTime(DateTime originalStartTime) {
		this.originalStartTime = originalStartTime;
	}

	public DateTime getOriginalEndTime() {
		return originalEndTime;
	}

	public void setOriginalEndTime(DateTime originalEndTime) {
		this.originalEndTime = originalEndTime;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getOtherLocation() {
		return otherLocation;
	}

	public void setOtherLocation(Boolean otherLocation) {
		this.otherLocation = otherLocation;
	}

	public Boolean getIsOtherChairPerson() {
		return isOtherChairPerson;
	}

	public void setIsOtherChairPerson(Boolean isOtherChairPerson) {
		this.isOtherChairPerson = isOtherChairPerson;
	}

	public String getChairPersonName() {
		return chairPersonName;
	}

	public void setChairPersonName(String chairPersonName) {
		this.chairPersonName = chairPersonName;
	}

	public String getLastUpdatePersonName() {
		return lastUpdatePersonName;
	}

	public void setLastUpdatePersonName(String lastUpdatePersonName) {
		this.lastUpdatePersonName = lastUpdatePersonName;
	}

	public Boolean getCarRegistered() {
		return carRegistered;
	}

	public void setCarRegistered(Boolean carRegistered) {
		this.carRegistered = carRegistered;
	}

	public Integer getPersonsRequireTravelling() {
		return personsRequireTravelling;
	}

	public void setPersonsRequireTravelling(Integer personsRequireTravelling) {
		this.personsRequireTravelling = personsRequireTravelling;
	}

	public String getContentBy() {
		return contentBy;
	}

	public void setContentBy(String contentBy) {
		this.contentBy = contentBy;
	}

	public EventAttendeeDto getChairPerson() {
		return chairPerson;
	}

	public void setChairPerson(EventAttendeeDto chairPerson) {
		this.chairPerson = chairPerson;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public DepartmentDto getOwnerDepartment() {
		return ownerDepartment;
	}

	public void setOwnerDepartment(DepartmentDto ownerDepartment) {
		this.ownerDepartment = ownerDepartment;
	}

	public PersonDto getOwnerPerson() {
		return ownerPerson;
	}

	public void setOwnerPerson(PersonDto ownerPerson) {
		this.ownerPerson = ownerPerson;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOtherAttendees() {
		return otherAttendees;
	}

	public void setOtherAttendees(String otherAttendees) {
		this.otherAttendees = otherAttendees;
	}

	public EventPriorityDto getPriority() {
		return priority;
	}

	public void setPriority(EventPriorityDto priority) {
		this.priority = priority;
	}

	public DepartmentDto getDepCreator() {
		return depCreator;
	}

	public void setDepCreator(DepartmentDto depCreator) {
		this.depCreator = depCreator;
	}

	public PersonDto getCreator() {
		return creator;
	}

	public void setCreator(PersonDto creator) {
		this.creator = creator;
	}

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public Set<EventAttendeeDto> getsAttendees() {
		return sAttendees;
	}

	public void setsAttendees(Set<EventAttendeeDto> sAttendees) {
		this.sAttendees = sAttendees;
	}

	public Set<EventAttendeeDto> getdAttendees() {
		return dAttendees;
	}

	public void setdAttendees(Set<EventAttendeeDto> dAttendees) {
		this.dAttendees = dAttendees;
	}

	public Set<EventAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<EventAttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public Boolean getIsDuplicate() {
		return isDuplicate;
	}

	public void setIsDuplicate(Boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public Integer getDuplicateType() {
		return duplicateType;
	}

	public void setDuplicateType(Integer duplicateType) {
		this.duplicateType = duplicateType;
	}

	public Boolean getIsPermissionEdit() {
		return isPermissionEdit;
	}

	public void setIsPermissionEdit(Boolean isPermissionEdit) {
		this.isPermissionEdit = isPermissionEdit;
	}
	
	

}
