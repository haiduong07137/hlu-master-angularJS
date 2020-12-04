package com.globits.document.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.globits.document.dto.SearchDto;
import com.globits.core.domain.FileDescription;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.service.FileDescriptionService;
import com.globits.document.DocumentConstant;
import com.globits.document.dto.DocumentAttachmentDto;
import com.globits.document.dto.DocumentDto;
import com.globits.document.service.DocumentService;

@RestController
@RequestMapping("/api/document")
public class RestDocument {
	@Autowired
	DocumentService service;
	@Autowired
	private FileDescriptionService fileService;
		
	@RequestMapping(value = "/{pageIndex}/{pageSize}", method = RequestMethod.GET)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public Page<DocumentDto> getList(@PathVariable int pageIndex, @PathVariable int pageSize) {
		return service.getListDocument(pageIndex, pageSize);
	}
	
	@RequestMapping(path="/{documentId}",method = RequestMethod.GET)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public DocumentDto findById( @PathVariable Long documentId) {
		return service.getByDocumentById(documentId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public DocumentDto createOrUpdate(@RequestBody DocumentDto dto) {
		return service.createOrUpdate(dto);
	}
	
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public Boolean removeDocumentCategory(@PathVariable("Id") String Id) {
		return service.removeDocument(new Long(Id));
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	public Page<DocumentDto> searchDocument(@RequestBody SearchDto search) {
		return service.searchDocumentBySearchDto(search);
	}
	
	@RequestMapping(value = "/uploadattachment", method = RequestMethod.POST)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
	@ResponseBody
	public ResponseEntity<DocumentAttachmentDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		DocumentAttachmentDto result = null;
		
		try {
			String filePath = uploadfile.getOriginalFilename();
			filePath =DocumentConstant.DocumentFolderPath+filePath;
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
			result = new DocumentAttachmentDto();
			result.setFile(fileDto);			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<DocumentAttachmentDto>(result,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/file/{fileId}",method = RequestMethod.GET)
//	@Secured({DocumentConstant.DOCUMENT_ADMIN,Constants.ROLE_ADMIN})
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
