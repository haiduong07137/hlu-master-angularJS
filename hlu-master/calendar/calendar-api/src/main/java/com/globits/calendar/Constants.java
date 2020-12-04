package com.globits.calendar;

public final class Constants {
	public static final String EventApplication = "Event"; 
	public static final String CALENDAR_EDITOR_ROLE ="CALENDAR_EDITOR_ROLE";//Quyền đăng ký lịch
	public static final String CALENDAR_APPROVER_ROLE ="CALENDAR_APPROVER_ROLE";//Quyền phê duyệt lịch
	public static final String CALENDAR_PUBLISHER_ROLE ="CALENDAR_PUBLISHER_ROLE";//Quyền xuất bản lịch
	public static final String ROLE_CALENDAR_MANAGEMENT ="ROLE_CALENDAR_MANAGEMENT";//Quyền Quản trị lịch
	
	// Trạng thái của lịch (0 = mới đăng ký, 1 = đã được phê duyệt, 2 = đã xuất bản,
	// 3 = da ket thuc, 4 = da huy
	// - có thể thay thế bằng visibily).
	
	public static enum EventStatusEnum{
		NewRegister(0),
		IsApproved(1),//Sinh viên hủy
		IsPublished(2),//Cố tình đăng ký trái phép
		IsFinished(3),//Đã kết thúc
		IsCancelled(-1);//Đã hủy
		
		private int value;    
		
		private EventStatusEnum(int value) {
		    this.value = value;
		}
	
		public int getValue() {
			return value;
		}
	}
	
	public static enum EventScopeEnum{
		OrganizationEvent(0),//Lịch cơ quan
		DepartmentEvent(1),//Lịch phòng ban
		PersonalEvent(2);//Lịch cá nhân
		
		private int value;    
		
		private EventScopeEnum(int value) {
		    this.value = value;
		}
	
		public int getValue() {
			return value;
		}
	}
	
	public static enum EventDuplicateEnum {
		DuplicateRoom(1),
		DuplicateChairman(2),
		DuplicateRoomChairman(3);
		
		private int value;    
		
		private EventDuplicateEnum(int value) {
		    this.value = value;
		}
	
		public int getValue() {
			return value;
		}
	}
}
