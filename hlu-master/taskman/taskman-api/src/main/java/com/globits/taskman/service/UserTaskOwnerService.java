package com.globits.taskman.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.dto.UserTaskOwnerDto;

public interface UserTaskOwnerService extends GenericService<UserTaskOwner, Long> {
	public UserTaskOwnerDto getUserTaskOwner(Long id);
	public UserTaskOwnerDto saveUserTaskOwner(UserTaskOwnerDto dto);
	public Boolean deleteUserTaskOwner(Long id);
	public Page<UserTaskOwnerDto> getListUserTaskOwner(int pageSize, int pageIndex);
	public UserTaskOwner setValue(UserTaskOwnerDto dto, UserTaskOwner entity, String userName, LocalDateTime currentDate);
	public List<UserTaskOwner> getListTaskUserByOwnerId(Long userId);
	public Boolean checkUserHasTaskRoleByUserName(String username, String roleCode);
}
