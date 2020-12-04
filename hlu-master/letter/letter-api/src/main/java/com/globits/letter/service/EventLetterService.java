package com.globits.letter.service;

import java.util.List;

import org.joda.time.DateTime;

import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.ViewDateEventDto;
import com.globits.letter.dto.EventLogDto;
import com.globits.letter.dto.ResultDataEventDto;

public interface EventLetterService {

	public ResultDataEventDto saveOne(EventDto dto);

	public ResultDataEventDto cancelEvent(Long eventId);

	public List<ViewDateEventDto> getEventPublic(DateTime date);

	public ResultDataEventDto restoreEvent(Long eventId);

	public Integer saveList(List<EventDto> dtos);

	public List<EventLogDto> getEventLog();

}
