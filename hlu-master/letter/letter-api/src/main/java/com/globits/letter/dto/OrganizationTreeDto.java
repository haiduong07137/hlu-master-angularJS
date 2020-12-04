package com.globits.letter.dto;

import java.io.Serializable;

import com.globits.core.domain.Department;
import com.globits.core.domain.Organization;

public class OrganizationTreeDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -991560624348607019L;
	
	private String id;
	private String parent;
	private String icon;
	private String text;
	
	public OrganizationTreeDto(Organization organization) {
		if (organization == null) {
			return;
		}

		this.id = organization.getId().toString();
		this.text = organization.getName() + " - " + organization.getCode();
		this.icon = "fa fa-building";
		if (organization.getParent() != null) {
			this.parent = organization.getParent().getId().toString();
		} else {
			this.parent = "#";
		}
		
	}

	public String getId() {
		return id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}