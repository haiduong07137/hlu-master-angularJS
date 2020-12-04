package com.globits.calendar.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.globits.core.domain.Department;
import com.globits.hr.domain.Staff;

@Entity
@Table(name = "tbl_calendar_event_attendee")
public class EventAttendee implements Serializable {

	@Transient
	private static final long serialVersionUID = -1322660829568528760L;

	@Id
	@GenericGenerator(name = "native", strategy = "native")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "is_chair_person")
	private Boolean isChairPerson;// Có thể chuyển sang liên kết với 1 bảng khác ví dụ EventRole???

	@Column(name = "optional")
	private Boolean isOptional;

	@Column(name = "email")
	private String email;

	@Column(name = "attendee_type")
	private Integer attendeeType = 0;// 0=Cá nhân, 1= tập thể, 2 = khác (có thể đơn vị/cá nhân mời nằm ngoài danh
										// sách của cơ quan).
	/*
	 * Cho phép hiển thị tại lịch của đơn vị (0= không hiển thị, 1= hiển thị), cá
	 * nhân liên quan Ví dụ : Khoa CNTT tham gia sự kiện sẽ cho hiển thị tại lịch
	 * khoa
	 */
	@Column(name = "visibility")
	private Integer visibility;

	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;// Tham gia cho sự kiện nào.

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "staff_id", nullable = true)
	private Staff staff;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "department_id", nullable = true)
	private Department department;

	@Column(name = "create_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	// @DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime createDate;

	@Column(name = "created_by", length = 100, nullable = false)
	private String createdBy;

	@Column(name = "modify_date", nullable = true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	// @DateTimeFormat(iso = ISO.DATE_TIME)
	private LocalDateTime modifyDate;

	@Column(name = "modified_by", length = 100, nullable = true)
	private String modifiedBy;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getIsChairPerson() {
		return isChairPerson;
	}

	public void setIsChairPerson(Boolean isChairPerson) {
		this.isChairPerson = isChairPerson;
	}

	public Boolean getIsOptional() {
		return isOptional;
	}

	public void setIsOptional(Boolean isOptional) {
		this.isOptional = isOptional;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAttendeeType() {
		return attendeeType;
	}

	public void setAttendeeType(Integer attendeeType) {
		this.attendeeType = attendeeType;
	}

	public Integer getVisibility() {
		return visibility;
	}

	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(LocalDateTime modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
