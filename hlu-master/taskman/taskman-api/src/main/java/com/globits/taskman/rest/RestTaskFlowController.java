package com.globits.taskman.rest;

import java.util.List;

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
import com.globits.taskman.dto.TaskFlowDto;
import com.globits.taskman.service.TaskFlowService;


@RestController
@RequestMapping("/api/taskman/taskflow")
public class RestTaskFlowController {
	@Autowired
	TaskFlowService service;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<TaskFlowDto> getTaskFlow(@PathVariable("id") Long id) {
		TaskFlowDto dto = service.getTaskFlow(id);
		return new ResponseEntity<TaskFlowDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TaskFlowDto> saveTaskFlow(@RequestBody TaskFlowDto dto) {
		dto = service.saveTaskFlow(dto);
		return new ResponseEntity<TaskFlowDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN,TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		Boolean ret = service.deleteTaskFlow(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<TaskFlowDto>> getAll() {
		List<TaskFlowDto> list = service.getAll();
		return new ResponseEntity<List<TaskFlowDto>>(list, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskFlowDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskFlowDto> page = service.getListTaskFlow(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskFlowDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value ="getbycode/{code}",method = RequestMethod.GET)
	public ResponseEntity<TaskFlowDto> getTaskFlowByCode(@PathVariable String code) {
		TaskFlowDto ret = service.getTaskFlowByCode(code);
		return new ResponseEntity<TaskFlowDto>(ret, HttpStatus.OK);
	}
}
