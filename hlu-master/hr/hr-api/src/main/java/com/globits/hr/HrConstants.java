package com.globits.hr;

public class HrConstants {
	public static final String ROLE_HR_MANAGEMENT="ROLE_HR_MANAGEMENT"; 
	public static final String ROLE_STUDENT_MANAGERMENT ="ROLE_STUDENT_MANAGERMENT";
	public static final String ROLE_EDUCATION_MANAGERMENT ="ROLE_EDUCATION_MANAGERMENT";
	public static final String ROLE_ADMIN ="ROLE_ADMIN";
	public static final String ROLE_STUDENT ="ROLE_STUDENT";
	public static final String ROLE_CANDIDATE ="ROLE_CANDIDATE";
	public static final String ROLE_EXAM_MANAGERMENT="ROLE_EXAM_MANAGERMENT";
	public static final String ROLE_FINANCIAL_MANAGERMENT="ROLE_FINANCIAL_MANAGERMENT";
	
	public static enum WorkingStatusEnum{
		Working(1),//Đang làm việc
		Retired(2),//Đã nghỉ hưu
		ContractTerminated(3),//Chấm dứt hợp đồng, thôi việc;
		TransferOut(4),//Đã chuyển đi
		LeaveWithoutPay(5)//Nghỉ không lương
		;
		
		private int value;    
		
		private WorkingStatusEnum(int value) {
		    this.value = value;
		}
	
		public int getValue() {
			return value;
		}
	}
}
