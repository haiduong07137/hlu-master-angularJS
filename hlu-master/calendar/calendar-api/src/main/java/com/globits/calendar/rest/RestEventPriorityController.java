package com.globits.calendar.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.globits.calendar.dto.EventPriorityDto;
import com.globits.calendar.service.EventPriorityService;
import com.globits.core.utils.CommonUtils;

@RestController
@RequestMapping(path = "/api/priority")
public class RestEventPriorityController {

	@Autowired
	private EventPriorityService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<EventPriorityDto> getPriority(@PathVariable("id") Long id) {

		EventPriorityDto dto = service.getOne(id);

		if (dto == null) {
			return new ResponseEntity<EventPriorityDto>(new EventPriorityDto(), HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<EventPriorityDto>(dto, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<EventPriorityDto> savePriority(@RequestBody EventPriorityDto dto) {

		EventPriorityDto _dto = service.saveOne(dto);

		if (CommonUtils.isPositive(dto.getId(), true)) {
			return new ResponseEntity<EventPriorityDto>(_dto, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<EventPriorityDto>(dto, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deletePriorities(@RequestBody EventPriorityDto[] dtos) {

		boolean result = service.deleteMultiple(dtos);

		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ResponseEntity<Page<EventPriorityDto>> getPrioritiesPageable(
			@RequestParam(name = "page", required = true, defaultValue = "0") int pageIndex,
			@RequestParam(name = "size", required = true, defaultValue = "25") int pageSize) {

		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<EventPriorityDto> page = service.getPage(pageable);

		return new ResponseEntity<Page<EventPriorityDto>>(page, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getall", method = RequestMethod.GET)
	public ResponseEntity<List<EventPriorityDto>> getPriorities() {

		List<EventPriorityDto> page = service.getAll();

		return new ResponseEntity<List<EventPriorityDto>>(page, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<EventPriorityDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<EventPriorityDto> page = service.getList(pageSize, pageIndex);
		return new ResponseEntity<Page<EventPriorityDto>>(page, HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<EventPriorityDto>> getAllPriorities() {

		List<EventPriorityDto> results = service.getAll();

		return new ResponseEntity<List<EventPriorityDto>>(results, HttpStatus.OK);
	}
	
	@RequestMapping(value = "search/{textSearch}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public Page<EventPriorityDto> getPriority(@PathVariable String textSearch,@PathVariable int pageIndex, @PathVariable int pageSize) {
		return service.findPage(textSearch, pageIndex, pageSize);
	}
	
}
