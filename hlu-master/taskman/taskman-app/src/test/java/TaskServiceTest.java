import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.hr.dto.StaffDto;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.TaskManConstant.ParticipateStateEnum;
import com.globits.taskman.TaskManConstant.ParticipateTypeEnum;
import com.globits.taskman.config.DatabaseConfig;
import com.globits.taskman.dto.CommentFileAttachmentDto;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFileAttachmentDto;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.TaskOwnerDto;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.service.TaskService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class TaskServiceTest {
	@Autowired
	TaskService service;
	//@Test
	public void listTest() {
		List<Long> roleIds = new ArrayList<Long>();
		ArrayList<Long> departmentIds = new ArrayList<Long>();
		Long flowId = 1L;
		int pageSize = 2;
		int pageIndex =1;
		//roleIds.add(1L);
		roleIds.add(2L);
		departmentIds.add(103L);
		//Page<TaskDto> page =  service.getListPersonalTask(4235L,roleIds,1L,2,1);
		//Page<TaskDto> page =  service.getListDepartmentTask(departmentIds, roleIds, flowId,0, pageSize, pageIndex);
		//Long departmentId=103L;//P10
		Long departmentId=64L;//P2
		Long staffId = 4274L;
		Long currentStepId = 1L;
		//Page<TaskDto> page =  service.getAllDepartmentAndPersonalTask(staffId, flowId,currentStepId, null, pageSize, pageIndex);
		Page<TaskDto> page =  service.getListTask(pageSize, pageIndex);
		if(page!=null)
			System.out.println(page.getTotalElements());
		else {
			System.out.println("NULL");
		}
	}
	//@Test
	public void deleteTaskTest() {
		//service.deleteTask(38L);
	}
	//@Test
	public void updateTaskTest() {
		TaskDto task = service.getTask(48L);
		task.setCurrentStep(new TaskStepDto());
		task.getCurrentStep().setId(5L);
		
		service.saveTask(task);
	}
	//@Test
	public void createTaskTest() {
		TaskDto task = new TaskDto();//Tạo mới 1 công việc
		
		//Người giao việc - Hiệu trưởng - Trịnh Minh Thụ
		ParticipateDto assigner = new ParticipateDto();
		assigner.setParticipateType(ParticipateTypeEnum.OtherType.getValue());
		//assigner.setId(3621L);//ID của thầy Thụ
		assigner.setTaskOwner(new TaskOwnerDto());
		assigner.getTaskOwner().setId(2L);
		//assigner.getEmployee().setId(3621L);//ID của thầy Thụ
		//assigner.getEmployee().setId(4235L);//ID của thầy Kim
		
		TaskRoleDto assignerRole = new TaskRoleDto();
		assignerRole.setCode("001");//Giao xử lý văn bản7
		assignerRole.setId(1L);//Giao xử lý văn bản
		assigner.setRole(assignerRole);
		assigner.setComments(new HashSet<TaskCommentDto>());
		TaskCommentDto assignerComment = new  TaskCommentDto();
		assignerComment.setComment("Giao P10, P2 và P3 phối hợp xử lý văn bản này");
		assignerComment.setAttachments(new HashSet<CommentFileAttachmentDto>());
		CommentFileAttachmentDto commentAttach = new CommentFileAttachmentDto();
		commentAttach.setFile(new FileDescriptionDto());
		commentAttach.getFile().setId(4L);
		assignerComment.getAttachments().add(commentAttach);
		assigner.getComments().add(assignerComment);
		assigner.setCurrentState(TaskManConstant.ParticipateStateEnum.CommentedType.getValue());
		assigner.setParticipateType(ParticipateTypeEnum.OtherType.getValue());
		task.setParticipates(new HashSet<ParticipateDto>());
		task.getParticipates().add(assigner);//Thêm người giao việc vào danh sách tham gia
		
		
		ParticipateDto chairman = new ParticipateDto();
		chairman.setParticipateType(ParticipateTypeEnum.OtherType.getValue());//Là đơn vị tham gia xử lý
		chairman.setTaskOwner(new TaskOwnerDto());
		chairman.getTaskOwner().setId(3L);//Phòng tổ chức cán bộ
		TaskRoleDto chairmanRole = new TaskRoleDto();
		chairmanRole.setCode("002");//Chủ trì xử lý văn bản
		chairmanRole.setId(2L);//Chủ trì xử lý văn bản
		chairman.setRole(chairmanRole);
		
		task.getParticipates().add(chairman);//Thêm người chủ trì vào danh sách tham gia
		
		
//		ParticipateDto participate1 = new ParticipateDto();
//		participate1.setParticipateType(ParticipateTypeEnum.DepartmentType.getValue());//Là đơn vị tham gia xử lý
//		participate1.setDepartment(new DepartmentDto());
//		participate1.getDepartment().setId(103L);//Phòng hành chính tổng hợp
//		TaskRoleDto participateRole = new TaskRoleDto();
//		participateRole.setCode("003");//Tham gia xử lý văn bản
//		participateRole.setId(3L);//Tham gia xử lý văn bản
//		participate1.setRole(participateRole);
//		
//		task.getParticipates().add(participate1);//Thêm người tham gia vào danh sách tham gia
		
		
		ParticipateDto participate2 = new ParticipateDto();
		participate2.setParticipateType(ParticipateTypeEnum.OtherType.getValue());//Là người tham gia xử lý
		participate2.setTaskOwner(new TaskOwnerDto());
		participate2.getTaskOwner().setId(5L);
		
		TaskRoleDto participateRole2 = new TaskRoleDto();
		participateRole2.setCode("003");//Tham gia xử lý văn bản
		participateRole2.setId(1L);//Chủ trì
		participate2.setRole(participateRole2);
		
		task.getParticipates().add(participate2);//Thêm người tham gia vào danh sách tham gia - thêm chính thầy Kim vào với vai trò tham gia xử lý
		
		
		task.setAttachments(new HashSet<TaskFileAttachmentDto>());
		
		TaskFileAttachmentDto attachment = new TaskFileAttachmentDto();
		attachment.setFile(new FileDescriptionDto());
		attachment.getFile().setId(3L);
		task.getAttachments().add(attachment);
		
		task.setFlow(new TaskFlowDto());
		task.getFlow().setId(1L);
		
		task.setCurrentStep(new TaskStepDto());
		task.getCurrentStep().setId(1L);
		service.saveTask(task);
	}
	@Test
	public void createTaskTaskOwnerTest() {
		TaskDto task = new TaskDto();//Tạo mới 1 công việc
		
		//Người giao việc - Hiệu trưởng - Trịnh Minh Thụ
		ParticipateDto assigner = new ParticipateDto();
		assigner.setParticipateType(ParticipateTypeEnum.OtherType.getValue());//Trường hợp khác
		//assigner.setId(3621L);//ID của thầy Thụ
		
		//assigner.getEmployee().setId(3621L);//ID của thầy Thụ
		assigner.setTaskOwner(new TaskOwnerDto());
		assigner.getTaskOwner().setId(2L);
		
		TaskRoleDto assignerRole = new TaskRoleDto();
		assignerRole.setCode("001");//Giao xử lý văn bản
		assignerRole.setId(1L);//Giao xử lý văn bản
		assigner.setRole(assignerRole);
		assigner.setComments(new HashSet<TaskCommentDto>());
		TaskCommentDto assignerComment = new  TaskCommentDto();
		assignerComment.setComment("Giao P10, P2 và P3 phối hợp xử lý văn bản này");
		assignerComment.setAttachments(new HashSet<CommentFileAttachmentDto>());
		CommentFileAttachmentDto commentAttach = new CommentFileAttachmentDto();
		commentAttach.setFile(new FileDescriptionDto());
		commentAttach.getFile().setId(4L);
		assignerComment.getAttachments().add(commentAttach);
		assigner.getComments().add(assignerComment);
		assigner.setCurrentState(ParticipateStateEnum.CommentedType.getValue());
		task.setParticipates(new HashSet<ParticipateDto>());
		task.getParticipates().add(assigner);//Thêm người giao việc vào danh sách tham gia
		
		
		ParticipateDto chairman = new ParticipateDto();
		chairman.setParticipateType(ParticipateTypeEnum.OtherType.getValue());//Là đơn vị tham gia xử lý
		chairman.setTaskOwner(new TaskOwnerDto());
		chairman.getTaskOwner().setId(3L);//Phòng tổ chức cán bộ
		TaskRoleDto chairmanRole = new TaskRoleDto();
		chairmanRole.setCode("002");//Chủ trì xử lý văn bản
		chairmanRole.setId(2L);//Chủ trì xử lý văn bản
		chairman.setRole(chairmanRole);
		
		task.getParticipates().add(chairman);//Thêm người chủ trì vào danh sách tham gia
		
		
		ParticipateDto participate1 = new ParticipateDto();
		participate1.setParticipateType(ParticipateTypeEnum.OtherType.getValue());//Là đơn vị tham gia xử lý
		participate1.setTaskOwner(new TaskOwnerDto());
		participate1.getTaskOwner().setId(6L);//Khoa cơ khí
		TaskRoleDto participateRole = new TaskRoleDto();
		participateRole.setCode("003");//Tham gia xử lý văn bản
		participateRole.setId(3L);//Tham gia xử lý văn bản
		participate1.setRole(participateRole);
		
		task.getParticipates().add(participate1);//Thêm người tham gia vào danh sách tham gia
		
		task.setAttachments(new HashSet<TaskFileAttachmentDto>());
		
		TaskFileAttachmentDto attachment = new TaskFileAttachmentDto();
		attachment.setFile(new FileDescriptionDto());
		attachment.getFile().setId(3L);
		task.getAttachments().add(attachment);
		
		task.setFlow(new TaskFlowDto());
		task.getFlow().setId(1L);
		
		task.setCurrentStep(new TaskStepDto());
		task.getCurrentStep().setId(1L);
		service.saveTask(task);
	}
}
