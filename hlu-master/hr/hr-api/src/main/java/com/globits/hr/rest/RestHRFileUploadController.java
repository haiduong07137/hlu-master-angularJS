package com.globits.hr.rest;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.globits.core.Constants;
import com.globits.core.domain.Department;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.service.DepartmentService;
import com.globits.hr.HrConstants;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.service.StaffService;
import com.globits.hr.utils.ImportExportExcelUtil;
import com.globits.security.domain.User;

@Controller
@RequestMapping("/api/hr/file")
public class RestHRFileUploadController {
	@Autowired
	StaffService staffService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	private DepartmentRepository departmentRepository;

	/**
	 * POST /uploadFile -> receive and locally save a file.
	 * 
	 * @param uploadfile
	 *            The uploaded file as Multipart file parameter in the HTTP request.
	 *            The RequestParam name must be the same of the attribute "name" in
	 *            the input tag with type file.
	 * 
	 * @return An http OK status in case of success, an http 4xx status in case of
	 *         errors.
	 */
	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("uploadfile") MultipartFile uploadfile) {

		try {
			// Get the filename and build the local file path
			String filename = uploadfile.getOriginalFilename();
			String directory = "C:\\temp";
			String filepath = Paths.get(directory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	} // method uploadFile

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/importStaff", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> importStaffFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(uploadfile.getBytes());
			List<StaffDto> list = ImportExportExcelUtil.getListStaffFromInputStream(bis);
			staffService.saveListStaff(list);
			// System.out.println(list.size());
			// for(int i=0;i<list.size();i++) {
			// StaffDto staff = list.get(i);
			// staff = staffService.createStaffFromDto(staff);//Cần tính đến vấn đề
			// exception để báo các trường hợp không đúng
			// }
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	} // method upload Student

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/importDepartment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> importDepartmentFile(@RequestParam("uploadfile") MultipartFile uploadfile) {
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(uploadfile.getBytes());
			List<DepartmentDto> list = ImportExportExcelUtil.getListDepartmentFromInputStream(bis);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				DepartmentDto department = list.get(i);
				department = createDepartmentFromDto(department);// Cần tính đến vấn đề exception để báo các trường hợp
																	// không đúng
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Transactional
	public DepartmentDto createDepartmentFromDto(DepartmentDto departmentDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		Department department = null;
		if (departmentDto.getId() != null) {
			department = departmentRepository.findOne(departmentDto.getId());
		}
		if (department == null && departmentDto.getCode() != null) {
			department = findByCode(departmentDto.getCode());
		}
		if (department == null) {
			department = new Department();
			department.setCreateDate(currentDate);
			if (modifiedUser != null) {
				department.setCreatedBy(modifiedUser.getUsername());
			} else {
				department.setCreatedBy(currentUserName);
			}
		}

		if (departmentDto.getName() != null) {
			department.setName(departmentDto.getName());
		}
		if (departmentDto.getCode() != null) {
			department.setCode(departmentDto.getCode());
		}

		department = departmentRepository.save(department);
		return new DepartmentDto(department);
	}

	@Query("select u from Department u where u.code = ?1")
	public Department findByCode(String code) {
		Department dep = new Department();
		return dep;
	}

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/fileuploaddep", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFileDep(@RequestParam("uploadfile") MultipartFile uploadfile) {

		try {
			// Get the filename and build the local file path
			String filename = uploadfile.getOriginalFilename();
			String directory = "C:\\temp";
			String filepath = Paths.get(directory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	} // method uploadFile

	@Secured({ HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN })
	@RequestMapping(value = "/exportDepartment", method = { RequestMethod.POST })
	public void exportDepartment(WebRequest request, HttpServletResponse response,
			@RequestBody List<DepartmentDto> dtos) {
		// if(!Context.isAuthenticated()) {
		// response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// return;
		// }
		int startIndex = -1;
		int limit = 0; // get all

		List<DepartmentDto> list = new ArrayList<DepartmentDto>();
		if (dtos != null && dtos.size() > 0) {
			Department department = null;
			for (DepartmentDto dto : dtos) {
				if (dto.getId() != null) {
					department = departmentRepository.findOne(dto.getId());
				}
				if (dto.getCode() != null) {
					department.setCode(dto.getCode());
				}
				if (dto.getName() != null) {
					department.setName(dto.getName());
				}
				list.add(dto);
			}
		}

		String title = "Phòng Ban ";

		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(title);

			HSSFCellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			headerCellStyle.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);

			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 12);
			font.setColor(HSSFFont.COLOR_NORMAL);
			font.setFontName("");
			// font.setBoldweight(HSSFFont.FONT_ARIAL);
			//
			headerCellStyle.setBorderTop(BorderStyle.THIN);
			headerCellStyle.setBorderBottom(BorderStyle.THIN);
			headerCellStyle.setBorderRight(BorderStyle.THIN);
			headerCellStyle.setBorderLeft(BorderStyle.THIN);
			headerCellStyle.setFont(font);
			// headerCellStyle.setAlignment(AlignmentStyle.ALIGN_CENTER);
			headerCellStyle.setWrapText(false);

			int rowCount = 1;
			int columnCount = 0;
			HSSFRow row;
			HSSFCell cell;

			// if(!StringUtils.isNullOrEmpty(title))
			// rowCount++;
			// if(!StringUtils.isNullOrEmpty(description))
			// rowCount++;

			// for header row
			row = worksheet.createRow(rowCount);
			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("STT");
			worksheet.setColumnWidth(columnCount - 1, 6 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Mã Phòng Ban");
			worksheet.setColumnWidth(columnCount - 1, 20 * 256);

			cell = row.createCell(columnCount++);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue("Tên Phòng Ban");
			worksheet.setColumnWidth(columnCount - 1, 50 * 256);

			// row = worksheet.createRow(rowCount);
			// cell = row.createCell(columnCount++);
			// cell.setCellStyle(headerCellStyle);
			// cell.setCellValue("CSDT do PF tài trợ");
			// worksheet.setColumnWidth(columnCount - 1, 12 * 256);

			// cell = row.createCell(columnCount++);
			// cell.setCellStyle(headerCellStyle);
			// cell.setCellValue("% BN có thẻ BHYT");
			// worksheet.setColumnWidth(columnCount - 1, 20 * 256);

			// cell = row.createCell(columnCount++);
			// cell.setCellStyle(headerCellStyle);
			// cell.setCellValue("CSYT được BHXH thanh toán cho các dịch vụ, cả ARV");
			// worksheet.setColumnWidth(columnCount - 1, 20 * 256);
			//
			// cell = row.createCell(columnCount++);
			// cell.setCellStyle(headerCellStyle);
			// cell.setCellValue("Số BN được thanh toán ARV qua BHYT");
			// worksheet.setColumnWidth(columnCount - 1, 20 * 256);

			// title row
			int titleRow = 0;
			row = worksheet.createRow(titleRow++);
			HSSFCellStyle style0 = workbook.createCellStyle();
			HSSFFont font0 = workbook.createFont();
			font0.setFontHeightInPoints((short) 13);
			// font0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style0.setFont(font0);
			// style0.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell = row.createCell(0);
			cell.setCellStyle(style0);
			cell.setCellValue(title.toUpperCase());
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount - 1));

			HSSFCellStyle dateCellStyle = workbook.createCellStyle();
			short df = workbook.createDataFormat().getFormat("dd//MM/yyyy");
			dateCellStyle.setDataFormat(df);
			// dateCellStyle.setBorderTop((short)1);
			// dateCellStyle.setBorderBottom((short)1);
			// dateCellStyle.setBorderRight((short)1);
			// dateCellStyle.setBorderLeft((short)1);
			HSSFCellStyle normalCellStyle = workbook.createCellStyle();
			// normalCellStyle.setBorderTop((short)1);
			// normalCellStyle.setBorderBottom((short)1);
			// normalCellStyle.setBorderRight((short)1);
			// normalCellStyle.setBorderLeft((short)1);

			if (list != null && list.size() > 0) {
				for (DepartmentDto s : list) {

					rowCount++;
					columnCount = 0;

					row = worksheet.createRow(rowCount);
					// stt
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					cell.setCellValue(rowCount - titleRow);

					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					if (s.getCode() != null)
						cell.setCellValue(s.getCode());
					//
					cell = row.createCell(columnCount++);
					cell.setCellStyle(normalCellStyle);
					if (s.getName() != null)
						cell.setCellValue(s.getName());

					//

				}
			}

			workbook.write(response.getOutputStream());

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=BaoCaoHangThang.xls");
			response.flushBuffer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}

}