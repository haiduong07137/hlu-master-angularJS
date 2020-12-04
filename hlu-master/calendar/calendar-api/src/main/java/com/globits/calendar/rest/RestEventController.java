package com.globits.calendar.rest;

import java.beans.PropertyEditorSupport;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.cfg.FormatConfig;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.globits.calendar.Constants;
import com.globits.calendar.dto.CalendarPermissionDto;
import com.globits.calendar.dto.EventAttachmentDto;
import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.EventQueryParamDto;
import com.globits.calendar.dto.SearchEventDto;
import com.globits.calendar.dto.ViewDateEventDto;
import com.globits.calendar.service.EventService;
import com.globits.core.domain.FileDescription;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.service.FileDescriptionService;
import com.globits.core.utils.CommonUtils;
import com.globits.core.utils.FileUtils;

class EventEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		ObjectMapper mapper = new ObjectMapper();
		EventDto value = null;
        JodaModule jodaModule = new JodaModule();
        jodaModule.addDeserializer(DateTime.class,DateTimeDeserializer.forType(DateTime.class));
        mapper.registerModule(jodaModule);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			value = mapper.readValue(text, EventDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setValue(value);
	}
}

@RestController
@RequestMapping(path = "/api/calendar/event")
public class RestEventController {
	private static  String FolderPath =null;;

	@Autowired
	private Environment env;
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(EventDto.class, new EventEditor());
	}
	@Autowired
	private EventService eventService;
	
	@Autowired
	private FileDescriptionService fileService;

	@RequestMapping(value = "/getpermission", method = RequestMethod.GET)
	public CalendarPermissionDto getPermission(){
		return eventService.getPermission();
	}
	
	@RequestMapping(value = "/getbyweek/{startDate}", method = RequestMethod.GET)
	public List<ViewDateEventDto> getListEventByWeek(@PathVariable String startDate){
		DateTime date = DateTime.parse(startDate);
		return eventService.getListEventByWeek(date);
	}
	
	@RequestMapping(value = "/getByWeekAndStatusPublish/{startDate}", method = RequestMethod.GET)
	public List<ViewDateEventDto> getListEventByWeekAndStatusPublish(@PathVariable String startDate){
		DateTime date = DateTime.parse(startDate);
		return eventService.getListEventByWeekAndStatusPublish(date);
	}
	
	@RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
	public ResponseEntity<EventDto> getEvent(@PathVariable("eventId") Long eventId) {
		EventDto dto = eventService.getOne(eventId);

		return new ResponseEntity<EventDto>(dto, HttpStatus.OK);
	}
	@Secured({com.globits.core.Constants.ROLE_ADMIN, Constants.ROLE_CALENDAR_MANAGEMENT,Constants.CALENDAR_PUBLISHER_ROLE})
	@RequestMapping(value = "/publishAll", method = RequestMethod.POST)
	public ResponseEntity<Integer> publishAll(@RequestBody List<EventDto> dtos) {
		Integer result = eventService.approveEvents(dtos);
		
		return new ResponseEntity<Integer>(result, result!=null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, Constants.ROLE_CALENDAR_MANAGEMENT,Constants.CALENDAR_PUBLISHER_ROLE})
	@RequestMapping(value = "/savelistevent", method = RequestMethod.POST)
	public ResponseEntity<Integer> saveListEvent(@RequestBody List<EventDto> dtos) {
		Integer result = eventService.saveListEvents(dtos);
		return new ResponseEntity<Integer>(result, result!=null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@Secured({com.globits.core.Constants.ROLE_ADMIN,Constants.CALENDAR_APPROVER_ROLE, Constants.ROLE_CALENDAR_MANAGEMENT,Constants.CALENDAR_PUBLISHER_ROLE})
	@RequestMapping(value = "/approveAll", method = RequestMethod.POST)
	public ResponseEntity<Integer> approveAll(@RequestBody List<EventDto> dtos) {
		Integer result = eventService.approveEvents(dtos);
		
		return new ResponseEntity<Integer>(result, result!=null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<EventDto> saveEvent(@RequestBody EventDto eventDto) {
		EventDto dto = eventService.saveOne(eventDto);

		if (CommonUtils.isPositive(eventDto.getId(), true)) {
			return new ResponseEntity<EventDto>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<EventDto>(dto, HttpStatus.CREATED);
		}
	}
	//public void uploadFile(HttpServletRequest req, @RequestParam("event") Event event) {
	@RequestMapping(value = "/eventfile",method = RequestMethod.POST)
	public ResponseEntity<EventDto> saveEventWithFile(HttpServletRequest req, @RequestParam("event") EventDto eventDto) {
		
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) req;
		// Files
		Iterator<String> filenames = mRequest.getFileNames();
		if (filenames != null) {
			while (filenames.hasNext()) {
				MultipartFile file = mRequest.getFile(filenames.next());
				try {
					ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes());
					System.out.println(" Save File some where in the Disk of Server");
					
					System.out.println(file.getContentType());
					System.out.println(file.getOriginalFilename());
					EventAttachmentDto attachment = new EventAttachmentDto();
//					attachment.setTitle(file.getOriginalFilename());
//					attachment.setContent(file.getBytes());
//					attachment.setContentType(file.getContentType());
					attachment.setCreateDate(LocalDateTime.now());
					attachment.setCreatedBy("Test");//Thay đoạn này sao
//					attachment.setContentLength(file.getSize());
					eventDto.getAttachments().clear();
					eventDto.getAttachments().add(attachment);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		EventDto dto = eventService.saveOne(eventDto);

		if (CommonUtils.isPositive(eventDto.getId(), true)) {
			return new ResponseEntity<EventDto>(dto, HttpStatus.OK);
		} else {
			return new ResponseEntity<EventDto>(dto, HttpStatus.CREATED);
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<List<EventDto>> listEventByScope(@RequestBody EventQueryParamDto params) {
		List<EventDto> list = eventService.getEvents(params);

		return new ResponseEntity<List<EventDto>>(list, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteEvents(@RequestBody EventDto[] dtos) {

		boolean result = eventService.deleteMultiple(dtos);

		return new ResponseEntity<Boolean>(result, result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/eventfile/{fileId}",method = RequestMethod.GET)
	public void downloadFileResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileId") Long fileId) throws IOException {

		FileDescription fileDesc= fileService.findById(fileId);
		if(fileDesc!=null) {
			String filePath = fileDesc.getFilePath();
			File file = new File(filePath);
			//File file = new File(EXTERNAL_FILE_PATH + fileName);
			if (file.exists()) {

				//get the mimetype
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					//unknown mimetype so set the mimetype to application/octet-stream
					mimeType = "application/octet-stream";
				}

				response.setContentType(mimeType);

				/**
				 * In a regular HTTP response, the Content-Disposition response header is a
				 * header indicating if the content is expected to be displayed inline in the
				 * browser, that is, as a Web page or as part of a Web page, or as an
				 * attachment, that is downloaded and saved locally.
				 * 
				 */

				/**
				 * Here we have mentioned it to show inline
				 */
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

				 //Here we have mentioned it to show as attachment
				 //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

				response.setContentLength((int) file.length());

				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				FileCopyUtils.copy(inputStream, response.getOutputStream());
			}
		}
	}
	
	@RequestMapping(value = "/uploadattachment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<EventAttachmentDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		EventAttachmentDto result = null;
		if(FolderPath==null) {
			FolderPath = env.getProperty("calendar.file.folder");
		}
		try {
			String filePath = uploadfile.getOriginalFilename();
			filePath =FolderPath+filePath;
			FileOutputStream stream = new FileOutputStream(filePath);
			try {
			    stream.write(uploadfile.getBytes());
			} finally {
			    stream.close();
			}
			
			FileDescription file = new FileDescription();
			file.setContentSize(uploadfile.getSize());
			file.setContentType(uploadfile.getContentType());
			file.setName(uploadfile.getOriginalFilename());
			file.setFilePath(filePath);
			file = fileService.save(file);
			FileDescriptionDto fileDto = new FileDescriptionDto(file);
			result = new EventAttachmentDto();
			result.setFile(fileDto);			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<EventAttachmentDto>(result,HttpStatus.OK);
	} // method upload Student
	
	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<FileDescriptionDto> uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
		FileDescriptionDto result = null;
		if(FolderPath==null) {
			FolderPath = env.getProperty("calendar.file.folder");
		}
		try {
			
			String filePath = uploadfile.getOriginalFilename();
			filePath ="C:\\Projects\\Globits\\FileData\\"+filePath;
			
			FileOutputStream stream = new FileOutputStream(filePath);
			try {
			    stream.write(uploadfile.getBytes());
			} finally {
			    stream.close();
			}
			
			FileDescription file = new FileDescription();
			file.setContentSize(uploadfile.getSize());
			file.setContentType(uploadfile.getContentType());
			file.setName(uploadfile.getOriginalFilename());
			file.setFilePath(filePath);
			file = fileService.save(file);
			result = new FileDescriptionDto(file);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<FileDescriptionDto>(result,HttpStatus.OK);
	} // method upload Student
	@RequestMapping(path = "/upload/{eventId}", method = RequestMethod.POST)
	public ResponseEntity<EventDto> uploadDocument(@PathVariable("eventId") Long eventId,
			@RequestParam("file") MultipartFile file) {

		EventDto event = null;
		EventAttachmentDto document = null;

		if (CommonUtils.isPositive(eventId, true)) {
			event = eventService.getOne(eventId);
		}

		if (event == null) {
			return new ResponseEntity<EventDto>(new EventDto(), HttpStatus.OK);
		}

		try {
			if (!file.isEmpty()) {
				document = new EventAttachmentDto();
				byte[] data = file.getBytes();

//				document.setContent(data);
//				document.setContentLength((long) data.length);
//				document.setContentType(file.getContentType());

				document.setEventId(eventId);

				String fileName = file.getOriginalFilename();
//				document.setExtension(FileUtils.getFileExtension(fileName));
//				document.setTitle(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (document != null) {
			event.getAttachments().add(document);
			event = eventService.saveAttachments(event);
		}

		return new ResponseEntity<EventDto>(event, HttpStatus.OK);
	}

	@RequestMapping(path = "/status", method = RequestMethod.PUT)
	public ResponseEntity<EventDto> changeEventStatus(@RequestBody EventDto dto) {
		return new ResponseEntity<EventDto>(eventService.changeStatus(dto), HttpStatus.OK);
	}

	@RequestMapping(path = "/location", method = RequestMethod.PUT)
	public ResponseEntity<EventDto> changeEventLocation(@RequestBody EventDto dto) {
		return new ResponseEntity<EventDto>(eventService.changeLocation(dto), HttpStatus.OK);
	}

	@RequestMapping(path = "/time", method = RequestMethod.PUT)
	public ResponseEntity<EventDto> changeEventTime(@RequestBody EventDto dto) {
		return new ResponseEntity<EventDto>(eventService.changeTime(dto), HttpStatus.OK);
	}
	
	
	public static void main(String args[]) {
		//File file = new File("C:\\Projects\\Globits\\Education\\globits-ecosystem\\documents\\event.json");
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Projects\\Globits\\Education\\globits-ecosystem\\documents\\event.json"))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    
			EventEditor editor = new EventEditor();
			editor.setAsText(everything);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//api tìm kiếm
	@RequestMapping(path="/getEventBySearchDto",method = RequestMethod.POST)
	public List<EventDto> getEventBySearchDto(@RequestBody SearchEventDto searchEventDto) {
		return eventService.getEventBySearchDto(searchEventDto);
	}
}
