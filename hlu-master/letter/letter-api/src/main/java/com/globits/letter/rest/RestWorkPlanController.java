package com.globits.letter.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.globits.calendar.dto.EventAttachmentDto;
import com.globits.calendar.dto.EventDto;
import com.globits.core.domain.FileDescription;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.service.FileDescriptionService;
import com.globits.core.utils.CommonUtils;
import com.globits.letter.LetterConstant;
import com.globits.letter.service.WorkPlanService;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.TaskCommentDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.service.TaskCommentService;

@RestController
@RequestMapping("/api/workplan")
public class RestWorkPlanController {
	@Autowired
	WorkPlanService workPlanService;
	
	@Autowired
	TaskCommentService taskCommentService;
	@Autowired
	private FileDescriptionService fileService;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public TaskDto saveWorkPlan(@RequestBody TaskDto task) {
		task = workPlanService.saveWorkPlan(task);
		return task;
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteWorkPlans(@RequestBody TaskDto[] dtos) {
		boolean result = workPlanService.deleteMultiple(dtos);
		return new ResponseEntity<Boolean>(result, result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/deleteCommentById/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteCommentById(@PathVariable Long id) {
		boolean result = taskCommentService.deleteTaskComment(id);
		return new ResponseEntity<Boolean>(result, result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TaskDto getWorkPlansById(@PathVariable Long id) {
		return workPlanService.getWorkPlansById(id);
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<TaskDto> getListWorkPlans(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return workPlanService.getListWorkPlan(pageIndex, pageSize);
	}
	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/{stepId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<TaskDto> getListWorkPlans(@PathVariable Long stepId, @PathVariable int pageIndex, @PathVariable int pageSize) {
		return workPlanService.getListWorkPlanByStep(stepId,pageIndex, pageSize);
	}	
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/addcomment", method = RequestMethod.POST)
	public ResponseEntity<Boolean> addComment(@RequestBody TaskCommentDto comment) {
		boolean result = workPlanService.addComment(comment);
		return new ResponseEntity<Boolean>(result, result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
	}
	@Secured({TaskManConstant.SYSTEM_ADMIN, LetterConstant.WORKPLAN_ADMIN, LetterConstant.WORKPLAN_USER, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN})
	@RequestMapping(value = "/getcomment/{taskId}/{pageIndex}/{pageSize}", method = RequestMethod.POST)
	public ResponseEntity<Page<TaskCommentDto>> getCommentByTask(@PathVariable Long taskId, @PathVariable int pageSize, @PathVariable int pageIndex) {
		Page<TaskCommentDto> result = taskCommentService.getListTaskCommentByTaskId(taskId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskCommentDto>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/uploadattachment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<EventAttachmentDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		EventAttachmentDto result = null;

		try {
			String filePath = uploadfile.getOriginalFilename();
			filePath =LetterConstant.WorkPlanFolderPath+filePath;
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
	
	
	@RequestMapping(value = "/planfile/{fileId}",method = RequestMethod.GET)
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
}
