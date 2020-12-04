package com.globits.hr.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.globits.core.service.GenericService;
import com.globits.hr.domain.ShiftWorkTimePeriod;
import com.globits.hr.dto.ShiftWorkTimePeriodDto;

public interface ShiftWorkTimePeriodService extends GenericService<ShiftWorkTimePeriod, Long> {
	public Page<ShiftWorkTimePeriodDto> getPage(int pageSize, int pageIndex);
	public ShiftWorkTimePeriodDto saveShiftWorkTimePeriod(ShiftWorkTimePeriodDto dto);
	public Boolean deleteShiftWorkTimePeriod(Long id);
	public ShiftWorkTimePeriodDto getShiftWorkTimePeriod(Long id);		
	public List<ShiftWorkTimePeriodDto> getAllByShiftWorkId(long shiftworkId);
	public int deleteShiftWorkTimePeriods(List<ShiftWorkTimePeriodDto> dtos);
}
