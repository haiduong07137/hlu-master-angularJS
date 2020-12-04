package com.globits.hr.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.globits.core.domain.Department;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.EthnicsDto;
import com.globits.core.dto.PersonDto;
import com.globits.core.dto.ReligionDto;
import com.globits.core.dto.TrainingBaseDto;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.service.DepartmentService;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.StaffDto;
import com.globits.security.domain.Role;
import com.globits.security.domain.User;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
import com.globits.security.repository.RoleRepository;
import com.globits.security.service.RoleService;

public class ImportExportExcelUtil {
	private static Hashtable<String, Integer> hashStaffColumnConfig = new Hashtable<String, Integer>();
	private static Hashtable<String, Integer> hashDepartmentColumnConfig = new Hashtable<String, Integer>();
	private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static DecimalFormat numberFormatter = new DecimalFormat("######################");
	private static Hashtable<String, String> hashColumnPropertyConfig = new Hashtable<String, String>();
	private static RoleService roleService;
	private static RoleRepository roleRepository;
	private static DepartmentRepository departmentRepository;
	private static DepartmentService departmentService;
	
	private static void scanStaffColumnExcelIndex(Sheet datatypeSheet, int scanRowIndex) {
		Row row = datatypeSheet.getRow(scanRowIndex);
		int numberCell = row.getPhysicalNumberOfCells();

		hashColumnPropertyConfig.put("staffCode".toLowerCase(),"staffCode");
		hashColumnPropertyConfig.put("firstName".toLowerCase(), "firstName");
		hashColumnPropertyConfig.put("lastName".toLowerCase(), "lastName");
		hashColumnPropertyConfig.put("displayName".toLowerCase(), "displayName");
		hashColumnPropertyConfig.put("birthdate".toLowerCase(), "birthdate");
		hashColumnPropertyConfig.put("birthdateMale".toLowerCase(), "birthdateMale");
		hashColumnPropertyConfig.put("birthdateFeMale".toLowerCase(), "birthdateFeMale");
		hashColumnPropertyConfig.put("gender".toLowerCase(), "gender");
		hashColumnPropertyConfig.put("address".toLowerCase(), "address");// Cái này cần xem lại
		hashColumnPropertyConfig.put("userName".toLowerCase(), "userName");
		hashColumnPropertyConfig.put("password".toLowerCase(), "password");
		hashColumnPropertyConfig.put("email".toLowerCase(), "email");
		hashColumnPropertyConfig.put("BirthPlace".toLowerCase(), "BirthPlace");
		hashColumnPropertyConfig.put("phoneNumber".toLowerCase(), "phoneNumber");
		hashColumnPropertyConfig.put("department".toLowerCase(), "department");
		hashColumnPropertyConfig.put("position".toLowerCase(), "position");
		hashColumnPropertyConfig.put("role".toLowerCase(), "role");
		hashColumnPropertyConfig.put("departmentCode".toLowerCase(), "departmentCode");
		hashColumnPropertyConfig.put("MaNgach".toLowerCase(), "MaNgach");
		hashColumnPropertyConfig.put("IDCard".toLowerCase(), "IDCard");
		
		for(int i=0;i<numberCell;i++) {
			Cell cell = row.getCell(i);
			if(cell!=null && cell.getCellTypeEnum()==CellType.STRING) {
				String cellValue = cell.getStringCellValue();
				if(cellValue!=null && cellValue.length()>0) {
					cellValue = cellValue.toLowerCase().trim();
					String propertyName = hashColumnPropertyConfig.get(cellValue);
					if(propertyName!=null) {
						hashStaffColumnConfig.put(propertyName,i);
					}
				}	
			}
		}
	}
	
	public static List<DepartmentDto> getListDepartmentFromInputStream(InputStream is) {
		try {

			List<DepartmentDto> ret = new ArrayList<DepartmentDto>();
			// FileInputStream excelFile = new FileInputStream(new File(filePath));
			// Workbook workbook = new XSSFWorkbook(excelFile);
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(is);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			// Iterator<Row> iterator = datatypeSheet.iterator();
			int rowIndex = 4;

			hashDepartmentColumnConfig.put("code", 0);
			
			hashDepartmentColumnConfig.put("name", 1);

			int num = datatypeSheet.getLastRowNum();
			while (rowIndex <= num) {
				Row currentRow = datatypeSheet.getRow(rowIndex);
				Cell currentCell = null;
				if (currentRow != null) {
					DepartmentDto department = new DepartmentDto();
					Integer index = hashDepartmentColumnConfig.get("code");
					if (index != null) {
						currentCell = currentRow.getCell(index);// code
						if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							double value = currentCell.getNumericCellValue();
							String code = numberFormatter.format(value);
							department.setCode(code);
						} else if (currentCell != null && currentCell.getStringCellValue() != null) {
							String code = currentCell.getStringCellValue();
							department.setCode(code);
						}
					}
				index = hashDepartmentColumnConfig.get("name");
				if (index != null) {
					currentCell = currentRow.getCell(index);// name
					if (currentCell != null && currentCell.getStringCellValue() != null) {
						String name = currentCell.getStringCellValue();
						department.setName(name);
					}
				}
				ret.add(department);
			}
				rowIndex++;
			}
			return ret;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<StaffDto> getListStaffFromInputStream(InputStream is) {
		try {

			List<StaffDto> ret = new ArrayList<StaffDto>();
			// FileInputStream excelFile = new FileInputStream(new File(filePath));
			// Workbook workbook = new XSSFWorkbook(excelFile);
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(is);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			// Iterator<Row> iterator = datatypeSheet.iterator();
			int rowIndex = 1;
			int scanRowIndex=0;
			scanStaffColumnExcelIndex(datatypeSheet, scanRowIndex);
//			hashStaffColumnConfig.put("staffCode", 39);
//			
//			hashStaffColumnConfig.put("birthdateMale", 5);
//			hashStaffColumnConfig.put("birthdateFeMale", 6);
//			hashStaffColumnConfig.put("email", 16);
//			hashStaffColumnConfig.put("phoneNumber", 17);
//			hashStaffColumnConfig.put("firstName", 40);
//			hashStaffColumnConfig.put("lastName", 41);
//			hashStaffColumnConfig.put("password", 42);
//			hashStaffColumnConfig.put("birthPlace", 43);

			int num = datatypeSheet.getLastRowNum();
			while (rowIndex <= num) {
				System.out.println("rowIndex="+rowIndex);
				Row currentRow = datatypeSheet.getRow(rowIndex);
				Cell currentCell = null;
				if (currentRow != null) {
					StaffDto staff = new StaffDto();
					
					Integer index = hashStaffColumnConfig.get("staffCode");
					if (index != null) {
						currentCell = currentRow.getCell(index);// staffCode
						if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							double value = currentCell.getNumericCellValue();
							String staffCode = numberFormatter.format(value);
							staff.setStaffCode(staffCode);
						} else if (currentCell != null && currentCell.getStringCellValue() != null) {
							String staffCode = currentCell.getStringCellValue();
							if(staffCode!=null) {
								staffCode = staffCode.trim();
							}
							staff.setStaffCode(staffCode);
						}
					}
					
					if(staff.getStaffCode() != null && staff.getStaffCode().length() > 0) {
						index = hashStaffColumnConfig.get("firstName");
						if (index != null) {
							currentCell = currentRow.getCell(index);// firstName
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String firstName = currentCell.getStringCellValue();
								staff.setFirstName(firstName);
							}
						}
						index = hashStaffColumnConfig.get("lastName");
						if (index != null) {
							currentCell = currentRow.getCell(index);// lastName
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String lastName = currentCell.getStringCellValue();
								staff.setLastName(lastName);
							}
						}
						index = hashStaffColumnConfig.get("displayName");
						if (index != null) {
							currentCell = currentRow.getCell(index);// lastName
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String displayName =  currentCell.getStringCellValue();
								staff.setDisplayName(displayName);
								if(staff.getFirstName()==null) {
									int lastIndex = displayName.lastIndexOf(' ');
									if(lastIndex>0 && lastIndex < displayName.length()) {
										int endIndex = displayName.length();
										String firstName = displayName.substring(lastIndex, endIndex);
										String lastName = displayName.substring(0, lastIndex);
										staff.setFirstName(firstName);
										staff.setLastName(lastName);
									}
								}
							}
						}
						index = hashStaffColumnConfig.get("birthdateMale");
						if (index != null) {
							currentCell = currentRow.getCell(index);// birthdate
							
							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getStringCellValue() != null) {
								String strBirthdate = currentCell.getStringCellValue();
								if (strBirthdate != null) {
									try {
										Date birthDate = dateFormat.parse(strBirthdate);
										staff.setBirthDate(birthDate);
										staff.setGender("M");
									} catch (Exception ex) {
	
									}
								}
							}else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								double dateValue = (double)currentCell.getNumericCellValue();
								if (dateValue>0) {
									try {
										Date birthDate = new Date( Math.round(dateValue));
										staff.setBirthDate(birthDate);
										staff.setGender("F");
									} catch (Exception ex) {
	
									}
								}
							}
						}
						index = hashStaffColumnConfig.get("birthdateFeMale");
						if (index != null) {
							currentCell = currentRow.getCell(index);// birthdate
							
							if (currentCell != null && currentCell.getStringCellValue() != null  && currentCell.getCellTypeEnum() == CellType.STRING) {
								String strBirthdate = currentCell.getStringCellValue();
								if (strBirthdate != null) {
									try {
										Date birthDate = dateFormat.parse(strBirthdate);
										staff.setBirthDate(birthDate);
										staff.setGender("M");
									} catch (Exception ex) {
	
									}
								}
							}else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								double dateValue = (double)currentCell.getNumericCellValue();
								if (dateValue>0) {
									try {
										Date birthDate = new Date( Math.round(dateValue));
										staff.setBirthDate(birthDate);
										staff.setGender("M");
									} catch (Exception ex) {
	
									}
								}
							}
						}
						index = hashStaffColumnConfig.get("birthdate");
						if (index != null) {
							currentCell = currentRow.getCell(index);// birthdate
							
							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getStringCellValue() != null) {
								String strBirthdate = currentCell.getStringCellValue();
								if (strBirthdate != null) {
									try {
										Date birthDate = dateFormat.parse(strBirthdate);
										staff.setBirthDate(birthDate);
										//staff.setGender("M");
									} catch (Exception ex) {
	
									}
								}
							}else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								double dateValue = (double)currentCell.getNumericCellValue();
								if (dateValue>0) {
									try {
										Date birthDate = new Date( Math.round(dateValue));
										staff.setBirthDate(birthDate);
										//staff.setGender("F");
									} catch (Exception ex) {
	
									}
								}
							}
						}
						index = hashStaffColumnConfig.get("gender");
						if (index != null) {
							currentCell = currentRow.getCell(index);// gender
							
							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getStringCellValue() != null) {
								String gender = currentCell.getStringCellValue();
								if (gender != null && gender =="0") {
									staff.setGender("M");
								}else if(gender != null && gender =="1") {
									staff.setGender("F");
								}else {
									staff.setGender("U");
								}
							}else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								int dateValue = (int)currentCell.getNumericCellValue();
								if (dateValue==0) {
									staff.setGender("M");
								}else if(dateValue ==1) {
									staff.setGender("F");
								}else {
									staff.setGender("U");
								}
							}
						}
						
						index = hashStaffColumnConfig.get("IDCard");//cmnd
						if (index != null) {
							currentCell = currentRow.getCell(index);// cmnd
							
							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getStringCellValue() != null) {
								String cmnd = currentCell.getStringCellValue();
								if (cmnd != null ) {
									staff.setIdNumber(cmnd);
								}
							}else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								double value = currentCell.getNumericCellValue();
								String cmnd = numberFormatter.format(value);								
								if (cmnd!=null) {
									staff.setIdNumber(cmnd);
								}
							}
						}
						
						index = hashStaffColumnConfig.get("userName");  // create userName nếu có
						if (index != null) {
							currentCell = currentRow.getCell(index);// userName
							String userName =null;
							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING) {
								userName = currentCell.getStringCellValue();
							}else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
								userName = ""+currentCell.getNumericCellValue();
							}
							if(userName != null) {
									UserDto user = new UserDto();
									user.setUsername(userName);
									index = hashStaffColumnConfig.get("password");
									if(index!=null) {
										currentCell = currentRow.getCell(index);// password
										if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
											double dPassword = currentCell.getNumericCellValue();
											String password = numberFormatter.format(dPassword);
											user.setPassword(password);
										} else if (currentCell != null && currentCell.getStringCellValue() != null) {
											String password = currentCell.getStringCellValue();
											user.setPassword(password);
										}
									}else {
										user.setPassword("123456");
									}
									index = hashStaffColumnConfig.get("email");
									if(index!=null) {
										currentCell = currentRow.getCell(index);// email
										if (currentCell != null && currentCell.getStringCellValue() != null) {
											String email = currentCell.getStringCellValue();
											user.setEmail(email);
											staff.setUser(user);
										}else {
										String email = staff.getStaffCode()+"@hlu.edu.vn";
										user.setEmail(email);
										staff.setUser(user);
										}
									}else {
										String email = staff.getStaffCode()+"@hlu.edu.vn";
										user.setEmail(email);
										staff.setUser(user);
									}								
								}else if(staff.getStaffCode()!=null) {// lấy tạm userName là staffCode
									UserDto user = new UserDto();
									user.setUsername(staff.getStaffCode());
									index = hashStaffColumnConfig.get("password");
									if(index!=null) {
										currentCell = currentRow.getCell(index);// password
										if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
											double dPassword = currentCell.getNumericCellValue();
											String password = numberFormatter.format(dPassword);
											user.setPassword(password);
										} else if (currentCell != null && currentCell.getStringCellValue() != null) {
											String password = currentCell.getStringCellValue();
											user.setPassword(password);
										}
									}else if(staff.getIdNumber()!=null) {
										user.setPassword(staff.getIdNumber());
									}
									else {
										user.setPassword("123456");
									}
									index = hashStaffColumnConfig.get("email");
									if(index!=null) {
										currentCell = currentRow.getCell(index);// email
										if (currentCell != null && currentCell.getStringCellValue() != null) {
											String email = currentCell.getStringCellValue();
											user.setEmail(email);
											staff.setUser(user);
										}else {
											String email = staff.getStaffCode()+"@hlu.edu.vn";
											user.setEmail(email);
											staff.setUser(user);
										}						
									}else {
										String email = staff.getStaffCode()+"@hlu.edu.vn";
										user.setEmail(email);
										staff.setUser(user);
									}	
								}
							}else if(staff.getStaffCode()!=null) {// lấy tạm userName là staffCode
								UserDto user = new UserDto();
								user.setUsername(staff.getStaffCode());
								index = hashStaffColumnConfig.get("password");
								if(index!=null) {
									currentCell = currentRow.getCell(index);// password
									if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
										double dPassword = currentCell.getNumericCellValue();
										String password = numberFormatter.format(dPassword);
										user.setPassword(password);
									} else if (currentCell != null && currentCell.getStringCellValue() != null) {
										String password = currentCell.getStringCellValue();
										user.setPassword(password);
									}
								}else if(staff.getIdNumber()!=null) {
									user.setPassword(staff.getIdNumber());
								}
								else {
									user.setPassword("123456");
								}
								index = hashStaffColumnConfig.get("email");
								if(index!=null) {
									currentCell = currentRow.getCell(index);// email
									if (currentCell != null && currentCell.getStringCellValue() != null) {
										String email = currentCell.getStringCellValue();
										user.setEmail(email);
										staff.setUser(user);
									}					
								}else {
									String email = staff.getStaffCode()+"@hlu.edu.vn";
									user.setEmail(email);
									staff.setUser(user);
								}	
							}
//						index = hashStaffColumnConfig.get("departmentCode");
//						if (index != null) {ex
//							currentCell = currentRow.getCell(index);
//							if (currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
//								double departmentID = currentCell.getNumericCellValue();
//								PositionStaffDto position = new PositionStaffDto();
//								
//								position.setDepartment(new DepartmentDto());
//								position.getDepartment().setId((long)departmentID);
//								position.setPosition(new PositionTitleDto());
//								
//								index = hashStaffColumnConfig.get("position");
//								currentCell = currentRow.getCell(index);
//								if (currentCell!=null && currentCell.getCellTypeEnum()==CellType.NUMERIC) {
//									double positionId = currentCell.getNumericCellValue();
//									position.getPosition().setId((long)positionId);
//									position.setMainPosition(true);
//									position.setCurrent(true);
//								}
//								staff.getPositions().add(position);
//							}
//						}
						
						index = hashStaffColumnConfig.get("BirthPlace");
						if (index != null) {
							currentCell = currentRow.getCell(index);// birthPlace
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String birthPlace = currentCell.getStringCellValue();
								staff.setBirthPlace(birthPlace);
							}
						}
						index = hashStaffColumnConfig.get("ethnic");
						if (index != null) {
							currentCell = currentRow.getCell(index);// ethnic
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String ethnicCode = currentCell.getStringCellValue();
								EthnicsDto ethnicsDto = new EthnicsDto();
								ethnicsDto.setCode(ethnicCode);
								staff.setEthnics(ethnicsDto);
							}
						}
						index = hashStaffColumnConfig.get("religion");
						if (index != null) {
							currentCell = currentRow.getCell(index);// religion
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String religionCode = currentCell.getStringCellValue();
								ReligionDto religionDto = new ReligionDto();
								religionDto.setCode(religionCode);
								staff.setReligion(religionDto);
							}
						}
						index = hashStaffColumnConfig.get("phoneNumber");
						if (index != null) {
							currentCell = currentRow.getCell(index);// phoneNumber
							if (currentCell != null && currentCell.getStringCellValue() != null) {
								String phoneNumber = currentCell.getStringCellValue();
								PersonDto p = new PersonDto();
								p.setPhoneNumber(phoneNumber);
								staff.setPhoneNumber(p.getPhoneNumber());
							}
						}
//						index = hashStaffColumnConfig.get("position");
//						if (index != null) {
//							currentCell = currentRow.getCell(index);// position
//							if (currentCell != null && currentCell.getStringCellValue() != null) {
//									String position = currentCell.getStringCellValue();
//									PositionStaffDto positionStaffDto = new PositionStaffDto();
//									PositionTitleDto positionTitleDto = new PositionTitleDto();
//									positionStaffDto.getPosition().setCode(position);
//									positionStaffDto.setCurrent(true);
//									staff.getPositions().add(positionStaffDto);							
//								}
//						}
//						index = hashStaffColumnConfig.get("department");
//						if (index != null) {
//							currentCell = currentRow.getCell(index);// position
//							if (currentCell != null && currentCell.getStringCellValue() != null) {
//									String department = currentCell.getStringCellValue();
//									PositionStaffDto positionStaffDto = new PositionStaffDto();
//									DepartmentDto departmentDto = new DepartmentDto();
//									positionStaffDto.getDepartment().setCode(department);
//									staff.getPositions().add(positionStaffDto);						
//								}
//						}
//						index = hashStaffColumnConfig.get("role");
//						if (index != null) {
//							currentCell = currentRow.getCell(index);// position
//							if (currentCell != null && currentCell.getStringCellValue() != null) {
//								double role = currentCell.getNumericCellValue();
////									Role rle = new Role();
////									User user = new User();
////									Set<Role> roles = new HashSet<Role>();
////									rle = roleRepository.findByName(role);
////									roles.add(rle);
////									//user.setRoles(roles);
//									staff.getU
//							}
//						}
//						index = hashStaffColumnConfig.get("MaNgach");
//						if (index != null) {
//							currentCell = currentRow.getCell(index);// MaNgach
//							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//								double value = currentCell.getNumericCellValue();
//								String code = numberFormatter.format(value);
//								
//							} else if (currentCell != null && currentCell.getStringCellValue() != null) {
//								String code = currentCell.getStringCellValue();
//								if(code!=null) {
//									
//								}
//								
//							}
//						}
						
						ret.add(staff);
					}
				}
				rowIndex++;
			}
			return ret;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] agrs) {
		try {
			
				FileInputStream fileIn = new FileInputStream(new File("C:\\Desktop\\Import_Staffs.xlsx"));
				List lst = getListStaffFromInputStream(fileIn);
				System.out.println(lst.size());
			}catch (Exception ex) {
					ex.printStackTrace();
			}

	}
}
