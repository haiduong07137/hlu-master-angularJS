package com.globits.letter.listeners;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.globits.core.dto.PersonDto;
import com.globits.core.utils.CommonUtils;
import com.globits.letter.LetterConstant;
import com.globits.security.domain.Role;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
import com.globits.security.service.RoleService;
import com.globits.security.service.UserService;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.TaskFlowStepDto;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.service.TaskFlowService;
import com.globits.taskman.service.TaskRoleService;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

	private static boolean eventFired = false;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;
	
	@Autowired
	TaskFlowService taskFlowService;
	
	
	@Autowired
	TaskRoleService taskRoleService;
	
	@Autowired
	private Environment env;
//	@Autowired
//	private ResourceDefService resDefService;

	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (eventFired) {
			return;
		}

		logger.info("Application started.");

		eventFired = true;

		try {
			createRoles();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		createAdminUser();
		try {
			createTastFlow();
			LetterConstant.WorkPlanFlow =  createTastFlow("work-plan-flow.xml");
			LetterConstant.ProjectFlow =  createTastFlow("project-flow.xml");
			LetterConstant.DailyWorksFlow =  createTastFlow("daily-works-flow.xml");
			
			createTaskRoles("letter-in-task-roles.xml");
			LetterConstant.LetterOutDocumentFlow = createTastFlow("letter-out-flow.xml");
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(LetterConstant.InDocumentFolderPath==null) {
			LetterConstant.InDocumentFolderPath = env.getProperty("indocument.file.folder");
		}
		if(LetterConstant.OutDocumentFolderPath==null) {
			LetterConstant.OutDocumentFolderPath = env.getProperty("outdocument.file.folder");
		}
		
		if(LetterConstant.WorkPlanFolderPath==null) {
			LetterConstant.WorkPlanFolderPath = env.getProperty("workplan.file.folder");
		}
		
		if(LetterConstant.DailyWorksPath==null) {
			LetterConstant.DailyWorksPath = env.getProperty("dailyworks.file.folder");
		}
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	private void createTaskRoles(String resourceFile) throws XMLStreamException {
		
		List<String> codes = new ArrayList<>();
		List<String> descriptions = new ArrayList<>();
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = getClass().getClassLoader().getResourceAsStream(resourceFile);
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in, "UTF-8");

//		streamReader.nextTag();
//		streamReader.nextTag();
		String roleCode=null;
		while (streamReader.hasNext()) {
			if (streamReader.isStartElement()) {
				switch (streamReader.getLocalName()) {
					case "task-role-code": {
						//codes.add(streamReader.getElementText());
						roleCode = streamReader.getElementText();
						if(roleCode!=null) {
							codes.add(roleCode.trim());	
						}						
						break;
					}
					case "description": {
						descriptions.add(streamReader.getElementText());
						break;
					}
					
				}
			}
			streamReader.next();
		}
		streamReader.close();
		for (int i=0;i<codes.size();i++) {
			String code = codes.get(i);
			String description = descriptions.get(i);
			TaskRoleDto taskRoleDto = taskRoleService.getTaskRoleByCode(code);
			if(taskRoleDto==null) {
				taskRoleDto = new TaskRoleDto();
				taskRoleDto.setCode(code);
				taskRoleDto.setName(description);
				taskRoleService.saveTaskRole(taskRoleDto);
			}		
		}
	}	
	private TaskFlow createTastFlow(String resourceFile) throws XMLStreamException {
		
		List<String> codes = new ArrayList<>();
		List<String> descriptions = new ArrayList<>();
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = getClass().getClassLoader().getResourceAsStream(resourceFile);
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in, "UTF-8");

		streamReader.nextTag();
		streamReader.nextTag();
		String flowCode=null;
		String flowName=null;
		while (streamReader.hasNext()) {
			if (streamReader.isStartElement()) {
				switch (streamReader.getLocalName()) {
					case "flow-code": {
						//codes.add(streamReader.getElementText());
						flowCode = streamReader.getElementText();
						break;
					}
					case "flow-name": {
						//codes.add(streamReader.getElementText());
						flowName = streamReader.getElementText();
						break;
					}					
					case "step-code": {
						codes.add(streamReader.getElementText());
						break;
					}
					case "description": {
						descriptions.add(streamReader.getElementText());
						break;
					}
					
				}
			}
			streamReader.next();
		}

		streamReader.close();

		TaskFlowDto taskFlow = taskFlowService.getTaskFlowByCode(flowCode);
		if(taskFlow==null) {
			taskFlow = new TaskFlowDto();
			taskFlow.setCode(flowCode);
			taskFlow.setName(flowName);
			taskFlow.setSteps(new ArrayList<TaskFlowStepDto>());
			int orderIndex =0;
			for (int i=0;i<codes.size();i++) {
				String stepCode = codes.get(i);
				String description = descriptions.get(i);
				TaskFlowStepDto step = new TaskFlowStepDto();
				step.setFlow(taskFlow);
				step.setStep(new TaskStepDto());
				step.getStep().setCode(stepCode);
				step.getStep().setName(description);
				step.setOrderIndex(orderIndex);
				taskFlow.getSteps().add(step);
				orderIndex++;
				taskFlow = taskFlowService.saveTaskFlow(taskFlow);
			}
		}
		TaskFlow outFlow =  taskFlowService.getFullTaskFlowById(taskFlow.getId());
		return outFlow;
	}
	private void createTastFlow() throws XMLStreamException {
		
		List<String> codes = new ArrayList<>();

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = getClass().getClassLoader().getResourceAsStream("letter-in-flow.xml");
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in, "UTF-8");

		streamReader.nextTag();
		streamReader.nextTag();
		String flowCode=null;
		while (streamReader.hasNext()) {
			if (streamReader.isStartElement()) {
				switch (streamReader.getLocalName()) {
					case "flow-code": {
						//codes.add(streamReader.getElementText());
						flowCode = streamReader.getElementText();
						break;
					}
					
					case "step-code": {
						codes.add(streamReader.getElementText());
						break;
					}
				}
			}
			streamReader.next();
		}

		streamReader.close();

		TaskFlowDto taskFlow = taskFlowService.getTaskFlowByCode(LetterConstant.LetterInDocumentFlowCode);
		
		if(taskFlow==null) {
			taskFlow = new TaskFlowDto();
			taskFlow.setCode(LetterConstant.LetterInDocumentFlowCode);
			taskFlow.setName("Xử lý văn bản đến");
			taskFlow.setSteps(new ArrayList<TaskFlowStepDto>());
			TaskFlowStepDto docClerk = new TaskFlowStepDto();
			docClerk.setFlow(taskFlow);
			docClerk.setStep(new TaskStepDto());
			docClerk.getStep().setCode(LetterConstant.LetterInDocumentFlowCode+"Step1");
			docClerk.getStep().setName("Vào sổ văn bản");
			docClerk.setOrderIndex(0);
			taskFlow.getSteps().add(docClerk);//Xong văn thư
			
			TaskFlowStepDto fowardClerk = new TaskFlowStepDto();
			fowardClerk.setFlow(taskFlow);
			fowardClerk.setStep(new TaskStepDto());
			fowardClerk.getStep().setCode(LetterConstant.LetterInDocumentFlowCode+"Step2");
			fowardClerk.getStep().setName("Phân luồng văn bản");
			fowardClerk.setOrderIndex(1);
			taskFlow.getSteps().add(fowardClerk);//Giao xử lý
			
			TaskFlowStepDto assigner = new TaskFlowStepDto();
			assigner.setFlow(taskFlow);
			assigner.setStep(new TaskStepDto());
			assigner.getStep().setCode(LetterConstant.LetterInDocumentFlowCode+"Step3");
			assigner.getStep().setName("Giao xử lý");
			assigner.setOrderIndex(2);
			taskFlow.getSteps().add(assigner);//Xử lý
			
			TaskFlowStepDto proccess = new TaskFlowStepDto();
			proccess.setFlow(taskFlow);
			proccess.setStep(new TaskStepDto());
			proccess.getStep().setCode(LetterConstant.LetterInDocumentFlowCode+"Step4");
			proccess.getStep().setName("Xử lý văn bản");
			proccess.setOrderIndex(3);
			taskFlow.getSteps().add(proccess);//Xử lý
			
			TaskFlowStepDto finish = new TaskFlowStepDto();
			finish.setFlow(taskFlow);
			finish.setStep(new TaskStepDto());
			finish.getStep().setCode(LetterConstant.LetterInDocumentFlowCode+"Step5");
			finish.getStep().setName("Hoàn thành xử lý văn bản");
			finish.setOrderIndex(4);
			taskFlow.getSteps().add(finish);//Kết thúc
			
			taskFlow = taskFlowService.saveTaskFlow(taskFlow);
		}
		LetterConstant.LetterInDocumentFlow = taskFlowService.getFullTaskFlowById(taskFlow.getId());
	}
	private void createAdminUser() {

		UserDto userDto = userService.findByUsername(com.globits.core.Constants.USER_ADMIN_USERNAME);

		if (CommonUtils.isNotNull(userDto)) {
			return;
		}

		userDto = new UserDto();
		userDto.setUsername(com.globits.core.Constants.USER_ADMIN_USERNAME);
		//userDto.setPassword(SecurityUtils.getHashPassword("admin"));
		userDto.setPassword("admin");
		userDto.setEmail("admin@globits.net");
		userDto.setActive(true);
		userDto.setDisplayName("Admin User");

		Role role = roleService.findByName(com.globits.core.Constants.ROLE_ADMIN);
		
		userDto.getRoles().addAll(Arrays.asList(new RoleDto(role)));
		
		PersonDto person = new PersonDto();
		person.setGender("M");
		person.setFirstName("Admin");
		person.setLastName("User");
		person.setDisplayName("Admin User");
		
		userDto.setPerson(person);
		
		try {
			userService.save(userDto);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void createRoles() throws XMLStreamException {

		List<String> roleNames = new ArrayList<>();

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = getClass().getClassLoader().getResourceAsStream("sys-roles.xml");
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in, "UTF-8");

		streamReader.nextTag();
		streamReader.nextTag();

		while (streamReader.hasNext()) {
			if (streamReader.isStartElement()) {
				switch (streamReader.getLocalName()) {
				case "name": {
					roleNames.add(streamReader.getElementText());
					break;
				}
				}
			}
			streamReader.next();
		}

		streamReader.close();

		for (String roleName : roleNames) {
			createRoleIfNotExist(roleName);
		}
	}

	private void createRoleIfNotExist(String roleName) {

		if (CommonUtils.isEmpty(roleName)) {
			return;
		}

		Role role = roleService.findByName(roleName);

		if (CommonUtils.isNotNull(role)) {
			return;
		}

		if (role == null) {
			role = new Role();
			role.setName(roleName);
		}

		try {
			roleService.save(role);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
