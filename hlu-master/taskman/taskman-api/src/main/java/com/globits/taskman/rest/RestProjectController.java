package com.globits.taskman.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.globits.core.domain.FileDescription;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.service.FileDescriptionService;
import com.globits.hr.dto.StaffDto;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.ProjectDto;
import com.globits.taskman.dto.TaskDto;
import com.globits.taskman.dto.TaskFileAttachmentDto;
import com.globits.taskman.service.ProjectService;
import com.globits.taskman.service.TaskService;


@RestController
@RequestMapping("/api/taskman/project")
public class RestProjectController {
	@Autowired
	ProjectService service;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	private FileDescriptionService fileService;
	
	@Secured({TaskManConstant.SYSTEM_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ProjectDto> getProject(@PathVariable("id") Long id) {
		ProjectDto dto = service.getProject(id);
		return new ResponseEntity<ProjectDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ProjectDto> saveProject(@RequestBody ProjectDto dto) {
		dto = service.saveProject(dto);
		return new ResponseEntity<ProjectDto>(dto, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN})
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		Boolean ret = service.deleteProject(id);
		return new ResponseEntity<Boolean>(ret, HttpStatus.OK);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN})
	@RequestMapping(value = "/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<ProjectDto>> getList(@PathVariable("pageSize") int pageSize, @PathVariable("pageIndex") int pageIndex) {
		Page<ProjectDto> page = service.getListProject(pageSize, pageIndex);
		return new ResponseEntity<Page<ProjectDto>>(page, HttpStatus.OK);
	}
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_ADMIN, TaskManConstant.TASKMAN_USER})
	@RequestMapping(value = "/getmylistproject", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectDto>> getMyListProject() {
		List<ProjectDto> page = service.getMyListProject();
		return new ResponseEntity<List<ProjectDto>>(page, HttpStatus.OK);
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_STAFF","ROLE_STUDENT","ROLE_USER","ROLE_STUDENT_MANAGERMENT","ROLE_EDUCATION_MANAGERMENT" })
	@RequestMapping(value = "/projectcode/{textSearch}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public Page<ProjectDto> getProjectsByCode(@PathVariable String textSearch, @PathVariable int pageIndex, @PathVariable int pageSize) {
		return service.findPageByCodeOrName(textSearch, pageIndex, pageSize);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN})
	@RequestMapping(value = "/{projectId}/{pageSize}/{pageIndex}", method = RequestMethod.GET)
	public ResponseEntity<Page<TaskDto>> getListRootTaskByProjectId(@PathVariable Long projectId,@PathVariable int pageSize,@PathVariable int pageIndex) {
		Page<TaskDto> page = taskService.getListRootTaskByProjectId(projectId, pageSize, pageIndex);
		return new ResponseEntity<Page<TaskDto>>(page, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/uploadattachment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<TaskFileAttachmentDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		TaskFileAttachmentDto result = null;

		try {
			String filePath = uploadfile.getOriginalFilename();
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
			result = new TaskFileAttachmentDto();
			result.setFile(fileDto);			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<TaskFileAttachmentDto>(result,HttpStatus.OK);
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
