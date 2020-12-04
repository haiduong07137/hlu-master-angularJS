package com.globits.letter.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.core.domain.FileDescription;
import com.globits.core.domain.Organization;
import com.globits.core.repository.FileDescriptionRepository;
import com.globits.core.repository.OrganizationRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.CommonUtils;
import com.globits.hr.service.StaffService;
import com.globits.letter.LetterConstant;
import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.domain.LetterDocBookGroup;
import com.globits.letter.domain.LetterDocField;
import com.globits.letter.domain.LetterDocPriority;
import com.globits.letter.domain.LetterDocSecurityLevel;
import com.globits.letter.domain.LetterDocumentAttachment;
import com.globits.letter.domain.LetterDocumentType;
import com.globits.letter.domain.LetterDocumentUser;
import com.globits.letter.domain.LetterOutDocument;
import com.globits.letter.dto.LetterDocumentAttachmentDto;
import com.globits.letter.dto.LetterDocumentUserDto;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.LetterOutDocumentDto;
import com.globits.letter.dto.ReportLetterByStepDto;
import com.globits.letter.dto.ResponseDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.repository.LetterDocBookGroupRepository;
import com.globits.letter.repository.LetterDocBookRepository;
import com.globits.letter.repository.LetterDocFieldRepository;
import com.globits.letter.repository.LetterDocPriorityRepository;
import com.globits.letter.repository.LetterDocSecurityLevelRepository;
import com.globits.letter.repository.LetterDocumentAttachmentRepository;
import com.globits.letter.repository.LetterDocumentTypeRepository;
import com.globits.letter.repository.LetterDocumentUserRepository;
import com.globits.letter.repository.LetterOutDocumentRepository;
import com.globits.letter.service.LetterOutDocumentService;
import com.globits.security.domain.User;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.dto.TaskFlowStepDto;
import com.globits.taskman.dto.TaskOwnerDto;
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.dto.TaskStepDto;
import com.globits.taskman.dto.UserTaskOwnerDto;
import com.globits.taskman.repository.CommentFileAttachmentRepository;
import com.globits.taskman.repository.TaskFlowRepository;
import com.globits.taskman.service.ParticipateService;
import com.globits.taskman.service.TaskCommentService;
import com.globits.taskman.service.TaskOwnerService;
import com.globits.taskman.service.TaskRoleService;
import com.globits.taskman.service.TaskService;
import com.globits.taskman.service.TaskStepService;

@Service
public class LetterOutDocumentServiceImpl extends GenericServiceImpl<LetterOutDocument, Long>
		implements LetterOutDocumentService {
	@Autowired
	LetterOutDocumentRepository letterOutDocumentRepository;
	@Autowired
	TaskService taskService;
	@Autowired
	TaskRoleService taskRoleService;

	@Autowired
	TaskStepService taskStepService;
	@Autowired
	ParticipateService participateService;
	@Autowired
	StaffService staffService;

	@Autowired
	TaskCommentService taskCommentService;

	@Autowired
	TaskOwnerService taskOwnerService;

	@Autowired
	LetterDocumentAttachmentRepository letterDocumentAttachmentRepository;

	@Autowired
	LetterDocumentUserRepository letterDocumentUserRepository;

	@Autowired
	CommentFileAttachmentRepository commentFileAttachmentRepository;
	@Autowired
	FileDescriptionRepository fileDescriptionRepository;
	@Autowired
	LetterDocFieldRepository letterDocFieldRepository;
	@Autowired
	LetterDocPriorityRepository letterDocPriorityRepository;
	@Autowired
	LetterDocSecurityLevelRepository letterDocSecurityLevelRepository;
	@Autowired
	LetterDocumentTypeRepository letterDocumentTypeRepository;
	@Autowired
	OrganizationRepository organizationRepository;

	@Autowired
	LetterDocBookRepository letterDocBookRepository;

	@Autowired
	LetterDocBookGroupRepository letterDocBookGroupRepository;

	@Autowired
	TaskFlowRepository taskFlowRepository;
	@Autowired
	EntityManager entityManager;

	@Override
	public LetterOutDocumentDto createLetterOutDocument(LetterOutDocumentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		if (dto.getTask() == null) {
			dto.setTask(new TaskDto());
			/*
			 * tạo danh sách những người liên quan đến văn bản đi đó. mặc định sẽ có Văn
			 * thư.
			 */
			dto.getTask().setParticipates(new HashSet<ParticipateDto>());
			ParticipateDto docClerk = new ParticipateDto();// Tạo mới đối tượng văn thư
			// Đặt loại tham gia xử lý là khác (không xác định là người hay phòng ban)
			docClerk.setParticipateType(TaskManConstant.TaskOwnerTypeEnum.OtherType.getValue());
			// Tìm vai trò văn thư
			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ClerkRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docClerkOwner = null;
				List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerDtoByRole(role.getId());
				if (taskOwners != null && taskOwners.size() > 0) {
					docClerkOwner = taskOwners.get(0);
				}
				docClerk.setRole(role);
				docClerk.setTaskOwner(docClerkOwner);
			}
			// thêm Văn thư vào participate
			dto.getTask().getParticipates().add(docClerk);
		}

		/*
		 * tìm flow là xử lý văn bản đi
		 */
		dto.getTask().setFlow(new TaskFlowDto(LetterConstant.LetterOutDocumentFlow));
		TaskFlowStepDto firstStep = null;
		firstStep = dto.getTask().getFlow().getSteps().get(0);
		TaskStepDto fowardStep = dto.getTask().getFlow().getSteps().get(1).getStep();
		if (firstStep != null) {
			dto.getTask().setCurrentStep(firstStep.getStep());
		}
		// thêm người nhận văn bản đi vào
		if (dto.getParticipateFowards() != null) {
			for (int i = 0, len = dto.getParticipateFowards().size(); i < len; i++) {
				ParticipateDto fowarder = dto.getParticipateFowards().get(i);
				dto.getTask().getParticipates().add(fowarder);
				dto.getTask().setCurrentStep(fowardStep);
			}
		}
		return saveLetterOutDocument(dto);
	}

	@Override
	public LetterOutDocumentDto forwardLetterOutDocument(LetterOutDocumentDto dto, Long docClerkOwnerId,
			Long draftersId, Long fowarderId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		if (dto.getTask() == null) {
			dto.setTask(new TaskDto());
			/*
			 * Tạo danh sách những người tham gia xử lý trong đó người đầu tiên là Văn thư
			 */
			dto.getTask().setParticipates(new HashSet<ParticipateDto>());
			ParticipateDto docClerk = new ParticipateDto();// Tạo mới đối tượng văn thư
			// Đặt loại tham gia xử lý là khác (không xác định là người hay phòng ban)
			docClerk.setParticipateType(TaskManConstant.TaskOwnerTypeEnum.OtherType.getValue());
			// Tìm vai trò văn thư
			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ClerkRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docClerkOwner = null;
//				if (docClerkOwnerId == null || docClerkOwnerId <= 0) {
				List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerByRoleCode(role.getCode());
				if (taskOwners != null && taskOwners.size() > 0) {
					docClerkOwner = taskOwners.get(0);
				}
//				} else {
//					docClerkOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
//				}
				docClerk.setRole(role);
				docClerk.setTaskOwner(docClerkOwner);
			}
			// Thêm Văn thư vào danh sách những người tham gia
			dto.getTask().getParticipates().add(docClerk);
		}
		/*
		 * Vì là quy trình xử lý văn bản đến nên Flow là Xử lý văn bản đến
		 */
		dto.getTask().setFlow(new TaskFlowDto(LetterConstant.LetterOutDocumentFlow));

		TaskFlowStepDto firstStep = null;
		firstStep = dto.getTask().getFlow().getSteps().get(0);// Tìm bước đầu tiên của quy trình xử lý văn bản
		if (firstStep != null)
			dto.getTask().setCurrentStep(firstStep.getStep());

		// Thêm chuyên viên vào người tham gia
		if (draftersId != null && draftersId > 0) {
			// Tìm role tương ứng với vai trò người chuyên viên
			ParticipateDto drafters = null;
			if (dto.getTask() != null && dto.getTask().getParticipates() != null) {
				for (ParticipateDto p : dto.getTask().getParticipates()) {
					if (p.getRole() != null && p.getRole().getCode().equals(LetterConstant.DraftersRole)) {
						if (p.getTaskOwner().getId() == draftersId) {
							if (drafters == null) {
								drafters = p;
								break;
							}
						}
					}
				}
			}

			List<TaskRoleDto> draftersRoles = taskRoleService.getTaskRoleByCodes(LetterConstant.DraftersRole);
			if (draftersRoles != null && draftersRoles.size() > 0) {
				TaskRoleDto draftersRole = draftersRoles.get(0);
				TaskOwnerDto draftersOwer = taskOwnerService.getTaskOwner(draftersId);
				if (drafters == null) {
					drafters = new ParticipateDto();
					drafters.setRole(draftersRole);
					drafters.setTaskOwner(draftersOwer);
					dto.getTask().getParticipates().add(drafters);
				}
			}
		}

		// Nếu được Forward luôn đến người phân luồng thì assignerId sẽ khác NULL
		if (fowarderId != null && fowarderId > 0) {
			// Tìm role tương ứng với vai trò người phân luồng
			ParticipateDto fowarder = null;
			if (dto.getTask() != null && dto.getTask().getParticipates() != null) {
				for (ParticipateDto p : dto.getTask().getParticipates()) {
					if (p.getRole() != null && p.getRole().getCode().equals(LetterConstant.ManagerRole)) {
						if (p.getTaskOwner().getId() == fowarderId) {
							if (fowarder == null) {
								fowarder = p;
								break;
							}
						}
					}
				}
			}

			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ManagerRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docFowardOwner = taskOwnerService.getTaskOwner(fowarderId);
				if (fowarder == null) {
					fowarder = new ParticipateDto();
					fowarder.setRole(role);
					fowarder.setTaskOwner(docFowardOwner);
					dto.getTask().getParticipates().add(fowarder);
				}

			}
			TaskStepDto fowardStep = dto.getTask().getFlow().getSteps().get(1).getStep();
			dto.getTask().setCurrentStep(fowardStep);
		}
		return saveLetterOutDocument(dto);
	}

	@Override
	public void transferLetterOutDocument(Long documentId, Long leaderId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterOutDocument document = findById(documentId);
		String stepCode = LetterConstant.LetterOutDocumentFlowCode + "3";
		TaskStep toStep = taskStepService.getTaskStepEntityByCode(stepCode);
		if (document.getTask() == null) {
			document.setTask(new Task());
			document.getTask().setCreateDate(currentDate);
			document.getTask().setCreatedBy(currentUserName);
		}
		document.getTask().setCurrentStep(toStep);

		if (document.getTask() != null && document.getTask().getParticipates() != null) {
			for (Participate a : document.getTask().getParticipates()) {
				if (a.getRole() != null && a.getRole().getCode().equals(LetterConstant.FowardRole)) {
					if (a.getTaskOwner().getId() != leaderId) {
						document.getTask().getParticipates().remove(a);
						break;
					}
				}
			}
		}

		// Participate p =
		// convertParticipateDtoToEntity(pDto,currentUserName,currentDate);
		Participate p = new Participate();
		p.setTask(document.getTask());
		TaskRole role = taskRoleService.getTaskRoleEntityByCode(LetterConstant.FowardRole);
		TaskOwner taskOwner = taskOwnerService.findById(leaderId);
		p.setRole(role);
		p.setTaskOwner(taskOwner);
		p.setDisplayName(taskOwner.getDisplayName());
		if (document.getTask().getParticipates() == null) {
			document.getTask().setParticipates(new HashSet<Participate>());
		}

		if (p.getTaskOwner().getId() != null && p.getTaskOwner().getId() != null && p.getRole().getId() != null) {
			Participate participate = participateService.getTaskParticipateBy(p.getTask().getId(),
					p.getTaskOwner().getId(), p.getRole().getId());
			if (participate == null) {
				document.getTask().getParticipates().add(p);
			}
		}
		letterOutDocumentRepository.save(document);
	}

	@Override
	public void signLetterOutDocument(Long documentId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterOutDocument document = findById(documentId);
		String stepCode = LetterConstant.LetterOutDocumentFlowCode + "4";
		TaskStep toStep = taskStepService.getTaskStepEntityByCode(stepCode);
		if (document.getTask() == null) {
			document.setTask(new Task());
			document.getTask().setCreateDate(currentDate);
			document.getTask().setCreatedBy(currentUserName);
		}
		document.getTask().setCurrentStep(toStep);

		letterOutDocumentRepository.save(document);
	}

	private LetterOutDocumentDto saveLetterOutDocument(LetterOutDocumentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		if (dto != null) {
			LetterOutDocument entity = null;
			if (dto.getId() != null)
				entity = letterOutDocumentRepository.findOne(dto.getId());
			if (entity == null) {
				entity = new LetterOutDocument();
				entity.setCreateDate(currentDate);
				entity.setCreatedBy(currentUserName);
			}

			entity.setTask(new Task());
			entity.getTask().setFlow(LetterConstant.LetterOutDocumentFlow);
			entity = setValue(dto, entity, currentUserName, currentDate);
			if (dto.getTask() != null && dto.getTask().getParticipates() != null
					&& dto.getTask().getParticipates().size() > 0) {
				TaskDto taskDto = taskService.saveTask(dto.getTask());
				Task task = taskService.findById(taskDto.getId());
				entity.setTask(task);
			}

			if (dto.getDocOriginCode() != null && StringUtils.hasText(dto.getDocOriginCode())) {
				entity.getTask().setName(LetterConstant.HardCodeTaskNameOfLetterOutDocument + dto.getDocOriginCode());
			}
			if(dto.getPromulgate() != null) {
				entity.setPromulgate(dto.getPromulgate());
			}

			if (dto.getAttachments() != null && dto.getAttachments().size() > 0) {
				if (entity.getAttachments() != null) {
					entity.getAttachments().clear();
				} else {
					entity.setAttachments(new HashSet<LetterDocumentAttachment>());
				}
				for (LetterDocumentAttachmentDto _a : dto.getAttachments()) {

					LetterDocumentAttachment a = null;
					if (CommonUtils.isPositive(_a.getId(), true)) {
						a = letterDocumentAttachmentRepository.findOne(_a.getId());
					}

					if (a == null) {
						a = new LetterDocumentAttachment();
						a.setCreateDate(currentDate);
						a.setCreatedBy(currentUserName);
					} else {
						a.setModifyDate(currentDate);
						a.setModifiedBy(currentUserName);
					}
					a.setDocument(entity);
					if (_a.getFile() != null) {
						FileDescription file = new FileDescription();
						file.setId(_a.getFile().getId());
						if (_a.getFile().getId() == null) {
							file.setContentSize(_a.getFile().getContentSize());
							file.setContentType(_a.getFile().getContentType());
							file.setName(_a.getFile().getName());
							file.setFilePath(_a.getFile().getFilePath());
						}
						a.setFile(file);
					}
					entity.getAttachments().add(a);
				}
			}
			
			if (dto.getUserPermission() != null && dto.getIsLimitedRead() != null && dto.getIsLimitedRead() == true) {
				List<LetterDocumentUser> userPermissions = new ArrayList<LetterDocumentUser>();
				for (LetterDocumentUserDto uDto : dto.getUserPermission()) {
					LetterDocumentUser docUser = null;
					if (CommonUtils.isPositive(uDto.getId(), true)) {
						docUser = letterDocumentUserRepository.findOne(uDto.getId());
					}

					if (docUser == null) {
						docUser = new LetterDocumentUser();
						docUser.setCreateDate(currentDate);
						docUser.setCreatedBy(currentUserName);
					} else {
						docUser.setModifyDate(currentDate);
						docUser.setModifiedBy(currentUserName);
					}
					docUser.setLetterDocument(entity);
					if (uDto.getUser() != null) {
						User user = new User();
						user.setId(uDto.getUser().getId());
						docUser.setUser(user);
					}
					userPermissions.add(docUser);
				}
				if (entity.getUserPermission() == null) {
					entity.setUserPermission(new HashSet<LetterDocumentUser>());
				}
				entity.getUserPermission().clear();
				entity.getUserPermission().addAll(userPermissions);
			}
			entity = letterOutDocumentRepository.save(entity);
			return new LetterOutDocumentDto(entity);
		}

		return null;
	}

	private LetterOutDocument setValue(LetterOutDocumentDto dto, LetterOutDocument entity, String userName,
			LocalDateTime currentDate) {
		if (entity == null) {
			entity = new LetterOutDocument();
			entity.setCreateDate(currentDate);
			entity.setCreatedBy(userName);
		}
		entity.setIsLimitedRead(dto.getIsLimitedRead());
		entity.setTitle(dto.getTitle());
		entity.setBriefNote(dto.getBriefNote());
		entity.setDescription(dto.getDescription());
		entity.setDocCode(dto.getDocCode());
		entity.setDocOriginCode(dto.getDocOriginCode());
		entity.setRefDocOriginCode(dto.getRefDocOriginCode());
		entity.setOfficialDate(dto.getOfficialDate());
		entity.setDeliveredDate(dto.getDeliveredDate());
		entity.setIssuedDate(dto.getIssuedDate());
		entity.setExpiredDate(dto.getExpiredDate());
		entity.setLetterLine(dto.getLetterLine());
		entity.setSaveLetterType(dto.getSaveLetterType());
		entity.setRegisteredDate(dto.getRegisteredDate());
		entity.setSendEmail(dto.getSendEmail());
		entity.setSendSMS(dto.getSendSMS());
		entity.setSignedPerson(dto.getSignedPerson());
		entity.setOtherIssuePerson(dto.getOtherIssuePerson());
		entity.setSignedPost(dto.getSignedPost());
		entity.setDocumentOrderNoByBook(dto.getDocumentOrderNoByBook());
		entity.setNumberOfPages(dto.getNumberOfPages());
		if (dto.getSigner() != null && dto.getSigner().getId() != null) {
			TaskOwner taskOwner = taskOwnerService.findById(dto.getSigner().getId());
			if (taskOwner != null) {
				entity.setSigner(taskOwner);
			}
		}
		if (dto.getChiefOfStaff() != null && dto.getChiefOfStaff().getId() != null) {
			TaskOwner taskOwner = taskOwnerService.findById(dto.getChiefOfStaff().getId());
			if (taskOwner != null) {
				entity.setChiefOfStaff(taskOwner);
			}
		}
		if (dto.getEditorUnit() != null && dto.getEditorUnit().getId() != null) {
			TaskOwner taskOwner = taskOwnerService.findById(dto.getEditorUnit().getId());
			if (taskOwner != null) {
				entity.setEditorUnit(taskOwner);
			}
		}
		if (dto.getLetterDocBook() != null && dto.getLetterDocBook().getId() != null) {
			LetterDocBook letterDocBook = letterDocBookRepository.findOne(dto.getLetterDocBook().getId());
			entity.setLetterDocBook(letterDocBook);
		}

		if (dto.getLetterDocBookGroup() != null && dto.getLetterDocBookGroup().getId() != null) {
			LetterDocBookGroup letterDocBookGroup = letterDocBookGroupRepository
					.findOne(dto.getLetterDocBookGroup().getId());
			entity.setLetterDocBookGroup(letterDocBookGroup);
		}

		entity.setIsOtherIssueOrgan(dto.getIsOtherIssueOrgan());
		if (dto.getIsOtherIssueOrgan() != null) {
			entity.setOtherIssueOrgan(dto.getOtherIssueOrgan());
		} else {
			if (dto.getIssueOrgan() != null && dto.getIssueOrgan().getId() != null) {
				Organization issueOrgan = organizationRepository.findById(dto.getIssueOrgan().getId());
				if (issueOrgan != null)
					entity.setIssueOrgan(issueOrgan);
			}
		}

		if (dto.getLetterDocField() != null && dto.getLetterDocField().getId() != null) {
			LetterDocField LetterOutDocField = letterDocFieldRepository.getOne(dto.getLetterDocField().getId());
			if (LetterOutDocField != null)
				entity.setLetterDocField(LetterOutDocField);
		}
		if (dto.getLetterDocPriority() != null && dto.getLetterDocPriority().getId() != null) {
			LetterDocPriority letterDocPriority = letterDocPriorityRepository
					.getOne(dto.getLetterDocPriority().getId());
			if (letterDocPriority != null)
				entity.setLetterDocPriority(letterDocPriority);
		}
		if (dto.getLetterDocSecurityLevel() != null && dto.getLetterDocSecurityLevel().getId() != null) {
			LetterDocSecurityLevel letterDocSecurityLevel = letterDocSecurityLevelRepository
					.getOne(dto.getLetterDocSecurityLevel().getId());
			if (letterDocSecurityLevel != null)
				entity.setLetterDocSecurityLevel(letterDocSecurityLevel);
		}
		if (dto.getLetterDocumentType() != null && dto.getLetterDocumentType().getId() != null) {
			LetterDocumentType letterDocumentType = letterDocumentTypeRepository
					.getOne(dto.getLetterDocumentType().getId());
			if (letterDocumentType != null)
				entity.setLetterDocumentType(letterDocumentType);
		}
		return entity;
	}

	@Override
	public Page<LetterOutDocumentDto> getAllLetterOutDocumentByCurrentUser(int pageIndex, int pageSize) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long userId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			/*
			 * if (modifiedUser.getPerson() != null) { userId = modifiedUser.getId(); }
			 */
			userId = modifiedUser.getId();
		}
		if (pageIndex > 1) {
			pageIndex--;
		} else
			pageIndex = 0;
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		TaskFlow flow = LetterConstant.LetterOutDocumentFlow;
		Long step = null;
		for (TaskFlowStep taskFlowStep : flow.getSteps()) {
			if (step == null) {
				step = taskFlowStep.getId();
			}
		}
		return this.letterOutDocumentRepository.getAllLetterOutDocument(userId, pageable);
	}

//	@Override
//	public LetterOutDocumentDto getLetterOutDocumentById(Long id) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		User modifiedUser = null;
//		LocalDateTime currentDate = LocalDateTime.now();
//		String currentUserName = "Unknown User";
//		Long userId = null;
//		if (authentication != null) {
//			modifiedUser = (User) authentication.getPrincipal();
//			currentUserName = modifiedUser.getUsername();
//			/*if (modifiedUser.getPerson() != null) {
//				userId = modifiedUser.getId();
//			}*/
//			userId = modifiedUser.getId();
//		}
//		
//		TaskFlow flow = LetterConstant.LetterOutDocumentFlow;
//		Long step = null;
//		for(TaskFlowStep taskFlowStep : flow.getSteps()) {
//			if(step == null) {
//				step = taskFlowStep.getId();
//			}
//		}
//		LetterOutDocumentDto letterOutDocumentDto =  this.letterOutDocumentRepository.getLetterOutById(userId, id);
//		if (letterOutDocumentDto != null && letterOutDocumentDto.getTask() != null && letterOutDocumentDto.getTask().getParticipates() != null && letterOutDocumentDto.getTask().getParticipates().size() > 0) {
//
//			Boolean hasClerkRole =false;
//			Boolean hasFowardRole =false;
//			Boolean hasAssignerRole =false;
//			Boolean hasChairmanRole =false;
//			Boolean hasProcessRole =false;
//			Boolean hasOwnerPermission =false;
//
//			for(ParticipateDto participateDto: letterOutDocumentDto.getTask().getParticipates()) {
//
//				if (participateDto.getTaskOwner() != null && participateDto.getTaskOwner().getUserTaskOwners() != null && participateDto.getTaskOwner().getUserTaskOwners().size() > 0) {
//					for ( UserTaskOwnerDto uto : participateDto.getTaskOwner().getUserTaskOwners()) {
//
//						//check quyền comment
//						if (!participateDto.getHasOwnerPermission()) {
//							Boolean ownerPermission = uto.getUser().getId().equals(userId);
//							participateDto.setHasOwnerPermission(hasOwnerPermission || ownerPermission);
//						}
//						
//						//check quyền văn thư
//						if (!hasClerkRole) {
//							Boolean clerkRole = participateDto.getRole().getCode().equals(LetterConstant.ClerkRole) && (uto.getUser().getId().equals(userId));
//							hasClerkRole = hasClerkRole || clerkRole;
//						}
//
//						//check quyền phân luồng
//						if (!hasFowardRole) {
//							Boolean fowardRole = participateDto.getRole().getCode().equals(LetterConstant.FowardRole) && (uto.getUser().getId().equals(userId));
//							hasFowardRole = hasFowardRole || fowardRole;
//						}
//
//						//check quyền giao xử lý
//						if (!hasAssignerRole) {
//							Boolean assignerRole = participateDto.getRole().getCode().equals(LetterConstant.AssignerRole) && (uto.getUser().getId().equals(userId));
//							hasAssignerRole = hasAssignerRole || assignerRole;
//						}
//
//						//check quyền chủ trì
//						if (!hasChairmanRole) {
//							Boolean chairmanRole = participateDto.getRole().getCode().equals(LetterConstant.ChairmanRole) && (uto.getUser().getId().equals(userId));
//							hasChairmanRole = hasChairmanRole || chairmanRole;
//						}
//
//						//check quyền tham gia
//						if (!hasProcessRole) {
//							Boolean processRole = participateDto.getRole().getCode().equals(LetterConstant.ProcessRole) && (uto.getUser().getId().equals(userId));
//							hasProcessRole = hasProcessRole || processRole;
//						}
//					}
//				}
//			}
//			
//			letterOutDocumentDto.setHasClerkRole(hasClerkRole);
//			letterOutDocumentDto.setHasFowardRole(hasFowardRole);
//			letterOutDocumentDto.setHasAssignerRole(hasAssignerRole);
//			letterOutDocumentDto.setHasChairmanRole(hasChairmanRole);
//			letterOutDocumentDto.setHasProcessRole(hasProcessRole);
//		}
//		
//		return letterOutDocumentDto;
//	}

	@Override
	public LetterOutDocumentDto getLetterOutDocumentById(Long id) {
		LetterOutDocumentDto letterOutDocumentDto = letterOutDocumentRepository.getLetterOutDocumentById(id);
		if (letterOutDocumentDto != null && letterOutDocumentDto.getTask() != null
				&& letterOutDocumentDto.getTask().getParticipates() != null
				&& letterOutDocumentDto.getTask().getParticipates().size() > 0) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User modifiedUser = null;
			LocalDateTime currentDate = LocalDateTime.now();
			String currentUserName = "Unknown User";
			Long userId = null;
			if (authentication != null) {
				modifiedUser = (User) authentication.getPrincipal();
				currentUserName = modifiedUser.getUsername();
				/*
				 * if (modifiedUser.getPerson() != null) { userId = modifiedUser.getId(); }
				 */
				userId = modifiedUser.getId();
			}

			TaskFlow flow = LetterConstant.LetterOutDocumentFlow;
			Long step = null;
			for (TaskFlowStep taskFlowStep : flow.getSteps()) {
				if (step == null) {
					step = taskFlowStep.getId();
				}
			}

			Boolean hasClerkRole = false;
			Boolean hasFowardRole = false;
			Boolean hasAssignerRole = false;
			Boolean hasChairmanRole = false;
			Boolean hasProcessRole = false;
			Boolean hasOwnerPermission = false;
			Boolean hasManagerRole = false;
			Boolean hasDraftersRole = false;

			for (ParticipateDto participateDto : letterOutDocumentDto.getTask().getParticipates()) {

				if (participateDto.getTaskOwner() != null && participateDto.getTaskOwner().getUserTaskOwners() != null
						&& participateDto.getTaskOwner().getUserTaskOwners().size() > 0) {
					for (UserTaskOwnerDto uto : participateDto.getTaskOwner().getUserTaskOwners()) {

						// check quyền comment
						if (!participateDto.getHasOwnerPermission()) {
							Boolean ownerPermission = uto.getUser().getId().equals(userId);
							participateDto.setHasOwnerPermission(hasOwnerPermission || ownerPermission);
						}

						// check quyền văn thư
						if (!hasClerkRole) {
							Boolean clerkRole = participateDto.getRole().getCode().equals(LetterConstant.ClerkRole)
									&& (uto.getUser().getId().equals(userId));
							hasClerkRole = hasClerkRole || clerkRole;
						}

						// check quyền chuyên viên
						if (!hasDraftersRole) {
							Boolean draftersRole = participateDto.getRole().getCode()
									.equals(LetterConstant.DraftersRole) && (uto.getUser().getId().equals(userId));
							hasDraftersRole = hasDraftersRole || draftersRole;
						}

						// check quyền trưởng phòng
						if (!hasManagerRole) {
							Boolean ManagerRole = participateDto.getRole().getCode().equals(LetterConstant.ManagerRole)
									&& (uto.getUser().getId().equals(userId));
							hasManagerRole = hasManagerRole || ManagerRole;
						}

						// check quyền phân luồng
						if (!hasFowardRole) {
							Boolean fowardRole = participateDto.getRole().getCode().equals(LetterConstant.FowardRole)
									&& (uto.getUser().getId().equals(userId));
							hasFowardRole = hasFowardRole || fowardRole;
						}

						// check quyền giao xử lý
						if (!hasAssignerRole) {
							Boolean assignerRole = participateDto.getRole().getCode()
									.equals(LetterConstant.AssignerRole) && (uto.getUser().getId().equals(userId));
							hasAssignerRole = hasAssignerRole || assignerRole;
						}

						// check quyền chủ trì
						if (!hasChairmanRole) {
							Boolean chairmanRole = participateDto.getRole().getCode()
									.equals(LetterConstant.ChairmanRole) && (uto.getUser().getId().equals(userId));
							hasChairmanRole = hasChairmanRole || chairmanRole;
						}

						// check quyền tham gia
						if (!hasProcessRole) {
							Boolean processRole = participateDto.getRole().getCode().equals(LetterConstant.ProcessRole)
									&& (uto.getUser().getId().equals(userId));
							hasProcessRole = hasProcessRole || processRole;
						}
					}
				}
			}

			letterOutDocumentDto.setHasDraftersRole(hasDraftersRole);
			letterOutDocumentDto.setHasClerkRole(hasClerkRole);
			letterOutDocumentDto.setHasFowardRole(hasFowardRole);
			letterOutDocumentDto.setHasAssignerRole(hasAssignerRole);
			letterOutDocumentDto.setHasChairmanRole(hasChairmanRole);
			letterOutDocumentDto.setHasProcessRole(hasProcessRole);
			letterOutDocumentDto.setHasManagerRole(hasManagerRole);
		}

		return letterOutDocumentDto;
	}

	@Override
	public Long getMaxDocumentId() {
		String sql = "select max(d.id) from LetterOutDocument d";
		Query q = manager.createQuery(sql);
		Object resultObject = q.getSingleResult();
		Long result = 1L;
		if (resultObject != null) {
			result = (Long) resultObject;
		}
		return result;
	}

	@Override
	public LetterOutDocumentDto generateDtoLetterOut() {
		LetterOutDocumentDto dto = new LetterOutDocumentDto();
		String docCode = LetterConstant.NumberOriginal + this.getMaxDocumentId() + "";
		dto.setDocCode(docCode);
		return dto;
	}

	@Override
	public Page<LetterOutDocumentDto> getLetterByString(String search, int pageIndex, int pageSize) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long userId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			/*
			 * if (modifiedUser.getPerson() != null) { userId = modifiedUser.getId(); }
			 */
			userId = modifiedUser.getId();
		}

		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);

		int startPosition = (pageIndex - 1) * pageSize;

		String sql = "select distinct new com.globits.letter.dto.LetterOutDocumentDto(d) from LetterOutDocument d inner join Participate p on d.task.id=p.task.id  where p.id in (select p1.id from Participate p1 inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id";
		String sqlCount = "select count(distinct d.id) from LetterOutDocument d inner join Participate p on d.task.id=p.task.id where p.id in (select p1.id from Participate p1 inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id";

		String whereQuery = " where (sdtr.user.id=:userId))";// Hết đoạn select in

		sql += whereQuery;
		sqlCount += whereQuery;
		if (search != null) {
			sql += " and (d.docOriginCode like :search or d.briefNote like :search)";
			sqlCount += " and (d.docOriginCode like :search or d.briefNote like :search)";
		}

		Query q = manager.createQuery(sql, LetterOutDocumentDto.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);

		q.setParameter("userId", userId);
		qCount.setParameter("userId", userId);

		if (search != null) {
			String converSearch = "%" + search + "%";
			q.setParameter("search", converSearch);
			qCount.setParameter("search", converSearch);
		}

		List<LetterOutDocumentDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<LetterOutDocumentDto> page = new PageImpl<LetterOutDocumentDto>(list, pageable, total);
		return page;
	}

	@Override
	public Page<LetterOutDocumentDto> getAllLetterOutDocumentBy(int stepIndex, Integer currentParticipateState,
			int pageIndex, int pageSize) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		Long userId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			userId = modifiedUser.getId();
		}
		TaskFlowStep flowStep = null;
		if (stepIndex >= 0) {
			for (TaskFlowStep tfs : LetterConstant.LetterOutDocumentFlow.getSteps()) {
				if (tfs.getOrderIndex() == stepIndex) {
					flowStep = tfs;
				}
			}
		}
		Long stepId = null;
		if (flowStep != null && flowStep.getStep() != null) {
			stepId = flowStep.getStep().getId();
		}
		return getAllLetterOutDocumentBy(userId, LetterConstant.LetterOutDocumentFlow.getId(), stepId,
				currentParticipateState, pageIndex, pageSize);
	}

	private Page<LetterOutDocumentDto> getAllLetterOutDocumentBy(Long userId, Long flowId, Long stepId,
			Integer currentParticipateState, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		List<Long> listTaskOwnerIds;
		String ownerQuery = "select utw.taskOwner.id from UserTaskOwner utw where utw.user.id=:userId";

		Query qOwnerList = manager.createQuery(ownerQuery, Long.class);
		qOwnerList.setParameter("userId", userId);
		listTaskOwnerIds = qOwnerList.getResultList();


		if(listTaskOwnerIds != null && listTaskOwnerIds.size() > 0) {
			String sqlTaskDocQuery = "from LetterOutDocument d where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)";
			String sqlCountTaskDocQuery = "select count(d.id) from LetterOutDocument d  where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)";
	
			String pParticipateCondition = "";
			String dDocumentCondition = "";
	
			if (stepId != null) {
				pParticipateCondition += " and (p.task.currentStep.id=:stepId)";
			}
	
			if (flowId != null) {
				pParticipateCondition += " and p.task.flow.id=:flowId";
			}
			if (currentParticipateState != null && currentParticipateState >= 0) {
				pParticipateCondition += " and p.currentState=:currentParticipateState";
			}
			sqlTaskDocQuery += pParticipateCondition + ")";
			sqlCountTaskDocQuery += pParticipateCondition + ")";
	
			/*
			 * if(searchDocumentDto != null &&
			 * StringUtils.hasText(searchDocumentDto.getText())) { dDocumentCondition +=
			 * " and (d.docOriginCode like :search or d.briefNote like :search)";
			 * System.out.println("text: " + searchDocumentDto.getText()); }
			 */
	
			sqlTaskDocQuery += dDocumentCondition;
			sqlCountTaskDocQuery += dDocumentCondition;
			String sqlOrderBy = "";
			sqlOrderBy = " order by d.task.currentStep.code ASC, d.deliveredDate DESC";
			sqlTaskDocQuery += sqlOrderBy;
			Query qTaskDocQuery = manager.createQuery(sqlTaskDocQuery, LetterOutDocument.class);
			Query qCountTaskDocQuery = manager.createQuery(sqlCountTaskDocQuery, Long.class);
	
			if (stepId != null) {
				qTaskDocQuery.setParameter("stepId", stepId);
				qCountTaskDocQuery.setParameter("stepId", stepId);
			}
			if (flowId != null) {
				qTaskDocQuery.setParameter("flowId", flowId);
				qCountTaskDocQuery.setParameter("flowId", flowId);
			}
	
			if (currentParticipateState != null && currentParticipateState >= 0) {
				qTaskDocQuery.setParameter("currentParticipateState", currentParticipateState);
				qCountTaskDocQuery.setParameter("currentParticipateState", currentParticipateState);
			}
	
			/*
			 * if(searchDocumentDto != null &&
			 * StringUtils.hasText(searchDocumentDto.getText())) { String converSearch =
			 * "%"+searchDocumentDto.getText()+"%"; qTaskDocQuery.setParameter("search",
			 * converSearch); qCountTaskDocQuery.setParameter("search", converSearch); }
			 */
	
			qTaskDocQuery.setParameter("listTaskOwnerIds", listTaskOwnerIds);
			qCountTaskDocQuery.setParameter("listTaskOwnerIds", listTaskOwnerIds);
			qTaskDocQuery.setFirstResult(startPosition);
			qTaskDocQuery.setMaxResults(pageSize);
			long totalDoc = (Long) qCountTaskDocQuery.getSingleResult();
			List<LetterOutDocument> listDoc = qTaskDocQuery.getResultList();
	
			// phân quyền trên server ()
			List<LetterOutDocumentDto> listDocDTO = new ArrayList<LetterOutDocumentDto>();
	
			for (int i = 0; i < listDoc.size(); i++) {
				Boolean hasClerkRole = false;
				Boolean hasFowardRole = false;
				Boolean hasAssignerRole = false;
				Boolean hasChairmanRole = false;
				Boolean hasProcessRole = false;
				Boolean hasManagerRole = false;
				Boolean hasDraftersRole = false;
				LetterOutDocument d = listDoc.get(i);
				LetterOutDocumentDto docDto = new LetterOutDocumentDto(d);
	
				if (d.getTask() != null && d.getTask().getParticipates() != null
						&& d.getTask().getParticipates().size() > 0) {
					for (Participate p : d.getTask().getParticipates()) {
						if (p.getTaskOwner() != null && p.getTaskOwner().getUserTaskOwners() != null
								&& p.getTaskOwner().getUserTaskOwners().size() > 0) {
							for (UserTaskOwner uto : p.getTaskOwner().getUserTaskOwners()) {
	
								// check quyền văn thư
								if (!hasClerkRole) {
									Boolean clerkRole = p.getRole().getCode().equals(LetterConstant.ClerkRole)
											&& (uto.getUser().getId().equals(userId));
									hasClerkRole = hasClerkRole || clerkRole;
								}
	
								// check quyền chuyên viên
								if (!hasDraftersRole) {
									Boolean draftersRole = p.getRole().getCode().equals(LetterConstant.DraftersRole)
											&& (uto.getUser().getId().equals(userId));
									hasDraftersRole = hasDraftersRole || draftersRole;
								}
	
								// check quyền phân luồng
								if (!hasFowardRole) {
									Boolean fowardRole = p.getRole().getCode().equals(LetterConstant.FowardRole)
											&& (uto.getUser().getId().equals(userId));
									hasFowardRole = hasFowardRole || fowardRole;
								}
	
								// check quyền phân luồng
								if (!hasManagerRole) {
									Boolean ManagerRole = p.getRole().getCode().equals(LetterConstant.ManagerRole)
											&& (uto.getUser().getId().equals(userId));
									hasManagerRole = hasManagerRole || ManagerRole;
								}
	
								// check quyền giao xử lý
								if (!hasAssignerRole) {
									Boolean assignerRole = p.getRole().getCode().equals(LetterConstant.AssignerRole)
											&& (uto.getUser().getId().equals(userId));
									hasAssignerRole = hasAssignerRole || assignerRole;
								}
	
								// check quyền chủ trì
								if (!hasChairmanRole) {
									Boolean chairmanRole = p.getRole().getCode().equals(LetterConstant.ChairmanRole)
											&& (uto.getUser().getId().equals(userId));
									hasChairmanRole = hasChairmanRole || chairmanRole;
								}
	
								// check quyền tham gia
								if (!hasProcessRole) {
									Boolean processRole = p.getRole().getCode().equals(LetterConstant.ProcessRole)
											&& (uto.getUser().getId().equals(userId));
									hasProcessRole = hasProcessRole || processRole;
								}
							}
						}
					}
				}
	
				docDto.setHasDraftersRole(hasDraftersRole);
				docDto.setHasClerkRole(hasClerkRole);
				docDto.setHasFowardRole(hasFowardRole);
				docDto.setHasAssignerRole(hasAssignerRole);
				docDto.setHasChairmanRole(hasChairmanRole);
				docDto.setHasProcessRole(hasProcessRole);
				docDto.setHasManagerRole(hasManagerRole);
	
				listDocDTO.add(docDto);
			}
			PageImpl<LetterOutDocumentDto> page = new PageImpl<LetterOutDocumentDto>(listDocDTO, pageable, totalDoc);
			return page;
		}
		return null;
	}

	@Override
	public LetterOutDocumentDto getLetterOutDocumentByTaskId(Long taskId) {

		LetterOutDocumentDto outDocument = letterOutDocumentRepository.getletterOutDocumentByTaskId(taskId);
		if (outDocument != null) {
			return this.getLetterOutDocumentById(outDocument.getId());
		}
		return null;
	}

	@Override
	public Page<LetterOutDocumentDto> getLetterByString(int stepIndex, int currentParticipateState,
			SearchDocumentDto searchDocumentDto, int pageIndex, int pageSize) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long userId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
//			if (modifiedUser.getPerson() != null) {
//				userId = modifiedUser.getId();
//			}
			userId = modifiedUser.getId();
		}
		TaskFlowStep flowStep = null;
		if (stepIndex >= 0) {
			for (TaskFlowStep tfs : LetterConstant.LetterOutDocumentFlow.getSteps()) {
				if (tfs.getOrderIndex() == stepIndex) {
					flowStep = tfs;
				}
			}
		}
		Long stepId = null;
		if (flowStep != null && flowStep.getStep() != null) {
			stepId = flowStep.getStep().getId();
		}

		return searchByText(userId, LetterConstant.LetterOutDocumentFlow.getId(), stepId, currentParticipateState,
				searchDocumentDto, pageSize, pageIndex);

	}

	@Override
	public int saveListLetterOutDocument(List<LetterOutDocumentDto> dtos) {
		int ret = 0;
		for (int i = 0; i < dtos.size(); i++) {
			saveLetterOutDocument(dtos.get(i));
			ret++;
		}
		return ret;
	}

	private Page<LetterOutDocumentDto> searchByText(Long userId, Long flowId, Long stepId,
			Integer currentParticipateState, SearchDocumentDto searchDocumentDto, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		List<Long> listTaskOwnerIds;
		String ownerQuery = "select utw.taskOwner.id from UserTaskOwner utw where utw.user.id=:userId";

		Query qOwnerList = manager.createQuery(ownerQuery, Long.class);
		qOwnerList.setParameter("userId", userId);
		listTaskOwnerIds = qOwnerList.getResultList();

		if(listTaskOwnerIds != null && listTaskOwnerIds.size() > 0) {
			String sqlTaskDocQuery = "from LetterOutDocument d where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)";
			String sqlCountTaskDocQuery = "select count(d.id) from LetterOutDocument d  where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)";
	
			String pParticipateCondition = "";
			String dDocumentCondition = "";
	
			if (stepId != null) {
				pParticipateCondition += " and (p.task.currentStep.id=:stepId)";
			}
	
			if (flowId != null) {
				pParticipateCondition += " and p.task.flow.id=:flowId";
			}
			if (currentParticipateState != null && currentParticipateState >= 0) {
				pParticipateCondition += " and p.currentState=:currentParticipateState";
			}
	
			sqlTaskDocQuery += pParticipateCondition + ")";
			sqlCountTaskDocQuery += pParticipateCondition + ")";
	
			if (searchDocumentDto != null && StringUtils.hasText(searchDocumentDto.getText())) {
				dDocumentCondition += " and (d.docOriginCode like :search or d.briefNote like :search or d.refDocOriginCode like :search)";
				System.out.println("text: " + searchDocumentDto.getText());
			}
			if (searchDocumentDto != null && searchDocumentDto.getDocumentTypeId() != null
					&& searchDocumentDto.getDocumentTypeId() > 0) {
				dDocumentCondition += " and (d.letterDocumentType.id=:documentTypeId)";
			}
	
			if (searchDocumentDto != null && searchDocumentDto.getLetterDocFieldId() != null
					&& searchDocumentDto.getLetterDocFieldId() > 0) {
				dDocumentCondition += " and (d.letterDocField.id=:letterDocFieldId)";
			}
			if (searchDocumentDto.getIssueOrgan() != null) {
				dDocumentCondition += " and (d.issueOrgan.id=:issueOrganId)";
			}
			if (searchDocumentDto != null && StringUtils.hasText(searchDocumentDto.getOtherIssueOrgan())) {
				dDocumentCondition += " and (d.issueOrgan.name like :otherIssueOrgan)";
			}
			if (StringUtils.hasText(searchDocumentDto.getOtherIssueOrgan())) {
				dDocumentCondition += " and (d.issueOrgan.name like :otherIssueOrgan)";
			}
			if (searchDocumentDto.getDateFrom() != null && searchDocumentDto.getDateTo() == null) {
				dDocumentCondition += " and d.deliveredDate >=:dateFrom";
			}
			if (searchDocumentDto.getDateFrom() == null && searchDocumentDto.getDateTo() != null) {
				dDocumentCondition += " and d.deliveredDate <=:dateTo";
			}
			if (searchDocumentDto.getDateFrom() != null && searchDocumentDto.getDateTo() != null) {
				dDocumentCondition += " and d.deliveredDate >=:dateFrom and d.deliveredDate <=:dateTo";
			}
	
			sqlTaskDocQuery += dDocumentCondition;
			sqlCountTaskDocQuery += dDocumentCondition;
			String sqlOrderBy = "";
			sqlOrderBy = " order by d.task.currentStep.code ASC, d.deliveredDate DESC";
	//		sqlOrderBy = " order by d.deliveredDate DESC";
			sqlTaskDocQuery += sqlOrderBy;
			Query qTaskDocQuery = manager.createQuery(sqlTaskDocQuery, LetterOutDocument.class);
			Query qCountTaskDocQuery = manager.createQuery(sqlCountTaskDocQuery, Long.class);
	
			if (stepId != null) {
				qTaskDocQuery.setParameter("stepId", stepId);
				qCountTaskDocQuery.setParameter("stepId", stepId);
			}
			if (flowId != null) {
				qTaskDocQuery.setParameter("flowId", flowId);
				qCountTaskDocQuery.setParameter("flowId", flowId);
			}
	
			if (currentParticipateState != null && currentParticipateState >= 0) {
				qTaskDocQuery.setParameter("currentParticipateState", currentParticipateState);
				qCountTaskDocQuery.setParameter("currentParticipateState", currentParticipateState);
			}
	
			if (searchDocumentDto != null && StringUtils.hasText(searchDocumentDto.getText())) {
				String converSearch = "%" + searchDocumentDto.getText() + "%";
				qTaskDocQuery.setParameter("search", converSearch);
				qCountTaskDocQuery.setParameter("search", converSearch);
			}
	
			if (searchDocumentDto != null && searchDocumentDto != null && searchDocumentDto.getDocumentTypeId() != null
					&& searchDocumentDto.getDocumentTypeId() > 0) {
				qTaskDocQuery.setParameter("documentTypeId", searchDocumentDto.getDocumentTypeId());
				qCountTaskDocQuery.setParameter("documentTypeId", searchDocumentDto.getDocumentTypeId());
			}
			if (searchDocumentDto != null && searchDocumentDto.getLetterDocFieldId() != null
					&& searchDocumentDto.getLetterDocFieldId() > 0) {
				qTaskDocQuery.setParameter("letterDocFieldId", searchDocumentDto.getLetterDocFieldId());
				qCountTaskDocQuery.setParameter("letterDocFieldId", searchDocumentDto.getLetterDocFieldId());
			}
			if (searchDocumentDto.getIssueOrgan() != null) {
				qTaskDocQuery.setParameter("issueOrganId", searchDocumentDto.getIssueOrgan());
				qCountTaskDocQuery.setParameter("issueOrganId", searchDocumentDto.getIssueOrgan());
			}
			if (searchDocumentDto != null && StringUtils.hasText(searchDocumentDto.getOtherIssueOrgan())) {
				String converSearch = "%" + searchDocumentDto.getOtherIssueOrgan() + "%";
				qTaskDocQuery.setParameter("otherIssueOrgan", converSearch);
				qCountTaskDocQuery.setParameter("otherIssueOrgan", converSearch);
			}
			if (searchDocumentDto.getDateFrom() != null && searchDocumentDto.getDateTo() == null) {
				qTaskDocQuery.setParameter("dateFrom", getStartOfDay(searchDocumentDto.getDateFrom()));
				qCountTaskDocQuery.setParameter("dateFrom", getStartOfDay(searchDocumentDto.getDateFrom()));
			}
			if (searchDocumentDto.getDateFrom() == null && searchDocumentDto.getDateTo() != null) {
				qTaskDocQuery.setParameter("dateTo", getEndOfDay(searchDocumentDto.getDateTo()));
				qCountTaskDocQuery.setParameter("dateTo", getEndOfDay(searchDocumentDto.getDateTo()));
			}
			if (searchDocumentDto.getDateFrom() != null && searchDocumentDto.getDateTo() != null) {
				qTaskDocQuery.setParameter("dateFrom", getStartOfDay(searchDocumentDto.getDateFrom()));
				qCountTaskDocQuery.setParameter("dateFrom", getStartOfDay(searchDocumentDto.getDateFrom()));
	
				qTaskDocQuery.setParameter("dateTo", getEndOfDay(searchDocumentDto.getDateTo()));
				qCountTaskDocQuery.setParameter("dateTo", getEndOfDay(searchDocumentDto.getDateTo()));
			}
	
			qTaskDocQuery.setParameter("listTaskOwnerIds", listTaskOwnerIds);
			qCountTaskDocQuery.setParameter("listTaskOwnerIds", listTaskOwnerIds);
			qTaskDocQuery.setFirstResult(startPosition);
			qTaskDocQuery.setMaxResults(pageSize);
			long totalDoc = (Long) qCountTaskDocQuery.getSingleResult();
			List<LetterOutDocument> listDoc = qTaskDocQuery.getResultList();
	
			// phân quyền trên server ()
			List<LetterOutDocumentDto> listDocDTO = new ArrayList<LetterOutDocumentDto>();
	
			for (int i = 0; i < listDoc.size(); i++) {
				Boolean hasClerkRole = false;
				Boolean hasFowardRole = false;
				Boolean hasAssignerRole = false;
				Boolean hasChairmanRole = false;
				Boolean hasProcessRole = false;
				Boolean hasManagerRole = false;
				Boolean hasDraftersRole = false;
				LetterOutDocument d = listDoc.get(i);
				LetterOutDocumentDto docDto = new LetterOutDocumentDto(d);
	
				if (d.getTask() != null && d.getTask().getParticipates() != null
						&& d.getTask().getParticipates().size() > 0) {
					for (Participate p : d.getTask().getParticipates()) {
						if (p.getTaskOwner() != null && p.getTaskOwner().getUserTaskOwners() != null
								&& p.getTaskOwner().getUserTaskOwners().size() > 0) {
							for (UserTaskOwner uto : p.getTaskOwner().getUserTaskOwners()) {
	
								// check quyền văn thư
								if (!hasClerkRole) {
									Boolean clerkRole = p.getRole().getCode().equals(LetterConstant.ClerkRole)
											&& (uto.getUser().getId().equals(userId));
									hasClerkRole = hasClerkRole || clerkRole;
								}
	
								// check quyền chuyên viên
								if (!hasDraftersRole) {
									Boolean draftersRole = p.getRole().getCode().equals(LetterConstant.DraftersRole)
											&& (uto.getUser().getId().equals(userId));
									hasDraftersRole = hasDraftersRole || draftersRole;
								}
	
								// check quyền phân luồng
								if (!hasFowardRole) {
									Boolean fowardRole = p.getRole().getCode().equals(LetterConstant.FowardRole)
											&& (uto.getUser().getId().equals(userId));
									hasFowardRole = hasFowardRole || fowardRole;
								}
	
								// check quyền phân luồng
								if (!hasManagerRole) {
									Boolean ManagerRole = p.getRole().getCode().equals(LetterConstant.ManagerRole)
											&& (uto.getUser().getId().equals(userId));
									hasManagerRole = hasManagerRole || ManagerRole;
								}
	
								// check quyền giao xử lý
								if (!hasAssignerRole) {
									Boolean assignerRole = p.getRole().getCode().equals(LetterConstant.AssignerRole)
											&& (uto.getUser().getId().equals(userId));
									hasAssignerRole = hasAssignerRole || assignerRole;
								}
	
								// check quyền chủ trì
								if (!hasChairmanRole) {
									Boolean chairmanRole = p.getRole().getCode().equals(LetterConstant.ChairmanRole)
											&& (uto.getUser().getId().equals(userId));
									hasChairmanRole = hasChairmanRole || chairmanRole;
								}
	
								// check quyền tham gia
								if (!hasProcessRole) {
									Boolean processRole = p.getRole().getCode().equals(LetterConstant.ProcessRole)
											&& (uto.getUser().getId().equals(userId));
									hasProcessRole = hasProcessRole || processRole;
								}
							}
						}
					}
				}
	
				docDto.setHasDraftersRole(hasDraftersRole);
				docDto.setHasClerkRole(hasClerkRole);
				docDto.setHasFowardRole(hasFowardRole);
				docDto.setHasAssignerRole(hasAssignerRole);
				docDto.setHasChairmanRole(hasChairmanRole);
				docDto.setHasProcessRole(hasProcessRole);
				docDto.setHasManagerRole(hasManagerRole);
	
				listDocDTO.add(docDto);
			}
			PageImpl<LetterOutDocumentDto> page = new PageImpl<LetterOutDocumentDto>(listDocDTO, pageable, totalDoc);
			return page;
		}
		return null;
	}

	@Override
	public ResponseDto backToPreviousStep(Long documentId, TaskCommentDto commentDto) {

		ResponseDto ret = new ResponseDto();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterOutDocument document = findById(documentId);
		// Tạm thời harcode để tìm được role người trả về

		TaskStep currentStep = document.getTask().getCurrentStep();
		TaskStep previousStep = document.getTask().getCurrentStep();
		for (TaskFlowStep flowStep : document.getTask().getFlow().getSteps()) {
			TaskStep step = flowStep.getStep();
			if (step.getId() == currentStep.getId()) {
				break;
			}
			previousStep = flowStep.getStep();
		}
		Participate participate = null;

		for (Participate p : document.getTask().getParticipates()) {
			for (UserTaskOwner uto : p.getTaskOwner().getUserTaskOwners()) {
				if (uto.getUser().getId().equals(modifiedUser.getId())) {
					participate = p;
					break;
				}
			}
		}
		if (participate != null) {
			if (previousStep != null) {
				document.getTask().setCurrentStep(previousStep);// Đặt lại bước trước
				TaskComment comment = null;
				if (commentDto != null) {
					if (commentDto.getId() != null) {
						comment = taskCommentService.findById(commentDto.getId());
					}
					if (comment == null) {
						comment = new TaskComment();
						comment.setCreateDate(currentDate);
						comment.setCreatedBy(currentUserName);
						comment.setParticipate(participate);
						comment.setUserName(currentUserName);
					}
					comment.setComment(commentDto.getComment());
					participate.getComments().add(comment);
					letterOutDocumentRepository.save(document);
					ret.setIsSuccess(true);
				}
			}

		}

		return ret;
	}

	@Override
	public LetterOutDocumentDto transferLetterOutDocument1(LetterOutDocumentDto dto, Long docClerkOwnerId,
			Long fowarderId, Long leaderId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		if (dto.getTask() == null) {
			dto.setTask(new TaskDto());
			/*
			 * Tạo danh sách những người tham gia xử lý trong đó người đầu tiên là Văn thư
			 */
			dto.getTask().setParticipates(new HashSet<ParticipateDto>());
			ParticipateDto docClerk = new ParticipateDto();// Tạo mới đối tượng văn thư
			// Đặt loại tham gia xử lý là khác (không xác định là người hay phòng ban)
			docClerk.setParticipateType(TaskManConstant.TaskOwnerTypeEnum.OtherType.getValue());
			// Tìm vai trò văn thư
			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ClerkRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docClerkOwner = null;
//				if (docClerkOwnerId == null || docClerkOwnerId <= 0) {
				List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerByRoleCode(role.getCode());
				if (taskOwners != null && taskOwners.size() > 0) {
					docClerkOwner = taskOwners.get(0);
				}
//				} else {
//					docClerkOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
//				}
				docClerk.setRole(role);
				docClerk.setTaskOwner(docClerkOwner);
			}
			// Thêm Văn thư vào danh sách những người tham gia
			dto.getTask().getParticipates().add(docClerk);
		}
		/*
		 * Vì là quy trình xử lý văn bản đến nên Flow là Xử lý văn bản đến
		 */
		dto.getTask().setFlow(new TaskFlowDto(LetterConstant.LetterOutDocumentFlow));

		String stepCode = LetterConstant.LetterOutDocumentFlowCode + "3";
		TaskStepDto toStep = taskStepService.getTaskStepByCode(stepCode);
		TaskFlowStepDto step = null;
		step = dto.getTask().getFlow().getSteps().get(0);// Tìm bước đầu tiên của quy trình xử lý văn bản
		if (step != null) {
			dto.getTask().setCurrentStep(toStep);
		}

		// Nếu được Forward luôn đến người phân luồng thì assignerId sẽ khác NULL
		if (fowarderId != null && fowarderId > 0) {
			// Tìm role tương ứng với vai trò người phân luồng
			ParticipateDto fowarder = null;
			if (dto.getTask() != null && dto.getTask().getParticipates() != null) {
				for (ParticipateDto p : dto.getTask().getParticipates()) {
					if (p.getRole() != null && p.getRole().getCode().equals(LetterConstant.ManagerRole)) {
						if (p.getTaskOwner().getId() == fowarderId) {
							if (fowarder == null) {
								fowarder = p;
								break;
							}
						}
					}
				}
			}

			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ManagerRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docFowardOwner = taskOwnerService.getTaskOwner(fowarderId);
				if (fowarder == null) {
					fowarder = new ParticipateDto();
					fowarder.setRole(role);
					fowarder.setTaskOwner(docFowardOwner);
					dto.getTask().getParticipates().add(fowarder);
				}
			}
		}

		// Thêm người ký văn bản
		if (leaderId != null && leaderId > 0) {
			ParticipateDto leader = null;
			if (dto.getTask() != null && dto.getTask().getParticipates() != null) {
				for (ParticipateDto p : dto.getTask().getParticipates()) {
					if (p.getRole() != null && p.getRole().getCode().equals(LetterConstant.FowardRole)) {
						if (p.getTaskOwner().getId() == leaderId) {
							if (leader == null) {
								leader = p;
								break;
							}
						}
					}
				}
			}

			List<TaskRoleDto> fowardRoles = taskRoleService.getTaskRoleByCodes(LetterConstant.FowardRole);
			if (fowardRoles != null && fowardRoles.size() > 0) {
				TaskRoleDto fowardRole = fowardRoles.get(0);
				TaskOwnerDto fowardOwer = taskOwnerService.getTaskOwner(leaderId);
				if (leader == null) {
					leader = new ParticipateDto();
					leader.setRole(fowardRole);
					leader.setTaskOwner(fowardOwer);
					dto.getTask().getParticipates().add(leader);
				}
			}
		}

		return saveLetterOutDocument(dto);
	}

	@Override
	public List<ReportLetterByStepDto> getReportLetterByStep() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();

			List<Long> listTaskOwnerIds;
			String ownerQuery = "select utw.taskOwner.id from UserTaskOwner utw where utw.user.id=:userId";

			Query qOwnerList = manager.createQuery(ownerQuery, Long.class);
			qOwnerList.setParameter("userId", modifiedUser.getId());
			listTaskOwnerIds = qOwnerList.getResultList();

			if(listTaskOwnerIds!=null && listTaskOwnerIds.size() > 0) {
				//Đếm văn bản theo người liên quan
				String sqlCount = "select count(d.id) from LetterOutDocument d  where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds) and (p.task.currentStep.id=:stepId))";
				//Đếm văn bản không theo người liên quan
	//			String sqlCount = "select count(d.id) from LetterOutDocument d where d.task.id in (select p.task.id from Participate p where (p.task.currentStep.id=:stepId))";
				Query qCount = manager.createQuery(sqlCount, Long.class);
	
				List<ReportLetterByStepDto> list = new ArrayList<ReportLetterByStepDto>();
				List<Long> step = new ArrayList<Long>();
				step.add(LetterConstant.LetterOutStep1);
				step.add(LetterConstant.LetterOutStep2);
				step.add(LetterConstant.LetterOutStep3);
				step.add(LetterConstant.LetterOutStep4);
	
				if (step != null && step.size() > 0) {
					for (Long a : step) {
						ReportLetterByStepDto dto = new ReportLetterByStepDto();
						if (a == LetterConstant.LetterOutStep1) {
							qCount.setParameter("stepId", LetterConstant.LetterOutStep1);
							dto.setStep(LetterConstant.LetterOutStep1);
						}
						if (a == LetterConstant.LetterOutStep2) {
							qCount.setParameter("stepId", LetterConstant.LetterOutStep2);
							dto.setStep(LetterConstant.LetterOutStep2);
						}
						if (a == LetterConstant.LetterOutStep3) {
							qCount.setParameter("stepId", LetterConstant.LetterOutStep3);
							dto.setStep(LetterConstant.LetterOutStep3);
						}
						if (a == LetterConstant.LetterOutStep4) {
							qCount.setParameter("stepId", LetterConstant.LetterOutStep4);
							dto.setStep(LetterConstant.LetterOutStep4);
						}
	
						qCount.setParameter("listTaskOwnerIds", listTaskOwnerIds);
						Long quantity = (Long) qCount.getSingleResult();
	
						dto.setQuantity(quantity);
						list.add(dto);
					}
				}
				return list;
			}
		}
		return null;
	}

	@Override
	public LetterOutDocumentDto saveLetterOut(LetterOutDocumentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();

			if (dto.getTask() == null) {
				dto.setTask(new TaskDto());
			}

			// add văn thư
			/*
			 * tạo danh sách những người liên quan đến văn bản đi đó. mặc định sẽ có Văn
			 * thư.
			 */
			dto.getTask().setParticipates(new HashSet<ParticipateDto>());
			ParticipateDto docClerk = new ParticipateDto();// Tạo mới đối tượng văn thư
			// Đặt loại tham gia xử lý là khác (không xác định là người hay phòng ban)
			docClerk.setParticipateType(TaskManConstant.TaskOwnerTypeEnum.OtherType.getValue());
			// Tìm vai trò văn thư
			List<TaskRoleDto> taskRoles = taskRoleService.getTaskRoleByCodes(LetterConstant.ClerkRole);
			if (taskRoles != null && taskRoles.size() > 0) {
				TaskRoleDto role = taskRoles.get(0);
				TaskOwnerDto docClerkOwner = null;
				List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerDtoByRole(role.getId());
				if (taskOwners != null && taskOwners.size() > 0) {
					docClerkOwner = taskOwners.get(0);
				}
				docClerk.setRole(role);
				docClerk.setTaskOwner(docClerkOwner);
			}
			// thêm Văn thư vào participate
			dto.getTask().getParticipates().add(docClerk);
			
			// add người ký vào văn bản
			if (dto.getSigner() != null) {
				ParticipateDto signer = null;
				List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.FowardRole);
				if (roles != null && roles.size() > 0) {
					TaskRoleDto role = roles.get(0);
					if (signer == null) {
						signer = new ParticipateDto();
						signer.setRole(role);
						signer.setTaskOwner(dto.getSigner());
						dto.getTask().getParticipates().add(signer);
					}
				}
			}
	
			// add chánh văn phòng vào văn bản
			if (dto.getChiefOfStaff() != null) {
				ParticipateDto chiefOfStaff = null;
				List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ChiefOfStaffRole);
				if (roles != null && roles.size() > 0) {
					TaskRoleDto role = roles.get(0);
					if (chiefOfStaff == null) {
						chiefOfStaff = new ParticipateDto();
						chiefOfStaff.setRole(role);
						chiefOfStaff.setTaskOwner(dto.getChiefOfStaff());
						dto.getTask().getParticipates().add(chiefOfStaff);
					}
				}
			}

			// add đơn vị soạn thảo vào văn bản(người đại diện)
			if (dto.getEditorUnit() != null) {
				ParticipateDto editorUnit = null;
				List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ManagerRole);
				if (roles != null && roles.size() > 0) {
					TaskRoleDto role = roles.get(0);
					if (editorUnit == null) {
						editorUnit = new ParticipateDto();
						editorUnit.setRole(role);
						editorUnit.setTaskOwner(dto.getEditorUnit());
						dto.getTask().getParticipates().add(editorUnit);
					}
				}
			}
			
			/*
			 * tìm flow là xử lý văn bản đi
			 */
			dto.getTask().setFlow(new TaskFlowDto(LetterConstant.LetterOutDocumentFlow));
			TaskFlowStepDto firstStep = null;
			firstStep = dto.getTask().getFlow().getSteps().get(0);
			TaskStepDto fowardStep = dto.getTask().getFlow().getSteps().get(1).getStep();
			if (firstStep != null) {
				dto.getTask().setCurrentStep(firstStep.getStep());
			}
			return saveLetterOutDocument(dto);
		}
		return null;
	}
	public static Date getEndOfDay(Date date) {
		if(date!=null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.set(Calendar.HOUR_OF_DAY, 23);
		    calendar.set(Calendar.MINUTE, 59);
		    calendar.set(Calendar.SECOND, 59);
		    calendar.set(Calendar.MILLISECOND, 999);
		    return calendar.getTime();
		}
	    return null;
	}
	
	public static Date getStartOfDay(Date date) {
		if(date!=null) {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.set(Calendar.HOUR_OF_DAY, 00);
		    calendar.set(Calendar.MINUTE, 00);
		    calendar.set(Calendar.SECOND, 00);
		    calendar.set(Calendar.MILLISECOND, 000);
		    return calendar.getTime();
		}
	    return null;
	}
	@Override
	public List<LetterOutDocumentDto> searchLetterOutDto(SearchDocumentDto searchDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = null;
		if (authentication != null) {
			currentUser = (User) authentication.getPrincipal();
		}
		if(currentUser != null) {
			String sql = " select new com.globits.letter.dto.LetterOutDocumentDto(outdoc) from LetterOutDocument outdoc where (1=1)";	
			String whereClause ="";
			if(searchDto != null) {
				if (StringUtils.hasText(searchDto.getText())) {
					whereClause += " and (outdoc.docOriginCode like :text or outdoc.briefNote like :text or outdoc.otherIssueOrgan like :text)";
				}
//				if(searchDto.getDocumentTypeId()!=null) {//lọc theo loại văn bản
//					whereClause += " and outdoc.letterDocumentType.id = :documentTypeId";
//				}
//				if(searchDto.getLetterDocFieldId()!=null) {
//					whereClause += " and outdoc.letterDocFieldId.id = :letterDocFieldId";
//				}
				if(searchDto.getDateFrom()!=null) {
					whereClause += " and outdoc.deliveredDate >= :dateFrom";
				}
				if(searchDto.getDateTo()!=null) {
					whereClause += " and outdoc.deliveredDate <= :dateTo";
				}
				if(searchDto.getDateFrom() != null && searchDto.getDateTo() != null) {
					whereClause += " and outdoc.deliveredDate >=:dateFrom and outdoc.deliveredDate <=:dateTo";
				}
			}
			
			sql +=whereClause;
			sql +=" order by outdoc.deliveredDate desc ";		
	
			Query q = manager.createQuery(sql,LetterOutDocumentDto.class);	
			
			if(searchDto != null) {
				if (StringUtils.hasText(searchDto.getText())) {
					String converSearch = "%" + searchDto.getText() + "%";
					q.setParameter("text", converSearch);
				}
//				if(searchDto.getDocumentTypeId()!=null) {			
//					q.setParameter("documentTypeId", searchDto.getDocumentTypeId());			
//				}
//				if(searchDto.getLetterDocFieldId()!=null) {			
//					q.setParameter("letterDocFieldId", searchDto.getLetterDocFieldId());			
//				}
				if(searchDto.getDateFrom()!=null) {
					q.setParameter("dateFrom", getStartOfDay(searchDto.getDateFrom()));			
				}
				if(searchDto.getDateTo()!=null) {
					q.setParameter("dateTo", getEndOfDay(searchDto.getDateTo()));			
				}
			}
			return q.getResultList();
		}else {
			return null;
		}
	}

//	@Override
//	public Page<LetterOutDocumentDto> getAllLetter(int pageIndex, int pageSize) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		User modifiedUser = null;
//		String currentUserName = "Unknown User";
//		Long userId = null;
//		if (authentication != null) {
//			modifiedUser = (User) authentication.getPrincipal();
//			currentUserName = modifiedUser.getUsername();
//			/*
//			 * if (modifiedUser.getPerson() != null) { userId = modifiedUser.getId(); }
//			 */
//			userId = modifiedUser.getId();
//			if (pageIndex > 1) {
//				pageIndex--;
//			} else
//				pageIndex = 0;
//			Pageable pageable = new PageRequest(pageIndex, pageSize);
//			TaskFlow flow = LetterConstant.LetterOutDocumentFlow;
//			Long step = null;
//			for (TaskFlowStep taskFlowStep : flow.getSteps()) {
//				if (step == null) {
//					step = taskFlowStep.getId();
//				}
//			}
//			return this.letterOutDocumentRepository.getAllLetter(pageable);
//		}
//		return null;
//	}

}
