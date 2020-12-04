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
import com.globits.letter.dto.LetterOutDocumentDto;
import com.globits.letter.dto.ReportLetterByStepDto;
import com.globits.letter.dto.ResponseDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.service.LetterOutDocumentService;
import com.globits.letter.utils.ExcelUtils;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.dto.TaskCommentDto;

@RestController
@RequestMapping("/api/letter/outdocument")
public class RestLetterOutDocumentController {
	@Autowired
	private LetterOutDocumentService service;
	
	@Autowired
	private FileDescriptionService fileService;
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create",method = RequestMethod.POST)
	public LetterOutDocumentDto createLetterOutDocument(@RequestBody LetterOutDocumentDto dto) {
		return service.createLetterOutDocument(dto);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create/{docClerkOwnerId}/{draftersId}/{fowarderId}",method = RequestMethod.POST)
	public LetterOutDocumentDto createLetterOutDocument(@RequestBody LetterOutDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long draftersId, @PathVariable Long fowarderId) {
		return service.forwardLetterOutDocument(dto, docClerkOwnerId, draftersId, fowarderId);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/create/{docClerkOwnerId}/{draftersId}/{fowarderId}/{leaderId}",method = RequestMethod.POST)
	public LetterOutDocumentDto createLetterOutDocument(@RequestBody LetterOutDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long draftersId, @PathVariable Long fowarderId, @PathVariable Long leaderId) {
		service.transferLetterOutDocument(dto.getId(), leaderId);
		return dto;
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/managercreate/{docClerkOwnerId}/{fowarderId}/{leaderId}",method = RequestMethod.POST)
	public LetterOutDocumentDto createLetterOutDocument1(@RequestBody LetterOutDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long fowarderId, @PathVariable Long leaderId) {
		return service.transferLetterOutDocument1(dto, docClerkOwnerId, fowarderId, leaderId);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/sign/{docClerkOwnerId}/{draftersId}/{fowarderId}/{leaderId}",method = RequestMethod.POST)
	public LetterOutDocumentDto signLetterOutDocument(@RequestBody LetterOutDocumentDto dto, @PathVariable Long docClerkOwnerId, @PathVariable Long draftersId, @PathVariable Long fowarderId, @PathVariable Long leaderId) {
		service.signLetterOutDocument(dto.getId());
		return dto;
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/{pageIndex}/{pageSize}",method = RequestMethod.GET)
	public Page<LetterOutDocumentDto> getAllLetterOutDocumentByCurrentUser(@PathVariable("pageIndex")int pageIndex, @PathVariable("pageSize")int pageSize){
		return service.getAllLetterOutDocumentByCurrentUser(pageIndex, pageSize);
	}
	
	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/{id}",method = RequestMethod.GET)
	public LetterOutDocumentDto getLetterOutDocumentById(@PathVariable("id")Long id){
		return service.getLetterOutDocumentById(id);
	}

	@Secured({"ROLE_ADMIN", LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getByTaskId/{id}",method = RequestMethod.GET)
	public LetterOutDocumentDto getLetterOutDocumentByTaskId(@PathVariable Long id) {
		return service.getLetterOutDocumentByTaskId(id);
	}

	@Secured({TaskManConstant.SYSTEM_ADMIN, TaskManConstant.TASKMAN_USER, TaskManConstant.TASKMAN_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getbystep/{stepIndex}/{currentParticipateState}/{pageIndex}/{pageSize}",method = RequestMethod.GET)
	public Page<LetterOutDocumentDto> getAllLetterOutDocumentBy(@PathVariable int stepIndex, @PathVariable Integer currentParticipateState, @PathVariable int pageIndex, @PathVariable int pageSize){
		return service.getAllLetterOutDocumentBy(stepIndex, currentParticipateState, pageIndex, pageSize);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/generatedtoletterout",method = RequestMethod.GET)
	public LetterOutDocumentDto generateDtoLetterOut() {
		return service.generateDtoLetterOut();
	}
	
	@RequestMapping(value = "/uploadattachment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<LetterDocumentAttachmentDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		LetterDocumentAttachmentDto result = null;
		
		try {
			String filePath = uploadfile.getOriginalFilename();
			filePath =LetterConstant.OutDocumentFolderPath+filePath;
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
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(path="/getletterby/{stepIndex}/{currentParticipateState}/{pageIndex}/{pageSize}",method = RequestMethod.POST)
	public Page<LetterOutDocumentDto> getLetterByString(@PathVariable("stepIndex") int stepIndex, @PathVariable("currentParticipateState") int currentParticipateState, @PathVariable("pageIndex") int pageIndex, @PathVariable("pageSize") int pageSize, @RequestBody SearchDocumentDto searchDocumentDto) {
		return service.getLetterByString(stepIndex, currentParticipateState, searchDocumentDto, pageIndex, pageSize);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN})
	@RequestMapping(value = "/importdocument", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Integer> importOutDocument(@RequestParam("uploadfile") MultipartFile uploadfile) {
		int ret =0;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(uploadfile.getBytes());
			List<LetterOutDocumentDto> letterOutDocumentDto = ExcelUtils.importOutDocument(bis);
			ret = service.saveListLetterOutDocument(letterOutDocumentDto);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Integer>(ret,HttpStatus.OK);
	}
	
	@RequestMapping(path="/sendBack/{documentId}/{toStepId}",method = RequestMethod.POST)
	public ResponseDto sendToBackLetterInDocument(@PathVariable Long documentId, @PathVariable Long toStepId,@RequestBody TaskCommentDto commentDto) {
		return service.backToPreviousStep(documentId,commentDto);
	}
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public List<ReportLetterByStepDto> reportstep() {
		return service.getReportLetterByStep();
	}
	
//	@RequestMapping(path="/getAll/{pageIndex}/{pageSize}",method = RequestMethod.GET)
//	public Page<LetterOutDocumentDto> getAllLetter(@PathVariable int pageIndex, @PathVariable int pageSize){
//		return service.getAllLetter(pageIndex, pageSize);
//	}
	
	@RequestMapping(path="/save",method = RequestMethod.POST)
	public LetterOutDocumentDto saveLetterOutDocument(@RequestBody LetterOutDocumentDto dto) {
		return service.saveLetterOut(dto);
	}
}
