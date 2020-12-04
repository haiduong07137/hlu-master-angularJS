package com.globits.taskman.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.service.TaskService;


@RestController
@RequestMapping("/api/taskman/tasks")
public class RestTaskController {
	@Autowired
	TaskService service;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TaskDto> getTask(@PathVariable("id") Long id) {
		TaskDto dto = service.getTask(id);
		return new ResponseEntity<TaskDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TaskDto> saveTask(@RequestBody TaskDto dto) {
		dto = service.saveTask(dto);
		return new ResponseEntity<TaskDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/saveDaiLyWorks",method = RequestMethod.POST)
	public ResponseEntity<TaskDto> saveDaiLyWorks(@RequestBody TaskDto dto) {
		dto = service.saveDaiLyWorks(dto);
		return new ResponseEntity<TaskDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		Boolean ret = service.deleteTask(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskDto> page = service.getListTask(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "getListTaskBy/{flowId}/{stepId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getAllTaskBy(@PathVariable("flowId") long flowId, @PathVariable("stepId") long stepId, @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
		Page<TaskDto> page = service.getListTaskBy(flowId, stepId, pageIndex, pageSize);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/getalldepandpersonal/{staffId}/{flowId}/{stepId}/{currentParticipateState}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getAllDepartmentAndPersonalTask(@PathVariable("staffId") Long staffId,@PathVariable("flowId") Long flowId,@PathVariable("stepId") Long stepId,@PathVariable Integer currentParticipateState,@PathVariable int pageSize,@PathVariable  int pageIndex) {
		Page<TaskDto> page = service.getAllDepartmentAndPersonalTask(staffId,flowId,stepId,currentParticipateState,pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "roottask/{projectId}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getListRootTaskByProjectId(@PathVariable Long projectId,@PathVariable int pageSize,@PathVariable int pageIndex) {
		Page<TaskDto> page = service.getListRootTaskByProjectId(projectId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "roottask/{projectId}/{stepId}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getListRootTaskByProjectIdAndStepId(@PathVariable Long projectId, @PathVariable Long stepId, @PathVariable int pageSize,@PathVariable int pageIndex) {
		Page<TaskDto> page = service.getListRootTaskByProjectIdAndStepId(projectId, stepId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "roottask/orderByType/{projectId}/{orderByType}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getListRootTaskByProjectIdAndOrderByType(@PathVariable Long projectId, @PathVariable int orderByType, @PathVariable int pageSize,@PathVariable int pageIndex) {
		Page<TaskDto> page = service.getListRootTaskByProjectIdAndOrderByType(projectId, orderByType, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "roottask/{projectId}/{stepId}/{orderByType}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getProjectTasksByStepIdAndOrderByType(@PathVariable Long projectId, @PathVariable Long stepId, @PathVariable int orderByType, @PathVariable int pageSize, @PathVariable int pageIndex) {
		Page<TaskDto> page = service.getProjectTasksByStepIdAndOrderByType(projectId, stepId, orderByType, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "myroottask/{projectId}/{stepId}/{priorityId}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getMyRootTaskByProjectIdAndStepId(@PathVariable Long projectId, @PathVariable Long stepId,@PathVariable Long priorityId, @PathVariable int pageSize,@PathVariable int pageIndex) {
		Page<TaskDto> page = service.getMyRootTaskByProjectIdAndStepId(projectId, stepId,priorityId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
}
