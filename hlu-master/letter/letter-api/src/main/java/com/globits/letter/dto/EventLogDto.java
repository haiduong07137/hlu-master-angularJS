package com.globits.letter.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.globits.calendar.dto.EventDto;
import com.globits.core.dto.AuditableEntityDto;
import com.globits.core.dto.RoomDto;
import com.globits.letter.domain.EventLog;

public class EventLogDto extends AuditableEntityDto implements Serializable {

	private static final long serialVersionUID = 4344046558272888186L;

	private Long id;
	private Long eventId;
	private DateTime startTime;
	private DateTime endTime;
	private RoomDto room;
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public RoomDto getRoom() {
		return room;
	}

	public void setRoom(RoomDto room) {
		this.room = room;
	}

	public EventLogDto() {

	}

	public EventLogDto(EventLog entity) {
		if (entity != null) {
			id = entity.getId();
			startTime = entity.getStartTime();
			endTime = entity.getEndTime();
			status = entity.getStatus();
			eventId = entity.getEventId();
			if (entity.getRoom() != null) {
				room = new RoomDto(entity.getRoom());
			}
		}
		return;
	}
}
