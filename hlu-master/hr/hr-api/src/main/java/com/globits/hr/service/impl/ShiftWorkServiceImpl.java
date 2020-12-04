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
import com.globits.hr.domain.ShiftWork;
import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.SearchShiftWorkDto;
import com.globits.hr.dto.ShiftWorkDto;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;
import com.globits.hr.repository.ShiftWorkRepository;
import com.globits.hr.repository.ShiftWorkTimePeriodRepository;
import com.globits.hr.service.ShiftWorkService;
import com.globits.security.domain.User;

@Transactional
@Service
public class ShiftWorkServiceImpl extends GenericServiceImpl<ShiftWork, Long> implements ShiftWorkService {
	@Autowired
	ShiftWorkRepository shiftWorkRepository;
	@Autowired
	ShiftWorkTimePeriodRepository shiftWorkTimePeriodRepository;

	@Override
	public Page<ShiftWorkDto> getPage(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return shiftWorkRepository.getListPage(pageable);
	}

	@Override
	public ShiftWorkDto saveShiftWork(ShiftWorkDto dto) {
		ShiftWork shiftWork = null;
		if (dto != null) {
			if (dto.getId() != null)
				shiftWork = shiftWorkRepository.findOne(dto.getId());

			if (shiftWork == null) {// Nếu không tìm thấy thì tạo mới 1 đối tượng
				shiftWork = new ShiftWork();
			}

			shiftWork.setCode(dto.getCode());
			shiftWork.setName(dto.getName());
			shiftWork.setTotalHours(dto.getTotalHours());
			List<ShiftWorkTimePeriod> tps = new ArrayList<>();

			if (dto.getTimePeriods() != null) {
				for (ShiftWorkTimePeriodDto swtDto : dto.getTimePeriods()) {
					ShiftWorkTimePeriod swt = null;

					if (swtDto.getId() != null)
						swt = shiftWorkTimePeriodRepository.findOne(swtDto.getId());

					if (swt == null) {
						swt = new ShiftWorkTimePeriod();
						swt.setShiftWork(shiftWork);
					}

					swt.setStartTime(swtDto.getStartTime());
					swt.setEndTime(swtDto.getEndTime());

					tps.add(swt);
				}
			}

			shiftWork.getTimePeriods().clear();
			shiftWork.getTimePeriods().addAll(tps);

			shiftWork = shiftWorkRepository.save(shiftWork);

			return new ShiftWorkDto(shiftWork);
		}
		return null;
	}

	@Override
	public Boolean deleteShiftWork(Long id) {
		ShiftWork shiftWork = shiftWorkRepository.findOne(id);
		if (shiftWork != null) {
			shiftWorkRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public ShiftWorkDto getShiftWork(Long id) {
		ShiftWork shiftWork = shiftWorkRepository.findOne(id);
		if (shiftWork != null) {
			return new ShiftWorkDto(shiftWork);
		}
		return null;
	}

	@Override
	public int deleteShiftWorks(List<ShiftWorkDto> dtos) {
		if (dtos == null) {
			return 0;
		}
		int ret = 0;
		for (ShiftWorkDto dto : dtos) {
			if (dto.getId() != null) {
				shiftWorkRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

	@Override
	public Page<ShiftWorkDto> searchShiftWork(SearchShiftWorkDto dto, int pageIndex, int pageSize) {
		String name = '%' + dto.getName() + '%';
		String code = '%' + dto.getCode() + '%';
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return shiftWorkRepository.searchByPage(name, code, pageable);
	}

}
