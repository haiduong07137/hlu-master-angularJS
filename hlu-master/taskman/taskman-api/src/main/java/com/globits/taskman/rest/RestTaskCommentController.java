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
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.service.TaskCommentService;


@RestController
@RequestMapping("/api/taskman/taskcomment")
public class RestTaskCommentController {
	@Autowired
	TaskCommentService service;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TaskCommentDto> getTaskComment(@PathVariable("id") Long id) {
		TaskCommentDto dto = service.getTaskComment(id);
		return new ResponseEntity<TaskCommentDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TaskCommentDto> saveTaskComment(@RequestBody TaskCommentDto dto) {
		dto = service.saveTaskComment(dto);
		return new ResponseEntity<TaskCommentDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		Boolean ret = service.deleteTaskComment(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskCommentDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskCommentDto> page = service.getListTaskComment(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskCommentDto>>(page, HttpStatus.OK);
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/getcomment/{taskId}/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<TaskCommentDto>> getCommentByTask(@PathVariable Long taskId, @PathVariable int pageSize, @PathVariable int pageIndex) {
		Page<TaskCommentDto> result = service.getListTaskCommentByTaskId(taskId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskCommentDto>>(result, HttpStatus.OK);
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/deleteCommentById/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteCommentById(@PathVariable Long id) {
		boolean result = service.deleteTaskComment(id);
		return new ResponseEntity<Boolean>(result, result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{projectId}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskCommentDto>> getList(@PathVariable Long projectId, @PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskCommentDto> page = service.getListTaskCommentByTaskId(projectId,pageSize, pageIndex);
		return new ResponseEntity<Page<TaskCommentDto>>(page, HttpStatus.OK);
	}	
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/addedittaskcomment", method = RequestMethod.POST)
	public ResponseEntity<TaskCommentDto> addEditTaskComment(@RequestBody TaskCommentDto taskCommentDto) {
		TaskCommentDto task = service.addEditTaskComment(taskCommentDto);
		return new ResponseEntity<TaskCommentDto>(task, HttpStatus.OK);
	}	
}
