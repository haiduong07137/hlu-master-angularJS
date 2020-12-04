package com.globits.hr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.Position;
import com.globits.hr.repository.PositionRepository;
import com.globits.hr.service.PositionService;

@Transactional
@Service
public class PositionServiceImpl extends GenericServiceImpl<Position, Long> implements PositionService {
	@Autowired
	private PositionRepository positionRepository;

	public Position findById(Long id) {
		Position position = positionRepository.findById(id);
		return position;
	}

	public Position updatePosition(Position position, Long positionId) {
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();

		// User modifiedUser = null;
		// Date currentDate = new Date();
		// String currentUserName = "Unknown User";
		// if (authentication != null) {
		// modifiedUser = (User) authentication.getPrincipal();
		// currentUserName = modifiedUser.getUsername();
		// }
		Position updatePosition = positionRepository.findById(positionId);
		if (updatePosition != null) {
			// updatePosition.setModifyDate(currentDate);
			if (position.getName() != null)
				updatePosition.setName(position.getName());
			if (position.getDescription() != null)
				updatePosition.setDescription(position.getDescription());
			if (position.getStatus() > 0)
				updatePosition.setStatus(position.getStatus());

			// if (modifiedUser != null) {
			// updatePosition.setModifiedBy(currentUserName);
			// } else {
			// updatePosition.setModifiedBy("Unknown user modified");
			// }
		}

		return positionRepository.save(updatePosition);
	}
}
