package com.globits.taskman.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.domain.Department;
import com.globits.core.domain.FileDescription;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.repository.FileDescriptionRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Staff;
import com.globits.hr.repository.StaffRepository;
import com.globits.security.domain.User;
import com.globits.security.service.UserService;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.TaskManConstant.ParticipateStateEnum;
import com.globits.taskman.TaskManConstant.ParticipateTypeEnum;
import com.globits.taskman.domain.CommentFileAttachment;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.Project;
import com.globits.taskman.domain.ProjectTaskOwner;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.domain.TaskFileAttachment;
import com.globits.taskman.domain.TaskFlow;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.domain.TaskPriority;
import com.globits.taskman.domain.TaskRole;
import com.globits.taskman.domain.TaskStep;
import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.dto.CommentFileAttachmentDto;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFileAttachmentDto;
import com.globits.taskman.repository.CommentFileAttachmentRepository;
import com.globits.taskman.repository.ParticipateRepository;
import com.globits.taskman.repository.ProjectRepository;
import com.globits.taskman.repository.TaskCommentRepository;
import com.globits.taskman.repository.TaskFileAttachmentRepository;
import com.globits.taskman.repository.TaskFlowRepository;
import com.globits.taskman.repository.TaskRepository;
import com.globits.taskman.repository.TaskRoleRepository;
import com.globits.taskman.repository.TaskStepRepository;
import com.globits.taskman.service.ParticipateService;
import com.globits.taskman.service.ProjectService;
import com.globits.taskman.service.TaskOwnerService;
import com.globits.taskman.service.TaskPriorityService;
import com.globits.taskman.service.TaskService;

@Service
// @Transactional
public class TaskServiceImpl extends GenericServiceImpl<Task, Long> implements TaskService {
	@Autowired
	TaskRepository repository;
	@Autowired
	TaskFlowRepository taskFlowRepository;

	@Autowired
	TaskStepRepository taskStepRepository;

	@Autowired
	ParticipateService participateService;

	@Autowired
	ParticipateRepository participateRepository;
	@Autowired
	TaskRoleRepository taskRoleRepository;
	@Autowired
	StaffRepository staffRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	TaskCommentRepository taskCommentRepository;

	@Autowired
	TaskFileAttachmentRepository fileAttachmentRepository;

	@Autowired
	FileDescriptionRepository fileDescriptionRepository;
	@Autowired
	CommentFileAttachmentRepository commentFileAttachmentRepository;
	@Autowired
	TaskOwnerService taskOwnerService;

	@Autowired
	ProjectService projectService;

	@Autowired
	TaskPriorityService taskPriorityService;
	@Autowired
	UserService userService;

	@Autowired
	ProjectRepository projectRepository;

	// @Transactional
	public Participate setParticipateValue(ParticipateDto dto, Participate entity, String userName,
			LocalDateTime currentDate) {
		if (dto != null) {
			// Xử lý các trường đơn trước
			if (dto.getId() != null) {
				entity.setId(dto.getId());// trường hợp cập nhật dữ liệu
			} else {// Nếu là trường hợp thêm mới thì mới cho phép cập nhật lại loại đối tượng
				entity.setParticipateType(dto.getParticipateType());
			}
			entity.setDisplayName(dto.getDisplayName());
			// Đặt Role cho participate
			TaskRole role = null;
			if (dto.getRole() != null) {
				if (dto.getId() != null) {
					role = taskRoleRepository.findOne(dto.getRole().getId());
				}
				if (role == null && dto.getRole().getCode() != null) {
					List<TaskRole> roles = taskRoleRepository.getTaskRoleEntityByCode(dto.getRole().getCode());
					if (roles != null && roles.size() > 0) {
						role = roles.get(0);
					}

				}
			}
			entity.setRole(role);
			// Nếu participate này đã có 1 số comment và được đẩy lên
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
						comment = taskCommentRepository.findOne(commentDto.getId());
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
					/* comment.setId(commentDto.getId()); */ //comment dong nay lai
					comment.setParticipate(entity);
					// comment = taskCommentRepository.save(comment);//thêm dòng này để đảm bảo tạo
					// ra comment file
					entity.getComments().add(comment);// thêm comment vào

				}
			}
			if (dto.getParticipateType() == ParticipateTypeEnum.PersonalType.getValue()) {
				if (dto.getEmployee() != null && dto.getEmployee().getId() != null) {
					Staff staff = staffRepository.findOne(dto.getEmployee().getId());
					entity.setEmployee(staff);
				}
			} else if (dto.getParticipateType() == ParticipateTypeEnum.DepartmentType.getValue()) {
				if (dto.getDepartment() != null && dto.getDepartment().getId() != null) {
					Department department = departmentRepository.findOne(dto.getDepartment().getId());
					entity.setDepartment(department);
				}
			}
			if (dto.getTaskOwner() != null) {
				TaskOwner taskOwner = taskOwnerService.findById(dto.getTaskOwner().getId());
				entity.setTaskOwner(taskOwner);
				if (taskOwner != null && entity.getDisplayName() == null) {
					entity.setDisplayName(taskOwner.getDisplayName());
				}
			}
		}
		return entity;
	}

	@Transactional
	private Task setValue(TaskDto dto, Task entity, String userName, LocalDateTime currentDate) {
		if (dto != null) {
			entity.setId(dto.getId());
			entity.setDateDue(dto.getDateDue());
			entity.setDateStart(dto.getDateStart());
			entity.setDescription(dto.getDescription());
			TaskFlow flow = null;
			if (dto.getFlow() != null) {
				if (dto.getFlow().getId() != null) {
					flow = taskFlowRepository.findOne(dto.getFlow().getId());
				}
			}
			TaskStep currentStep = null;
			if (dto.getCurrentStep() != null && dto.getCurrentStep().getId() != null) {
				currentStep = taskStepRepository.findOne(dto.getCurrentStep().getId());
			}
			entity.setCurrentStep(currentStep);
			entity.setFlow(flow);
			entity.setName(dto.getName());
			entity.setSummary(dto.getSummary());
			entity.setTitle(dto.getTitle());
			entity.setEstimateTime(dto.getEstimateTime());
			entity.setEstimateTimeUnit(dto.getEstimateTimeUnit());
			entity.setDuration(dto.getDuration());
			entity.setTotalEffort(dto.getTotalEffort());
			entity.setCompletionRate(dto.getCompletionRate());

			if (dto.getParticipates() != null && dto.getParticipates().size() > 0) {// Nếu có cập nhật danh sách tham
																					// gia
				HashSet<Participate> participates = new HashSet<Participate>();
				if (dto.getParticipates() != null && dto.getParticipates().size() > 0) {
					for (ParticipateDto pDto : dto.getParticipates()) {
						Participate participate = null;
						if (pDto.getId() != null) {
							participate = participateRepository.findOne(pDto.getId());
						}
						if (participate == null) {
							participate = new Participate();
							participate.setCreateDate(currentDate);
							participate.setCreatedBy(userName);
						}
						participate = setParticipateValue(pDto, participate, userName, currentDate);// Thiết lập value
																									// cho người/phòng
																									// ban tham gia tại
																									// đây
						if (participate.getComments() != null && participate.getComments().size() > 0) {
							participate.setCurrentState(ParticipateStateEnum.CommentedType.getValue());
						} else {
							participate.setCurrentState(ParticipateStateEnum.NotCommentType.getValue());
						}
						participate.setTask(entity);
						// participate = participateRepository.save(participate);//Cần dòng này để đảm
						// bảo không bị lỗi detach - chưa hiểu sao
						participates.add(participate);
					}
					if (entity.getParticipates() == null) {
						entity.setParticipates(new HashSet<Participate>());
					} else {
						entity.getParticipates().clear();
					}
					entity.getParticipates().addAll(participates);
				}
			}
			if (entity.getAttachments() != null && entity.getAttachments().size() > 0) {
				entity.getAttachments().clear();
			}
			if (dto.getAttachments() != null && dto.getAttachments().size() > 0) {
				for (TaskFileAttachmentDto aDto : dto.getAttachments()) {
					TaskFileAttachment attachment = null;
					if (aDto.getId() != null) {
						attachment = fileAttachmentRepository.findOne(aDto.getId());
					}
					if (attachment == null) {
						attachment = new TaskFileAttachment();
						attachment.setCreateDate(currentDate);
						attachment.setCreatedBy(userName);
					}
					attachment.setTask(entity);
					FileDescription file = null;
					if (aDto.getFile() != null && aDto.getFile().getId() != null) {
						file = fileDescriptionRepository.findOne(aDto.getFile().getId());
					}
					attachment.setFile(file);
					attachment = fileAttachmentRepository.save(attachment);// Cần dòng này để đảm bảo không bị lỗi
																			// detach - chưa hiểu sao
					entity.getAttachments().add(attachment);
				}
			}
			if (dto.getProject() != null && dto.getProject().getId() != null) {
				Project project = projectService.findById(dto.getProject().getId());
				entity.setProject(project);
			} else {
				entity.setProject(null);
			}
			if (dto.getParent() != null && dto.getParent().getId() != null) {
				Task parent = repository.findOne(dto.getParent().getId());
				entity.setParent(parent);
			} else {
				entity.setParent(null);
			}
			if (dto.getPriority() != null && dto.getPriority().getId() != null) {
				TaskPriority priority = taskPriorityService.findById(dto.getPriority().getId());
				entity.setPriority(priority);
			} else {
				entity.setPriority(null);
			}
		}
		return entity;
	}

	@Override
	public TaskDto getTask(Long id) {
		Task docCategory = repository.findOne(id);
		return new TaskDto(docCategory);
	}

	@Override
	@Transactional
	public TaskDto saveTask(TaskDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		if (dto != null) {
			Task entity = null;
			if (dto.getId() != null) {// Có khả năng là update
				entity = repository.findOne(dto.getId());// Lấy dữ liệu từ database ra.
			}
			if (entity == null) {// trường hợp thêm mới
				entity = new Task();
				entity.setCurrentState(0);
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto, entity, currentUserName, currentDate);// Thiết lập giá trị tại đây
			entity = repository.save(entity);
			return new TaskDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTask(Long id) {
		Task doc = repository.findOne(id);
		if (doc != null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public Page<TaskDto> getListTask(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTask(pageable);
	}

	public Page<TaskDto> getListPersonalTask(int pageSize, int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		Long staffId = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			staffId = modifiedUser.getPerson().getId();
		}
		return getListPersonalTask(staffId, null, null, pageSize, pageIndex);
	}

	/*
	 * Lấy lịch cá nhân của 1 người theo Role Tham số đầu vào : staffId : Id của
	 * Staff cần lấy danh sách công việc roleId : roleId của Staff trong công việc -
	 * nếu trường này bằng NULL sẽ lấy tất cả Kết quả trả về : Danh sách các công
	 * việc của người này theo role tương ứng
	 */
	@Override
	public Page<TaskDto> getListPersonalTask(Long staffId, List<Long> roleIds, Long flowId, int pageSize,
			int pageIndex) {
		Staff staff = staffRepository.findById(staffId);
		if (staff != null) {
			Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
			int startPosition = (pageIndex - 1) * pageSize;
			String sql = "select distinct p.task from Participate p";
			String sqlCount = "select count(distinct p.task.id) from Participate p";
			String whereQuery = " where p.participateType=:participateType and p.employee.id=:staffId";
			if (roleIds != null) {
				whereQuery += " and p.role.id in :roleIds";
			}
			if (flowId != null) {
				whereQuery += " and p.task.flow.id=:flowId";
			}
			sql += whereQuery;
			sqlCount += whereQuery;

			Query q = manager.createQuery(sql, Task.class);
			Query qCount = manager.createQuery(sqlCount);
			q.setFirstResult(startPosition);
			q.setMaxResults(pageSize);
			q.setParameter("staffId", staffId);
			qCount.setParameter("staffId", staffId);
			q.setParameter("participateType", ParticipateTypeEnum.PersonalType.getValue());
			qCount.setParameter("participateType", ParticipateTypeEnum.PersonalType.getValue());

			if (roleIds != null) {
				q.setParameter("roleIds", roleIds);
				qCount.setParameter("roleIds", roleIds);
			}

			if (flowId != null) {
				q.setParameter("flowId", flowId);
				qCount.setParameter("flowId", flowId);
			}
			List<Task> list = q.getResultList();
			ArrayList<TaskDto> content = new ArrayList<TaskDto>();
			for (int i = 0; i < list.size(); i++) {
				content.add(new TaskDto(list.get(i)));
			}
			long total = (Long) qCount.getSingleResult();
			PageImpl<TaskDto> page = new PageImpl<TaskDto>(content, pageable, total);
			return page;
		}
		return null;
	}

	/*
	 * Lấy công việc của một danh sách các Deparments đầu vào Tham số đầu vào :
	 * departmentIds : Danh sách Id của các phòng cần lấy danh sách công việc
	 * roleIds : danh sách các Role trong công việc - nếu trường này bằng NULL sẽ
	 * lấy tất cả Kết quả trả về : Danh sách các công việc của các phòng này theo
	 * Role tương ứng
	 */
	@Override
	public Page<TaskDto> getListDepartmentTask(List<Long> departmentIds, List<Long> roleIds, Long flowId,
			Integer currentState, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		String sql = "select distinct p.task from Participate p";
		String sqlCount = "select count(distinct p.task.id) from Participate p";
		String whereQuery = " where p.participateType=:participateType and p.department.id in :departmentIds";
		if (roleIds != null) {
			whereQuery += " and p.role.id in :roleIds";
		}
		if (flowId != null) {
			whereQuery += " and p.task.flow.id=:flowId";
		}
		if (currentState != null && currentState >= 0) {
			whereQuery += " and p.task.currentState=:currentState";
		}
		sql += whereQuery;
		sqlCount += whereQuery;

		Query q = manager.createQuery(sql, Task.class);
		Query qCount = manager.createQuery(sqlCount);
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		q.setParameter("departmentIds", departmentIds);
		qCount.setParameter("departmentIds", departmentIds);
		q.setParameter("participateType", ParticipateTypeEnum.DepartmentType.getValue());
		qCount.setParameter("participateType", ParticipateTypeEnum.DepartmentType.getValue());

		if (roleIds != null) {
			q.setParameter("roleIds", roleIds);
			qCount.setParameter("roleIds", roleIds);
		}

		if (flowId != null) {
			q.setParameter("flowId", flowId);
			qCount.setParameter("flowId", flowId);
		}

		if (currentState != null && currentState >= 0) {
			q.setParameter("currentState", currentState);
			qCount.setParameter("currentState", currentState);
		}
		List<Task> list = q.getResultList();
		ArrayList<TaskDto> content = new ArrayList<TaskDto>();
		for (int i = 0; i < list.size(); i++) {
			content.add(new TaskDto(list.get(i)));
		}
		long total = (Long) qCount.getSingleResult();
		PageImpl<TaskDto> page = new PageImpl<TaskDto>(content, pageable, total);
		return page;
	}

	/*
	 * Lấy tất cả các công việc của 1 phòng ban mà người tiếp nhận được giao có vai
	 * trò tham gia và các công việc cá nhân của người đó Tham số đầu vào : staffId
	 * : Id của Staff cần lấy departmentId : Id của phòng cần lấy Kết quả trả về :
	 * Danh sách các công việc của các phòng này theo Role tương ứng
	 */
	@Override
	public Page<TaskDto> getAllTask(Long staffId, Long departmentId, Long flowId, Integer currentState, int pageSize,
			int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		List<Long> roleIds = new ArrayList<Long>();

		String sqlGetRole = "select sdtr.role.id from StaffDepartmentTaskRole sdtr where sdtr.staff.id=:staffId and sdtr.department.id=:departmentId";
		Query qGetRole = manager.createQuery(sqlGetRole, Long.class);
		qGetRole.setParameter("staffId", staffId);
		qGetRole.setParameter("departmentId", departmentId);
		roleIds = qGetRole.getResultList();
		if (roleIds != null && roleIds.size() > 0) {
			String sql = "select distinct p.task from Participate p";
			String sqlCount = "select count(distinct p.task.id) from Participate p";
			String whereQuery = " where ((p.participateType=:personalParticipateType and p.employee.id =:staffId)";
			whereQuery += " or (p.participateType=:departmentParticipateType and p.department.id =:departmentId))";

			if (roleIds != null) {
				whereQuery += " and p.role.id in :roleIds";
			}
			if (flowId != null) {
				whereQuery += " and p.task.flow.id=:flowId";
			}
			if (currentState != null && currentState >= 0) {
				whereQuery += " and p.task.currentState=:currentState";
			}
			sql += whereQuery;
			sqlCount += whereQuery;

			Query q = manager.createQuery(sql, Task.class);
			Query qCount = manager.createQuery(sqlCount);
			q.setFirstResult(startPosition);
			q.setMaxResults(pageSize);

			q.setParameter("staffId", staffId);
			qCount.setParameter("staffId", staffId);

			q.setParameter("departmentId", departmentId);
			qCount.setParameter("departmentId", departmentId);

			q.setParameter("departmentParticipateType", ParticipateTypeEnum.DepartmentType.getValue());
			qCount.setParameter("departmentParticipateType", ParticipateTypeEnum.DepartmentType.getValue());

			q.setParameter("personalParticipateType", ParticipateTypeEnum.PersonalType.getValue());
			qCount.setParameter("personalParticipateType", ParticipateTypeEnum.PersonalType.getValue());

			if (roleIds != null) {
				q.setParameter("roleIds", roleIds);
				qCount.setParameter("roleIds", roleIds);
			}

			if (flowId != null) {
				q.setParameter("flowId", flowId);
				qCount.setParameter("flowId", flowId);
			}

			if (currentState != null && currentState >= 0) {
				q.setParameter("currentState", currentState);
				qCount.setParameter("currentState", currentState);
			}
			List<Task> list = q.getResultList();
			ArrayList<TaskDto> content = new ArrayList<TaskDto>();
			for (int i = 0; i < list.size(); i++) {
				content.add(new TaskDto(list.get(i)));
			}
			long total = (Long) qCount.getSingleResult();
			PageImpl<TaskDto> page = new PageImpl<TaskDto>(content, pageable, total);
			return page;
		}
		return null;
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
	public Page<TaskDto> getAllDepartmentAndPersonalTask(Long staffId, Long flowId, Long stepId,
			Integer currentParticipateState, int pageSize, int pageIndex) {
		int startPosition = (pageIndex - 1) * pageSize;
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		List<Long> departmentIds = new ArrayList<Long>();

		String sqlGetDep = "select distinct ps.department.id from StaffDepartmentTaskRole ps where ps.staff.id=:staffId";
		Query qGetDep = manager.createQuery(sqlGetDep, Long.class);
		qGetDep.setParameter("staffId", staffId);

		departmentIds = qGetDep.getResultList();

		String sql = "select distinct p.task from Participate p where (p.id in (select p1.id from Participate p1 inner join StaffDepartmentTaskRole sdtr on p1.role.id=sdtr.role.id and p1.department.id=sdtr.department.id";
		String sqlCount = "select count(distinct p.task.id) from Participate p where (p.id in (select p1.id from Participate p1 inner join StaffDepartmentTaskRole sdtr on p1.role.id=sdtr.role.id and p1.department.id=sdtr.department.id";

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

		Query q = manager.createQuery(sql, Task.class);
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
		List<Task> list = q.getResultList();
		ArrayList<TaskDto> content = new ArrayList<TaskDto>();
		for (int i = 0; i < list.size(); i++) {
			content.add(new TaskDto(list.get(i)));
		}
		long total = (Long) qCount.getSingleResult();
		PageImpl<TaskDto> page = new PageImpl<TaskDto>(content, pageable, total);
		return page;
	}

	@Override
	public Page<TaskDto> getAllDepartmentAndPersonalTask(Long flowId, Long stepId, Integer currentParticipateState,
			int pageSize, int pageIndex) {
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
	public Page<TaskDto> getListTaskByFlow(Long flowId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskByFlowId(flowId, pageable);
	}

	@Override
	public Page<TaskDto> getListTaskByFlowAndStepId(Long flowId, Long stepId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		if (stepId != null && stepId > 0) {
			return repository.getListTaskByFlowAndStepId(flowId, stepId, pageable);
		} else {
			return getListTaskByFlow(flowId, pageSize, pageIndex);
		}

	}

	@Override
	public Boolean addComment(TaskCommentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		TaskComment comment = null;
		if (dto.getId() != null) {
			comment = taskCommentRepository.findOne(dto.getId());
		}
		if (dto.getParticipate() != null) {
			Participate participate = participateService.findById(dto.getParticipate().getId());
			if (participate != null && participate.getTask() != null) {
				TaskOwner taskOwner = participate.getTaskOwner();
				if (taskOwner != null) {
					if (taskOwner.getUserTaskOwners() != null) {
						Boolean hasCommentPermission = false;
						for (UserTaskOwner uto : taskOwner.getUserTaskOwners()) {
							if (uto.getUser() != null && (uto.getUser().getId() != null)
									&& uto.getUser().getId().equals(modifiedUser.getId())) {
								hasCommentPermission = true;
								break;
							}
						}
						if (hasCommentPermission) {
							if (comment == null) {
								comment = new TaskComment();
								comment.setCreateDate(currentDate);
								comment.setCreatedBy(currentUserName);
							}
							comment.setParticipate(participate);
							comment.setComment(dto.getComment());
							comment = taskCommentRepository.save(comment);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public Page<TaskDto> getAllTaskByProject(Long projectId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskByProjectId(projectId, pageable);
	}

	@Override
	public Page<TaskDto> getListRootTaskByProjectId(Long projectId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		// return repository.getListRootTaskByProjectId(projectId, pageable);
		Page<TaskDto> result = repository.getListRootTaskByProjectId(projectId, pageable);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		Boolean isTaskAdmin = false;
		Project project = projectRepository.findOne(projectId);
		for (ProjectTaskOwner pto : project.getMembers()) {
			for (UserTaskOwner uto : pto.getTaskOwner().getUserTaskOwners()) {
				if (uto.getUser().getId().equals(modifiedUser.getId())) {
					if ((pto.getMainRole() != null)
							&& (pto.getMainRole().getCode().equals(TaskManConstant.TASKMAN_ADMIN))) {
						isTaskAdmin = true;
					}
				}
			}
		}
		for (TaskDto taskDto : result.getContent()) {
			// taskDto.setEditable(isTaskAdmin||taskDto.isHasChairmanPermission());
			// taskDto.setDeletable(isTaskAdmin);
			// for(TaskDto c : taskDto.getChildren()) {
			// c.setEditable(isTaskAdmin||c.isHasChairmanPermission());
			// c.setDeletable(isTaskAdmin);
			// }
			setPermission(taskDto, isTaskAdmin);
		}

		return result;
	}

	private void setPermission(TaskDto taskDto, boolean isTaskAdmin) {
		taskDto.setEditable(isTaskAdmin || taskDto.isHasChairmanPermission());
		taskDto.setDeletable(isTaskAdmin);
		Double totalEffort = 0D;
		for (TaskDto c : taskDto.getChildren()) {
			setPermission(c, isTaskAdmin);
			if (c.getTotalEffort() == null) {
				c.setTotalEffort(c.getEstimateTime());
			}
			if (c.getTotalEffort() != null) {
				totalEffort += c.getTotalEffort();
			}
		}
		if (totalEffort > 0) {
			taskDto.setTotalEffort(totalEffort);
		} else {
			taskDto.setTotalEffort(taskDto.getEstimateTime());
		}
	}

	@Override
	public Page<TaskDto> getListRootTaskByProjectIdAndStepId(Long projectId, Long stepId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		Page<TaskDto> result = repository.getListRootTaskByProjectIdAndStepId(projectId, stepId, pageable);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		Boolean isTaskAdmin = false;
		Project project = projectRepository.findOne(projectId);
		for (ProjectTaskOwner pto : project.getMembers()) {
			for (UserTaskOwner uto : pto.getTaskOwner().getUserTaskOwners()) {
				if (uto.getUser().getId().equals(modifiedUser.getId())) {
					if ((pto.getMainRole() != null)
							&& (pto.getMainRole().getCode().equals(TaskManConstant.TASKMAN_ADMIN))) {
						isTaskAdmin = true;
					}
				}
			}
		}
		for (TaskDto taskDto : result.getContent()) {
			taskDto.setEditable(isTaskAdmin || taskDto.isHasChairmanPermission());
			taskDto.setDeletable(isTaskAdmin);
			for (TaskDto c : taskDto.getChildren()) {
				c.setEditable(isTaskAdmin || c.isHasChairmanPermission());
				c.setDeletable(isTaskAdmin);
			}
		}
		return result;
	}

	@Override
	public Page<TaskDto> getMyRootTaskByProjectIdAndStepId(Long projectId, Long stepId, Long priorityId, int pageSize,
			int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		return getListRootTaskByProjectIdAndStepId(projectId, stepId, priorityId, modifiedUser.getId(), pageSize,
				pageIndex);
	}

	@Override
	public Page<TaskDto> getListRootTaskByProjectIdAndStepId(Long projectId, Long stepId, Long priorityId, Long userId,
			int pageSize, int pageIndex) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		String whereQuery = " p.taskOwner.id in (select uto.taskOwner.id from UserTaskOwner uto where uto.user.id=:userId) and (p.task.parent=null)";
		if (projectId != null) {
			whereQuery += " and p.task.project.id=:projectId";
		}

		if (stepId != null) {
			whereQuery += " and p.task.currentStep.id=:stepId";
		}

		String query = "select p.task from Participate p left join fetch p.task.subTask sub where (p.task.currentStep.id=sub.currentStep.id)";

		if (priorityId != null && priorityId > 0) {
			query += " and (p.task.priority.id=sub.priority.id)";
		}

		query += " and " + whereQuery;

		String queryCount = "select count(p.task.id) from Participate p where " + whereQuery;

		Query q = manager.createQuery(query, Task.class);
		Query qCount = manager.createQuery(queryCount);

		q.setParameter("userId", userId);
		qCount.setParameter("userId", userId);

		if (projectId != null) {
			q.setParameter("projectId", projectId);
			qCount.setParameter("projectId", projectId);
		}

		if (stepId != null) {
			q.setParameter("stepId", stepId);
			qCount.setParameter("stepId", stepId);
		}

		q.setMaxResults(pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		q.setFirstResult(startPosition);
		List<Task> list = q.getResultList();
		ArrayList<TaskDto> content = new ArrayList<TaskDto>();
		Project project = projectRepository.findOne(projectId);

		for (Task task : list) {
			TaskDto taskDto = new TaskDto(task, stepId, userId);
			taskDto.setEditable(false);
			taskDto.setDeletable(false);
			if (modifiedUser != null && modifiedUser.getRoles() != null) {
				for (ProjectTaskOwner pto : project.getMembers()) {
					for (UserTaskOwner uto : pto.getTaskOwner().getUserTaskOwners()) {
						if (uto.getUser().getId() == userId) {
							if (uto.getRole().getCode().equals(TaskManConstant.TASKMAN_ADMIN)) {
								taskDto.setEditable(true);
								taskDto.setDeletable(true);
							}
						}
					}
				}
				// for(Role r:modifiedUser.getRoles()) {
				// if(r.getName()!=null &&
				// r.getName().equals(com.globits.core.Constants.ROLE_ADMIN)) {
				// taskDto.setDeletable(true);
				// taskDto.setEditable(true);
				// }else if(r.getName()!=null &&
				// r.getName().equals(TaskManConstant.TASKMAN_ADMIN)) {
				// taskDto.setEditable(true);
				// }else {
				// taskDto.setDeletable(false);
				// taskDto.setEditable(false);
				// }
				// }
			}
			content.add(taskDto);
		}
		Long total = (Long) qCount.getSingleResult();
		Page<TaskDto> result = new PageImpl<TaskDto>(content, pageable, total);
		return result;
	}

	@Override
	public Page<TaskDto> getProjectTasksByStepIdAndOrderByType(Long projectId, Long stepId, int orderByType,
			int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);

		// nếu orderByType = 1 Sắp xếp theo ngày giao việc (ngày giao việc gần nhất lên
		// đầu)
		// nếu orderByType = 2 Sắp xếp theo ngày hết hạn (gần nhất lên đầu)
		// nếu orderByType = 3 Sắp xếp theo độ ưu tiên (ưu tiên cao nhất lên đầu)

		if (orderByType == 1) {
			return repository.getProjectTasksByStepIdAndOrderByDateStart(projectId, stepId, pageable);
		} else if (orderByType == 2) {
			return repository.getProjectTasksByStepIdAndOrderByDateDue(projectId, stepId, pageable);
		} else {
			return repository.getProjectTasksByStepIdAndOrderByPriority(projectId, stepId, pageable);
		}
	}

	@Override
	public Page<TaskDto> getListRootTaskByProjectIdAndOrderByType(Long projectId, int orderByType, int pageSize,
			int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);

		// nếu orderByType = 1 Sắp xếp theo ngày giao việc (ngày giao việc gần nhất lên
		// đầu)
		// nếu orderByType = 2 Sắp xếp theo ngày hết hạn (gần nhất lên đầu)
		// nếu orderByType = 3 Sắp xếp theo độ ưu tiên (ưu tiên cao nhất lên đầu)

		if (orderByType == 1) {
			return repository.getListRootTaskByProjectIdAndOrderByDateStart(projectId, pageable);
		} else if (orderByType == 2) {
			return repository.getListRootTaskByProjectIdAndOrderByDateDue(projectId, pageable);
		} else {
			return repository.getListRootTaskByProjectIdAndOrderByPriority(projectId, pageable);
		}
	}

	@Override
	public Page<TaskDto> getListTaskBy(long flowId, long stepId, int pageIndex, int pageSize) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			user = (User) authentication.getPrincipal();
			currentUserName = user.getUsername();
			if (user != null && user.getId() != null) {

				long userId = user.getId();
				Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
				
				List<Long> listTaskOwnerIds;
				String ownerQuery = "select utw.taskOwner.id from UserTaskOwner utw where utw.user.id=:userId";
				
				Query qOwnerList = manager.createQuery(ownerQuery, Long.class);
				qOwnerList.setParameter("userId", userId);
				listTaskOwnerIds = qOwnerList.getResultList();

				String query = "SELECT new com.globits.taskman.dto.TaskDto(t) from Task t where t.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)) ";
				String queryCount = "SELECT count(t.id) from Task t where t.id in (select p.task.id from Participate p where p.taskOwner.id in (:listTaskOwnerIds)) ";
				
				String whereQuery = "";
				
				if (flowId > 0) {
					whereQuery += " and t.flow.id =:flowId";
				}

				if (stepId > 0) {
					whereQuery += " and t.currentStep.id =:stepId";
				}

				String orderBy = " ORDER BY t.createDate DESC";
				
				query += whereQuery + orderBy;
				queryCount += whereQuery;

				Query q = manager.createQuery(query, TaskDto.class);
				Query qCount = manager.createQuery(queryCount);

				q.setParameter("listTaskOwnerIds", listTaskOwnerIds);
				qCount.setParameter("listTaskOwnerIds", listTaskOwnerIds);

				if (flowId > 0) {
					q.setParameter("flowId", flowId);
					qCount.setParameter("flowId", flowId);
				}

				if (stepId > 0) {
					q.setParameter("stepId", stepId);
					qCount.setParameter("stepId", stepId);
				}

				q.setMaxResults(pageSize);
				int startPosition = (pageIndex - 1) * pageSize;
				q.setFirstResult(startPosition);
				List<TaskDto> list = q.getResultList();

				Long total = (Long) qCount.getSingleResult();
				Page<TaskDto> result = new PageImpl<TaskDto>(list, pageable, total);
				return result;
			}
		}

		if (flowId > 0) {

		}
		return null;
	}

	@Override
	public TaskDto saveDaiLyWorks(TaskDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		if (dto != null) {
			Task entity = null;
			if (dto.getId() != null) {// Có khả năng là update
				entity = repository.findOne(dto.getId());// Lấy dữ liệu từ database ra.
			}
			if (entity == null) {// trường hợp thêm mới
				entity = new Task();
				entity.setCurrentState(0);
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto, entity, currentUserName, currentDate);// Thiết lập giá trị tại đây
			entity = repository.save(entity);
			return new TaskDto(entity);
		}
		return null;
	}
}
