package com.globits.taskman.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.UserTaskOwnerDto;
import com.globits.taskman.service.UserTaskOwnerService;

@RestController
@RequestMapping("/api/taskman/user_task_owner")
public class RestUserTaskOwnerController {
	@Autowired
	UserTaskOwnerService service;
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public UserTaskOwnerDto saveUserTaskOwner(@RequestBody UserTaskOwnerDto dto) {
		return service.saveUserTaskOwner(dto);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public Boolean deleteUserTaskOwner(@PathVariable("id") Long id) {
		return service.deleteUserTaskOwner(id);
	}
	/**
	 * @param: uname is username of user
	 * @param: roleCode is code of task_role 
	 * @return: Boolean is result of find in UserTaskOwner if(uname, roleCode) have a pair exists return true else return false
	 * @author khoadv56
	 * */
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/check_user_has_taskrole/{uname}/{roleCode}",method = RequestMethod.GET)
	public Boolean checkUserHasTaskRoleByUserNameAndRoleCode(@PathVariable("uname") String uname, @PathVariable("roleCode") String roleCode) {
		return service.checkUserHasTaskRoleByUserName(uname, roleCode);
	}
	
}
