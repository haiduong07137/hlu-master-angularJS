import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.hr.config.DatabaseConfig;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.timesheet.dto.TimeSheetDetailDto;
import com.globits.hr.timesheet.dto.TimeSheetDto;
import com.globits.hr.timesheet.repository.TimeSheetRepository;
import com.globits.hr.timesheet.service.TimeSheetDetailService;
import com.globits.hr.timesheet.service.TimeSheetService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class TimeSheetTest {
	@Autowired
	TimeSheetRepository timeSheetRepository;
	@Autowired
	TimeSheetService service;
	@Autowired
	TimeSheetDetailService detailService;
//	@Test
//	public void test() {
//		
//	}
	
	//@Test
	public void saveTimeSheet() {
		TimeSheetDto dto = new TimeSheetDto();
		//dto.setId(2L);
		Date date = new Date();
		dto.setWorkingDate(date);
		StaffDto staffDto = new StaffDto();
		staffDto.setId(872L);
		dto.setEmployee(staffDto);
		TimeSheetDetailDto detailDto = new TimeSheetDetailDto();
		detailDto.setDuration(8);
		detailDto.setStartTime(date);
		detailDto.setEndTime(date);
		dto.setDetails(new ArrayList<TimeSheetDetailDto>());
		dto.getDetails().add(detailDto);
		dto = service.saveTimeSheet(dto);
		System.out.println(dto.getId());
	}
	
	//@Test
	public void saveTimeSheetDetail() throws ParseException {
//		TimeSheetDto dto = new TimeSheetDto();
//		dto.setId(2L);
//		Date date = new Date();
//		dto.setWorkingDate(date);
//		StaffDto staffDto = new StaffDto();
//		staffDto.setId(872L);
//		dto.setEmployee(staffDto);
//		dto = service.saveTimeSheet(dto);
//		System.out.println(dto.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String dateInString = "27-07-2018 08:30:00";
		Date date = sdf.parse(dateInString);
		TimeSheetDetailDto dto = new TimeSheetDetailDto();
		dto.setDuration(3);
		Date startTime =sdf.parse(dateInString);
		 dateInString = "27-07-2018 14:30:00";
		Date endTime =sdf.parse(dateInString);
		dto.setStartTime(startTime);
		dto.setEndTime(endTime);
		detailService.saveTimeSheetDetail(dto);
		
	}	
	//@Test
	public void getTimeSheetByDate() {
		TimeSheetDto dto = new TimeSheetDto();
		Date date = new Date();
		dto = service.findTimeSheetByDate(date);
		System.out.println(dto.getId());
	}
	@Test
	public void searchStaff() {
		Page<StaffDto> page = service.findPageByName("D",1, 10);
		System.out.println(page.getSize());
	}
}
