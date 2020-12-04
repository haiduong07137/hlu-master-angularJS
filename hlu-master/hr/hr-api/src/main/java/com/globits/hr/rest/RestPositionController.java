package com.globits.hr.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.Constants;
import com.globits.hr.HrConstants;
import com.globits.hr.domain.Position;
import com.globits.hr.service.PositionService;

/*
 * Quản lý chức vụ công tác
 */
@RestController
@RequestMapping("/api/position")
public class RestPositionController {
	@Autowired
	private PositionService positionService;

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{positionId}", method = RequestMethod.GET)
	public Position getPosition(@PathVariable("positionId") String positionId) {
		return positionService.findById(new Long(positionId));
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<Position> getPositions(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return positionService.getList(pageIndex, pageSize);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{positionId}", method = RequestMethod.PUT)
	public Position updatePosition(@RequestBody Position position, @PathVariable("positionId") Long positionId) {
		return positionService.updatePosition(position, positionId);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	public Position savePosition(@RequestBody Position position) {
		return positionService.save(position);
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/{positionId}", method = RequestMethod.DELETE)
	public Position removePosition(@PathVariable("positionId") String positionId) {
		Position position = positionService.delete(new Long(positionId));
		return position;
	}
}
