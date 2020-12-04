package com.globits.letter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.dialect.function.VarArgsSQLFunction;

import com.globits.core.dto.FileDescriptionDto;
import com.globits.letter.dto.LetterDocPriorityDto;
import com.globits.letter.dto.LetterDocumentAttachmentDto;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.LetterOutDocumentDto;
import com.globits.taskman.dto.TaskDto;


public class ExcelUtils {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static List<LetterInDocumentDto> importInDocument(InputStream is) throws IOException {
		String rootFilePath = "D:/Data/letter-documents/InDocument/";
		Workbook workbook = new HSSFWorkbook(is);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		int rowIndex = 1;
		int num = datatypeSheet.getLastRowNum();
		List<LetterInDocumentDto> listLetterInDocument = new ArrayList<LetterInDocumentDto>();
		while (rowIndex <= num) {
			Row currentRow = datatypeSheet.getRow(rowIndex);
			if(currentRow==null) {
				rowIndex++;
				continue;
			}
			Cell currentCell = null;
			
			LetterInDocumentDto dto = new LetterInDocumentDto();
			
			currentCell = currentRow.getCell(1);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String docOriginCode = currentCell.getStringCellValue();
				dto.setDocOriginCode(docOriginCode);
			}

			currentCell = currentRow.getCell(2);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String title = currentCell.getStringCellValue();
				dto.setTitle(title);				
			}
			
			currentCell = currentRow.getCell(3);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String briefNote = currentCell.getStringCellValue();
				dto.setBriefNote(briefNote);				
			}else if(currentCell == null) {
//				String briefNote = "Trống";
//				dto.setBriefNote(briefNote);
			}
			
			currentCell = currentRow.getCell(4);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String date = currentCell.getStringCellValue();
				try {
					Date issuedDate = dateFormat.parse(date);
					dto.setIssuedDate(issuedDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			currentCell = currentRow.getCell(5);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String date = currentCell.getStringCellValue();
				try {
					Date deliveredDate = dateFormat.parse(date);
					dto.setDeliveredDate(deliveredDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
//			currentCell = currentRow.getCell(7);
//			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
//				Integer number = (int) currentCell.getNumericCellValue();
//				LetterDocPriorityDto letterDocPriorityDto = new LetterDocPriorityDto();
//				if(number == 0) {
//					letterDocPriorityDto.setCode("");
//					letterDocPriorityDto.setName("Thường");
//				}else if(number == 1) {
//					letterDocPriorityDto.setCode("");
//					letterDocPriorityDto.setName("Mật");
//				}else if(number == 2) {
//					letterDocPriorityDto.setCode("");
//					letterDocPriorityDto.setName("Tuyệt mật");
//				}
//				dto.setLetterDocPriority(letterDocPriorityDto);
//				System.out.println(letterDocPriorityDto);
//			}else {
//				System.out.println("letterDocPriority="+rowIndex);
//			}
			
			currentCell = currentRow.getCell(11);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer saveLetterType = (int) currentCell.getNumericCellValue();
				dto.setSaveLetterType(saveLetterType);
			}
			
			dto.setAttachments(new ArrayList<LetterDocumentAttachmentDto>());
			LetterDocumentAttachmentDto attachmentDto = new LetterDocumentAttachmentDto();
			FileDescriptionDto fileInfo = new FileDescriptionDto();
			currentCell = currentRow.getCell(16);
			String fileName = currentCell.getStringCellValue();
			fileInfo.setFilePath(rootFilePath+fileName);
			fileInfo.setContentType("application/pdf");
			fileInfo.setName(fileName);
			attachmentDto.setFile(fileInfo);
			dto.getAttachments().add(attachmentDto);
			
			currentCell = currentRow.getCell(17);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer numberOfPages = (int) currentCell.getNumericCellValue();
				dto.setNumberOfPages(numberOfPages);				
			}
			
			currentCell = currentRow.getCell(18);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String signedPerson = currentCell.getStringCellValue();
				dto.setSignedPerson(signedPerson);				
			}
			
			currentCell = currentRow.getCell(27);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer letterLine = (int) currentCell.getNumericCellValue();
				dto.setLetterLine(letterLine);
			}
			
			currentCell = currentRow.getCell(33);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer docCode = (int) currentCell.getNumericCellValue();
				dto.setDocCode(String.valueOf(docCode)); // convert integer to string
			}
			
			currentCell = currentRow.getCell(36);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String date = currentCell.getStringCellValue();
				if(date.equals("NULL")) {
//					System.out.println(rowIndex);
				}else{
					try {
						Date registeredDate = dateFormat.parse(date);
						dto.setRegisteredDate(registeredDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				};
			}
			
			listLetterInDocument.add(dto);
			rowIndex++;
		}
		
		return listLetterInDocument;
	}
	
	public static List<LetterOutDocumentDto> importOutDocument(InputStream is) throws IOException {
		String rootFilePath = "D:/Data/letter-documents/OutDocument/";
		Workbook workbook = new HSSFWorkbook(is);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		int rowIndex = 1;
		int num = datatypeSheet.getLastRowNum();
		List<LetterOutDocumentDto> listLetterOutDocument = new ArrayList<LetterOutDocumentDto>();
		while (rowIndex <= num) {
			Row currentRow = datatypeSheet.getRow(rowIndex);
			if(currentRow==null) {
				rowIndex++;
				continue;
			}
			Cell currentCell = null;
			
			LetterOutDocumentDto dto = new LetterOutDocumentDto();
			
			currentCell = currentRow.getCell(1);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String docOriginCode = currentCell.getStringCellValue();
				dto.setDocOriginCode(docOriginCode);
			}

			currentCell = currentRow.getCell(2);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String title = currentCell.getStringCellValue();
				dto.setTitle(title);				
			}
			
			currentCell = currentRow.getCell(3);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String briefNote = currentCell.getStringCellValue();
				dto.setBriefNote(briefNote);				
			}else if(currentCell == null) {
//				String briefNote = "Trống";
//				dto.setBriefNote(briefNote);
			}
			
			currentCell = currentRow.getCell(4);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String date = currentCell.getStringCellValue();
				try {
					Date issuedDate = dateFormat.parse(date);
					dto.setIssuedDate(issuedDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			currentCell = currentRow.getCell(5);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String date = currentCell.getStringCellValue();
				try {
					Date deliveredDate = dateFormat.parse(date);
					dto.setDeliveredDate(deliveredDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
//			currentCell = currentRow.getCell(7);
//			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
//				Integer number = (int) currentCell.getNumericCellValue();
//				LetterDocPriorityDto letterDocPriorityDto = new LetterDocPriorityDto();
//				if(number == 0) {
//					letterDocPriorityDto.setCode("");
//					letterDocPriorityDto.setName("Thường");
//				}else if(number == 1) {
//					letterDocPriorityDto.setCode("");
//					letterDocPriorityDto.setName("Mật");
//				}else if(number == 2) {
//					letterDocPriorityDto.setCode("");
//					letterDocPriorityDto.setName("Tuyệt mật");
//				}
//				dto.setLetterDocPriority(letterDocPriorityDto);
//				System.out.println(letterDocPriorityDto);
//			}else {
//				System.out.println("letterDocPriority="+rowIndex);
//			}
			
			currentCell = currentRow.getCell(11);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer saveLetterType = (int) currentCell.getNumericCellValue();
				dto.setSaveLetterType(saveLetterType);
			}
			
			dto.setAttachments(new ArrayList<LetterDocumentAttachmentDto>());
			LetterDocumentAttachmentDto attachmentDto = new LetterDocumentAttachmentDto();
			FileDescriptionDto fileInfo = new FileDescriptionDto();
			currentCell = currentRow.getCell(16);
			String fileName = currentCell.getStringCellValue();
			fileInfo.setFilePath(rootFilePath+fileName);
			fileInfo.setContentType("application/pdf");
			fileInfo.setName(fileName);
			attachmentDto.setFile(fileInfo);
			dto.getAttachments().add(attachmentDto);
			
			currentCell = currentRow.getCell(17);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer numberOfPages = (int) currentCell.getNumericCellValue();
				dto.setNumberOfPages(numberOfPages);				
			}
			
			currentCell = currentRow.getCell(18);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String signedPerson = currentCell.getStringCellValue();
				dto.setSignedPerson(signedPerson);				
			}
			
			currentCell = currentRow.getCell(27);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer letterLine = (int) currentCell.getNumericCellValue();
				dto.setLetterLine(letterLine);
			}
			
			currentCell = currentRow.getCell(33);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
				Integer docCode = (int) currentCell.getNumericCellValue();
				dto.setDocCode(String.valueOf(docCode)); // convert integer to string
			}
			
			currentCell = currentRow.getCell(36);
			if(currentCell!=null && currentCell.getCellTypeEnum()==CellType.STRING) {
				String date = currentCell.getStringCellValue();
				if(date.equals("NULL")) {
//					System.out.println(rowIndex);
				}else{
					try {
						Date registeredDate = dateFormat.parse(date);
						dto.setRegisteredDate(registeredDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				};
			}
			
			listLetterOutDocument.add(dto);
			rowIndex++;
		}
		
		return listLetterOutDocument;
	}
	public static void main(String[] args) {
		FileInputStream f;
		try {
			f = new FileInputStream(new File("D:\\OutDocument.xls"));
//			f = new FileInputStream(new File("D:\\Projects\\AgribankHP\\InDocumentAgrHP.xls"));
//			List<LetterInDocumentDto> listAllDocument = importInDocument(f);
			List<LetterOutDocumentDto> listAllDocument = importOutDocument(f);
			System.out.println(listAllDocument.size());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}
