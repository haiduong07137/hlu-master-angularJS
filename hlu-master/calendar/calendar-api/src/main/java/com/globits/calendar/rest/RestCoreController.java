package com.globits.calendar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.core.dto.RoomDto;
import com.globits.core.service.RoomService;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;

@RestController
@RequestMapping(path = "/api/core")
public class RestCoreController {
	@Autowired
	RoomService roomService;
	
	@RequestMapping(value = "/room/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public Page<RoomDto> getListRoom(@RequestBody StaffSearchDto dto,@PathVariable int pageSize,@PathVariable int pageIndex){
		return roomService.getListByPage(pageSize, pageIndex);
	}
	@RequestMapping(value = "/room/search/{keyword}/{pageSize}/{pageIndex}", method = RequestMethod.POST)
	public Page<RoomDto> searchRoom(@PathVariable  String keyword ,@PathVariable int pageSize,@PathVariable int pageIndex){
		return roomService.searchRoom(keyword, pageSize, pageIndex);
	}
}
