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
import com.globits.taskman.dto.TaskRoleDto;
import com.globits.taskman.service.TaskRoleService;


@RestController
@RequestMapping("/api/taskman/taskroles")
public class RestTaskRoleController {
	@Autowired
	TaskRoleService service;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TaskRoleDto> getTaskRole(@PathVariable("id") Long id) {
		TaskRoleDto dto = service.getTaskRole(id);
		return new ResponseEntity<TaskRoleDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TaskRoleDto> saveTaskRole(@RequestBody TaskRoleDto dto) {
		dto = service.saveTaskRole(dto);
		return new ResponseEntity<TaskRoleDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		Boolean ret = service.deleteTaskRole(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskRoleDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskRoleDto> page = service.getListTaskRole(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskRoleDto>>(page, HttpStatus.OK);
	}
	
}
