package com.globits.letter.rest;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.ViewDateEventDto;
import com.globits.calendar.service.EventService;
import com.globits.core.dto.DepartmentTreeDto;
import com.globits.core.service.DepartmentService;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.StaffService;
import com.globits.letter.dto.EventLogDto;
import com.globits.letter.dto.ResultDataEventDto;
import com.globits.letter.dto.SearchTaskOwnerDto;
import com.globits.letter.service.EventLetterService;
import com.globits.letter.service.TaskOwnerLetterService;
import com.globits.taskman.dto.TaskOwnerDto;

@RestController
@RequestMapping(path = "/api/")
public class RestPublicController {
	@Autowired
	EventLetterService eventLetterService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	EventService eventService;
	@Autowired
	StaffService staffService;
	@Autowired
	TaskOwnerLetterService taskOwnerLetterService;

	@RequestMapping(value = "calendar/event/save",method = RequestMethod.POST)
	public ResultDataEventDto saveEvent(@RequestBody EventDto eventDto) {
		return eventLetterService.saveOne(eventDto);
	}
	
	@RequestMapping(value = "calendar/event/savelist",method = RequestMethod.POST)
	public ResponseEntity<Integer> saveList(@RequestBody List<EventDto> dtos) {
		Integer result = eventLetterService.saveList(dtos);
		return new ResponseEntity<Integer>(result, result!=null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/gettaskownerbyrolecode/{roleCode}", method = RequestMethod.POST)
	public List<TaskOwnerDto> getListTaskOwnerByRoleCode(@PathVariable String roleCode, @RequestBody SearchTaskOwnerDto search) {
		return taskOwnerLetterService.getListTaskOwnerByRoleCode(roleCode, search);
	}
	
	@RequestMapping(value = "calendar/event/cancel/{eventId}",method = RequestMethod.GET)
	public ResultDataEventDto cancelEvent(@PathVariable("eventId") Long eventId) {
		return eventLetterService.cancelEvent(eventId);
	}
	
	@RequestMapping(value = "calendar/eventlog",method = RequestMethod.GET)
	public List<EventLogDto> getEventLog() {
		return eventLetterService.getEventLog();
	}
	
	@RequestMapping(value = "calendar/event/restore/{eventId}",method = RequestMethod.GET)
	public ResultDataEventDto restoreEvent(@PathVariable("eventId") Long eventId) {
		return eventLetterService.restoreEvent(eventId);
	}

	@RequestMapping(value = "calendar/event/get-public-event/{startDate}",method = RequestMethod.GET)
	public List<ViewDateEventDto> getEventPublic(@PathVariable String startDate){
		DateTime date = DateTime.parse(startDate);
		return eventLetterService.getEventPublic(date);
	}
	
	@RequestMapping(value = "staff/departmenttreestaff",method = RequestMethod.GET)
	public List<DepartmentTreeDto> getTreeData() {
		return departmentService.getTreeData();
	}
	
	@RequestMapping(value = "calendar/savelistevent", method = RequestMethod.POST)
	public ResponseEntity<Integer> saveListEvent(@RequestBody List<EventDto> dtos) {
		Integer result = eventService.saveListEvents(dtos);
		return new ResponseEntity<Integer>(result, result!=null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "staff/getstaff/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<StaffDto>> findStaffs(@RequestBody StaffSearchDto dto, @PathVariable int pageIndex,
			@PathVariable int pageSize) {

		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<StaffDto> page = staffService.searchStaff(dto, pageSize, pageIndex);

		return new ResponseEntity<Page<StaffDto>>(page, HttpStatus.OK);
	}
}
