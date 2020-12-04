package com.globits.calendar.dto;

import java.util.List;

import org.joda.time.DateTime;

public class ViewDateEventDto implements Comparable {
	private int id;
	private String name;
	private DateTime date;
	private List<EventDto> events;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public List<EventDto> getEvents() {
		return events;
	}
	public void setEvents(List<EventDto> events) {
		this.events = events;
	}

	@Override
	public int compareTo(Object o) {
	   return this.id-((ViewDateEventDto)o).getId();
   }
	
}
