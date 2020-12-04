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
import com.globits.calendar.dto.EventAttendeeDto;
import com.globits.calendar.dto.EventDto;
import com.globits.calendar.dto.ViewDateEventDto;
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
public class EventTest {
	@Autowired
	EventService eventService;

	@Autowired
	RoleService roleService;

	@Autowired
	UserService userService;

	//@Test
	public void saveTest() {

//		 Role role = new Role();
//		 role.setName("ROLE_CALENDAR_APPROVER");
//		
//		 roleService.save(role);

		// Calendar users
//		UserDto user = new UserDto();
//		// user.setAccountNonExpired(true);
//		// user.setAccountNonLocked(true);
//		user.setActive(true);
//		user.setEmail("cal_user3@globits.net");
//		user.setUsername("cal_user3");
//		// user.setJustCreated(true);
//		user.setPassword("1234567");
//		Set<RoleDto> roles = new HashSet<RoleDto>();
//
//		Role role = roleService.findById(10l); // role-calendar-management
//		// Role role = roleService.findById(8l); // role-staff
//
//		roles.add(new RoleDto(role));
//		user.setRoles(roles);
//
//		PersonDto person = new PersonDto();
//		person.setFirstName("Van Huyen");
//		person.setLastName("Bui");
//		person.setDisplayName("Bui Van Huyen");
//		person.setGender("M");
//
//		user.setPerson(person);
//
//		userService.save(user);

		 EventDto dto = new EventDto();
		 dto.setOtherAttendees("Toàn bộ lãnh đạo khối sản xuất");
		 Set<EventAttendeeDto> attendees = new HashSet<EventAttendeeDto>();
		 EventAttendeeDto att = new EventAttendeeDto();
		 att.setAttendeeType(0);
		 DepartmentDto depDto = new DepartmentDto();
		 depDto.setId(1L);
		 att.setDepartment(depDto);
		 attendees.add(att);
		 // dto.setAttendees(attendees);
		
		 att = new EventAttendeeDto();
		 att.setIsChairPerson(true);
		 att.setAttendeeType(1);
		 StaffDto staffDto = new StaffDto();
		 staffDto.setId(872L);
		 att.setStaff(staffDto);
		 attendees.add(att);
		 dto.setdAttendees(attendees);
		
		 att = new EventAttendeeDto();
		 att.setIsChairPerson(false);
		 att.setAttendeeType(2);
		 att.setDisplayName("Hoang Quoc Dung");
		 attendees.add(att);
		 dto.setdAttendees(attendees);
		
		 dto.setTitle("Test8h15");
		 dto.setLocation("Test8h15");
		 dto.setScope(0);
		 DateTime startDateTime = new DateTime(2018, 8, 27, 8, 15);
		 // Date startTime = startDateTime.toDate();
		 dto.setStartTime(startDateTime);
		 eventService.saveOne(dto);
	}

	 @Test
	public void listTest() {
//		EventDto dto = new EventDto();
//		dto.setTitle("Test");
//		dto.setLocation("Test");
//		DateTime fromDateTime = new DateTime(2017, 11, 20, 0, 0);
//		DateTime toDateTime = new DateTime(2017, 11, 27, 12, 59);
//		// Date fromDate = fromDateTime.toDate();
		// Date toDate = toDateTime.toDate();
		// List<EventDto> list = eventService.listEventByScopeAndDate(0, 0,
		// fromDateTime, toDateTime);
		// System.out.println(list.size());
		 DateTime startDateTime = new DateTime(2018, 8, 26, 0, 0);
		 List<ViewDateEventDto> listEvent= eventService.getListEventByWeek(startDateTime);
		 System.out.println(listEvent.size());
	}

	// @Test
	public void deleteTest() {
		// Page<Event> page = eventService.getList(1, 10);
		// if (page != null && page.getContent() != null)
		// eventService.deleteMultiple(page.getContent());
	}
}
