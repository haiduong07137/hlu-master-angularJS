package com.globits.calendar.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.globits.core.dto.PersonDto;
import com.globits.core.utils.CommonUtils;
import com.globits.security.domain.Role;
import com.globits.security.dto.RoleDto;
import com.globits.security.dto.UserDto;
import com.globits.security.service.RoleService;
import com.globits.security.service.UserService;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

	private static boolean eventFired = false;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	private void createRoleIfNotExist(String roleName) {

		if (CommonUtils.isEmpty(roleName)) {
			return;
		}

		Role role = roleService.findByName(roleName);

		if (CommonUtils.isNotNull(role)) {
			return;
		}

		if (role == null) {
			role = new Role();
			role.setName(roleName);
		}

		try {
			roleService.save(role);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	private void createRoles() throws XMLStreamException {

		List<String> roleNames = new ArrayList<>();

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = getClass().getClassLoader().getResourceAsStream("sys-roles.xml");
		XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in, "UTF-8");

		streamReader.nextTag();
		streamReader.nextTag();

		while (streamReader.hasNext()) {
			if (streamReader.isStartElement()) {
				switch (streamReader.getLocalName()) {
				case "name": {
					roleNames.add(streamReader.getElementText());
					break;
				}
				}
			}
			streamReader.next();
		}

		streamReader.close();

		for (String roleName : roleNames) {
			createRoleIfNotExist(roleName);
		}
	}
	private void createAdminUser() {

		UserDto userDto = userService.findByUsername(com.globits.core.Constants.USER_ADMIN_USERNAME);

		if (CommonUtils.isNotNull(userDto)) {
			return;
		}

		userDto = new UserDto();
		userDto.setUsername(com.globits.core.Constants.USER_ADMIN_USERNAME);
		//userDto.setPassword(SecurityUtils.getHashPassword("admin"));
		userDto.setPassword("admin");
		userDto.setEmail("admin@globits.net");
		userDto.setActive(true);
		userDto.setDisplayName("Admin User");

		Role role = roleService.findByName(com.globits.core.Constants.ROLE_ADMIN);
		
		userDto.getRoles().addAll(Arrays.asList(new RoleDto(role)));
		
		PersonDto person = new PersonDto();
		person.setGender("M");
		person.setFirstName("Admin");
		person.setLastName("User");
		person.setDisplayName("Admin User");
		
		userDto.setPerson(person);
		
		try {
			userService.save(userDto);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// Only process the first time the event is fired (meaning when the
		// application context is sucessfully loaded)
		if (eventFired) {
			return;
		}

		// The application is started
		// logger.info("Application started.");

		eventFired = true;

		// Initialization here...
		createRole("ROLE_CALENDAR_MANAGEMENT");
		createRole("ROLE_CALENDAR_APPROVER");
		createAdminUser();
		createCalendarUsers();
	}

	private void createRole(String roleName) {

		System.out.println("Creating calendar roles...");

		if (roleName == null) {
			return;
		}

		Role role = roleService.findByName(roleName);

		if (role != null) {
			return;
		}

		role = new Role();
		role.setName(roleName);
		roleService.save(role);
	}

	private void createCalendarUsers() {

		System.out.println("Creating testing calendar users...");

		// User 1
		UserDto user = userService.findByUsername("cal_user1");

		if (user == null) {

			user = new UserDto();
			user.setActive(true);
			user.setEmail("cal_user1@globits.net");
			user.setUsername("cal_user1");
			user.setPassword("1234567");
			Set<RoleDto> roles = new HashSet<RoleDto>();

			Role role = roleService.findByName("ROLE_STAFF");
			roles.add(new RoleDto(role));
			user.setRoles(roles);

			PersonDto person = new PersonDto();
			person.setFirstName("Calendar");
			person.setLastName("User 1");
			person.setDisplayName("Calendar User 1");
			person.setGender("M");

			user.setPerson(person);
			userService.save(user);
		}

		// User 2
		user = userService.findByUsername("cal_user2");

		if (user == null) {
			user = new UserDto();
			user.setActive(true);
			user.setEmail("cal_user2@globits.net");
			user.setUsername("cal_user2");
			user.setPassword("1234567");
			Set<RoleDto> roles = new HashSet<RoleDto>();

			Role role = roleService.findByName("ROLE_CALENDAR_APPROVER");
			roles.add(new RoleDto(role));
			user.setRoles(roles);

			PersonDto person = new PersonDto();
			person.setFirstName("Calendar");
			person.setLastName("User 2");
			person.setDisplayName("Calendar User 2");
			person.setGender("M");

			user.setPerson(person);
			userService.save(user);
		}

		// User 3
		user = userService.findByUsername("cal_user3");

		if (user == null) {
			user = new UserDto();
			user.setActive(true);
			user.setEmail("cal_user3@globits.net");
			user.setUsername("cal_user3");
			user.setPassword("1234567");
			Set<RoleDto> roles = new HashSet<RoleDto>();

			Role role = roleService.findByName("ROLE_CALENDAR_MANAGEMENT");
			roles.add(new RoleDto(role));
			user.setRoles(roles);

			PersonDto person = new PersonDto();
			person.setFirstName("Calendar");
			person.setLastName("User 3");
			person.setDisplayName("Calendar User 3");
			person.setGender("M");

			user.setPerson(person);
			userService.save(user);
		}
	}
}
