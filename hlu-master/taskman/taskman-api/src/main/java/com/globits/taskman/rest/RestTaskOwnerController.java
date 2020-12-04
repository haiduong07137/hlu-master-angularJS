package com.globits.taskman.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.PersonDto;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.TaskOwnerDto;
import com.globits.taskman.service.TaskOwnerService;

@RestController
@RequestMapping("/api/taskman/taskowner")
public class RestTaskOwnerController {
	@Autowired
	TaskOwnerService service;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/getcurrenttaskowner", method = RequestMethod.GET)
	public List<TaskOwnerDto> getListCurrentTaskOwner() {
		return service.getListCurrentTaskOwner();
	}
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/gettaskownerbyrolecode/{roleCode}", method = RequestMethod.GET)
	public List<TaskOwnerDto> getListTaskOwnerByRoleCode(@PathVariable String roleCode) {
		return service.getListTaskOwnerByRoleCode(roleCode);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/save_from_person", method = RequestMethod.POST)
	public TaskOwnerDto saveTaskOwnerFromPerson(@RequestBody PersonDto personDto) {
		return service.saveTaskOwnerFromPerson(personDto);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/get_one_from_person/{personId}", method = RequestMethod.GET)
	public TaskOwnerDto getOneTaskOwnerFromPerson(@PathVariable Long personId) {
		return service.getOneTaskOwnerFromPerson(personId);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/get_list_from_person/{personId}", method = RequestMethod.GET)
	public List<TaskOwnerDto> getListTaskOwnerFromPerson(@PathVariable Long personId) {
		return service.getListTaskOwnerFromPerson(personId);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/save_from_department", method = RequestMethod.POST)
	public TaskOwnerDto saveTaskOwnerFromDepartment(@RequestBody DepartmentDto departmentDto) {
		return service.saveTaskOwnerFromDepartment(departmentDto);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/get_one_from_department/{departmentId}", method = RequestMethod.GET)
	public TaskOwnerDto getOneTaskOwnerFromDepartment(@PathVariable Long departmentId) {
		return service.getOneTaskOwnerFromDepartment(departmentId);
	}
	
//	/*Get List Person*/
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "get_list_person/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<PersonDto>> getListPerson(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<PersonDto> page = service.getListPerson(pageSize, pageIndex);
		return new ResponseEntity<Page<PersonDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskOwnerDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskOwnerDto> page = service.getListTaskOwner(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskOwnerDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/treegrid/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskOwnerDto>> getListTreeGrid(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskOwnerDto> page = service.getListTreeGrid(pageSize, pageIndex);
		return new ResponseEntity<Page<TaskOwnerDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "searchTaskOwnerByName/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<Page<TaskOwnerDto>> searchTaskOwnerByName(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex, @RequestBody String text) {
		Page<TaskOwnerDto> page = service.searchTaskOwnerByName(pageSize, pageIndex, text);
		return new ResponseEntity<Page<TaskOwnerDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "searchTaskOwnerByNameAndProjectId/{projectId}/{text}/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<Page<TaskOwnerDto>> searchTaskOwnerByNameAndProjectId(@PathVariable("projectId") long projectId, @PathVariable("text") String text, @PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskOwnerDto> page = service.searchTaskOwnerByNameAndProjectId(projectId, pageSize, pageIndex, text);
		return new ResponseEntity<Page<TaskOwnerDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "searchTaskOwnerByProjectId/{projectId}/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<Page<TaskOwnerDto>> searchTaskOwnerByProjectId(@PathVariable("projectId") long projectId, @PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<TaskOwnerDto> page = service.searchTaskOwnerByProjectId(projectId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskOwnerDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "searchTaskOwnerByPersonName/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public ResponseEntity<Page<TaskOwnerDto>> searchTaskOwnerByPersonName(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex, @RequestBody String text) {
		Page<TaskOwnerDto> page = service.searchTaskOwnerByPersonName(pageSize, pageIndex, text);
		return new ResponseEntity<Page<TaskOwnerDto>>(page, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TaskOwnerDto> saveTaskOwner(@RequestBody TaskOwnerDto dto) {
		dto = service.saveTaskOwner(dto);
		return new ResponseEntity<TaskOwnerDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value ={"/{id}"},method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteTaskOwner(@PathVariable Long id) {
		Boolean ret = service.deleteTaskOwner(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteMultiple(@RequestBody TaskOwnerDto[] dtos) {
		boolean result = service.deleteMultipleTaskOwner(dtos);
		return new ResponseEntity<Boolean>(result, result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value ={"/{id}"},method = RequestMethod.GET)
	public ResponseEntity<TaskOwnerDto> getTaskOwner(@PathVariable Long id) {
		TaskOwnerDto ret = service.getTaskOwner(id);
		return new ResponseEntity<TaskOwnerDto>(ret, HttpStatus.OK);
	}
	
	/**
	 * @category method: POST allowed .return all task owner by text and task owner
	 *  not include id different task owner have id = {@code taskOwnerId}.
	 * @author khoadv56
	 * 
	 * */
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value ={"/excluding/{id}/{pageIndex}/{pageSize}"},method = RequestMethod.POST)
	public ResponseEntity<Page<TaskOwnerDto>> getTaskOwnerByTextSearchExcludingTaskOwner(@RequestBody List<String> text, @PathVariable("id") Long taskOwnerId, @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize) {
		String txt = null;
		if(text.size() > 0) {
			txt = text.get(0);
		}
		Page<TaskOwnerDto> result = service.getTaskOwnerByTextSearchExcludingTaskOwner(txt, taskOwnerId, pageIndex, pageSize);
		return new ResponseEntity<Page<TaskOwnerDto>>(result, HttpStatus.OK); 
	}
}
