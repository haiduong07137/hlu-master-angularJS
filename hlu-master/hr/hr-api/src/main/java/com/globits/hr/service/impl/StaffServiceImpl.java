package com.globits.hr.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.core.domain.Country;
import com.globits.core.domain.Department;
import com.globits.core.domain.Ethnics;
import com.globits.core.domain.Person;
import com.globits.core.domain.PersonAddress;
import com.globits.core.domain.Religion;
import com.globits.core.repository.CountryRepository;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.repository.EthnicsRepository;
import com.globits.core.repository.PersonRepository;
import com.globits.core.repository.ReligionRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.CommonUtils;
import com.globits.core.utils.SecurityUtils;
import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.domain.PositionStaff;
import com.globits.hr.domain.PositionTitle;
import com.globits.hr.domain.Staff;
import com.globits.hr.domain.StaffLabourAgreement;
import com.globits.hr.domain.StaffLabourAgreementAttachment;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffLabourAgreementDto;
import com.globits.hr.dto.StaffSearchDto;
import com.globits.hr.repository.PositionStaffRepository;
import com.globits.hr.repository.PositionTitleRepository;
import com.globits.hr.repository.StaffRepository;
import com.globits.hr.service.LabourAgreementTypeService;
import com.globits.hr.service.StaffLabourAgreementService;
import com.globits.hr.service.StaffService;
import com.globits.security.domain.User;
import com.globits.security.repository.UserRepository;

//@Transactional
@Service
public class StaffServiceImpl extends GenericServiceImpl<Staff, Long>  implements StaffService { // extends GenericServiceImpl<Staff, Long> implements
	private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);
	@Autowired
	private StaffRepository staffRepository;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PositionStaffRepository positionStaffRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private ReligionRepository religionRepository;
	
	@Autowired
	private EthnicsRepository ethnicsRepository;
	
	@Autowired
	DepartmentRepository departmentRepository; 
	
	@Autowired
	private PositionTitleRepository positionTitleRepository;
	// @Autowired
	// private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	StaffLabourAgreementService staffLabourAgreement;

	@Autowired
	LabourAgreementTypeService labourAgreementTypeService;
	
	public Staff getStaff(Long staffId) {
		Staff staff = staffRepository.findById(staffId);

		Staff retStaff = new Staff(staff);
		HashSet<PositionStaff> positions = new HashSet<PositionStaff>();
		if (staff.getPositions() != null && staff.getPositions().size() > 0) {
			Iterator<PositionStaff> iter = staff.getPositions().iterator();
			while (iter.hasNext()) {
				PositionStaff ps = iter.next();
				ps = new PositionStaff(ps);
				positions.add(ps);
			}
		}
		
		retStaff.setPositions(positions);
		return retStaff;
	}

	public Page<StaffDto> findByPageBasicInfo(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return staffRepository.findByPageBasicInfo(pageable);
	}
	@Transactional
	public Staff updateStaff(Staff staff, Long staffId) {
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// User modifiedUser = null;
		// Date currentDate = new Date();
		// String currentUserName = "Unknown User";
		// if (authentication != null) {
		// modifiedUser = (User) authentication.getPrincipal();
		// currentUserName = modifiedUser.getUsername();
		// }
		Staff updateStaff = staffRepository.findById(staffId);
		if (updateStaff != null) {
			// updateStaff.setModifyDate(currentDate);

			if (staff.getStaffCode() != null)
				updateStaff.setStaffCode(staff.getStaffCode());
			if (staff.getFirstName() != null)
				updateStaff.setFirstName(staff.getFirstName());
			if (staff.getLastName() != null)
				updateStaff.setLastName(staff.getLastName());
			if (staff.getBirthDate() != null)
				updateStaff.setBirthDate(staff.getBirthDate());
			if (staff.getBirthPlace() != null)
				updateStaff.setBirthPlace(staff.getBirthPlace());
			if (staff.getGender() != null)
				updateStaff.setGender(staff.getGender());
			if (staff.getPhoto() != null)
				updateStaff.setPhoto(staff.getPhoto());
			if (staff.getDisplayName() != null)
				updateStaff.setDisplayName(staff.getDisplayName());
			if (staff.getPhoneNumber() != null)
				updateStaff.setPhoneNumber(staff.getPhoneNumber());

			if (staff.getEthnics() != null)
				updateStaff.setEthnics(staff.getEthnics());
			if (staff.getEmail() != null)
				updateStaff.setEmail(staff.getEmail());
			if (staff.getNationality() != null)
				updateStaff.setNationality(staff.getNationality());
			if (staff.getNativeVillage() != null)
				updateStaff.setNativeVillage(staff.getNativeVillage());
			if (staff.getReligion() != null)
				updateStaff.setReligion(staff.getReligion());
			if (staff.getIdNumber() != null)
				updateStaff.setIdNumber(staff.getIdNumber());
			if (staff.getIdNumberIssueBy() != null)
				updateStaff.setIdNumberIssueBy(staff.getIdNumberIssueBy());
			if (staff.getIdNumberIssueDate() != null)
				updateStaff.setIdNumberIssueDate(staff.getIdNumberIssueDate());
			if(staff.getMaritalStatus()!=null){
				updateStaff.setMaritalStatus(staff.getMaritalStatus());
			}

			// if (modifiedUser != null) {
			// updateStaff.setModifiedBy(currentUserName);
			// } else {
			// updateStaff.setModifiedBy("Unknown user modified");
			// }

			if (staff.getAddress() != null && staff.getAddress().size() > 0) {
				HashSet<PersonAddress> temp = new HashSet<PersonAddress>();
				Iterator<PersonAddress> iter = staff.getAddress().iterator();
				while (iter.hasNext()) {
					PersonAddress pa = iter.next();

					// pa.setCreateDate(currentDate);
					// pa.setCreatedBy(currentUserName);

					pa.setPerson(updateStaff);
					temp.add(pa);
				}
				if (updateStaff.getAddress() == null) {
					updateStaff.setAddress(staff.getAddress());
				} else {
					updateStaff.getAddress().clear();
					updateStaff.getAddress().addAll(temp);
				}
			}
			/* Doan code nay dung de xu ly van de nhan Positions */
			if (staff.getPositions() != null && staff.getPositions().size() > 0) {
				HashSet<PositionStaff> positions = new HashSet<PositionStaff>();
				Iterator<PositionStaff> iter = staff.getPositions().iterator();
				while (iter.hasNext()) {
					PositionStaff ps = iter.next();
					PositionStaff newPs = new PositionStaff();

					// ps.setCreateDate(createDate);
					// newPs.setCreateDate(currentDate);
					// newPs.setCreatedBy(currentUserName);7

					newPs.setCurrent(ps.isCurrent());
					newPs.setMainPosition(ps.getMainPosition());
					if(ps.getPosition()!=null && ps.getPosition().getId()!=null) {
						newPs.setPosition(ps.getPosition());	
					}					
					newPs.setDepartment(ps.getDepartment());
					newPs.setFromDate(ps.getFromDate());
					newPs.setToDate(ps.getToDate());
					newPs.setStaff(updateStaff);
					positions.add(newPs);
				}
				if (updateStaff.getPositions() != null) {// Can luu y neu da ton tai position thi phai thuc hien clear
															// no di va add lai chu khong set
					updateStaff.getPositions().clear();
					updateStaff.getPositions().addAll(positions);
				} else {
					updateStaff.setPositions(positions);// Neu chua co thi set vao
				}
			} else if (updateStaff.getPositions() != null) {// Truong hop xoa het chuc vu
				updateStaff.getPositions().clear();
			}

			if (staff.getUser() != null && staff.getUser().getUsername()!=null) {
				//User user = userRepository.findByUsername(staff.getUser().getUsername());
				User user = staff.getUser();
				if (user == null) {
					user = new User();
					// user.setCreateDate(currentDate);
					// user.setCreatedBy(currentUserName);
				}
				user.setUsername(staff.getUser().getUsername());
				if (staff.getUser().getPassword() != null) {
					if (staff.getUser().getPassword().equals(staff.getUser().getConfirmPassword())) {
						// String password =
						// bCryptPasswordEncoder.encode(staff.getUser().getPassword());
						String password = SecurityUtils.getHashPassword(staff.getUser().getPassword());
						user.setPassword(password);
					}
				}
				else {
					user.setPassword("");
				}
				if (staff.getUser().getEmail() != null) {
					user.setEmail(staff.getUser().getEmail());
				}
				else {
					user.setEmail("");
				}
				if (staff.getUser().getRoles() != null) {
					user.setRoles(staff.getUser().getRoles());
				}
				else {
					user.setRoles(null);
				}
				if (staff.getUser().getActive() != null) {
					user.setActive(staff.getUser().getActive());
				}

				updateStaff.setUser(user);
				user.setPerson(updateStaff);
			}
		}
		updateStaff = staffRepository.save(updateStaff);
		return new Staff(updateStaff);
	}
	@Transactional
	public Staff createStaff(Staff staff) {
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// User modifiedUser = null;
		// if (authentication != null) {
		// modifiedUser = (User) authentication.getPrincipal();
		// }

		// if (staff.getCreateDate() == null) {
		// staff.setCreateDate(new Date());
		// if (modifiedUser != null) {
		// staff.setCreatedBy(modifiedUser.getUsername());
		// } else {
		// staff.setCreatedBy("Unknown user created");
		// }
		// }

		staff = staffRepository.save(staff);

		return new Staff(staff);
		// return repository.save(Staff);
	}

	@Override
	@Transactional
	public Staff createStaffAndAccountByCode(Staff staffDto) {
		String currentUserName = "Unknown User";
		Authentication authentication =
		SecurityContextHolder.getContext().getAuthentication();
		LocalDateTime currentDate = new LocalDateTime();
		User modifiedUser = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		User user = new User();
		Staff staff = new Staff(staffDto);

		if (staff.getStaffCode() != null)
			staff.setStaffCode(staff.getStaffCode());
		if (staff.getFirstName() != null)
			staff.setFirstName(staff.getFirstName());
		if (staff.getLastName() != null)
			staff.setLastName(staff.getLastName());
		if (staff.getBirthDate() != null)
			staff.setBirthDate(staff.getBirthDate());
		if (staff.getBirthPlace() != null)
			staff.setBirthPlace(staff.getBirthPlace());
		if (staff.getGender() != null)
			staff.setGender(staff.getGender());
		if (staff.getPhoto() != null)
			staff.setPhoto(staff.getPhoto());
		if (staff.getDisplayName() != null)
			staff.setDisplayName(staff.getDisplayName());
		if (staff.getPhoneNumber() != null)
			staff.setPhoneNumber(staff.getPhoneNumber());

		if (staff.getEthnics() != null)
			staff.setEthnics(staff.getEthnics());
		if (staff.getEmail() != null)
			staff.setEmail(staff.getEmail());
		if (staff.getNationality() != null)
			staff.setNationality(staff.getNationality());
		if (staff.getNativeVillage() != null)
			staff.setNativeVillage(staff.getNativeVillage());
		if (staff.getReligion() != null)
			staff.setReligion(staff.getReligion());
		if (staff.getIdNumber() != null)
			staff.setIdNumber(staff.getIdNumber());
		if (staff.getIdNumberIssueBy() != null)
			staff.setIdNumberIssueBy(staff.getIdNumberIssueBy());
		if (staff.getIdNumberIssueDate() != null)
			staff.setIdNumberIssueDate(staff.getIdNumberIssueDate());
		if(staff.getMaritalStatus()!=null){
			staff.setMaritalStatus(staff.getMaritalStatus());
		}

		// if (modifiedUser != null) {
		// updateStaff.setModifiedBy(currentUserName);
		// } else {
		// updateStaff.setModifiedBy("Unknown user modified");
		// }

		if (staffDto.getUser() != null) {
			user.setUsername(staffDto.getUser().getUsername());
			// String password =
			// bCryptPasswordEncoder.encode(staffDto.getUser().getPassword());
			String password = SecurityUtils.getHashPassword(staffDto.getUser().getPassword());
			user.setPassword(password);

			user.setPassword(password);
			user.setCreateDate(currentDate);
			user.setCreatedBy(currentUserName);
			user.setEmail(staffDto.getUser().getEmail());
			user.setRoles(staffDto.getUser().getRoles());
			user.setPerson(staff);
			staff.setUser(user);
		}
		if (staffDto.getAddress() != null && staffDto.getAddress().size() > 0) {
			HashSet<PersonAddress> temp = new HashSet<PersonAddress>();
			Iterator<PersonAddress> iter = staffDto.getAddress().iterator();
			while (iter.hasNext()) {
				PersonAddress pa = iter.next();

				// pa.setCreateDate(currentDate);
				// pa.setCreatedBy(currentUserName);

				pa.setPerson(staff);
				temp.add(pa);
			}
			if (staff.getAddress() == null) {
				staff.setAddress(staff.getAddress());
			} else {
				staff.getAddress().clear();
				staff.getAddress().addAll(temp);
			}
		}
		/* Doan code nay dung de xu ly van de nhan Positions */
		if (staffDto.getPositions() != null && staffDto.getPositions().size() > 0) {
			HashSet<PositionStaff> positions = new HashSet<PositionStaff>();
			Iterator<PositionStaff> iter = staffDto.getPositions().iterator();
			while (iter.hasNext()) {
				PositionStaff ps = iter.next();
				PositionStaff newPs = new PositionStaff();
				// ps.setCreateDate(createDate);
				// newPs.setCreateDate(currentDate);
				// newPs.setCreatedBy(currentUserName);

				newPs.setCurrent(ps.isCurrent());
				newPs.setMainPosition(ps.getMainPosition());
				newPs.setPosition(ps.getPosition());
				newPs.setDepartment(ps.getDepartment());
				newPs.setFromDate(ps.getFromDate());
				newPs.setToDate(ps.getToDate());
				newPs.setStaff(staff);
				positions.add(newPs);
			}
			if (staff.getPositions() != null) {// Can luu y neu da ton tai position thi phai thuc hien clear no di va
												// add lai chu khong set
				staff.getPositions().clear();
				staff.getPositions().addAll(positions);
			} else {
				staff.setPositions(positions);// Neu chua co thi set vao
			}
		} else if (staff.getPositions() != null) {// Truong hop xoa het chuc vu
			staff.getPositions().clear();
		}

		//Agreement
		if(staffDto.getAgreements()!=null && staffDto.getAgreements().size()>0) {
			staff.setAgreements(new HashSet<StaffLabourAgreement>());
			for (StaffLabourAgreement agr : staffDto.getAgreements()) {
				StaffLabourAgreement newAgr = new StaffLabourAgreement();
				newAgr.setStaff(staff);
				newAgr.setModifiedBy(currentUserName);
				newAgr.setModifyDate(currentDate);
				newAgr.setCreateDate(currentDate);
				newAgr.setCreatedBy(currentUserName);
				newAgr.setAgreementStatus(agr.getAgreementStatus());
//				newAgr.setAttachments(attachments);
				newAgr.setEndDate(agr.getEndDate());
				if(agr.getLabourAgreementType()!=null && agr.getLabourAgreementType().getId()!=null) {
					LabourAgreementType labourAgreementType = labourAgreementTypeService.findById(agr.getLabourAgreementType().getId());
					newAgr.setLabourAgreementType(labourAgreementType);
				}
				newAgr.setSignedDate(agr.getSignedDate());
				newAgr.setStartDate(agr.getStartDate());
				staff.getAgreements().add(newAgr);
			}
		}
		
		staff = staffRepository.save(staff);

		return new Staff(staff);
		// return repository.save(Staff);
	}

	public Page<PositionStaff> findTeacherByDepartment(Long departmentId, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return positionStaffRepository.findTeacherByDepartment(departmentId, pageable);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public StaffDto deleteStaff(Long id) {

		if (!CommonUtils.isPositive(id, true)) {
			throw new RuntimeException("Invalid argument!");
		}

		Staff entity = staffRepository.findById(id);

		if (entity != null) {
			staffRepository.delete(entity);
		} else {
			throw new RuntimeException("Invalid argument!");
		}

		return new StaffDto(entity);
	}
	
	@Override
	@Transactional
	public Boolean deleteMultiple(Staff[] staffs) {
		boolean ret = true;

		if (staffs == null || staffs.length <= 0) {
			return ret;
		}
		ArrayList<Staff> lststaffs = new ArrayList<Staff>();
		ArrayList<Person> lstPerson = new ArrayList<Person>();
		for (Staff st : staffs) {

			Staff entity = staffRepository.findOne(st.getId());
			Person person = personRepository.findOne(st.getId());
			if (entity == null || person ==null) {
				throw new RuntimeException();
			}
			lststaffs.add(entity);
			lstPerson.add(person);
		}
//		personRepository.delete(lstPerson);
		staffRepository.delete(lststaffs);//Sẽ chạy nhanh hơn delete ở trong vòng for

		return ret;
	}

	@Override
	public List<StaffDto> getAll() {

		Iterator<Staff> itr = staffRepository.findAll().iterator();
		List<StaffDto> list = new ArrayList<StaffDto>();

		while (itr.hasNext()) {
			list.add(new StaffDto(itr.next()));
		}

		return list;
	}

	@Override
	public Page<StaffDto> searchStaff(StaffSearchDto dto, int pageSize, int pageIndex) {
		DateTime date = DateTime.now();
//		String sqlCount ="select count(distinct s.id) from Staff s left join s.positions ps where (1=1)";
//		String sql ="select s from Staff s left join fetch s.positions ps where (1=1)";
		
		String sqlCount ="select count(distinct ps.staff.id) from PositionStaff ps where (1=1)";
		String sql ="select distinct ps.staff as s from PositionStaff ps where (1=1)";
		
		Department dep = null;
		if(dto.getDepartment()!=null) {
			dep = departmentRepository.findById(dto.getDepartment().getId());
			if(dep!=null) {
				sql +=" and ps.department.linePath like :linePath1 or ps.department.linePath like :linePath2";
				sqlCount +=" and ps.department.linePath like :linePath1 or ps.department.linePath like :linePath2";
			}
		}
		if(dto.getIsMainPosition()!=null) {
			sql +=" and ps.mainPosition=:mainPosition";
			sqlCount +=" and ps.mainPosition=:mainPosition";
		}

		if(dto.getKeyword()!=null) {
			sql +=" and ps.staff.displayName like :keyword";
			sqlCount +=" and ps.staff.displayName like :keyword";
		}
		
		
		Query q = manager.createQuery(sql);
		Query qCount = manager.createQuery(sqlCount);
		
		if(dto.getDepartment()!=null) {
//			q.setParameter("departmentId", dto.getDepartment().getId());
//			qCount.setParameter("departmentId", dto.getDepartment().getId());
			
			if(dep!=null) {
				String linePath1 =dep.getLinePath();
				String linePath2 =dep.getLinePath()+"/%";
				q.setParameter("linePath1",linePath1);
				qCount.setParameter("linePath1",linePath1);
				q.setParameter("linePath2",linePath2);
				qCount.setParameter("linePath2",linePath2);
			}
		}
		if(dto.getKeyword()!=null) {
			String keyword = "%"+dto.getKeyword()+"%";
			q.setParameter("keyword", keyword);
			qCount.setParameter("keyword", keyword);
		}
		
		if(dto.getIsMainPosition()!=null) {
//			sql +=" and ps.mainPosition=:mainPosition";
//			sqlCount +=" and ps.mainPosition=:mainPosition";
			q.setParameter("mainPosition", dto.getIsMainPosition());
			qCount.setParameter("mainPosition", dto.getIsMainPosition());
		}
		
		int startPosition = (pageIndex-1)* pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<Staff> entities = q.getResultList();
		DateTime endDate = DateTime.now();
		long diffInMillis = endDate.getMillis() - date.getMillis();
		System.out.println(diffInMillis);
		List<StaffDto> content = new ArrayList<StaffDto>();
		for(int i=0;i<entities.size();i++) {
			content.add(new StaffDto(entities.get(i)));
		}
		long count = (long)qCount.getSingleResult();
		Pageable pageable = new PageRequest(pageIndex-1,pageSize);
		Page<StaffDto> result = new PageImpl<StaffDto>(content, pageable,count);
		

		
		return result;
	}

	@Transactional
	@Override
	public StaffDto createStaffFromDto(StaffDto staffDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		Staff staff = null;
		if (staffDto.getId() != null) {
			staff = staffRepository.findOne(staffDto.getId());
		}
		if (staff == null && staffDto.getStaffCode() != null) {
			staff = staffRepository.findByCode(staffDto.getStaffCode());
		}
		if (staff == null) {
			staff = new Staff();
			staff.setCreateDate(currentDate);
			if (modifiedUser != null) {
				staff.setCreatedBy(modifiedUser.getUsername());
			} else {
				staff.setCreatedBy(currentUserName);
			}
		}
		String displayName = null;
		if (staffDto.getLastName() != null) {
			staff.setLastName(staffDto.getLastName());
			displayName = staffDto.getLastName();
		}
		if (staffDto.getFirstName() != null) {
			staff.setFirstName(staffDto.getFirstName());
			displayName = displayName + " " + staffDto.getFirstName();
		}
		if (displayName != null) {
			staff.setDisplayName(displayName);
		}
		if (staffDto.getBirthDate() != null) {
			staff.setBirthDate(staffDto.getBirthDate());
		}
		if (staffDto.getGender() != null) {
			staff.setGender(staffDto.getGender());
		}
		if (staffDto.getBirthPlace() != null)
			staff.setBirthPlace(staffDto.getBirthPlace());
		if (staffDto.getEmail() != null) {
			staff.setEmail(staffDto.getEmail());
		}
		if (staffDto.getStaffCode() != null) {
			staff.setStaffCode(staffDto.getStaffCode());
		}
		if (staffDto.getPhoneNumber() != null) {
			staff.setPhoneNumber(staffDto.getPhoneNumber());
		}
		if(staffDto.getIdNumber()!=null) {
			staff.setIdNumber(staffDto.getIdNumber());
		}
		if(staffDto.getIdNumberIssueBy()!=null) {
			staff.setIdNumberIssueBy(staffDto.getIdNumberIssueBy());
		}
		if(staffDto.getIdNumberIssueDate()!=null) {
			staff.setIdNumberIssueDate(staffDto.getIdNumberIssueDate());
		}
		
		if (staffDto.getNationality() != null) {
			Country country = null;
			if (staffDto.getNationality().getId() != null) {
				country = countryRepository.getOne(staffDto.getNationality().getId());
			}
			if (country == null) {
				if (staffDto.getNationality().getCode() != null) {
					country = countryRepository.findByCode(staffDto.getNationality().getCode());
				}
			}
			staff.setNationality(country);
		}
		if (staffDto.getReligion() != null) {
			Religion religion = null;
			if (staffDto.getReligion().getId() != null) {
				religion = religionRepository.getOne(staffDto.getReligion().getId());
			}
			if (religion == null) {
				if (staffDto.getReligion().getCode() != null) {
					religion = religionRepository.findByCode(staffDto.getReligion().getCode());
				}
			}
			staff.setReligion(religion);
		}
		if (staffDto.getEthnics() != null) {
			Ethnics ethnics = null;
			if (staffDto.getEthnics().getId() != null) {
				ethnics = ethnicsRepository.findOne(staffDto.getEthnics().getId());
			}
			if (ethnics == null) {
				if (staffDto.getEthnics().getCode() != null) {
					ethnics = ethnicsRepository.findByCode(staffDto.getEthnics().getCode());
				}
			}
			staff.setEthnics(ethnics);
		}
		if (staffDto.getUser() != null) {
			User user = staff.getUser();
			if (user == null) {
				user = new User();
				// user.setCreateDate(currentDate);
				user.setUsername(staffDto.getUser().getUsername());
				// user.setCreateDate(new Date());
				// user.setCreatedBy(currentUserName);
			}
			String password = SecurityUtils.getHashPassword(staffDto.getUser().getPassword());
			if (password != null && password.length() > 0) {
				user.setPassword(password);
			}
			// user.setModifiedBy(currentUserName);
			// user.setModifyDate(currentDate);
			if (staffDto.getUser().getEmail() != null)
				user.setEmail(staffDto.getUser().getEmail());
			user.setPerson(staff);
			staff.setUser(user);
		}
		if (staffDto.getPositions() != null && staffDto.getPositions().size() > 0) {
			HashSet<PositionStaff> positions = new HashSet<PositionStaff>();
			Iterator<PositionStaffDto> iter = staffDto.getPositions().iterator();
			while (iter.hasNext()) {
				PositionStaffDto ps = iter.next();
				PositionStaff newPs = null;
				Department department = null;
				PositionTitle position = null;
				if(ps.getDepartment() != null && ps.getDepartment().getCode() != null) {
					List<Department> list = departmentRepository.findByCode(ps.getDepartment().getCode());
					if(list != null && list.size() > 0) {
						department = list.get(0);
					}
				}
				if(ps.getPosition() != null && ps.getPosition().getId() != null) {
					position = positionTitleRepository.findById(ps.getPosition().getId());
				}
				if(staff!=null && staff.getId()!=null && position != null && department != null && position.getId() != null && department.getId() != null) {
					List<PositionStaff> list = positionStaffRepository.findBy( staff.getId(), position.getId(), department.getId());
					if(list != null && list.size() > 0) {
						newPs = list.get(0);
					}
				}
				if(newPs == null) {
					newPs = new PositionStaff();
				}
				newPs.setCreateDate(currentDate);
				newPs.setCreatedBy(currentUserName);
				newPs.setCurrent(ps.isCurrent());
				newPs.setMainPosition(ps.getMainPosition());
				if(position != null) {
					newPs.setPosition(position);	
				}
				if(department != null) {
					newPs.setDepartment(department);
				}
//				newPs.setFromDate(ps.getFromDate());
//				newPs.setToDate(ps.getToDate());
				newPs.setStaff(staff);
				positions.add(newPs);
			}
			//Hợp đồng
			if(staffDto.getAgreements()!=null && staffDto.getAgreements().size()>0) {
				staff.setAgreements(new HashSet<StaffLabourAgreement>());
				for (StaffLabourAgreementDto agreementDto : staffDto.getAgreements()) {
					StaffLabourAgreement agreement = new StaffLabourAgreement();
					agreement.setAgreementStatus(agreementDto.getAgreementStatus());					
					agreement.setCreateDate(currentDate);
					agreement.setCreatedBy(currentUserName);					
					agreement.setModifyDate(currentDate);
					agreement.setModifiedBy(currentUserName);
					
					agreement.setStartDate(agreementDto.getStartDate());
					
					agreement.setSignedDate(agreementDto.getSignedDate());
					agreement.setEndDate(agreementDto.getEndDate());
					if(agreementDto.getLabourAgreementType()!=null && agreementDto.getLabourAgreementType().getId()!=null) {
						LabourAgreementType labourAgreementType = labourAgreementTypeService.findById(agreementDto.getLabourAgreementType().getId());
						agreement.setLabourAgreementType(labourAgreementType);
					}
//					if(agreementDto.get)
//					agreement.setAttachments(attachments);					
					agreement.setStaff(staff);
					staff.getAgreements().add(agreement);
				}
			}
			if (staff.getPositions() != null) {// Can luu y neu da ton tai position thi phai thuc hien clear
														// no di va add lai chu khong set
				staff.getPositions().clear();
				staff.getPositions().addAll(positions);
			} else {
				staff.setPositions(positions);// Neu chua co thi set vao
			}
		} else if (staff.getPositions() != null) {// Truong hop xoa het chuc vu
			staff.getPositions().clear();
		}
		System.out.println(staff.getDisplayName() +", " +staff.getStaffCode());
		staff = staffRepository.save(staff);
		return new StaffDto(staff);
	}
	@Override
	public int saveListStaff(List<StaffDto> dtos) {
		for(int i=0;i<dtos.size();i++) {
			createStaffFromDto(dtos.get(i));
		}
		return dtos.size();
	}
	@Override
	public Page<StaffDto> findPageByCode(StaffSearchDto staffSearchDto, int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		
		String sql = "select new com.globits.hr.dto.StaffDto(s) from Staff s where (1=1)";
		String sqlCountStaff = "select count(s.id) from Staff s where (1=1)";
		
		if (staffSearchDto != null && StringUtils.hasText(staffSearchDto.getKeyword())) {
			sql += " and (s.staffCode like :textsearch or s.displayName like :textsearch)";
			sqlCountStaff += " and (s.staffCode like :textsearch or s.displayName like :textsearch)";
		}
		
		sql +=" order by s.displayName desc ";
		Query sqlStaffQuery = manager.createQuery(sql, StaffDto.class);
		Query qCountStaffQuery = manager.createQuery(sqlCountStaff, Long.class);
		
		if (staffSearchDto != null && StringUtils.hasText(staffSearchDto.getKeyword())) {
			String convertextSearch = "%" + staffSearchDto.getKeyword() + "%";
			sqlStaffQuery.setParameter("textsearch", convertextSearch);
			qCountStaffQuery.setParameter("textsearch", convertextSearch);
		}
		
		sqlStaffQuery.setFirstResult(startPosition);
		sqlStaffQuery.setMaxResults(pageSize);
		long totalStaff = (Long) qCountStaffQuery.getSingleResult();
		List<StaffDto> listStaff = sqlStaffQuery.getResultList();
		PageImpl<StaffDto> page = new PageImpl<StaffDto>(listStaff, pageable, totalStaff);
		return page;
	}

	@Override
	public Boolean validateStaffCode(String staffCode, Long staffId) {
		if(staffCode != null) {
			List<Staff> staffs = staffRepository.findAll();
			for(Staff staff: staffs) {
				if(staff.getStaffCode().equalsIgnoreCase(staffCode)) {
					if(staffId != null && staff.getId() != null && staff.getId().equals(staffId)) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean validateUserName(String userName, Long userId) {
		if(userName != null) {
			List<Staff> staffs = staffRepository.findAll();
			for(Staff staff: staffs) {
				if(staff != null) {
					if(staff.getUser() != null) {
						if(staff.getUser().getUsername() != null && staff.getUser().getUsername().equalsIgnoreCase(userName)) {
							if(userId != null && staff.getUser() != null && staff.getUser().getId() != null && staff.getUser().getId().equals(userId)) {
								return Boolean.TRUE;
							}
							return Boolean.FALSE;
						}
					}
				}
			}
		}
		return Boolean.TRUE;
	}
	
}
