import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.domain.Organization;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.dto.OrganizationDto;
import com.globits.core.service.OrganizationService;
import com.globits.hr.dto.StaffDto;
import com.globits.letter.LetterConstant;
import com.globits.letter.config.DatabaseConfig;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.service.LetterCoreService;
import com.globits.letter.service.LetterInDocumentService;
import com.globits.taskman.TaskManConstant.ParticipateStateEnum;
import com.globits.taskman.TaskManConstant.ParticipateTypeEnum;
import com.globits.taskman.dto.CommentFileAttachmentDto;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFileAttachmentDto;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.service.TaskFlowService;
import com.globits.taskman.service.TaskService;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class InDocumentServiceTest {
	@Autowired
	LetterInDocumentService service;

	@Autowired
	LetterCoreService coreService;
	@Autowired
	OrganizationService orgService;
	@Autowired
	TaskFlowService taskFlowService;
	public void coreTest() {
		Organization dto = new Organization();
		dto.setCode("test");
		dto.setName("test");
		//orgService.saveOrganization(dto);
		List<OrganizationDto> list= coreService.getListOrganization(1,10);
		System.out.println("Test");
	}
	@Test
	public void listTest() {
		List<Long> roleIds = new ArrayList<Long>();
		ArrayList<Long> departmentIds = new ArrayList<Long>();
		Long flowId = 2L;
		int pageSize = 2;
		int pageIndex =1;
		roleIds.add(1L);
		//roleIds.add(2L);
		departmentIds.add(103L);
		Long staffId = 4461L;
		Long userId = 2L;
		Long stepId =6L;
		Integer currentParticipateState = null;
		
		//Page<LetterInDocumentDto> page =  service.getAllDepartmentAndPersonalTask(staffId, flowId, stepId, currentParticipateState, pageSize, pageIndex);
		Page<LetterInDocumentDto> page =  service.getAllDepartmentByOwner(userId, flowId, stepId, currentParticipateState, pageSize, pageIndex);
		if(page!=null)
			System.out.println(page.getTotalElements());
		else {
			System.out.println("NULL");
		}
	}
	//@Test
	public void createDocument() {
		
//		LetterInDocumentDto dto = new LetterInDocumentDto();
//		LetterConstant.LetterInDocumentFlow = taskFlowService.findById(1L);
//		dto.setTitle("Test IN Document 1");
//		dto.setBriefNote("Test In Document");
//		dto.setTask(new TaskDto());
//		dto.getTask().setCurrentStep(new TaskStepDto());
//		dto.getTask().getCurrentStep().setId(1L);
//		dto.getTask().setParticipates(new HashSet<ParticipateDto>());
//		dto.getTask().setFlow(new TaskFlowDto());
//		dto.getTask().getFlow().setId(1L);
//		ParticipateDto docClerk = new ParticipateDto();
//		docClerk.setParticipateType(ParticipateTypeEnum.PersonalType.getValue());//Là cá nhân tham gia xử lý
//		//assigner.setId(3621L);//ID của thầy Thụ
//		docClerk.setEmployee(new StaffDto());
//		docClerk.getEmployee().setId(4461L);//ID của Hoàng Quốc Dũng
//		docClerk.setRole(new TaskRoleDto());
//		docClerk.getRole().setId(1L);
//		dto.getTask().getParticipates().add(docClerk);
		LetterInDocumentDto dto = new LetterInDocumentDto();
		LetterConstant.LetterInDocumentFlow = taskFlowService.getFullTaskFlowById(2L);
		dto.setTitle("Test IN Document 1");
		dto.setBriefNote("Test In Document");
		dto.setTask(new TaskDto());
		Long docClerkOwnerId = 7L;
		service.createLetterInDocument(dto, docClerkOwnerId, null);
	}
		//@Test
		public void forwardDocument() {//Test trường hợp chuyển văn bản - với trường hợp là người chuyển văn bản
			
			
			LetterConstant.LetterInDocumentFlow = taskFlowService.findById(1L);
			TaskCommentDto commentDto = new TaskCommentDto();
			commentDto.setComment("Kính chuyển thầy Thụ");
			service.forwardDocument(6L,2L,3621L, 2L, commentDto);
//			
//			LetterInDocumentDto dto = service.findDocumentById(6L);
//			dto.getTask().setCurrentStep(new TaskStepDto());
//			dto.getTask().getCurrentStep().setId(2L);
//			
//			ParticipateDto docClerk =null;
//			Iterator<ParticipateDto> iters= dto.getTask().getParticipates().iterator();
//			while(iters.hasNext()) {
//				ParticipateDto part = iters.next();
//				if(part.getRole().getId()==1L) {
//					docClerk = part;
//				}
//			}
//			if(docClerk!=null) {
//				TaskCommentDto comment = new TaskCommentDto();
//				comment.setComment("Kính chuyển thầy Thụ");
//				comment.setParticipate(docClerk);
//				docClerk.getComments().add(comment);
//			}
//			
//			ParticipateDto participate = new ParticipateDto();
//			participate.setParticipateType(ParticipateTypeEnum.PersonalType.getValue());//Là cá nhân tham gia xử lý
//			participate.setEmployee(new StaffDto());
//			participate.getEmployee().setId(3621L);//ID của thầy Thụ
//			participate.setRole(new TaskRoleDto());
//			participate.getRole().setId(2L);
//
//			dto.getTask().getParticipates().add(participate);
//			service.saveLetterInDocument(dto);
		}
}
