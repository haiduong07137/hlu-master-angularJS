import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.dto.DepartmentDto;
import com.globits.hr.config.DatabaseConfig;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.service.StaffService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class StaffTest {
	@Autowired
	StaffService service;
	
	@Test
	public void testSearchStaff() {
		StaffSearchDto dto = new StaffSearchDto();
		dto.setDepartment(new DepartmentDto());
		dto.getDepartment().setId(30L);
		dto.setIsMainPosition(true);
		dto.setKeyword("Hoàng Quốc  Dũng");
		Page<StaffDto> page = service.searchStaff(dto, 10, 1);
		System.out.println(page.getSize());
	}
}
