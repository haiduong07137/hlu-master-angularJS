package com.globits.letter.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.core.domain.FileDescription;
import com.globits.core.domain.Organization;
import com.globits.core.repository.FileDescriptionRepository;
import com.globits.core.repository.OrganizationRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.CommonUtils;
import com.globits.hr.domain.Staff;
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
import com.globits.letter.domain.LetterInDocument;
import com.globits.letter.dto.LetterDocumentAttachmentDto;
import com.globits.letter.dto.LetterDocumentUserDto;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.ReportLetterByStepDto;
import com.globits.letter.dto.ResponseDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.dto.UserRolesDto;
import com.globits.letter.repository.LetterDocBookGroupRepository;
import com.globits.letter.repository.LetterDocBookRepository;
import com.globits.letter.repository.LetterDocFieldRepository;
import com.globits.letter.repository.LetterDocPriorityRepository;
import com.globits.letter.repository.LetterDocSecurityLevelRepository;
import com.globits.letter.repository.LetterDocumentAttachmentRepository;
import com.globits.letter.repository.LetterDocumentTypeRepository;
import com.globits.letter.repository.LetterDocumentUserRepository;
import com.globits.letter.repository.LetterInDocumentRepository;
import com.globits.letter.service.LetterInDocumentService;
import com.globits.letter.service.ViewDocumentUserService;
import com.globits.security.domain.User;
import com.globits.security.repository.UserRepository;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.TaskManConstant.ParticipateTypeEnum;
import com.globits.taskman.domain.CommentFileAttachment;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.dto.CommentFileAttachmentDto;
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
import com.globits.taskman.service.ParticipateService;
import com.globits.taskman.service.TaskCommentService;
import com.globits.taskman.service.TaskOwnerService;
import com.globits.taskman.service.TaskRoleService;
import com.globits.taskman.service.TaskService;
import com.globits.taskman.service.TaskStepService;

@Service
@Transactional
public class LetterInDocumentServiceImpl extends GenericServiceImpl<LetterInDocument, Long>
		implements LetterInDocumentService {
	@Autowired
	LetterInDocumentRepository letterInDocumentRepository;
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
	UserRepository userRepository;

	@Autowired
	ViewDocumentUserService viewDocumentUserService;
	private static final Logger LOGGER = LoggerFactory.getLogger(LetterInDocumentServiceImpl.class);

	private LetterInDocument setValue(LetterInDocumentDto dto, LetterInDocument entity, String userName,
			LocalDateTime currentDate) {
		if (entity == null) {
			entity = new LetterInDocument();
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
		entity.setSignedPost(dto.getSignedPost());
		entity.setDocumentOrderNoByBook(dto.getDocumentOrderNoByBook());
		entity.setNumberOfPages(dto.getNumberOfPages());
		
		if(dto.getTask() != null) {
			if(dto.getTask().getParticipates()!=null && dto.getTask().getParticipates().size() > 0) {
				for (ParticipateDto e : dto.getTask().getParticipates()) {
					if(e.getRole().getCode().equalsIgnoreCase(LetterConstant.ChiefOfStaffRole)) {
						TaskOwner taskOwner = taskOwnerService.findById(e.getTaskOwner().getId());
						if (taskOwner != null) {
							entity.setChiefOfStaff(taskOwner);
						}
					}
				}
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
		if (dto.getIsOtherIssueOrgan() != null && dto.getIsOtherIssueOrgan()) {
			entity.setOtherIssueOrgan(dto.getOtherIssueOrgan());
		} else {
			if (dto.getIssueOrgan() != null && dto.getIssueOrgan().getId() != null) {
				Organization issueOrgan = organizationRepository.findById(dto.getIssueOrgan().getId());
				if (issueOrgan != null)
					entity.setIssueOrgan(issueOrgan);
			}
		}

		if (dto.getLetterDocField() != null && dto.getLetterDocField().getId() != null) {
			LetterDocField letterInDocField = letterDocFieldRepository.getOne(dto.getLetterDocField().getId());
			if (letterInDocField != null)
				entity.setLetterDocField(letterInDocField);
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
	public LetterInDocumentDto findDocumentById(Long id) {
		LetterInDocument entity = letterInDocumentRepository.findOne(id);
		if (entity != null) {
			return new LetterInDocumentDto(entity);
		}
		return null;
	}

	/*
	 * Vào sổ văn bản đến với các tham số đầu vào bao gồm: LetterInDocumentDto: tham
	 * số đầu vào docClerkOwnerId: Id của taskOwner
	 */
	@Override
	public LetterInDocumentDto createLetterInDocument(LetterInDocumentDto dto, Long docClerkOwnerId, Long fowarderId) {
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
				if (docClerkOwnerId == null || docClerkOwnerId <= 0) {
					List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerDtoByRole(role.getId());
					if (taskOwners != null && taskOwners.size() > 0) {
						docClerkOwner = taskOwners.get(0);
					}
				} else {
					docClerkOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
				}
				docClerk.setRole(role);
				docClerk.setTaskOwner(docClerkOwner);
			}
			// Thêm Văn thư vào danh sách những người tham gia
			dto.getTask().getParticipates().add(docClerk);
		}
		/*
		 * Vì là quy trình xử lý văn bản đến nên Flow là Xử lý văn bản đến
		 */
		dto.getTask().setFlow(new TaskFlowDto(LetterConstant.LetterInDocumentFlow));

		TaskFlowStepDto firstStep = null;
		firstStep = dto.getTask().getFlow().getSteps().get(0);// Tìm bước đầu tiên của quy trình xử lý văn bản
		if (firstStep != null)
			dto.getTask().setCurrentStep(firstStep.getStep());
		// Nếu được Forward luôn đến người phân luồng thì fowarderId sẽ khác NULL
		if (fowarderId != null && fowarderId > 0) {
			
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

						
			// Tìm role tương ứng với vai trò người phân luồng
			ParticipateDto fowarder = null;
			if (dto.getTask() != null && dto.getTask().getParticipates() != null) {
				for (ParticipateDto p : dto.getTask().getParticipates()) {
					if (p.getRole() != null && p.getRole().getCode().equals(LetterConstant.FowardRole)) {
						if (p.getTaskOwner().getId() == fowarderId) {
							if (fowarder == null) {
								fowarder = p;
								break;
							}
						}
					}
				}

				for (ParticipateDto p : dto.getTask().getParticipates()) {
					if (p.getRole() != null && p.getRole().getCode().equals(LetterConstant.FowardRole)) {
						if (p.getTaskOwner().getId() != fowarderId) {
							dto.getTask().getParticipates().remove(p);
							break;
						}
					}
				}
			}

			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.FowardRole);
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
		return saveLetterInDocument(dto);
	}

	@Override
	public int saveListLetterInDocument(List<LetterInDocumentDto> dtos) {
		int ret = 0;
		for (int i = 0; i < dtos.size(); i++) {
			saveLetterInDocument(dtos.get(i));
			ret++;
		}
		return ret;
	}

	@Override
	public LetterInDocumentDto saveLetterInDocument(LetterInDocumentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		if (dto != null) {
			LetterInDocument entity = null;
			if (dto.getId() != null)
				entity = letterInDocumentRepository.findOne(dto.getId());
			if (entity == null) {
				entity = new LetterInDocument();
				entity.setCreateDate(currentDate);
				entity.setCreatedBy(currentUserName);
			}

			if (entity.getTask() == null) {
				entity.setTask(new Task());
			}
			entity.getTask().setFlow(LetterConstant.LetterInDocumentFlow);
			entity = setValue(dto, entity, currentUserName, currentDate);
			if (dto.getTask() != null && dto.getTask().getParticipates() != null
					&& dto.getTask().getParticipates().size() > 0) {
				TaskDto taskDto = taskService.saveTask(dto.getTask());
				Task task = taskService.findById(taskDto.getId());
				entity.setTask(task);
			}

			if (dto.getDocOriginCode() != null && StringUtils.hasText(dto.getDocOriginCode())) {
				entity.getTask().setName(LetterConstant.HardCodeTaskNameOfLetterInDocument + dto.getDocOriginCode());
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

							file = fileDescriptionRepository.save(file);
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
			entity = letterInDocumentRepository.save(entity);

			viewDocumentUserService.addView(modifiedUser.getId(), entity.getId());// add view
			return new LetterInDocumentDto(entity);
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
		LetterInDocument document = findById(documentId);
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
					letterInDocumentRepository.save(document);
					ret.setIsSuccess(true);
				}
			}

		}

		return ret;
	}

	/*
	 * Hàm này hỗ trợ việc chuyển trạng thái văn bản tới bước phân luồng văn bản.
	 */
	@Override
	public void forwardDocument(Long documentId, Long toStepId, Long toStaffId, Long roleId,
			TaskCommentDto commentDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterInDocument document = findById(documentId);
		TaskStep toStep = taskStepService.findById(toStepId);

		document.getTask().setCurrentStep(toStep);
		List<Participate> participates = participateService.getTaskParticipate(document.getTask().getId(), toStaffId,
				roleId, TaskManConstant.ParticipateTypeEnum.PersonalType.getValue());
		Participate nextStepParticipate = null;
		if (participates != null && participates.size() > 0) {
			nextStepParticipate = participates.get(0);
		}
		if (nextStepParticipate == null) {
			nextStepParticipate = new Participate();
			nextStepParticipate.setCreateDate(currentDate);
			nextStepParticipate.setCreatedBy(currentUserName);
			Staff employee = staffService.findById(toStaffId);
			nextStepParticipate.setParticipateType(TaskManConstant.ParticipateTypeEnum.PersonalType.getValue());
			nextStepParticipate.setEmployee(employee);
			if (document.getTask().getParticipates() == null) {
				document.getTask().setParticipates(new HashSet<Participate>());
			}
			document.getTask().getParticipates().add(nextStepParticipate);
		}
		if (nextStepParticipate.getRole() == null) {
			TaskRole role = taskRoleService.findById(roleId);
			nextStepParticipate.setRole(role);
		}

		nextStepParticipate.setTask(document.getTask());

		TaskComment comment = null;
		if (commentDto != null) {
			if (commentDto.getId() != null) {
				comment = taskCommentService.findById(commentDto.getId());
			}
			if (comment == null) {
				comment = new TaskComment();
				comment.setCreateDate(currentDate);
				comment.setCreatedBy(currentUserName);
				comment.setParticipate(nextStepParticipate);
				comment.setUserName(currentUserName);
			}
			comment.setComment(commentDto.getComment());
		}
		if (nextStepParticipate.getComments() == null) {
			nextStepParticipate.setComments(new HashSet<TaskComment>());
		}
		nextStepParticipate.getComments().add(comment);

		letterInDocumentRepository.save(document);
	}

	private Participate convertParticipateDtoToEntity(ParticipateDto dto, String userName, LocalDateTime currentDate) {
		Participate entity = null;
		if (dto.getId() != null) {
			entity = participateService.findById(dto.getId());
		}
		if (entity == null) {
			entity = new Participate();
		}
		entity.setParticipateType(dto.getParticipateType());
		if (dto.getTaskOwner() != null) {
			TaskOwner taskOwner = taskOwnerService.findById(dto.getTaskOwner().getId());
			entity.setTaskOwner(taskOwner);
		}
		if (dto.getTask() != null && dto.getTask().getId() != null) {
			entity.setTask(taskService.findById(dto.getTask().getId()));
		}
		if (dto.getRole() != null) {
			TaskRole role = null;
			if (dto.getRole().getId() != null) {
				role = taskRoleService.findById(dto.getRole().getId());
			} else if (dto.getRole().getCode() != null) {
				List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(dto.getRole().getCode());
				if (roles != null && roles.size() > 0) {
					role = taskRoleService.findById(roles.get(0).getId());
				}
			}
			entity.setRole(role);
		}

		if (dto.getComments() != null) {
			if (entity.getComments() == null) {// Nếu chưa có comment nào thì thêm 1 HashSet vào
				entity.setComments(new HashSet<TaskComment>());
			} else {// nếu không thì thì clear (xóa hết comment cũ) đi để thêm mới - đoạn này có thể
					// có vấn đề - cần xem lại trong quá trình chạy
				entity.getComments().clear();
			}

			for (TaskCommentDto commentDto : dto.getComments()) {
				TaskComment comment = null;
				if (commentDto.getId() != null) {
					comment = taskCommentService.findById(commentDto.getId());
				}
				if (comment == null) {
					comment = new TaskComment();
				}
				if (commentDto.getAttachments() != null && commentDto.getAttachments().size() > 0) {
					if (comment.getAttachments() == null) {
						comment.setAttachments(new HashSet<CommentFileAttachment>());
					} else {
						comment.getAttachments().clear();
					}
					for (CommentFileAttachmentDto attachDto : commentDto.getAttachments()) {
						CommentFileAttachment commentAttach = null;
						if (attachDto.getId() != null) {
							commentAttach = commentFileAttachmentRepository.findOne(attachDto.getId());
						}
						if (commentAttach == null) {
							commentAttach = new CommentFileAttachment();
							commentAttach.setCreateDate(currentDate);
							commentAttach.setCreatedBy(userName);
						}
						if (attachDto.getFile() != null && attachDto.getFile().getId() != null) {
							FileDescription file = fileDescriptionRepository.findOne(attachDto.getFile().getId());
							commentAttach.setFile(file);
						}
						commentAttach.setTaskComment(comment);
						comment.getAttachments().add(commentAttach);
					}
				}
				comment.setUserName(userName);
				comment.setComment(commentDto.getComment());
				comment.setId(commentDto.getId());
				comment.setParticipate(entity);
				// comment = taskCommentRepository.save(comment);//thêm dòng này để đảm bảo tạo
				// ra comment file
				entity.getComments().add(comment);// thêm comment vào
			}
		}
		return entity;
	}

	/*
	 * Hàm này thực hiện việc giao việc, đầu vào là 1 danh sách cá assignees phía
	 * client phải lo việc đẩy đầy đủ các thông tin assignees lên
	 */
	@Override
	public void assignTask(Long documentId, List<ParticipateDto> assignees) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long userId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			userId = modifiedUser.getId();
		}

		LetterInDocument document = findById(documentId);
		String stepCode = LetterConstant.LetterInDocumentFlowCode + "Step3";
		TaskStep toStep = taskStepService.getTaskStepEntityByCode(stepCode);
		if (document.getTask() == null) {
			document.setTask(new Task());
			document.getTask().setCreateDate(currentDate);
			document.getTask().setCreatedBy(currentUserName);
			document.getTask().setParticipates(new HashSet<Participate>());
		} else {
			if ( document.getTask().getParticipates() != null && document.getTask().getParticipates().size() > 0) {
				for (Participate p : document.getTask().getParticipates()) {
					if (p.getTaskOwner() != null && p.getTaskOwner().getUserTaskOwners() != null && p.getTaskOwner().getUserTaskOwners().size() > 0) {
						for (UserTaskOwner uto : p.getTaskOwner().getUserTaskOwners()) {
							if (uto.getUser().getId().equals(userId)) {
								p.setCurrentState(2);
							}
						}
					}
				}
			}
		}
		document.getTask().setCurrentStep(toStep);
		for (ParticipateDto pDto : assignees) {// Xử lý đoạn code để thêm người chủ trì, người tham gia, ...
			Participate p = convertParticipateDtoToEntity(pDto, currentUserName, currentDate);

			document.getTask().getParticipates().add(p);
		}

		letterInDocumentRepository.save(document);
	}

	/*
	 * Hàm này thực hiện việc giao việc, đầu vào là 1 danh sách cá assignees phía
	 * client phải lo việc đẩy đầy đủ các thông tin assignees lên
	 */
	@Override
	public void processingLetter(Long documentId, List<ParticipateDto> assignees) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterInDocument document = findById(documentId);
		String stepCode = LetterConstant.LetterInDocumentFlowCode + "Step3";
		TaskStep toStep = taskStepService.getTaskStepEntityByCode(stepCode);
		if (document.getTask() == null) {
			document.setTask(new Task());
			document.getTask().setCreateDate(currentDate);
			document.getTask().setCreatedBy(currentUserName);
		}
		document.getTask().setCurrentStep(toStep);
		for (ParticipateDto pDto : assignees) {// Xử lý đoạn code để thêm người chủ trì, người tham gia, ...
			Participate p = convertParticipateDtoToEntity(pDto, currentUserName, currentDate);
			if (document.getTask().getParticipates() == null) {
				document.getTask().setParticipates(new HashSet<Participate>());
			}
			document.getTask().getParticipates().add(p);
		}

		letterInDocumentRepository.save(document);
	}
	
	/*
	 * Lấy danh sách tất cả các công việc của 1 người bao gồm các công việc của cá
	 * nhân và của phòng ban liên quan Đầu vào : staffId : Id của người cần lấy dữ
	 * liệu currentParticipateState : Trạng thái của người tham gia xử lý công việc
	 * (có thể có các trạng thái : Chưa tham gia, đã tham gia). Nếu
	 * currentParticipateState = null sẽ lấy tất cả flowId : Id của quy trình (ví dụ
	 * : Quy trình xử lý văn bản) pageSize: Kích thước 1 trang pageIndex: Số thứ tự
	 * trang cần lấy (tính giá trị bắt đầu từ 1)
	 */
	@Override
	public Page<LetterInDocumentDto> getAllDepartmentAndPersonalTask(Long staffId, Long flowId, Long stepId,
			Integer currentParticipateState, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);

		/*
		 *****************************************************************************************************************
		 **/
		int startPosition = (pageIndex - 1) * pageSize;
		List<Long> departmentIds = new ArrayList<Long>();

		String sqlGetDep = "select distinct ps.department.id from StaffDepartmentTaskRole ps where ps.staff.id=:staffId";
		Query qGetDep = manager.createQuery(sqlGetDep, Long.class);
		qGetDep.setParameter("staffId", staffId);

		departmentIds = qGetDep.getResultList();

		String sql = "select distinct p.task from Participate p where (p.id in (select p1.id from Participate p1 inner join StaffDepartmentTaskRole sdtr on p1.role.id=sdtr.role.id and p1.department.id=sdtr.department.id";
		String sqlCount = "select count(distinct p.task.id) from Participate p where (p.id in (select p1.id from Participate p1 inner join StaffDepartmentTaskRole sdtr on p1.role.id=sdtr.role.id and p1.department.id=sdtr.department.id";

		sql = "select distinct new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d inner join Participate p on d.task.id=p.task.id  where (p.id in (select p1.id from Participate p1 inner join StaffDepartmentTaskRole sdtr on p1.role.id=sdtr.role.id and p1.department.id=sdtr.department.id";
		sqlCount = "select count(distinct d.id) from LetterInDocument d inner join Participate p on d.task.id=p.task.id where (p.id in (select p1.id from Participate p1 inner join StaffDepartmentTaskRole sdtr on p1.role.id=sdtr.role.id and p1.department.id=sdtr.department.id";

		String whereQuery = " where (sdtr.staff.id=:staffId and (p.participateType=:departmentParticipateType)";
		if (departmentIds != null && departmentIds.size() > 0)
			whereQuery += " and (p.department.id in :departmentIds)";// Kết thúc đoạn cho phòng ban
		whereQuery += "))";

		String sqlPersonal = " or (p.employee.id=:staffId and p.participateType =:personalParticipateType))";

		whereQuery += sqlPersonal;

		if (stepId != null) {
			whereQuery += " and (p.task.currentStep.id=:stepId)";
		}

		if (flowId != null) {
			whereQuery += " and p.task.flow.id=:flowId";
		}
		if (currentParticipateState != null && currentParticipateState >= 0) {
			whereQuery += " and p.currentState=:currentParticipateState";
		}
		sql += whereQuery;
		sqlCount += whereQuery;

		Query q = manager.createQuery(sql, LetterInDocumentDto.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);

		q.setParameter("staffId", staffId);
		qCount.setParameter("staffId", staffId);
		if (departmentIds != null && departmentIds.size() > 0) {
			q.setParameter("departmentIds", departmentIds);
			qCount.setParameter("departmentIds", departmentIds);
		}
		q.setParameter("departmentParticipateType", ParticipateTypeEnum.DepartmentType.getValue());
		qCount.setParameter("departmentParticipateType", ParticipateTypeEnum.DepartmentType.getValue());

		q.setParameter("personalParticipateType", ParticipateTypeEnum.PersonalType.getValue());
		qCount.setParameter("personalParticipateType", ParticipateTypeEnum.PersonalType.getValue());
		if (stepId != null) {
			q.setParameter("stepId", stepId);
			qCount.setParameter("stepId", stepId);
		}
		if (flowId != null) {
			q.setParameter("flowId", flowId);
			qCount.setParameter("flowId", flowId);
		}

		if (currentParticipateState != null && currentParticipateState >= 0) {
			q.setParameter("currentParticipateState", currentParticipateState);
			qCount.setParameter("currentParticipateState", currentParticipateState);
		}
		List<LetterInDocumentDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<LetterInDocumentDto> page = new PageImpl<LetterInDocumentDto>(list, pageable, total);
		return page;

//		Page<TaskDto> tasks = taskService.getAllDepartmentAndPersonalTask(staffId,flowId,stepId,currentParticipateState,pageSize, pageIndex);
//		if(tasks!=null && tasks.hasContent()) {
//			List<LetterInDocumentDto> contents = new ArrayList<LetterInDocumentDto>();
//			for(TaskDto task: tasks.getContent()) {
//				List<LetterInDocumentDto> documentDtos = letterInDocumentRepository.getListDocumentDtoByTask(task.getId());
//				if(documentDtos!=null && documentDtos.size()>0) {
//					LetterInDocumentDto dto = documentDtos.get(0);
//					contents.add(dto);
//				}
//			}
//			//Long total = tasks.getTotalElements();
//			PageImpl<LetterInDocumentDto> page = new PageImpl<LetterInDocumentDto>(contents,pageable,total);
//			return page;
//		}
//		return null;
	}

	@Override
	public Page<LetterInDocumentDto> getAllDepartmentAndPersonalTask(Long flowId, Long stepId,
			Integer currentParticipateState, int pageSize, int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long staffId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			if (modifiedUser.getPerson() != null) {
				staffId = modifiedUser.getPerson().getId();
			}
		}
		if (staffId != null) {
			return getAllDepartmentAndPersonalTask(staffId, flowId, currentParticipateState, pageSize, pageIndex);
		}
		return null;
	}

	@Override
	public Page<LetterInDocumentDto> getDocumentByStepIndex(int stepIndex, int pageIndex, int pageSize) {

		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long staffId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			if (modifiedUser.getPerson() != null) {
				staffId = modifiedUser.getPerson().getId();
			}
		}
		TaskFlowStep flowStep = null;

		String sql = "select distinct new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d";
		String sqlCount = "select count(distinct d.id) from LetterInDocument d";
		String sqlWhere = "";
		String sqlOrderBy = "";
		if (stepIndex >= 0) {
			for (TaskFlowStep tfs : LetterConstant.LetterInDocumentFlow.getSteps()) {
				if (tfs.getOrderIndex() == stepIndex) {
					flowStep = tfs;
				}
			}
			sqlWhere = " where d.task.currentStep.id=:stepId";
		}
		sql += sqlWhere;
		sqlCount += sqlWhere;

		/*
		 * sqlOrderBy = " order by d.task.currentStep.code ASC, d.deliveredDate DESC ";
		 * 
		 * sql += sqlOrderBy;
		 */

		Query q = manager.createQuery(sql, LetterInDocumentDto.class);
		Query qCount = manager.createQuery(sqlCount, Long.class);
		q.setMaxResults(pageSize);
		q.setFirstResult(startPosition);
		if (stepIndex >= 0) {
			q.setParameter("stepId", flowStep.getStep().getId());
			qCount.setParameter("stepId", flowStep.getStep().getId());
		}
		List<LetterInDocumentDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<LetterInDocumentDto> page = new PageImpl<LetterInDocumentDto>(list, pageable, total);
		return page;
	}

	@Override
	public Page<LetterInDocumentDto> getAllDepartmentByOwner(Integer stepIndex, Integer currentParticipateState,
			int pageSize, int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long userId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			userId = modifiedUser.getId();
		}
		TaskFlowStep flowStep = null;
		if (stepIndex >= 0) {
			for (TaskFlowStep tfs : LetterConstant.LetterInDocumentFlow.getSteps()) {
				if (tfs.getOrderIndex() == stepIndex) {
					flowStep = tfs;
				}
			}
		}
		Long stepId = null;
		if (flowStep != null && flowStep.getStep() != null) {
			stepId = flowStep.getStep().getId();
		}
//		return getAllDepartmentByOwner(userId, LetterConstant.LetterInDocumentFlow.getId(), stepId,
//				currentParticipateState, pageSize, pageIndex);

		return searchByText(userId, LetterConstant.LetterInDocumentFlow.getId(), stepId, currentParticipateState, null,
				pageSize, pageIndex);

	}

	@Override
	public Page<LetterInDocumentDto> getAllDepartmentByOwner(Long userId, Long flowId, Long stepId,
			Integer currentParticipateState, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);

		int startPosition = (pageIndex - 1) * pageSize;

		/*
		 * String sql =
		 * "select new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d inner join Participate p on d.task.id=p.task.id  where p.id in (select p1.id from Participate p1 inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id"
		 * ; String sqlCount =
		 * "select count( d.id) from LetterInDocument d inner join Participate p on d.task.id=p.task.id where p.id in (select p1.id from Participate p1 inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id"
		 * ;
		 */

		String sql = "select distinct new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d inner join Participate p on d.task.id=p.task.id  where p.id in (select p1.id from Participate p1 inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id";
		String sqlCount = "select count(distinct d.id) from LetterInDocument d inner join Participate p on d.task.id=p.task.id where p.id in (select p1.id from Participate p1 inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id";

		String whereQuery = " where (sdtr.user.id=:userId))";// Hết đoạn select in
		String sqlOrderBy = "";

		if (stepId != null) {
			whereQuery += " and (p.task.currentStep.id=:stepId)";
		}

		if (flowId != null) {
			whereQuery += " and p.task.flow.id=:flowId";
		}
		if (currentParticipateState != null && currentParticipateState >= 0) {
			whereQuery += " and p.currentState=:currentParticipateState";
		}
		sql += whereQuery;
		sqlCount += whereQuery;

		/*
		 * sqlOrderBy = " order by d.task.currentStep.code ASC, d.deliveredDate DESC ";
		 * 
		 * sql += sqlOrderBy;
		 */

		Query q = manager.createQuery(sql, LetterInDocumentDto.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);

		q.setParameter("userId", userId);
		qCount.setParameter("userId", userId);

		if (stepId != null) {
			q.setParameter("stepId", stepId);
			qCount.setParameter("stepId", stepId);
		}
		if (flowId != null) {
			q.setParameter("flowId", flowId);
			qCount.setParameter("flowId", flowId);
		}

		if (currentParticipateState != null && currentParticipateState >= 0) {
			q.setParameter("currentParticipateState", currentParticipateState);
			qCount.setParameter("currentParticipateState", currentParticipateState);
		}
		List<LetterInDocumentDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<LetterInDocumentDto> page = new PageImpl<LetterInDocumentDto>(list, pageable, total);
		return page;

	}

	@Override
	public LetterInDocumentDto getLetterInDocumentById(Long id) {
		LetterInDocumentDto letterInDocumentDto = letterInDocumentRepository.getLetterIndocumentDtoById(id);
		if (letterInDocumentDto != null && letterInDocumentDto.getTask() != null
				&& letterInDocumentDto.getTask().getParticipates() != null
				&& letterInDocumentDto.getTask().getParticipates().size() > 0) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User modifiedUser = null;
			LocalDateTime currentDate = LocalDateTime.now();
			String currentUserName = "Unknown User";
			Long userId = null;
			if (authentication != null) {
				modifiedUser = (User) authentication.getPrincipal();
				currentUserName = modifiedUser.getUsername();
				userId = modifiedUser.getId();
			}

			Boolean hasClerkRole = false;
			Boolean hasFowardRole = false;
			Boolean hasAssignerRole = false;
			Boolean hasChairmanRole = false;
			Boolean hasProcessRole = false;
			Boolean hasOwnerPermission = false;
			Boolean hasChiefOfStaffRole = false;
			Boolean hasFinishPermission = false;
			for (ParticipateDto participateDto : letterInDocumentDto.getTask().getParticipates()) {

				if (participateDto.getTaskOwner() != null && participateDto.getTaskOwner().getUserTaskOwners() != null
						&& participateDto.getTaskOwner().getUserTaskOwners().size() > 0) {
					for (UserTaskOwnerDto uto : participateDto.getTaskOwner().getUserTaskOwners()) {

						if (!hasFinishPermission) {
							hasFinishPermission = (participateDto.getParticipateType()==LetterConstant.ParticipantTypeMainProcess) && (uto.getUser().getId().equals(userId));
							
						}
						// check quyền comment
						if (participateDto != null && !participateDto.getHasOwnerPermission()) {
							Boolean ownerPermission = uto.getUser().getId().equals(userId);
							participateDto.setHasOwnerPermission(hasOwnerPermission || ownerPermission);
						}

						// check quyền văn thư
						if (!hasClerkRole) {
							Boolean clerkRole = participateDto.getRole().getCode().equals(LetterConstant.ClerkRole)
									&& (uto.getUser().getId().equals(userId));
							hasClerkRole = hasClerkRole || clerkRole;
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
						
						// check quyền chánh văn phòng
						if (!hasChiefOfStaffRole) {
							Boolean ChiefOfStaffRole = participateDto.getRole().getCode().equals(LetterConstant.ChiefOfStaffRole)
									&& (uto.getUser().getId().equals(userId));
							hasChiefOfStaffRole = hasChiefOfStaffRole || ChiefOfStaffRole;
						}
					}
				}
			}

			letterInDocumentDto.setHasClerkRole(hasClerkRole);
			letterInDocumentDto.setHasFowardRole(hasFowardRole);
			letterInDocumentDto.setHasAssignerRole(hasAssignerRole);
			letterInDocumentDto.setHasChairmanRole(hasChairmanRole);
			letterInDocumentDto.setHasProcessRole(hasProcessRole);
			letterInDocumentDto.setHasChiefOfStaffRole(hasChiefOfStaffRole);
			letterInDocumentDto.setHasFinishPermission(hasFinishPermission);
			viewDocumentUserService.addView(userId, id);// add view
		}
		return letterInDocumentDto;
	}

	@Override
	public void fowardLetterInDocument(Long documentId, Long assignerId, boolean isMainProcess) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterInDocument document = findById(documentId);
		String stepCode = LetterConstant.LetterInDocumentFlowCode + "Step3";
		TaskStep toStep = taskStepService.getTaskStepEntityByCode(stepCode);
		if (document.getTask() == null) {
			document.setTask(new Task());
			document.getTask().setCreateDate(currentDate);
			document.getTask().setCreatedBy(currentUserName);
		}
		document.getTask().setCurrentStep(toStep);

		// Participate p =
		// convertParticipateDtoToEntity(pDto,currentUserName,currentDate);
		Participate p = new Participate();
		p.setTask(document.getTask());
		TaskRole role = taskRoleService.getTaskRoleEntityByCode(LetterConstant.AssignerRole);
		TaskOwner taskOwner = taskOwnerService.findById(assignerId);
		p.setRole(role);
		p.setTaskOwner(taskOwner);
		p.setDisplayName(taskOwner.getDisplayName());
		p.setCurrentState(0);
		if (isMainProcess) {
			p.setParticipateType(LetterConstant.ParticipantTypeMainProcess);
		}
		if (document.getTask().getParticipates() == null) {
			document.getTask().setParticipates(new HashSet<Participate>());
		}
		
		if (p.getTaskOwner().getId() != null && p.getRole().getId() != null) {
			Participate participate = participateService.getTaskParticipateBy(p.getTask().getId(),
					p.getTaskOwner().getId(), p.getRole().getId());
			if (participate == null) {
				document.getTask().getParticipates().add(p);
			}
		}
		letterInDocumentRepository.save(document);
	}

	@Override
	public LetterInDocumentDto createLetterInDocument(LetterInDocumentDto dto, Long docClerkOwnerId, Long fowarderId,
			Long assignerId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		TaskFlowStepDto firstStep = null;

		if (dto.getTask() != null && dto.getTask().getFlow() != null && dto.getTask().getFlow().getSteps() != null) {
			firstStep = dto.getTask().getFlow().getSteps().get(0);
		}
		if (firstStep != null)
			dto.getTask().setCurrentStep(firstStep.getStep());

		dto.getTask().setParticipates(new HashSet<ParticipateDto>());
		ParticipateDto docClerk = new ParticipateDto();

		docClerk.setParticipateType(TaskManConstant.TaskOwnerTypeEnum.OtherType.getValue());
		List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ClerkRole);
		if (roles != null && roles.size() > 0) {
			TaskRoleDto role = roles.get(0);
			TaskOwnerDto docClerkOwner = null;
			if (docClerkOwnerId == null || docClerkOwnerId <= 0) {
				List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerDtoByRole(role.getId());
				if (taskOwners != null && taskOwners.size() > 0) {
					docClerkOwner = taskOwners.get(0);
				}
			} else {
				docClerkOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
			}
			docClerk.setRole(role);
			docClerk.setTaskOwner(docClerkOwner);
		}
		dto.getTask().getParticipates().add(docClerk);

		if (fowarderId != null && fowarderId > 0) {
			roles = taskRoleService.getTaskRoleByCodes(LetterConstant.FowardRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docFowardOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
				ParticipateDto fowarder = new ParticipateDto();
				fowarder.setTaskOwner(docFowardOwner);
				fowarder.setRole(role);
				dto.getTask().getParticipates().add(fowarder);
			}

			TaskStepDto fowardStep = null;
			if (dto.getTask().getFlow() != null && dto.getTask().getFlow().getSteps() != null
					&& dto.getTask().getFlow().getSteps().size() > 1) {
				fowardStep = dto.getTask().getFlow().getSteps().get(1).getStep();
			} else {
				for (TaskFlowStep step : LetterConstant.LetterInDocumentFlow.getSteps()) {
					if (step.getOrderIndex() == 1) {
						fowardStep = new TaskStepDto(step.getStep());
					}
				}
			}
			dto.getTask().setCurrentStep(fowardStep);
		}

		if (assignerId != null && assignerId > 0) {
			roles = taskRoleService.getTaskRoleByCodes(LetterConstant.AssignerRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docAssignerOwner = taskOwnerService.getTaskOwner(assignerId);
				ParticipateDto assigner = new ParticipateDto();
				assigner.setTaskOwner(docAssignerOwner);
				assigner.setRole(role);
				dto.getTask().getParticipates().add(assigner);
			}
			TaskStepDto assignerStep = null;

			if (dto.getTask().getFlow() != null && dto.getTask().getFlow().getSteps() != null
					&& dto.getTask().getFlow().getSteps().size() > 1) {
				assignerStep = dto.getTask().getFlow().getSteps().get(2).getStep();
			} else {
				for (TaskFlowStep step : LetterConstant.LetterInDocumentFlow.getSteps()) {
					if (step.getOrderIndex() == 2) {
						assignerStep = new TaskStepDto(step.getStep());
					}
				}
			}
			dto.getTask().setCurrentStep(assignerStep);
		}
		return saveLetterInDocument(dto);
	}

	@Override
	public LetterInDocumentDto assignProcess(LetterInDocumentDto dto, Long docClerkOwnerId, Long fowarderId,
			Long assignerId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		TaskFlowStepDto firstStep = null;

		firstStep = dto.getTask().getFlow().getSteps().get(0);
		if (firstStep != null)
			dto.getTask().setCurrentStep(firstStep.getStep());

		dto.getTask().setParticipates(new HashSet<ParticipateDto>());
		ParticipateDto docClerk = new ParticipateDto();

		docClerk.setParticipateType(TaskManConstant.TaskOwnerTypeEnum.OtherType.getValue());
		List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.ClerkRole);
		if (roles != null && roles.size() > 0) {
			TaskRoleDto role = roles.get(0);
			TaskOwnerDto docClerkOwner = null;
			if (docClerkOwnerId == null || docClerkOwnerId <= 0) {
				List<TaskOwnerDto> taskOwners = taskOwnerService.getListTaskOwnerDtoByRole(role.getId());
				if (taskOwners != null && taskOwners.size() > 0) {
					docClerkOwner = taskOwners.get(0);
				}
			} else {
				docClerkOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
			}
			docClerk.setRole(role);
			docClerk.setTaskOwner(docClerkOwner);
		}
		dto.getTask().getParticipates().add(docClerk);

		if (fowarderId != null && fowarderId > 0) {
			roles = taskRoleService.getTaskRoleByCodes(LetterConstant.FowardRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docFowardOwner = taskOwnerService.getTaskOwner(docClerkOwnerId);
				ParticipateDto fowarder = new ParticipateDto();
				fowarder.setTaskOwner(docFowardOwner);
				fowarder.setRole(role);
				dto.getTask().getParticipates().add(fowarder);
			}
			TaskStepDto fowardStep = dto.getTask().getFlow().getSteps().get(1).getStep();
			dto.getTask().setCurrentStep(fowardStep);
		}

		if (assignerId != null && assignerId > 0) {
			roles = taskRoleService.getTaskRoleByCodes(LetterConstant.AssignerRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docAssignerOwner = taskOwnerService.getTaskOwner(assignerId);
				ParticipateDto assigner = new ParticipateDto();
				assigner.setTaskOwner(docAssignerOwner);
				assigner.setRole(role);
				dto.getTask().getParticipates().add(assigner);
			}
		}

		TaskStepDto assignStep = dto.getTask().getFlow().getSteps().get(3).getStep();
		dto.getTask().setCurrentStep(assignStep);

		return saveLetterInDocument(dto);
	}

	@Override
	public LetterInDocumentDto forwardDocument(Long documentId, Long fowarderId) {
		LetterInDocumentDto dto = letterInDocumentRepository.getLetterIndocumentDtoById(documentId);
		if (fowarderId != null && fowarderId > 0) {
			List<TaskRoleDto> roles = taskRoleService.getTaskRoleByCodes(LetterConstant.FowardRole);
			if (roles != null && roles.size() > 0) {
				TaskRoleDto role = roles.get(0);
				TaskOwnerDto docFowardOwner = taskOwnerService.getTaskOwner(fowarderId);
				ParticipateDto fowarder = new ParticipateDto();
				fowarder.setTaskOwner(docFowardOwner);
				fowarder.setRole(role);
				dto.getTask().getParticipates().add(fowarder);
			}
			TaskStepDto fowardStep = dto.getTask().getFlow().getSteps().get(1).getStep();
			dto.getTask().setCurrentStep(fowardStep);
		}
		return saveLetterInDocument(dto);
	}

	@Override
	public LetterInDocumentDto removeDocument(Long documentId) {
		LetterInDocument doc = letterInDocumentRepository.findOne(documentId);
		if (doc != null) {
			letterInDocumentRepository.delete(doc);
			return new LetterInDocumentDto(doc);
		}
		return null;
	}

	@Override
	public void processingLetterIn(Long documentId) {
		if (documentId != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			User modifiedUser = null;
			LocalDateTime currentDate = LocalDateTime.now();
			String currentUserName = "Unknown User";
			if (authentication != null) {
				modifiedUser = (User) authentication.getPrincipal();
				currentUserName = modifiedUser.getUsername();
			}
			LetterInDocument document = findById(documentId);
			String stepCode = LetterConstant.LetterInDocumentFlowCode + "Step5";
			TaskStep toStep = taskStepService.getTaskStepEntityByCode(stepCode);
			LetterInDocument entity = letterInDocumentRepository.findOne(documentId);
			entity.getTask().setCurrentStep(toStep);
			letterInDocumentRepository.save(entity);
		}
	}

	@Override
	public Long getMaxDocumentId() {
		String sql = "select max(d.id) from LetterInDocument d";
		Query q = manager.createQuery(sql);
		Object resultObject = q.getSingleResult();
		Long result = 1L;
		if (resultObject != null) {
			result = (Long) resultObject;
		}
		return result;
	}

	@Override
	public LetterInDocumentDto generateDtoLetterIn() {
		LetterInDocumentDto dto = new LetterInDocumentDto();
		String docCode = LetterConstant.NumberOriginal + this.getMaxDocumentId() + "";
		dto.setDocCode(docCode);
		return dto;
	}

	@Override
	public Page<LetterInDocumentDto> getLetterByString(int stepIndex, int currentParticipateState,
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
			for (TaskFlowStep tfs : LetterConstant.LetterInDocumentFlow.getSteps()) {
				if (tfs.getOrderIndex() == stepIndex) {
					flowStep = tfs;
				}
			}
		}
		Long stepId = null;
		if (flowStep != null && flowStep.getStep() != null) {
			stepId = flowStep.getStep().getId();
		}

		return searchByText(userId, LetterConstant.LetterInDocumentFlow.getId(), stepId, currentParticipateState,
				searchDocumentDto, pageSize, pageIndex);

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
	
	private Page<LetterInDocumentDto> searchByText(Long userId, Long flowId, Long stepId,
			Integer currentParticipateState, SearchDocumentDto searchDocumentDto, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		List<Long> listTaskOwnerIds;
		String ownerQuery = "select utw.taskOwner.id from UserTaskOwner utw where utw.user.id=:userId";

		Query qOwnerList = manager.createQuery(ownerQuery, Long.class);
		qOwnerList.setParameter("userId", userId);
		listTaskOwnerIds = qOwnerList.getResultList();

		if(listTaskOwnerIds != null && listTaskOwnerIds.size() > 0) {
			String sqlTaskDocQuery = "from LetterInDocument d where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)";
			String sqlCountTaskDocQuery = "select count(d.id) from LetterInDocument d  where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)";
	
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
			
			if (searchDocumentDto != null) {
				if (searchDocumentDto != null && StringUtils.hasText(searchDocumentDto.getText())) {
					dDocumentCondition += " and (d.docOriginCode like :search or d.briefNote like :search)";
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
			}
	
			sqlTaskDocQuery += dDocumentCondition;
			sqlCountTaskDocQuery += dDocumentCondition;
			String sqlOrderBy = "";
	//		sqlOrderBy = " order by d.task.currentStep.code ASC, d.deliveredDate DESC";
			sqlOrderBy = " order by d.deliveredDate DESC";
			sqlTaskDocQuery += sqlOrderBy;
			Query qTaskDocQuery = manager.createQuery(sqlTaskDocQuery, LetterInDocument.class);
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
	
			if (searchDocumentDto != null) {
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
			}
	
			qTaskDocQuery.setParameter("listTaskOwnerIds", listTaskOwnerIds);
			qCountTaskDocQuery.setParameter("listTaskOwnerIds", listTaskOwnerIds);
			qTaskDocQuery.setFirstResult(startPosition);
			qTaskDocQuery.setMaxResults(pageSize);
			long totalDoc = (Long) qCountTaskDocQuery.getSingleResult();
			List<LetterInDocument> listDoc = qTaskDocQuery.getResultList();
	
			// phân quyền trên server ()
			List<LetterInDocumentDto> listDocDTO = new ArrayList<LetterInDocumentDto>();
	
			for (int i = 0; i < listDoc.size(); i++) {
				Boolean hasClerkRole = false;
				Boolean hasFowardRole = false;
				Boolean hasAssignerRole = false;
				Boolean hasChairmanRole = false;
				Boolean hasProcessRole = false;
				Boolean hasChiefOfStaffRole = false;
				LetterInDocument d = listDoc.get(i);
				LetterInDocumentDto docDto = new LetterInDocumentDto(d);
	
				if (d.getTask() != null && d.getTask().getParticipates() != null
						&& d.getTask().getParticipates().size() > 0) {
					for (Participate p : d.getTask().getParticipates()) {
						if (p.getTaskOwner() != null && p.getTaskOwner().getUserTaskOwners() != null
								&& p.getTaskOwner().getUserTaskOwners().size() > 0) {
							Set<UserTaskOwner> participateTaskOwner = p.getTaskOwner().getUserTaskOwners();
							for (UserTaskOwner uto : participateTaskOwner) {
								Long taskUserId = uto.getUser().getId();
								if (taskUserId.equals(userId)) {
									docDto.getTask().setCurrentState(p.getCurrentState());
								}
								
								// check quyền văn thư
								if (!hasClerkRole) {
									Boolean clerkRole = p.getRole().getCode().equals(LetterConstant.ClerkRole)
											&& (uto.getUser().getId().equals(userId));
									hasClerkRole = hasClerkRole || clerkRole;
								}
	
								// check quyền phân luồng
								if (!hasFowardRole) {
									Boolean fowardRole = p.getRole().getCode().equals(LetterConstant.FowardRole)
											&& (uto.getUser().getId().equals(userId));
									hasFowardRole = hasFowardRole || fowardRole;
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
								
								// check quyền chánh văn phòng
								if (!hasChiefOfStaffRole) {
									Boolean ChiefOfStaffRole = p.getRole().getCode().equals(LetterConstant.ChiefOfStaffRole)
											&& (uto.getUser().getId().equals(userId));
									hasChiefOfStaffRole = hasChiefOfStaffRole || ChiefOfStaffRole;
								}
							}
						}
					}
				}
	
				docDto.setHasClerkRole(hasClerkRole);
				docDto.setHasFowardRole(hasFowardRole);
				docDto.setHasAssignerRole(hasAssignerRole);
				docDto.setHasChairmanRole(hasChairmanRole);
				docDto.setHasProcessRole(hasProcessRole);
				docDto.setHasChiefOfStaffRole(hasChiefOfStaffRole);
	
				listDocDTO.add(docDto);
			}
			
			
			PageImpl<LetterInDocumentDto> page = new PageImpl<LetterInDocumentDto>(listDocDTO, pageable, totalDoc);
			return page;
		}
		return null;
	}

	@Override
	public List<UserRolesDto> getUserRoles() {
		List<UserRolesDto> lstUserRoleDto = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User userLogin = null;
		if (authentication != null) {
			userLogin = (User) authentication.getPrincipal();
			if (userLogin != null) {
				List<TaskOwnerDto> lstTaskOwner = taskOwnerService.getListCurrentTaskOwner();
				if (lstTaskOwner != null) {
					for (TaskOwnerDto taskOwnerDto : lstTaskOwner) {
					}
				}
			}
		}
		return lstUserRoleDto;
	}

	@Override
	public LetterInDocumentDto getLetterInDocumentByTaskId(Long taskId) {

		LetterInDocumentDto inDocument = letterInDocumentRepository.getletterInDocumentByTaskId(taskId);
		if (inDocument != null) {
			return this.getLetterInDocumentById(inDocument.getId());
		}
		return null;
	}

	@Override
	public List<ReportLetterByStepDto> getReportLetterByStep() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();

			List<Long> listTaskOwnerIds;
			String ownerQuery = "select utw.taskOwner.id from UserTaskOwner utw where utw.user.id=:userId";

			Query qOwnerList = manager.createQuery(ownerQuery, Long.class);
			qOwnerList.setParameter("userId", modifiedUser.getId());
			listTaskOwnerIds = qOwnerList.getResultList();
			
			if(listTaskOwnerIds!=null && listTaskOwnerIds.size() > 0) {
				String sqlCountByStep = "select count(d.id) from LetterInDocument d  where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds) and (p.task.currentStep.id=:stepId))";
				String sqlCountAll = "select count(d.id) from LetterInDocument d  where d.task.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds))";
	
				Query qCountByStep = manager.createQuery(sqlCountByStep, Long.class);
				Query qCountAll = manager.createQuery(sqlCountAll, Long.class);
	
				List<ReportLetterByStepDto> list = new ArrayList<ReportLetterByStepDto>();
				List<Long> step = new ArrayList<Long>();
				step.add(LetterConstant.LetterInStep1);
				step.add(LetterConstant.LetterInStep2);
				step.add(LetterConstant.LetterInStep3);
				step.add(LetterConstant.LetterInStep4);
				step.add(LetterConstant.LetterInStep5);
	
				if (step != null && step.size() > 0) {
					for (Long a : step) {
						ReportLetterByStepDto dto = new ReportLetterByStepDto();
						if (a == LetterConstant.LetterInStep1) {
							qCountByStep.setParameter("stepId", LetterConstant.LetterInStep1);
							dto.setStep(LetterConstant.LetterInStep1);
						}
						if (a == LetterConstant.LetterInStep2) {
							qCountByStep.setParameter("stepId", LetterConstant.LetterInStep2);
							dto.setStep(LetterConstant.LetterInStep2);
						}
						if (a == LetterConstant.LetterInStep3) {
							qCountByStep.setParameter("stepId", LetterConstant.LetterInStep3);
							dto.setStep(LetterConstant.LetterInStep3);
						}
						if (a == LetterConstant.LetterInStep4) {
							qCountByStep.setParameter("stepId", LetterConstant.LetterInStep4);
							dto.setStep(LetterConstant.LetterInStep4);
						}
						if (a == LetterConstant.LetterInStep5) {
							qCountByStep.setParameter("stepId", LetterConstant.LetterInStep5);
							dto.setStep(LetterConstant.LetterInStep5);
						}
	
						qCountByStep.setParameter("listTaskOwnerIds", listTaskOwnerIds);
						Long quantity = (Long) qCountByStep.getSingleResult();
						dto.setQuantity(quantity);
						list.add(dto);
					}
				}
				ReportLetterByStepDto dto = new ReportLetterByStepDto();
				qCountAll.setParameter("listTaskOwnerIds", listTaskOwnerIds);
				Long quantity = (Long) qCountAll.getSingleResult();
				dto.setStep(LetterConstant.LetterInStep0);
				dto.setQuantity(quantity);
				list.add(dto);
				return list;
			}
		}
		return null;
	}

	@Override
	public void addStaff(Long documentId, List<ParticipateDto> listStaff) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		LetterInDocument document = findById(documentId);
		if (document.getTask() == null) {
			document.setTask(new Task());
			document.getTask().setCreateDate(currentDate);
			document.getTask().setCreatedBy(currentUserName);
		}
		for (ParticipateDto pDto : listStaff) {// Xử lý đoạn code để thêm nhân viên xử lý
			Participate p = convertParticipateDtoToEntity(pDto, currentUserName, currentDate);
			if (document.getTask().getParticipates() == null) {
				document.getTask().setParticipates(new HashSet<Participate>());
			}
			document.getTask().getParticipates().add(p);
		}
		letterInDocumentRepository.save(document);
	}
	//Xuất excel công văn đến
	@Override
	public List<LetterInDocumentDto> searchLetterInDto(SearchDocumentDto searchDto) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = null;
		if (authentication != null) {
			currentUser = (User) authentication.getPrincipal();
		}
		if(currentUser != null) {
			String sql = " select new com.globits.letter.dto.LetterInDocumentDto(indoc) from LetterInDocument indoc where (1=1)";	
			String whereClause ="";
			if(searchDto != null) {
				if (StringUtils.hasText(searchDto.getText())) {
					whereClause += " and (indoc.docOriginCode like :text or indoc.briefNote like :text or indoc.issueOrgan.name like :text)";
				}
				if(searchDto.getDocumentTypeId()!=null) {//lọc theo loại văn bản
					whereClause += " and indoc.letterDocumentType.id = :documentTypeId";
				}
				if(searchDto.getLetterDocFieldId()!=null) {
					whereClause += " and indoc.letterDocFieldId.id = :letterDocFieldId";
				}
				if(searchDto.getDateFrom()!=null) {
					whereClause += " and indoc.deliveredDate >= :dateFrom";
				}
				if(searchDto.getDateTo()!=null) {
					whereClause += " and indoc.deliveredDate <= :dateTo";
				}
				if(searchDto.getDateFrom() != null && searchDto.getDateTo() != null) {
					whereClause += " and indoc.deliveredDate >=:dateFrom and indoc.deliveredDate <=:dateTo";
				}
			}
			
			sql +=whereClause;
			sql +=" order by indoc.deliveredDate desc ";		
	
			Query q = manager.createQuery(sql,LetterInDocumentDto.class);	
			
			if(searchDto != null) {
				if (StringUtils.hasText(searchDto.getText())) {
					String converSearch = "%" + searchDto.getText() + "%";
					q.setParameter("text", converSearch);
				}
				if(searchDto.getDocumentTypeId()!=null) {			
					q.setParameter("documentTypeId", searchDto.getDocumentTypeId());			
				}
				if(searchDto.getLetterDocFieldId()!=null) {			
					q.setParameter("letterDocFieldId", searchDto.getLetterDocFieldId());			
				}
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
}