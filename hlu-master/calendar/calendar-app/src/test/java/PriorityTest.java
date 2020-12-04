import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.calendar.config.DatabaseConfig;
import com.globits.calendar.domain.EventPriority;
import com.globits.calendar.dto.EventAttendeeDto;
import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.EventPriorityDto;
import com.globits.calendar.dto.ViewDateEventDto;
import com.globits.calendar.service.EventPriorityService;
import com.globits.calendar.service.EventService;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.PersonDto;
import com.globits.hr.dto.StaffDto;
import com.globits.security.domain.Role;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
import com.globits.security.service.RoleService;
import com.globits.security.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class PriorityTest {
	@Autowired
	EventPriorityService service;
	@Test
	public void saveTest() {
		EventPriorityDto dto = new EventPriorityDto();
		dto.setName("Test");
		dto.setPriority(1);
		dto = service.saveOne(dto);
		System.out.println(dto.getId());
	}
}
