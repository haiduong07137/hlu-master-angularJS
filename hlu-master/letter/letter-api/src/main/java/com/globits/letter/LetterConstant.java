package com.globits.letter;

import com.globits.taskman.domain.TaskFlow;

public class LetterConstant {
	public static final String LetterInDocumentType = "InDocument";
	public static final String LetterOutDocumentType = "OutDocument";
	public static final String WORKPLAN_ADMIN = "WORKPLAN_ADMIN";
	public static final String WORKPLAN_USER = "WORKPLAN_USER";
	public static final String ROLE_LETTER_ADMIN = "ROLE_LETTER_ADMIN";
	public static final String ROLE_LETTER_USER = "ROLE_LETTER_USER";
	public static String LetterInDocumentFlowCode = "LetterInCode";
	public static String LetterOutDocumentFlowCode = "LETTEROUTSTEP";
	public static String WorkPlanFlowCode = "WorkPlanFlowCode";
	public static String ClerkRole = "ClerkRole";
	public static String FowardRole = "FowardRole";
	public static String AssignerRole = "AssignerRole";
	public static String ChairmanRole = "ChairmanRole";
	public static String ProcessRole = "ProcessRole";
	public static String ManagerRole = "ManagerRole";
	public static String DraftersRole = "DraftersRole";
	public static String ChiefOfStaffRole = "ChiefOfStaffRole";
	public static TaskFlow LetterInDocumentFlow = new TaskFlow();
	public static TaskFlow LetterOutDocumentFlow = new TaskFlow();

	public static String HardCodeTaskNameOfLetterInDocument = "Văn bản đến số hiệu: ";
	public static String HardCodeTaskNameOfLetterOutDocument = "Văn bản đi số hiệu: ";

	public static TaskFlow WorkPlanFlow = new TaskFlow();
	public static TaskFlow ProjectFlow = new TaskFlow();
	public static String WorkPlanFolderPath = null;
	public static String InDocumentFolderPath = null;
	public static String OutDocumentFolderPath = null;
	public static Integer NumberOriginal = 1;

	public static TaskFlow DailyWorksFlow = new TaskFlow();
	public static String DailyWorksPath = null;;

	public static String FolderPath;

	public static Long LetterInStep0 = 0L; // văn bản đến mới vào sổ
	public static Long LetterInStep1 = 1L; // văn bản đến mới vào sổ
	public static Long LetterInStep2 = 2L; //
	public static Long LetterInStep3 = 3L; // văn bản đến chờ giao xử lý
	public static Long LetterInStep4 = 4L; // văn bản đến đang xử lý
	public static Long LetterInStep5 = 5L; // văn bản đến đã hoàn thành

	public static Long LetterOutStep1 = 17L; // dự thảo chờ lãnh đạo phòng duyệt
	public static Long LetterOutStep2 = 18L; // dự thảo đang xử lý
	public static Long LetterOutStep3 = 19L; // dự thảo đã phê duyệt
	public static Long LetterOutStep4 = 14L; // văn bản đã vào sổ

	public static Integer ParticipantTypeMainProcess = 3; // Loại tham gia cá nhân, xử lý chính
	// Trạng thái của lịch (0 = mới đăng ký, 1 = đã được phê duyệt, 2 = đã xuất bản,
	// 3 = da ket thuc, 4 = da huy
	// - có thể thay thế bằng visibily).

	public static enum EventStatusEnum {
		NewRegister(0),
		IsApproved(1), // Sinh viên hủy
		IsPublished(2), // Cố tình đăng ký trái phép
		IsFinished(3), // Đã kết thúc
		IsCancelledBeforPublished(4), // Đã kết thúc
		IsCancelled(-1);// Đã hủy

		private int value;

		private EventStatusEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
