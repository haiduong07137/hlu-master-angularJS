package com.globits.calendar.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.Department;
import com.globits.core.domain.Person;
import com.globits.core.domain.Room;

@Entity
@Table(name = "tbl_calendar_event")
public class Event extends BaseObject {

	@Transient
	private static final long serialVersionUID = -9073001725970695295L;

	@Column(name = "last_name", nullable = true)
	protected String lastName;
	
	@Column(name = "title", nullable = false)
	private String title;

	
	@Column(name = "description", nullable = true,columnDefinition = "TEXT")
	private String description;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name = "origin_start_time")
	private DateTime originalStartTime;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name = "origin_end_time", nullable = true)
	private DateTime originalEndTime;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name = "start_time")
	private DateTime startTime;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	@Column(name = "end_time", nullable = true)
	private DateTime endTime;

	@Column(name = "location", nullable = true)
	private String location;// Vị trí nói chung (mang tính chung chung ví dụ : Hà nội, Hưng Yên, cho người
							// dùng gõ text vào). Sử dụng khi trường usingOtherLocation=true

	@Column(name = "other_location", nullable = false)
	private Boolean otherLocation = false;// Bằng true khi sử dụng địa điểm khác (lúc đó trường location sẽ được lấy
											// trực tiếp ra).

	@Column(name = "is_other_chair_person", nullable = false)
	private Boolean isOtherChairPerson = false;// Bằng true khi người chủ trì là người ngoài đơn vị (hiếm khi sử dụng)

	@Column(name = "chair_person_name", nullable = true)
	private String chairPersonName;// Tên người chủ trì, nếu trường isOtherChairPerson là true thì trường này sẽ
									// nhận dữ liệu trực tiếp vào, nếu không thì sẽ lấy tên của Staff hoặc Dep trong
									// Attendee tương ứng điền vào

	@Column(name = "car_registered", nullable = false)
	private Boolean carRegistered = false;// Bằng true khi có đặt xe

	@Column(name = "persons_require_travelling", nullable = true)
	private Integer personsRequireTravelling = 0;// Số lượng người đi xe

	@Column(name = "content_by", nullable = true)
	private String contentBy; // Don vi chuan bi noi dung

	@Column(name = "event_type", nullable = false)
	private Integer eventType = 0;// Loại sự kiện (0 = lịch làm việc, 1 = thông báo).

	@Column(name = "scope", nullable = false)
	private Integer scope = 0;// Lịch toàn bộ cơ quan = 0, lịch đơn vị, phòng ban = 1, lịch cá nhân = 2 (cái
								// này có thể thay bằng bảng hoặc Enum)

	private Integer duplicate;//1 = duplicate room, 2 = duplicate chairman, 3 = duplicate room and chairman 
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "owner_department_id", nullable = true)
	private Department ownerDepartment;// Nếu là lịch phòng ban (scope=1) thì trường này sẽ dùng để xác định lịch là
										// của phòng ban nào (giúp query dữ liệu).

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "owner_person_id", nullable = true)
	private Person ownerPerson;// Nếu là lịch cá nhân (scope=2) thì trường này sẽ dùng để xác định lịch là của
								// cá nhân nào (giúp query dữ liệu).

	@Column(name = "event_status", nullable = false)
	private Integer status = 0;// Trạng thái của lịch (0 = mới đăng ký, 1 = đã được phê duyệt, 2 = đã xuất bản,
								// 3 = da ket thuc, -1 = da huy, 4=huỷ trước khi xuất bản
								// - có thể thay thế bằng visibily).

	@Column(name = "other_attendees", nullable = true,columnDefinition = "TEXT")
	private String otherAttendees;// Những người tham gia khác (cho gõ vào)
	
	@Column(name = "last_update_person_name", nullable = true)
	private String lastUpdatePersonName;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "priority_id", nullable = true)
	private EventPriority priority;// Mức ưu tiên của lịch

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "dep_creator_id", nullable = true)
	private Department depCreator;// Đơn vị đề xuất lịch

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "person_creator_id", nullable = true)
	private Person creator;// Người đề xuất lịch (nếu không xác định là đơn vị nào, đề xuất với tư cách cá
							// nhân).

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "room_id", nullable = true)
	private Room room;

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EventAttendee> attendees = new HashSet<EventAttendee>();//Danh sách người tham gia

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EventAttachment> attachments = new HashSet<EventAttachment>();

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

	public Boolean getOtherLocation() {
		return otherLocation;
	}

	public void setOtherLocation(Boolean otherLocation) {
		this.otherLocation = otherLocation;
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

	public Integer getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(Integer duplicate) {
		this.duplicate = duplicate;
	}

	public String getOtherAttendees() {
		return otherAttendees;
	}

	public void setOtherAttendees(String otherAttendees) {
		this.otherAttendees = otherAttendees;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastUpdatePersonName() {
		return lastUpdatePersonName;
	}

	public void setLastUpdatePersonName(String lastUpdatePersonName) {
		this.lastUpdatePersonName = lastUpdatePersonName;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public EventPriority getPriority() {
		return priority;
	}

	public void setPriority(EventPriority priority) {
		this.priority = priority;
	}

	public Department getDepCreator() {
		return depCreator;
	}

	public void setDepCreator(Department depCreator) {
		this.depCreator = depCreator;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public String getContentBy() {
		return contentBy;
	}

	public void setContentBy(String contentBy) {
		this.contentBy = contentBy;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Set<EventAttendee> getAttendees() {
		return attendees;
	}

	public void setAttendees(Set<EventAttendee> attendees) {
		this.attendees = attendees;
	}

	public Set<EventAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<EventAttachment> attachments) {
		this.attachments = attachments;
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

	public Department getOwnerDepartment() {
		return ownerDepartment;
	}

	public void setOwnerDepartment(Department ownerDepartment) {
		this.ownerDepartment = ownerDepartment;
	}

	public Person getOwnerPerson() {
		return ownerPerson;
	}

	public void setOwnerPerson(Person ownerPerson) {
		this.ownerPerson = ownerPerson;
	}
	
	@Override
	public String toString() {
	    return "{"+getId()+","+getTitle()+","+getDescription()+"}";
	}
}
