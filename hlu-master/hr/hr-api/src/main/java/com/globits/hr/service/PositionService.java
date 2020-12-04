package com.globits.hr.service;

import com.globits.core.service.GenericService;
import com.globits.hr.domain.Position;

public interface PositionService extends GenericService<Position, Long> {
	Position findById(Long id);

	Position updatePosition(Position staff, Long positionId);
}
