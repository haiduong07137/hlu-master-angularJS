package com.globits.taskman.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.globits.core.dto.BaseObjectDto;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.dto.PersonDto;
import com.globits.taskman.domain.CommentFileAttachment;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskComment;
import com.globits.taskman.domain.TaskFileAttachment;
import com.globits.taskman.domain.TaskFlowStep;
import com.globits.taskman.domain.UserTaskOwner;

public class TaskDto extends BaseObjectDto{
	private String name;
	private String title;
	private String summary; 
	private String description;
	
	private Date dateStart;
	private Date dateDue;
	private Date dateCreateConvert;
	
	private TaskFlowDto flow;
	private List<TaskDto> children;//Các Task con - tương ứng với Sub-Task
	private TaskDto parent;//Task cha
	private Double estimateTime;	//thời gian ước tính
	private String estimateTimeUnit;	//đơn vị thời gian ước tính
	private Double completionRate;	//tỷ lệ hoàn thành
	private Double duration;	//thời gian
	private Double totalEffort;		//tổng nỗ lực - tính bằng tổng số thời gian dự kiến của các công việc thành phần
	private Integer currentState;//Trạng thái hiện thời : 0 = chưa bắt đầu, 1 = đang xử lý, 2= đã hoàn thành.
	
	private TaskStepDto currentStep;
	private TaskPriorityDto priority;
	private Set<TaskFileAttachmentDto> attachments = new HashSet<TaskFileAttachmentDto>();
	private ParticipateDto chairman;
	private Set<ParticipateDto> participates;
	
	private ProjectDto project;
	private boolean isHaveChil;
	
	private boolean hasCommentPermission = true;//Có quyền thêm comment
	private boolean isEditable = true;//Được quyền sửa
	private boolean isDeletable = true;//Được quyền xóa
	private boolean hasChairmanPermission=false;
	
	public Date getDateCreateConvert() {
		return dateCreateConvert;
	}

	public void setDateCreateConvert(Date dateCreateConvert) {
		this.dateCreateConvert = dateCreateConvert;
	}

	public void setHasCommentPermission(boolean hasCommentPermission) {
		this.hasCommentPermission = hasCommentPermission;
	}

	public boolean isHaveChil() {
		return isHaveChil;
	}

	public void setHaveChil(boolean isHaveChil) {
		this.isHaveChil = isHaveChil;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}

	public ParticipateDto getChairman() {
		return chairman;
	}

	public void setChairman(ParticipateDto chairman) {
		this.chairman = chairman;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(Double completionRate) {
		this.completionRate = completionRate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public TaskFlowDto getFlow() {
		return flow;
	}

	public void setFlow(TaskFlowDto flow) {
		this.flow = flow;
	}

	public ProjectDto getProject() {
		return project;
	}

	public void setProject(ProjectDto project) {
		this.project = project;
	}

	public TaskStepDto getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(TaskStepDto currentStep) {
		this.currentStep = currentStep;
	}

	public TaskPriorityDto getPriority() {
		return priority;
	}

	public void setPriority(TaskPriorityDto priority) {
		this.priority = priority;
	}

	public Set<ParticipateDto> getParticipates() {
		return participates;
	}

	public void setParticipates(Set<ParticipateDto> participates) {
		this.participates = participates;
	}

	public Set<TaskFileAttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<TaskFileAttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public Boolean getHasCommentPermission() {
		return hasCommentPermission;
	}

	public void setHasCommentPermission(Boolean hasCommentPermission) {
		this.hasCommentPermission = hasCommentPermission;
	}

	public List<TaskDto> getChildren() {
		return children;
	}

	public void setChildren(List<TaskDto> children) {
		this.children = children;
	}

	public TaskDto getParent() {
		return parent;
	}

	public void setParent(TaskDto parent) {
		this.parent = parent;
	}

	public Double getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(Double estimateTime) {
		this.estimateTime = estimateTime;
	}

	public String getEstimateTimeUnit() {
		return estimateTimeUnit;
	}

	public void setEstimateTimeUnit(String estimateTimeUnit) {
		this.estimateTimeUnit = estimateTimeUnit;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Double getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Double totalEffort) {
		this.totalEffort = totalEffort;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isDeletable() {
		return isDeletable;
	}

	public void setDeletable(boolean isDeletable) {
		this.isDeletable = isDeletable;
	}


	public boolean isHasChairmanPermission() {
		return hasChairmanPermission;
	}

	public void setHasChairmanPermission(boolean hasChairmanPermission) {
		this.hasChairmanPermission = hasChairmanPermission;
	}

	public List<TaskDto> getListChildren(Task domain, Long currentStepId, Long userId){
		String hardCodeChairman = "PROJECT-CHAIRMAN";
		List<TaskDto> ret = new ArrayList<TaskDto>();
		
		if(domain.getSubTask()!=null && domain.getSubTask().size()>0) {
			
			for(Task task : domain.getSubTask()) {
				Boolean isOk=true;
				if(userId!=null) {
					isOk=false;
					for(Participate p : task.getParticipates()) {
						for(UserTaskOwner uto:p.getTaskOwner().getUserTaskOwners()) {
							isOk = isOk||(uto.getUser()!=null && uto.getUser().getId().equals(userId));
						}
					}
				}
				if(isOk && (currentStepId ==null ||(task.getCurrentStep()!=null && task.getCurrentStep().getId()==currentStepId))) {
					TaskDto taskDto = new TaskDto();
					taskDto.setId(task.getId());
					taskDto.setName(task.getName());
					taskDto.setDateStart(task.getDateStart());
					taskDto.setDateDue(task.getDateDue());
					taskDto.setDescription(task.getDescription());
					taskDto.setSummary(task.getSummary());
					taskDto.setTitle(task.getTitle());
					taskDto.setEstimateTime(task.getEstimateTime());
					taskDto.setEstimateTimeUnit(task.getEstimateTimeUnit());
					taskDto.setCompletionRate(task.getCompletionRate());
					taskDto.setDuration(task.getDuration());
					taskDto.setTotalEffort(task.getTotalEffort());
					taskDto.setCurrentState(task.getCurrentState());
					taskDto.setDateCreateConvert(task.getCreateDate().toDate());
					taskDto.setCreatedBy(task.getCreatedBy());

					//hard code tạm thời = true
					taskDto.isDeletable = this.isDeletable;
					taskDto.isEditable = this.isEditable;
					taskDto.hasCommentPermission = this.hasCommentPermission;
					
					if (task.getParent() != null) {
						taskDto.setParent(new TaskDto());
						TaskDto parentsub = new TaskDto();
						parentsub.setId(task.getParent().getId());
						parentsub.setName(task.getParent().getName());
						parentsub.setDateStart(task.getParent().getDateStart());
						parentsub.setDateDue(task.getParent().getDateDue());
						if (task.getParent().getPriority() != null) {
	
							TaskPriorityDto taskPriorityDto = new TaskPriorityDto();
							taskPriorityDto.setId(task.getParent().getPriority().getId());
							taskPriorityDto.setCode(task.getParent().getPriority().getCode());
							taskPriorityDto.setName(task.getParent().getPriority().getName());
	
							parentsub.setPriority(taskPriorityDto);
						}
						if(task.getParent().getCurrentStep() != null) {
	
							TaskStepDto taskStep = new TaskStepDto();
							taskStep.setId(task.getParent().getCurrentStep().getId());
							taskStep.setCode(task.getParent().getCurrentStep().getCode());
							taskStep.setName(task.getParent().getCurrentStep().getName());
	
							parentsub.setCurrentStep(taskStep);
						}
	
						taskDto.setParent(parentsub);
					}
					if (task.getPriority() != null) {
						taskDto.setPriority(new TaskPriorityDto());
						TaskPriorityDto taskPriorityDto = new TaskPriorityDto();
						taskPriorityDto.setId(task.getPriority().getId());
						taskPriorityDto.setCode(task.getPriority().getCode());
						taskPriorityDto.setName(task.getPriority().getName());
	
						taskDto.setPriority(taskPriorityDto);
					}
					if(task.getCurrentStep() != null) {
						taskDto.setCurrentStep(new TaskStepDto());
						TaskStepDto taskStep = new TaskStepDto();
						taskStep.setId(task.getCurrentStep().getId());
						taskStep.setCode(task.getCurrentStep().getCode());
						taskStep.setName(task.getCurrentStep().getName());
	
						taskDto.setCurrentStep(taskStep);
					}
					if (task.getProject() != null) {
						taskDto.setProject(new ProjectDto());
						ProjectDto project = new ProjectDto();
						project.setId(task.getProject().getId());
						project.setCode(task.getProject().getCode());
						project.setName(task.getProject().getName());
						
						taskDto.setProject(project);
					}
					if (task.getParticipates() != null && task.getParticipates().size() > 0) {
						Boolean isChairman = false;
						taskDto.setParticipates(new HashSet<ParticipateDto>());
						for(Participate p:task.getParticipates()) {
							isChairman = false;
							ParticipateDto pcDto = new ParticipateDto();
							pcDto.setId(p.getId());
							pcDto.setDisplayName(p.getDisplayName());
							pcDto.setParticipateType(p.getParticipateType());
							pcDto.setComments(new HashSet<TaskCommentDto>());
							if(p.getComments()!=null) {
								pcDto.setComments(new HashSet<TaskCommentDto>());
								for(TaskComment m : p.getComments()) {
									TaskCommentDto mDto = new TaskCommentDto();
									mDto.setComment(m.getComment());
									mDto.setId(m.getId());
									pcDto.getComments().add(mDto);
								}
							}
							if(p.getRole()!=null) {
								pcDto.setRole(new TaskRoleDto());
								pcDto.getRole().setId(p.getRole().getId());
								pcDto.getRole().setCode(p.getRole().getCode());
								pcDto.getRole().setName(p.getRole().getName());
								if (p.getRole().getCode().equals(hardCodeChairman)) {
									isChairman = true;
									if(p.getTaskOwner() != null && p.getTaskOwner().getUserTaskOwners() != null && p.getTaskOwner().getUserTaskOwners().size() > 0) {
										for(UserTaskOwner uto: p.getTaskOwner().getUserTaskOwners()) {
											if(uto.getUser().getId().equals(userId)) {
												hasChairmanPermission=true;
											}
										}
									}
								}
							}
							if(p.getTaskOwner()!=null) {
								pcDto.setTaskOwner(new TaskOwnerDto());
								pcDto.getTaskOwner().setId(p.getTaskOwner().getId());
								pcDto.getTaskOwner().setDisplayName(p.getTaskOwner().getDisplayName());
								pcDto.getTaskOwner().setOwnerType(p.getTaskOwner().getOwnerType());
							}
							taskDto.getParticipates().add(pcDto);
	
							//lấy ra chairman cho child
							if (isChairman) {
								taskDto.setChairman(pcDto);
							}
						}
					}
					
					if(task.getAttachments()!=null && task.getAttachments().size() > 0) {
						taskDto.setAttachments(new HashSet<TaskFileAttachmentDto>());
						for(TaskFileAttachment a:task.getAttachments()) {
							if(a!=null) {
								TaskFileAttachmentDto aDto = new TaskFileAttachmentDto();
								aDto.setId(a.getId());
								if(a.getFile()!=null) {
									aDto.setFile(new FileDescriptionDto());
									aDto.getFile().setContentSize(a.getFile().getContentSize());
									aDto.getFile().setContentType(a.getFile().getContentType());
									aDto.getFile().setExtension(a.getFile().getExtension());
									aDto.getFile().setFilePath(a.getFile().getFilePath());		
									aDto.getFile().setName(a.getFile().getName());
									aDto.getFile().setId(a.getFile().getId());
								}
				
								taskDto.getAttachments().add(aDto);				
							}
						}
					}
					taskDto.setChildren(getListChildren(task, currentStepId, userId));
					ret.add(taskDto);
				}
				this.setHaveChil(ret!=null && ret.size()>0);
			}
		}
		return ret;
	}

	public TaskDto() {
		
	}
	public TaskDto(Task domain) {
		this(domain,null);
	}
	
	public TaskDto(Task domain, Long currrentStepId) {
		this(domain,currrentStepId,null);
	}
	public TaskDto(Task domain, Long currrentStepId, Long userId) {
		String hardCodeChairman = "PROJECT-CHAIRMAN";
		
		this.setId(domain.getId());
		if(domain.getFlow()!=null) {
			this.flow= new TaskFlowDto();
			this.flow.setId(domain.getFlow().getId());
			this.flow.setCode(domain.getFlow().getCode());
			this.flow.setName(domain.getFlow().getName());

			this.flow.setSteps(new ArrayList<TaskFlowStepDto>());
			if (domain.getFlow().getSteps() != null && domain.getFlow().getSteps().size() > 0) {
				for(TaskFlowStep tfs : domain.getFlow().getSteps()) {
					TaskFlowStepDto tfsDto = new TaskFlowStepDto();
					
					tfsDto.setId(tfs.getId());
					tfsDto.setOrderIndex(tfs.getOrderIndex());
					tfsDto.setIsMandatory(tfs.getIsMandatory());
					if(tfs.getStep()!=null){
						TaskStepDto dto=new TaskStepDto();
						dto.setId(tfs.getStep().getId());
						dto.setCode(tfs.getStep().getCode());
						dto.setName(tfs.getStep().getName());
						tfsDto.setStep(dto);
					}				
					this.flow.getSteps().add(tfsDto);
				}
			}
		}
		if(domain.getCurrentStep() != null) {
			this.currentStep = new TaskStepDto();
			this.currentStep.setId(domain.getCurrentStep().getId());
			this.currentStep.setCode(domain.getCurrentStep().getCode());
			this.currentStep.setName(domain.getCurrentStep().getName());
		}
		this.name=domain.getName();
		this.dateDue = domain.getDateDue();
		this.dateStart=domain.getDateStart();
		this.description=domain.getDescription();
		this.summary=domain.getSummary();
		this.title=domain.getTitle();
		this.estimateTime=domain.getEstimateTime();
		this.estimateTimeUnit =domain.getEstimateTimeUnit();
		this.completionRate = domain.getCompletionRate();
		this.duration = domain.getDuration();
		this.totalEffort = domain.getTotalEffort();
		this.currentState = domain.getCurrentState();
		this.dateCreateConvert = domain.getCreateDate().toDate();
		this.createdBy = domain.getCreatedBy();
		this.createDate = domain.getCreateDate();
		this.modifyDate = domain.getModifyDate();
		//hard code tạm thời = true
		this.isDeletable = true;
		this.isEditable = true;
		this.hasCommentPermission = true;
		
		if (domain.getParent() != null) {
			this.parent = new TaskDto();
			this.parent.setId(domain.getParent().getId());
			this.parent.setName(domain.getParent().getName());
			this.parent.setDateStart(domain.getParent().getDateStart());
			this.parent.setDateDue(domain.getParent().getDateDue());
			if(domain.getParent().getCurrentStep() != null) {
				this.parent.currentStep = new TaskStepDto();
				this.parent.currentStep.setId(domain.getParent().getCurrentStep().getId());
				this.parent.currentStep.setCode(domain.getParent().getCurrentStep().getCode());
				this.parent.currentStep.setName(domain.getParent().getCurrentStep().getName());
			}
			if (domain.getParent().getPriority() != null) {
				this.parent.priority = new TaskPriorityDto();
				this.parent.priority.setId(domain.getParent().getPriority().getId());
				this.parent.priority.setCode(domain.getParent().getPriority().getCode());
				this.parent.priority.setName(domain.getParent().getPriority().getName());
			}
		}
		if (domain.getPriority() != null) {
			this.priority = new TaskPriorityDto();
			this.priority.setId(domain.getPriority().getId());
			this.priority.setCode(domain.getPriority().getCode());
			this.priority.setName(domain.getPriority().getName());
		}
		if (domain.getProject() != null) {
			this.project = new ProjectDto();
			this.project.setId(domain.getProject().getId());
			this.project.setCode(domain.getProject().getCode());
			this.project.setName(domain.getProject().getName());
		}
		if(domain.getParticipates()!=null) {
			Boolean isChairman = false;
			this.participates = new HashSet<ParticipateDto>();
			for(Participate p : domain.getParticipates()) {
				isChairman = false;
				ParticipateDto pDto = new ParticipateDto();
				pDto.setId(p.getId());
				pDto.setDisplayName(p.getDisplayName());
				pDto.setComments(new HashSet<TaskCommentDto>());
				pDto.setParticipateType(p.getParticipateType());
				if(p.getComments()!=null) {
					pDto.setComments(new HashSet<TaskCommentDto>());
					for(TaskComment m : p.getComments()) {
						TaskCommentDto mDto = new TaskCommentDto();
						mDto.setComment(m.getComment());
						mDto.setCreatedBy(m.getCreatedBy());
						mDto.setCreateDate(m.getCreateDate());
						mDto.setModifyDate(m.getModifyDate());
						mDto.setId(m.getId());
						
						for(CommentFileAttachment att : m.getAttachments() ){
							if(att!=null) {
								CommentFileAttachmentDto aDto = new CommentFileAttachmentDto();
								aDto.setId(att.getId());
								if(att.getFile()!=null) {
									aDto.setFile(new FileDescriptionDto());
									aDto.getFile().setContentSize(att.getFile().getContentSize());
									aDto.getFile().setContentType(att.getFile().getContentType());
									aDto.getFile().setExtension(att.getFile().getExtension());
									aDto.getFile().setFilePath(att.getFile().getFilePath());		
									aDto.getFile().setName(att.getFile().getName());
									aDto.getFile().setId(att.getFile().getId());
								}
								mDto.getAttachments().add(aDto);
							}
						}
						pDto.getComments().add(mDto);
					}
				}
				if(p.getRole()!=null) {
					pDto.setRole(new TaskRoleDto());
					pDto.getRole().setId(p.getRole().getId());
					pDto.getRole().setCode(p.getRole().getCode());
					pDto.getRole().setName(p.getRole().getName());
					if (p.getRole().getCode().equals(hardCodeChairman)) {
						isChairman = true;
					}
				}
				if(p.getTaskOwner()!=null) {
					pDto.setTaskOwner(new TaskOwnerDto());
					pDto.getTaskOwner().setId(p.getTaskOwner().getId());
					pDto.getTaskOwner().setDisplayName(p.getTaskOwner().getDisplayName());
					pDto.getTaskOwner().setOwnerType(p.getTaskOwner().getOwnerType());
					pDto.getTaskOwner().setCreatedBy(p.getTaskOwner().getCreatedBy());
					if (p.getTaskOwner().getPerson() != null) {
						pDto.getTaskOwner().setPerson(new PersonDto());
						pDto.getTaskOwner().getPerson().setId(p.getTaskOwner().getPerson().getId());
					}
					
					Set<UserTaskOwner> userTaskOwners = null;
					userTaskOwners = p.getTaskOwner().getUserTaskOwners();
					List<UserTaskOwnerDto> userTaskOwnerDtos = new ArrayList<UserTaskOwnerDto>();
					
					if(userTaskOwners != null && userTaskOwners.size() > 0) {
						for(UserTaskOwner userTaskOwner: userTaskOwners) {
							if(userTaskOwner != null) {
								userTaskOwnerDtos.add(new UserTaskOwnerDto(userTaskOwner));
							}
						}
					}
					pDto.getTaskOwner().setUserTaskOwners(new ArrayList<UserTaskOwnerDto>());
					pDto.getTaskOwner().getUserTaskOwners().addAll(userTaskOwnerDtos);
				}

				//lấy ra chairman
				if (isChairman) {
					this.chairman = new ParticipateDto();
					this.chairman = pDto;
				}
				this.participates.add(pDto);
			}
		}
		if(domain.getAttachments()!=null) {
			for(TaskFileAttachment a:domain.getAttachments()) {
				if(a!=null) {
					TaskFileAttachmentDto aDto = new TaskFileAttachmentDto();
					aDto.setId(a.getId());
					if(a.getFile()!=null) {
						aDto.setFile(new FileDescriptionDto());
						aDto.getFile().setContentSize(a.getFile().getContentSize());
						aDto.getFile().setContentType(a.getFile().getContentType());
						aDto.getFile().setExtension(a.getFile().getExtension());
						aDto.getFile().setFilePath(a.getFile().getFilePath());		
						aDto.getFile().setName(a.getFile().getName());
						aDto.getFile().setId(a.getFile().getId());
					}
	
					this.attachments.add(aDto);				
				}
			}
		}

		this.setChildren(getListChildren(domain, currrentStepId,userId));
		
	}
}
