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
import com.globits.taskman.dto.TaskPriorityDto;
import com.globits.taskman.service.TaskPriorityService;


@RestController
@RequestMapping("/api/taskman/taskpriority")
public class RestTaskPriorityController {
	@Autowired
	TaskPriorityService service;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TaskPriorityDto> getTaskPriority(@PathVariable("id") Long id) {
		TaskPriorityDto dto = service.getTaskPriority(id);
		return new ResponseEntity<TaskPriorityDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TaskPriorityDto> saveTaskPriority(@RequestBody TaskPriorityDto dto) {
		dto = service.saveTaskPriority(dto);
		return new ResponseEntity<TaskPriorityDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		Boolean ret = service.deleteTaskPriority(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskPriorityDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskPriorityDto> page = service.getListTaskPriority(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskPriorityDto>>(page, HttpStatus.OK);
	}
	
}
