import java.util.ArrayList;
import java.util.HashSet;

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
import com.globits.taskman.TaskManConstant.ParticipateTypeEnum;
import com.globits.taskman.config.DatabaseConfig;
import com.globits.taskman.dto.CommentFileAttachmentDto;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.StaffDepartmentTaskRoleDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFileAttachmentDto;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.TaskFlowStepDto;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.repository.StaffDepartmentTaskRoleRepository;
import com.globits.taskman.service.StaffDepartmentTaskRoleService;
import com.globits.taskman.service.TaskFlowService;
import com.globits.taskman.service.TaskFlowStepService;
import com.globits.taskman.service.TaskService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class TaskFlowServiceTest {
	@Autowired
	TaskFlowService service;
	@Autowired
	TaskFlowStepService taskFlowStepService;
	@Autowired
	StaffDepartmentTaskRoleRepository sdtrRepos;
	@Autowired
	StaffDepartmentTaskRoleService staffDepartmentTaskRoleService;
	//@Test
	public void testCreateStaffDepRole() {
		StaffDepartmentTaskRoleDto dto = new StaffDepartmentTaskRoleDto();
		dto.setStaff(new StaffDto());
		dto.getStaff().setId(4274L);
		dto.setDepartment(new DepartmentDto());
		//dto.getDepartment().setId(103L);//Phòng hành chính
		dto.getDepartment().setId(64L);//Phòng tổ chức cán bộ
		dto.setRole(new TaskRoleDto());
		dto.getRole().setId(2L);//Quyền tham gia xử lý các văn bản chủ trì
		staffDepartmentTaskRoleService.saveStaffDepartmentTaskRole(dto);
		
//		dto = new StaffDepartmentTaskRoleDto();
//		dto.setStaff(new StaffDto());
//		dto.getStaff().setId(4274L);
//		dto.setDepartment(new DepartmentDto());
//		dto.getDepartment().setId(103L);//Phòng hành chính
//		dto.setRole(new TaskRoleDto());
//		dto.getRole().setId(3L);//Thêm quyền xử lý văn bản
//		staffDepartmentTaskRoleService.saveStaffDepartmentTaskRole(dto);
	}
	
	//@Test
	public void deleteTaskFlowTest() {
		//service.deleteTask(38L);
	}
	//@Test
	public void createTaskFlowTest() {
		
		TaskFlowDto dto = new TaskFlowDto();
		dto.setCode("001");
		dto.setName("Quy trình xử lý văn bản đến");
		service.saveTaskFlow(dto);
		
	}
	@Test
	public void addTaskStep() {
		TaskFlowStepDto flowStepDto = new TaskFlowStepDto();
		
		flowStepDto.getStep().setCode("LETTEROUTSTEP2");
		flowStepDto.getStep().setName("Văn bản đang xử lý");
		
		taskFlowStepService.saveTaskFlowStep(flowStepDto);
	}
	
	//@Test
	public void updateTaskFlowTest() {
		TaskFlowDto dto = service.getTaskFlow(1L);
		dto.setCode("001");
		dto.setName("Quy trình xử lý văn bản đến");
		dto.setSteps(new ArrayList<TaskFlowStepDto>());
		TaskFlowStepDto flowStepDto = new TaskFlowStepDto();
		flowStepDto.setFlow(dto);
		flowStepDto.setStep(new TaskStepDto());
		flowStepDto.getStep().setId(1L);
		flowStepDto.getStep().setCode("001");
		flowStepDto.getStep().setName("Vào sổ văn bản đến");
		flowStepDto.setOrderIndex(1);
		dto.getSteps().add(flowStepDto);
		
		flowStepDto = new TaskFlowStepDto();
		flowStepDto.setFlow(dto);
		flowStepDto.setStep(new TaskStepDto());
		flowStepDto.getStep().setId(2L);
		flowStepDto.getStep().setCode("002");
		flowStepDto.getStep().setName("Phân luồng xử lý văn bản đến");
		flowStepDto.setOrderIndex(2);
		dto.getSteps().add(flowStepDto);
		
		flowStepDto = new TaskFlowStepDto();
		flowStepDto.setFlow(dto);
		flowStepDto.setStep(new TaskStepDto());
		flowStepDto.getStep().setId(3L);
		flowStepDto.getStep().setCode("003");
		flowStepDto.getStep().setName("Giao xử lý văn bản đến");
		flowStepDto.setOrderIndex(3);
		dto.getSteps().add(flowStepDto);
		
		
		flowStepDto = new TaskFlowStepDto();
		flowStepDto.setFlow(dto);
		flowStepDto.setStep(new TaskStepDto());
		flowStepDto.getStep().setId(4L);
		flowStepDto.getStep().setCode("004");
		flowStepDto.getStep().setName("Thực hiện xử lý văn bản đến");
		flowStepDto.setOrderIndex(4);
		dto.getSteps().add(flowStepDto);

		flowStepDto = new TaskFlowStepDto();
		flowStepDto.setFlow(dto);
		flowStepDto.setStep(new TaskStepDto());
		flowStepDto.getStep().setId(5L);
		flowStepDto.getStep().setCode("005");
		flowStepDto.getStep().setName("Hoàn thành xử lý văn bản đến");
		flowStepDto.setOrderIndex(5);
		dto.getSteps().add(flowStepDto);
		
		service.saveTaskFlow(dto);
	}
}
