package com.globits.letter.rest;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
import com.globits.letter.LetterConstant;
import com.globits.letter.dto.LetterDocumentAttachmentDto;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.ReportLetterByStepDto;
import com.globits.letter.dto.ResponseDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.dto.UserRolesDto;
import com.globits.letter.service.LetterInDocumentService;
import com.globits.letter.utils.ExcelUtils;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;

@RestController
@RequestMapping("/api/letter/indocument")
public class RestLetterInDocumentController {
	@Autowired
	LetterInDocumentService service;
	@Autowired
	private FileDescriptionService fileService;

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getUserRoles",method = RequestMethod.GET)
	public List<UserRolesDto> getUserRoles(){
		return service.getUserRoles();
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create/{docClerkOwnerId}/{fowarderId}",method = RequestMethod.POST)
	public LetterInDocumentDto createLetterInDocument(@RequestBody LetterInDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long fowarderId) {
		return service.createLetterInDocument(dto,docClerkOwnerId, fowarderId);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create/{docClerkOwnerId}/{fowarderId}/{assignerId}",method = RequestMethod.POST)
	public LetterInDocumentDto createLetterInDocument(@RequestBody LetterInDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long fowarderId, @PathVariable Long assignerId) {
		service.fowardLetterInDocument(dto.getId(), assignerId, false);
		return dto;
	}
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create/mainprocess/{assignerId}",method = RequestMethod.POST)
	public LetterInDocumentDto createMainLetterInDocument(@RequestBody LetterInDocumentDto dto, @PathVariable Long assignerId) {
		service.fowardLetterInDocument(dto.getId(), assignerId, true);
		return dto;
	}
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/foward",method = RequestMethod.POST)
	public void fowardLetterInDocument(@PathVariable Long documentId, Long toStepId,@PathVariable  Long toStaffId,@PathVariable  Long roleId,@PathVariable  TaskCommentDto commentDto) {
		service.forwardDocument(documentId, toStepId, toStaffId, roleId, commentDto);
	}
	@RequestMapping(path="/sendBack/{documentId}/{toStepId}",method = RequestMethod.POST)
	public ResponseDto sendToBackLetterInDocument(@PathVariable Long documentId, @PathVariable Long toStepId,@RequestBody TaskCommentDto commentDto) {
		return service.backToPreviousStep(documentId,commentDto);
	}
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create",method = RequestMethod.POST)
	public LetterInDocumentDto createLetterInDocument(@RequestBody LetterInDocumentDto dto) {
		return service.createLetterInDocument(dto,null,null);
	}
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getbystep/{stepIndex}/{pageSize}/{pageIndex}",method = RequestMethod.GET)
	public Page<LetterInDocumentDto> getDocumentByStepIndex(@PathVariable int stepIndex,@PathVariable int pageIndex,@PathVariable int pageSize){
		return service.getDocumentByStepIndex(stepIndex,pageIndex,pageSize);
	}
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getbystep/{stepIndex}/{currentParticipateState}/{pageSize}/{pageIndex}",method = RequestMethod.GET)
	public Page<LetterInDocumentDto> getAllDepartmentByOwner(@PathVariable int stepIndex,@PathVariable Integer currentParticipateState,@PathVariable int pageSize,@PathVariable int pageIndex){
		return service.getAllDepartmentByOwner(stepIndex,currentParticipateState,pageSize,pageIndex);
	}
	
	@RequestMapping(value = "/uploadattachment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<LetterDocumentAttachmentDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		LetterDocumentAttachmentDto result = null;
		
		try {
			String filePath = uploadfile.getOriginalFilename();
			filePath =LetterConstant.InDocumentFolderPath+filePath;
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
			result = new LetterDocumentAttachmentDto();
			result.setFile(fileDto);			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<LetterDocumentAttachmentDto>(result,HttpStatus.OK);
	} // method upload Student
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/get_one/{id}",method = RequestMethod.GET)
	public LetterInDocumentDto getLetterInDocumentById(@PathVariable Long id) {
		return service.getLetterInDocumentById(id);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getByTaskId/{id}",method = RequestMethod.GET)
	public LetterInDocumentDto getLetterInDocumentByTaskId(@PathVariable Long id) {
		return service.getLetterInDocumentByTaskId(id);
	}
	
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/assign_step/create/{docClerkOwnerId}/{fowarderId}/{assinerId}",method = RequestMethod.POST)
	public LetterInDocumentDto assignProcess(@RequestBody LetterInDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long fowarderId, @PathVariable Long assinerId) {
		return service.assignProcess(dto,docClerkOwnerId, fowarderId, assinerId);
	}
	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/remove/{documentId}",method = RequestMethod.DELETE)
	public LetterInDocumentDto delete( @PathVariable Long documentId) {
		return service.removeDocument(documentId);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/assigntask/{documentId}",method = RequestMethod.POST)
	public void assignTask( @PathVariable Long documentId,@RequestBody List<ParticipateDto> assignees) {
		service.assignTask(documentId, assignees);;
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/processingletter/{documentId}",method = RequestMethod.POST)
	public void processingLetter( @PathVariable Long documentId,@RequestBody List<ParticipateDto> assignees) {
		service.processingLetter(documentId, assignees);;
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/addStaff/{documentId}",method = RequestMethod.POST)
	public void addStaff( @PathVariable Long documentId,@RequestBody List<ParticipateDto> listStaff) {
		service.addStaff(documentId, listStaff);;
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/processingletterin/{documentId}",method = RequestMethod.POST)
	public void processingLetterIn( @PathVariable Long documentId) {
		service.processingLetterIn(documentId);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/generatedtoletterin",method = RequestMethod.GET)
	public LetterInDocumentDto generateDtoLetterIn() {
		return service.generateDtoLetterIn();
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN,LetterConstant.ROLE_LETTER_USER})
	@RequestMapping(path="/getletterby/{stepIndex}/{currentParticipateState}/{pageSize}/{pageIndex}",method = RequestMethod.POST)
	public Page<LetterInDocumentDto> getLetterByString(@PathVariable("stepIndex") int stepIndex, @PathVariable("currentParticipateState") int currentParticipateState, @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize, @RequestBody SearchDocumentDto searchDocumentDto) {
		return service.getLetterByString(stepIndex, currentParticipateState, searchDocumentDto, pageIndex, pageSize);
	}
	@Secured({com.globits.core.Constants.ROLE_ADMIN})
	@RequestMapping(value = "/importdocument", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Integer> importInDocument(@RequestParam("uploadfile") MultipartFile uploadfile) {
		int ret =0;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(uploadfile.getBytes());
			List<LetterInDocumentDto> listLetterInDocumentDto = ExcelUtils.importInDocument(bis);
			ret = service.saveListLetterInDocument(listLetterInDocumentDto);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Integer>(ret,HttpStatus.OK);
	} // method upload Thoi khoa bieu
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public List<ReportLetterByStepDto> reportstep() {
		return service.getReportLetterByStep();
	}
}
