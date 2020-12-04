package com.globits.letter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Room;

@Entity
@Table(name = "tbl_event_log")
public class EventLog extends BaseObject {

	@Transient
	private static final long serialVersionUID = -9073001725970695295L;

	@Column(name = "event_id")
	private Long eventId;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name = "start_time")
	private DateTime startTime;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name = "end_time", nullable = true)
	private DateTime endTime;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "room_id", nullable = true)
	private Room room;
	
	@Column(name = "event_status", nullable = false)
	private Integer status;		// Trạng thái của lịch (0 = mới đăng ký, 1 = đã được phê duyệt, 2 = đã xuất bản,
								// 3 = da ket thuc, -1 = da huy
								// - có thể thay thế bằng visibily).


	public Integer getStatus() {
		return status;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
}
