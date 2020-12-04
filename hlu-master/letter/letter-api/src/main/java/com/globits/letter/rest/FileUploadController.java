package com.globits.letter.rest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.SearchEventDto;
import com.globits.calendar.service.EventService;
import com.globits.core.domain.FileDescription;
import com.globits.core.dto.FileDescriptionDto;
import com.globits.core.service.FileDescriptionService;
import com.globits.letter.LetterConstant;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.LetterOutDocumentDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.service.LetterInDocumentService;
import com.globits.letter.service.LetterOutDocumentService;

@RestController
@RequestMapping("/api/letter/upload")
public class FileUploadController {
	
	@Autowired
	private Environment env;
	
	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private FileDescriptionService fileService;

	@Autowired
	private LetterInDocumentService letterInDocumentService;
	
	@Autowired
	private LetterOutDocumentService letterOutDocumentService;
	
	@Autowired
	private EventService eventService;
	
	@Secured({com.globits.core.Constants.ROLE_ADMIN, LetterConstant.ROLE_LETTER_ADMIN})
	@RequestMapping(value = "/file", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<FileDescriptionDto> uploadAttachment(@RequestParam("uploadfile") MultipartFile uploadfile) {
		FileDescriptionDto result = null;
		if(LetterConstant.FolderPath==null) {
			LetterConstant.FolderPath = env.getProperty("crm.file.folder");
		}
		try {
			String filePath = uploadfile.getOriginalFilename();
			filePath =LetterConstant.FolderPath+filePath;
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
			
			result= new FileDescriptionDto(file);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<FileDescriptionDto>(result,HttpStatus.OK);
	} // method upload Student
	
	@RequestMapping(value = "/exportLetterIn", method = { RequestMethod.POST })//Xuất excel công văn đến
	public void ExportLetterIn(WebRequest request, HttpServletResponse response, @RequestBody SearchDocumentDto searchDocumentDto) {
		List<LetterInDocumentDto> listLetterInDocument=new ArrayList<LetterInDocumentDto>();
		String title = "Thống kê Công văn đến";
		String titles = "Thống kê toàn bộ Công văn đến";
		String fromdate= " từ ngày ";
		String todate= " đến ngày ";
		listLetterInDocument = letterInDocumentService.searchLetterInDto(searchDocumentDto);
		
		
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = null;
//			if(dto.getDateFrom() != null && dto.getDateTo() != null) {
//				worksheet = workbook.createSheet(title + fromdate + dto.getDateFrom() + todate + dto.getDateTo()+".");
//			}else {
				worksheet = workbook.createSheet(title);
//			}
			
			HSSFCellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setColor(HSSFFont.COLOR_NORMAL);
			font.setFontName("");
			headerCellStyle.setBorderTop(BorderStyle.THIN);
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
			headerCellStyle.setBorderRight(BorderStyle.THIN);
			headerCellStyle.setBorderLeft(BorderStyle.THIN);
			headerCellStyle.setFont(font);
			// headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			headerCellStyle.setWrapText(false);

			int rowCount = 3;// ????
			int columnCount = 0;
			HSSFRow row;
			HSSFCell cell;

			// for header row
			row = worksheet.createRow(rowCount);
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("STT");
			worksheet.setColumnWidth(columnCount - 1, 6 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Số hiệu văn bản");
			worksheet.setColumnWidth(columnCount - 1, 22 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Trích yếu nội dung");
			worksheet.setColumnWidth(columnCount - 1, 80 * 256);
						
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Ngày tháng đến");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Nơi gửi");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
//			cell = row.createCell(columnCount++);
//			cell.setCellStyle(headerCellStyle);
//			cell.setCellValue("Người ký");
//			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			// title row
			int titleRow = 0;
			row = worksheet.createRow(titleRow++);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 0, columnCount - 1));

			HSSFCellStyle dateCellStyle = workbook.createCellStyle();
			short df = workbook.createDataFormat().getFormat("dd/MM/yyyy");
			dateCellStyle.setDataFormat(df);
			dateCellStyle.setBorderTop(BorderStyle.THIN);
			dateCellStyle.setBorderBottom(BorderStyle.THIN);
			dateCellStyle.setBorderRight(BorderStyle.THIN);
			dateCellStyle.setBorderLeft(BorderStyle.THIN);
			
			HSSFCellStyle normalCellStyle = workbook.createCellStyle();
			normalCellStyle.setBorderTop(BorderStyle.THIN);
			normalCellStyle.setBorderBottom(BorderStyle.THIN);
			normalCellStyle.setBorderRight(BorderStyle.THIN);
			normalCellStyle.setBorderLeft(BorderStyle.THIN);
			normalCellStyle.setWrapText(true);
			normalCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			
			HSSFCellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setBorderTop(BorderStyle.THIN);
			titleCellStyle.setBorderBottom(BorderStyle.THIN);
			titleCellStyle.setBorderRight(BorderStyle.THIN);
			titleCellStyle.setBorderLeft(BorderStyle.THIN);
			
			row = worksheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellStyle(titleCellStyle);
			cell.setCellValue("Bộ Giáo dục và đào tạo");
			
			row = worksheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellStyle(titleCellStyle);
			cell.setCellValue("Trường Đại Học Luật Hà Nội");
			
			HSSFCellStyle style0 = workbook.createCellStyle();
			HSSFFont font0 = workbook.createFont();
			font0.setFontHeightInPoints((short) 13);
			style0.setFont(font0);
			style0.setVerticalAlignment(VerticalAlignment.CENTER);
			style0.setAlignment(HorizontalAlignment.CENTER);
			row = worksheet.createRow(2);
			cell = row.createCell(0);
			cell.setCellStyle(style0);
			
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date oldDate= new Date(), newDate= new Date(0);
			for(LetterInDocumentDto indoc:listLetterInDocument) {
				if(indoc.getDeliveredDate() != null) {
					if(newDate.getTime() < indoc.getDeliveredDate().getTime()) {
						newDate = indoc.getDeliveredDate();
					}
					if(oldDate.getTime() >= indoc.getDeliveredDate().getTime()) {
						oldDate = indoc.getDeliveredDate();
					}
				}
			}
			if(searchDocumentDto.getDateTo() != null && searchDocumentDto.getDateFrom() != null) {
				cell.setCellValue(title + fromdate + simpleDateFormat.format(searchDocumentDto.getDateFrom()) + todate + simpleDateFormat.format(searchDocumentDto.getDateTo()));
			}else {
				cell.setCellValue(titles + fromdate + simpleDateFormat.format(oldDate) + todate + simpleDateFormat.format(newDate) );
			}
			
			if (listLetterInDocument != null && listLetterInDocument.size() > 0) {
		
				for (LetterInDocumentDto doc : listLetterInDocument) {

					rowCount++;
					columnCount = 0;
					
					row = worksheet.createRow(rowCount);
					// stt
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(rowCount - titleRow -2);

					// docOriginCode:số hiệu văn bản
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getDocOriginCode());

					// briefNote:trích yếu
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getBriefNote());					
					
					// deliveredDate: ngày tháng đến
					cell = row.createCell(columnCount++);
					cell.setCellStyle(dateCellStyle);
					cell.setCellValue(doc.getDeliveredDate());

					// issueOrgan: nơi gửi
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getOtherIssueOrgan());
					
					// signedPerson: người ký
//					cell = row.createCell(columnCount++);
//					cell.setCellStyle(normalCellStyle);
//					cell.setCellValue(doc.getSignedPerson());
					
				}
			}
			workbook.write(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=ThongKeCongVanDen.xls");
			response.flushBuffer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/exportLetterOut", method = { RequestMethod.POST })//Xuất excel công văn đi
	public void ExportLetterOut(WebRequest request, HttpServletResponse response, @RequestBody SearchDocumentDto dto) {
		List<LetterOutDocumentDto> listLetterOutDocument=new ArrayList<LetterOutDocumentDto>();
		String title = "Thống kê Công văn đi";
		String titles = "Thống kê toàn bộ Công văn đi";
		String fromdate= " từ ngày ";
		String todate= " đến ngày ";
		listLetterOutDocument = letterOutDocumentService.searchLetterOutDto(dto);

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = null;
//			if(dto.getDateFrom() != null && dto.getDateTo() != null) {
//				worksheet = workbook.createSheet(title + fromdate + dto.getDateFrom() + todate + dto.getDateTo()+".");
//			}else {
				worksheet = workbook.createSheet(title);
//			}
			
			HSSFCellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setColor(HSSFFont.COLOR_NORMAL);
			font.setFontName("");
			headerCellStyle.setBorderTop(BorderStyle.THIN);
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
			headerCellStyle.setBorderRight(BorderStyle.THIN);
			headerCellStyle.setBorderLeft(BorderStyle.THIN);
			headerCellStyle.setFont(font);
			// headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			headerCellStyle.setWrapText(false);

			int rowCount = 3;// ????
			int columnCount = 0;
			HSSFRow row;
			HSSFCell cell;

			// for header row
			row = worksheet.createRow(rowCount);
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("STT");
			worksheet.setColumnWidth(columnCount - 1, 6 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Số hiệu văn bản");
			worksheet.setColumnWidth(columnCount - 1, 22 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Trích yếu nội dung");
			worksheet.setColumnWidth(columnCount - 1, 80 * 256);
						
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Ngày tháng đi");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Nơi đến");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Người ký");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			// title row
			int titleRow = 0;
			row = worksheet.createRow(titleRow++);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 0, columnCount - 1));

			HSSFCellStyle dateCellStyle = workbook.createCellStyle();
			short df = workbook.createDataFormat().getFormat("dd/MM/yyyy");
			dateCellStyle.setDataFormat(df);
			dateCellStyle.setBorderTop(BorderStyle.THIN);
			dateCellStyle.setBorderBottom(BorderStyle.THIN);
			dateCellStyle.setBorderRight(BorderStyle.THIN);
			dateCellStyle.setBorderLeft(BorderStyle.THIN);
			
			HSSFCellStyle normalCellStyle = workbook.createCellStyle();
			normalCellStyle.setBorderTop(BorderStyle.THIN);
			normalCellStyle.setBorderBottom(BorderStyle.THIN);
			normalCellStyle.setBorderRight(BorderStyle.THIN);
			normalCellStyle.setBorderLeft(BorderStyle.THIN);
			normalCellStyle.setWrapText(true);
			normalCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			
			HSSFCellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setBorderTop(BorderStyle.THIN);
			titleCellStyle.setBorderBottom(BorderStyle.THIN);
			titleCellStyle.setBorderRight(BorderStyle.THIN);
			titleCellStyle.setBorderLeft(BorderStyle.THIN);
			
			row = worksheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellStyle(titleCellStyle);
			cell.setCellValue("Bộ Giáo dục và đào tạo");
			
			row = worksheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellStyle(titleCellStyle);
			cell.setCellValue("Trường Đại Học Luật Hà Nội");
			
			HSSFCellStyle style0 = workbook.createCellStyle();
			HSSFFont font0 = workbook.createFont();
			font0.setFontHeightInPoints((short) 13);
			style0.setFont(font0);
			style0.setVerticalAlignment(VerticalAlignment.CENTER);
			style0.setAlignment(HorizontalAlignment.CENTER);
			row = worksheet.createRow(2);
			cell = row.createCell(0);
			cell.setCellStyle(style0);
			
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			Date oldDate= new Date(), newDate= new Date(0);
			for(LetterOutDocumentDto outdoc:listLetterOutDocument) {
				if(outdoc.getDeliveredDate() != null) {
					if(newDate.getTime() < outdoc.getDeliveredDate().getTime()) {
						newDate = outdoc.getDeliveredDate();
					}
					if(oldDate.getTime() >= outdoc.getDeliveredDate().getTime()) {
						oldDate = outdoc.getDeliveredDate();
					}
				}
			}
			if(dto.getDateTo() != null && dto.getDateFrom() != null) {
				cell.setCellValue(title + fromdate + simpleDateFormat.format(dto.getDateFrom()) + todate + simpleDateFormat.format(dto.getDateTo()));
			}else {
				cell.setCellValue(titles + fromdate + simpleDateFormat.format(oldDate) + todate + simpleDateFormat.format(newDate) );
			}
			
			if (listLetterOutDocument != null && listLetterOutDocument.size() > 0) {
		
				for (LetterOutDocumentDto doc : listLetterOutDocument) {

					rowCount++;
					columnCount = 0;
					
					row = worksheet.createRow(rowCount);
					// stt
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(rowCount - titleRow -2);

					// docOriginCode:số hiệu văn bản
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getDocOriginCode());

					// briefNote:trích yếu
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getBriefNote());					
					
					// deliveredDate: ngày tháng đến
					cell = row.createCell(columnCount++);
					cell.setCellStyle(dateCellStyle);
					cell.setCellValue(doc.getDeliveredDate());

					// issueOrgan: nơi gửi
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getOtherIssueOrgan());
					
					// signedPerson: người ký
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(doc.getSigner().getDisplayName());
					
				}
			}
			workbook.write(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=ThongKeCongVanDi.xls");
			response.flushBuffer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	@RequestMapping(value = "/exportEvent", method = { RequestMethod.POST })//Xuất excel lịch
	public void ExportEvent(WebRequest request, HttpServletResponse response, @RequestBody SearchEventDto dto) {
		List<EventDto> listEventDto=new ArrayList<EventDto>();
		String title = "Thống kê Cuộc họp";
		String fromdate= " từ ngày ";
		String todate= " đến ngày ";
		listEventDto = eventService.getEventBySearchDto(dto);

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = null;
//			if(dto.getDateFrom() != null && dto.getDateTo() != null) {
//				worksheet = workbook.createSheet(title + fromdate + dto.getDateFrom() + todate + dto.getDateTo()+".");
//			}else {
				worksheet = workbook.createSheet(title);
//			}
			
			HSSFCellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setColor(HSSFFont.COLOR_NORMAL);
			font.setFontName("");
			headerCellStyle.setBorderTop(BorderStyle.THIN);
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
			headerCellStyle.setBorderRight(BorderStyle.THIN);
			headerCellStyle.setBorderLeft(BorderStyle.THIN);
			headerCellStyle.setFont(font);
			// headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			headerCellStyle.setWrapText(false);

			int rowCount = 3;// ????
			int columnCount = 0;
			HSSFRow row;
			HSSFCell cell;

			// for header row
			row = worksheet.createRow(rowCount);
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("STT");
			worksheet.setColumnWidth(columnCount - 1, 6 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Tiêu đề cuộc họp");
			worksheet.setColumnWidth(columnCount - 1, 65 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Thời gian");
			worksheet.setColumnWidth(columnCount - 1, 30 * 256);
			
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Địa điểm");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
						
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Xử lý chính");
			worksheet.setColumnWidth(columnCount - 1, 30 * 256);
			
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Hình thức họp");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Số người tham gia");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			
			// title row
			int titleRow = 0;
			row = worksheet.createRow(titleRow++);
			worksheet.addMergedRegion(new CellRangeAddress(2, 2, 0, columnCount - 1));

			HSSFCellStyle dateCellStyle = workbook.createCellStyle();
			short df = workbook.createDataFormat().getFormat("dd/MM/yyyy");
			dateCellStyle.setDataFormat(df);
			dateCellStyle.setBorderTop(BorderStyle.THIN);
			dateCellStyle.setBorderBottom(BorderStyle.THIN);
			dateCellStyle.setBorderRight(BorderStyle.THIN);
			dateCellStyle.setBorderLeft(BorderStyle.THIN);
			
			HSSFCellStyle normalCellStyle = workbook.createCellStyle();
			normalCellStyle.setBorderTop(BorderStyle.THIN);
			normalCellStyle.setBorderBottom(BorderStyle.THIN);
			normalCellStyle.setBorderRight(BorderStyle.THIN);
			normalCellStyle.setBorderLeft(BorderStyle.THIN);
			normalCellStyle.setWrapText(true);
			normalCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			
			HSSFCellStyle titleCellStyle = workbook.createCellStyle();
			titleCellStyle.setBorderTop(BorderStyle.THIN);
			titleCellStyle.setBorderBottom(BorderStyle.THIN);
			titleCellStyle.setBorderRight(BorderStyle.THIN);
			titleCellStyle.setBorderLeft(BorderStyle.THIN);
			
			row = worksheet.createRow(0);
			cell = row.createCell(0);
			//cell.setCellStyle(titleCellStyle);
			cell.setCellValue("Bộ Giáo dục và đào tạo");
			
			row = worksheet.createRow(1);
			cell = row.createCell(0);
			//cell.setCellStyle(titleCellStyle);
			cell.setCellValue("Trường Đại Học Luật Hà Nội");
			
			HSSFCellStyle style0 = workbook.createCellStyle();
			HSSFFont font0 = workbook.createFont();
			font0.setFontHeightInPoints((short) 13);
			style0.setFont(font0);
			style0.setVerticalAlignment(VerticalAlignment.CENTER);
			style0.setAlignment(HorizontalAlignment.CENTER);
			row = worksheet.createRow(2);
			cell = row.createCell(0);
			cell.setCellStyle(style0);
			
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			if(dto.getStartTime() != null && dto.getEndTime() != null) {
				cell.setCellValue(title + fromdate + simpleDateFormat.format(dto.getStartTime().toDate()) + todate + simpleDateFormat.format(dto.getEndTime().toDate()));
			}else {
				cell.setCellValue(title);
			}
			
			if (listEventDto != null && listEventDto.size() > 0) {
		
				for (EventDto e : listEventDto) {

					rowCount++;
					columnCount = 0;
					
					row = worksheet.createRow(rowCount);
					// stt
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(rowCount - titleRow -2);

					// title: Tiêu đề cuộc họp
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(e.getTitle());

					// Time: Thời gian bắt đầu- thời gian kết thúc
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					String dat = "";
					String  dateStart = simpleDateFormat.format(e.getStartTime().toDate());
					String  dateEnd = simpleDateFormat.format(e.getEndTime().toDate());
					dat = "từ " + dateStart + " đến " + dateEnd;
					cell.setCellValue(dat);					
					
					// room: Địa điểm
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					if(e.getRoom() != null)
						cell.setCellValue(e.getRoom().getName());

					// chairPerson: xử lý chính
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					if(e.getChairPerson() != null) {
						cell.setCellValue(e.getChairPerson().getDisplayName());
					}
					// eventType: Hình thức họp
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					String eventType = "";
					if(e.getEventType() != null) {
						if(e.getEventType() == 1) {
							eventType= "Họp tại phòng họp";
						}
						if(e.getEventType() == 0) {
							eventType= "Họp trực tuyến";
						}
					}
					cell.setCellValue(eventType);
					
					// sAttendees: Số người tham gia
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					//int countNumber = e.getdAttendees().size() + e.getsAttendees().size();
					if(e.getsAttendees() != null) {
						cell.setCellValue(e.getsAttendees().size() + e.getdAttendees().size());
					}
					
					
				}
			}
			workbook.write(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=ThongCuocHop.xls");
			response.flushBuffer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}
