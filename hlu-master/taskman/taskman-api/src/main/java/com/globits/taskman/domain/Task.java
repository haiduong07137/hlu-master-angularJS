package com.globits.taskman.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import com.globits.core.domain.BaseObject;
/*
 * Mỗi task tuân thủ một Flow nào đó.
 * Mỗi Flow sẽ có 1 số bước (Step) xử lý nào đó
 * Mỗi Step sẽ có 1 hoặc 1 số ROLE tham gia xử lý
 * 
 */
@Entity
@Table(name = "tbl_task")
@XmlRootElement
public class Task extends BaseObject{
	private static final long serialVersionUID = 4083584372545165622L;
	@Column(name="name")
	private String name;
	@Column(name="title")
	private String title;
	@Column(name="summary", length=4000)
	private String summary; 
	
	@Column(name="description", length=4000)
	private String description;
	
	@Column(name="start_date")
	private Date dateStart;//Ngày bắt đầu
	
	@Column(name="due_date")
	private Date dateDue;//Thời hạn
	
	@Column(name="complete_date")
	private Date completeDate;//Ngày hoàn thành
	
	@Column(name="estimate_time")
	private Double estimateTime;	//thời gian ước tính
	
	@Column(name="estimate_time_unit")
	private String estimateTimeUnit;	//đơn vị thời gian ước tính
	
	@Column(name="completion_rate")
	private Double completionRate;	//tỷ lệ hoàn thành
	
	@Column(name="duration")
	private Double duration;	//thời gian

	@Column(name="total_plan_effort")
	private Double totalPlanEffort;		//Tổng nỗ lực dự kiến

	
	@Column(name="total_effort")
	private Double totalEffort;		//Tổng nỗ lực thực tế

	
	@Column(name="current_state")
	private Integer currentState;//Trạng thái hiện thời : 0 = chưa bắt đầu, 1 = đang xử lý, 2= đã hoàn thành.
	
	@ManyToOne
	@JoinColumn(name="flow_id")
	private TaskFlow flow;//Thuộc Flow nào
	
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;	//dự án
	
	@ManyToOne
	@JoinColumn(name="step_id")
	private TaskStep currentStep;//Step hiện thời của task trong Flow là gì.
	@ManyToOne
	@JoinColumn(name="priority_id")
	private TaskPriority priority;	//mức độ ưu tiên
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Participate> participates;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<TaskFileAttachment> attachments = new HashSet<TaskFileAttachment>();
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name="parent_id")
	private Task parent;
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Task> subTask;

	public Integer getCurrentState() {
		return currentState;
	}
	public void setCurrentState(Integer currentState) {
		this.currentState = currentState;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public TaskFlow getFlow() {
		return flow;
	}
	public void setFlow(TaskFlow flow) {
		this.flow = flow;
	}
	public TaskStep getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(TaskStep currentStep) {
		this.currentStep = currentStep;
	}
	
	public Double getCompletionRate() {
		return completionRate;
	}
	public void setCompletionRate(Double completionRate) {
		this.completionRate = completionRate;
	}
	public TaskPriority getPriority() {
		return priority;
	}
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	public Set<Participate> getParticipates() {
		return participates;
	}
	public void setParticipates(Set<Participate> participates) {
		this.participates = participates;
	}
	public Task getParent() {
		return parent;
	}
	public void setParent(Task parent) {
		this.parent = parent;
	}
	public Set<Task> getSubTask() {
		return subTask;
	}
	public void setSubTask(Set<Task> subTask) {
		this.subTask = subTask;
	}
	public Set<TaskFileAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<TaskFileAttachment> attachments) {
		this.attachments = attachments;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
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
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	public Double getTotalPlanEffort() {
		return totalPlanEffort;
	}
	public void setTotalPlanEffort(Double totalPlanEffort) {
		this.totalPlanEffort = totalPlanEffort;
	}
	
}
