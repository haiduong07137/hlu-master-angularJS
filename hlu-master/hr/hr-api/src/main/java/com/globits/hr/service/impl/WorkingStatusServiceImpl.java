package com.globits.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.WorkingStatus;
import com.globits.hr.dto.WorkingStatusDto;
import com.globits.hr.repository.WorkingStatusRepository;
import com.globits.hr.service.WorkingStatusService;
import com.globits.security.domain.User;

@Transactional
@Service
public class WorkingStatusServiceImpl extends GenericServiceImpl<WorkingStatus, Long> implements WorkingStatusService {
	@Autowired
	WorkingStatusRepository workingStatusRepository;

	@Override
	public Page<WorkingStatusDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return workingStatusRepository.getListPage(pageable);
	}

	@Override
	public WorkingStatusDto saveWorkingStatus(WorkingStatusDto dto) {
		WorkingStatus workingStatus = null;
		if (dto != null) {
			if (dto.getId() != null)
				workingStatus = workingStatusRepository.findOne(dto.getId());

			if (workingStatus == null) {// Nếu không tìm thấy thì tạo mới 1 đối tượng
				workingStatus = new WorkingStatus();
			}
			workingStatus.setCode(dto.getCode());
			workingStatus.setName(dto.getName());
			workingStatus.setStatusValue(dto.getStatusValue());
			workingStatus = workingStatusRepository.save(workingStatus);
			return new WorkingStatusDto(workingStatus);
		}
		return null;
	}

	@Override
	public Boolean deleteWorkingStatus(Long id) {
		WorkingStatus workingStatus = workingStatusRepository.findOne(id);
		if (workingStatus != null) {
			workingStatusRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public WorkingStatusDto getWorkingStatus(Long id) {
		WorkingStatus workingStatus = workingStatusRepository.findOne(id);
		if (workingStatus != null) {
			return new WorkingStatusDto(workingStatus);
		}
		return null;
	}

	@Override
	public int deleteWorkingStatuses(List<WorkingStatusDto> dtos) {
		if (dtos == null) {
			return 0;
		}
		int ret = 0;
		for (WorkingStatusDto dto : dtos) {
			if (dto.getId() != null) {
				workingStatusRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}


}
