package com.globits.taskman.service;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.taskman.domain.Participate;
import com.globits.taskman.dto.ParticipateDto;

public interface ParticipateService extends GenericService<Participate, Long> {
	public ParticipateDto getParticipate(Long id);
	public ParticipateDto saveParticipate(ParticipateDto dto);
	public Boolean deleteParticipate(Long id);
	public Page<ParticipateDto> getListParticipate(int pageSize, int pageIndex);
	public Participate setValue(ParticipateDto dto, Participate entity, String userName, LocalDateTime currentDate);
	public List<Participate> getTaskParticipate(Long taskId, Long ownerId, Long roleId, Integer participateType);
	public Participate getTaskParticipateBy(Long taskId, Long taskownerId, Long roleId);

}
