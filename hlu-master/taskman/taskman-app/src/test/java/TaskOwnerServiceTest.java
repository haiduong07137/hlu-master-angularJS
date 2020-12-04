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
import com.globits.core.dto.PersonDto;
import com.globits.core.service.PersonService;
import com.globits.hr.dto.StaffDto;
import com.globits.security.dto.UserDto;
import com.globits.taskman.TaskManConstant;
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
import com.globits.taskman.dto.TaskOwnerDto;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.dto.UserTaskOwnerDto;
import com.globits.taskman.repository.StaffDepartmentTaskRoleRepository;
import com.globits.taskman.service.StaffDepartmentTaskRoleService;
import com.globits.taskman.service.TaskFlowService;
import com.globits.taskman.service.TaskOwnerService;
import com.globits.taskman.service.TaskService;
import com.globits.taskman.service.UserTaskOwnerService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class TaskOwnerServiceTest {
	@Autowired
	TaskOwnerService service;
	@Autowired
	UserTaskOwnerService userTaskOwnerService;

	@Autowired
	PersonService personService;
	@Test
	public void testCreateTaskOwnerFromPerson() {
		PersonDto dto = new PersonDto();
		dto.setId(4284L);//Chị xuyến
		TaskOwnerDto ownerDto =service.saveTaskOwnerFromPerson(dto);
		System.out.println(ownerDto.getDisplayName());
		
		UserTaskOwnerDto uto = new UserTaskOwnerDto();
		uto.setTaskOwner(new TaskOwnerDto());
		uto.getTaskOwner().setId(ownerDto.getId());
		uto.setUser(new UserDto());
		uto.getUser().setId(3668L);//Tài khoản Chị xuyến
		uto.setRole(new TaskRoleDto());
		uto.getRole().setId(1L);
		userTaskOwnerService.saveUserTaskOwner(uto);
	}
	
	//@Test
	public void testCreateTaskOwnerFromDepartment() {
		DepartmentDto dto = new DepartmentDto();
		dto.setId(2L);
		TaskOwnerDto ownerDto =service.saveTaskOwnerFromDepartment(dto);
		System.out.println(ownerDto.getDisplayName());
	}
	//@Test
	public void testCreateTaskOwner() {
		TaskOwnerDto owner = new TaskOwnerDto();
		owner.setDisplayName("Khoa CNTT");
//		owner.setPerson(new PersonDto());
//		owner.getPerson().setId(3694L);//Anh Tùng
//		owner.setOwnerType(TaskManConstant.TaskOwnerTypeEnum.PersonalType.getValue());
		owner.setDepartment(new DepartmentDto());
		owner.getDepartment().setId(1L);
		owner.setOwnerType(TaskManConstant.TaskOwnerTypeEnum.DepartmentType.getValue());
		owner = service.saveTaskOwner(owner);
		
		UserTaskOwnerDto dto = new UserTaskOwnerDto();
		dto.setTaskOwner(new TaskOwnerDto());
		dto.getTaskOwner().setId(owner.getId());
		dto.setUser(new UserDto());
		dto.getUser().setId(3668L);//Tài khoản của anh Tùng
		dto.setRole(new TaskRoleDto());
		dto.getRole().setId(2L);
		userTaskOwnerService.saveUserTaskOwner(dto);
	}
	
	//@Test
	public void testCreateUserTaskOwner() {
		UserTaskOwnerDto dto = new UserTaskOwnerDto();
		dto.setTaskOwner(new TaskOwnerDto());
		dto.getTaskOwner().setId(2L);
		dto.setUser(new UserDto());
		dto.getUser().setId(2L);
		dto.setRole(new TaskRoleDto());
		dto.getRole().setId(2L);
		userTaskOwnerService.saveUserTaskOwner(dto);
	}
	
	//@Test
	public void deleteTaskFlowTest() {
		//service.deleteTask(38L);
	}
	//@Test
	public void createTaskFlowTest() {
	}
	//@Test
	public void updateTaskFlowTest() {
	
	}
}
