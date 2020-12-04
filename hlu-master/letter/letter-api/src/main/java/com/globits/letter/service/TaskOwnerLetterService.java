package com.globits.letter.service;

import java.util.List;

import com.globits.letter.dto.SearchTaskOwnerDto;
import com.globits.taskman.dto.TaskOwnerDto;

public interface TaskOwnerLetterService {

	List<TaskOwnerDto> getListTaskOwnerByRoleCode(String roleCode, SearchTaskOwnerDto search);

}
