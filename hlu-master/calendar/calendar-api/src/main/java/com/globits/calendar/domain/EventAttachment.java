package com.globits.calendar.domain;

import java.io.Serializable;

import javax.jdo.annotations.Join;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.FileDescription;

@Entity
@Table(name = "tbl_calendar_event_attachment")
public class EventAttachment extends BaseObject{

	@Transient
	private static final long serialVersionUID = -8875313686216064164L;

	@ManyToOne
	@JoinColumn(name="file_id")
	private FileDescription file;
	
	@ManyToOne
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public FileDescription getFile() {
		return file;
	}

	public void setFile(FileDescription file) {
		this.file = file;
	}
}
