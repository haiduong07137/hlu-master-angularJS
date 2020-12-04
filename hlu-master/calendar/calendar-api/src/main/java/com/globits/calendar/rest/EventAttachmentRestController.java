package com.globits.calendar.rest;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.globits.calendar.dto.EventAttachmentDto;
import com.globits.calendar.service.EventAttachmentService;
import com.globits.core.utils.CommonUtils;

@RestController
@RequestMapping(path = "/api/calendar/document")
public class EventAttachmentRestController {

	@Autowired
	private EventAttachmentService service;

	@RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EventAttachmentDto> getDocument(@PathVariable("id") Long id) {
		EventAttachmentDto dto = service.getOne(id, false);

		if (dto == null) {
			return new ResponseEntity<EventAttachmentDto>(new EventAttachmentDto(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<EventAttachmentDto>(dto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EventAttachmentDto> saveDocument(@RequestBody EventAttachmentDto dto) {
		if (dto == null) {
			return new ResponseEntity<EventAttachmentDto>(new EventAttachmentDto(), HttpStatus.BAD_REQUEST);
		}

		dto = service.saveOne(dto);

		return new ResponseEntity<EventAttachmentDto>(dto, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> deleteDocuments(@RequestBody EventAttachmentDto[] dtos) {
		Boolean deleted = service.deleteMultiple(dtos);

		if (deleted) {
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/download/{documentId}", method = RequestMethod.GET)
	public void downloadDocument(@PathVariable("documentId") Long documentId, HttpServletResponse response) {

		if (!CommonUtils.isPositive(documentId, true)) {
			throw new RuntimeException();
		}

		EventAttachmentDto document = service.getOne(documentId, true);

		if (document == null) {
			throw new RuntimeException();
		}

//		String filename = document.getTitle();
//		String extension = "." + document.getExtension();

//		if (!CommonUtils.isEmpty(filename) && !filename.toLowerCase().endsWith(extension.toLowerCase())) {
//			filename = filename + extension;
//		}

		CacheControl cc = CacheControl.maxAge(360, TimeUnit.DAYS).cachePublic();

		response.addHeader("Access-Control-Expose-Headers", "x-filename");
		//response.addHeader("Content-disposition", "inline; filename=" + filename);
		//response.addHeader("x-filename", filename);
		response.setHeader("Cache-Control", cc.getHeaderValue());
		//response.setContentType(document.getContentType());

		try {
			//response.getOutputStream().write(document.getContent());
			response.flushBuffer();
			response.getOutputStream().close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}
}
