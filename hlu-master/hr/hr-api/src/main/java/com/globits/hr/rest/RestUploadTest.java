package com.globits.hr.rest;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globits.core.Constants;
import com.globits.hr.HrConstants;

@RestController
@RequestMapping("/api/test_upload")
public class RestUploadTest {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Event.class, new EventEditor());
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(method = RequestMethod.POST)
	// @PostMapping()
	// public void uploadFile(HttpServletRequest req, @RequestParam("event") Event
	// event) {
	public void uploadFile(HttpServletRequest req, @RequestParam("event") Event event) {

		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) req;

		// Files
		Iterator<String> filenames = mRequest.getFileNames();
		if (filenames != null) {
			while (filenames.hasNext()) {
				MultipartFile file = mRequest.getFile(filenames.next());
				System.out.println(file.getContentType());
				System.out.println(file.getOriginalFilename());
				System.out.println("-----------------");
			}
		}

		// Event object
		System.out.println("---------------- Event object ----------------");
		System.out.println("Title -> " + event.getTitle());
		System.out.println("Description -> " + event.getDescription());

		// Additional data
		System.out.println("---------------- additional data --------------");
		Enumeration<String> params = mRequest.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			System.out.println(paramName + " --> " + mRequest.getParameter(paramName));
		}
	}
}

class Event implements Serializable {
	private static final long serialVersionUID = -3078112791353241758L;
	private String title;
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

class EventEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper();

		Event value = null;

		try {
			value = new Event();
			JsonNode root = mapper.readTree(text);
			value.setTitle(root.path("title").asText());
			value.setDescription(root.path("description").asText());
		} catch (IOException e) {
			// handle error
		}

		setValue(value);
	}
}
