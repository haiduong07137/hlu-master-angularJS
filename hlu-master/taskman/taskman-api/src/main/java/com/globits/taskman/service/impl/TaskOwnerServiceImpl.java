package com.globits.taskman.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.poi.util.StringUtil;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.domain.Department;
import com.globits.core.domain.Person;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.PersonDto;
import com.globits.core.repository.DepartmentRepository;
import com.globits.core.repository.PersonRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.CommonUtils;
import com.globits.security.domain.User;
import com.globits.taskman.TaskManConstant;
import com.globits.taskman.domain.Task;
import com.globits.taskman.domain.TaskOwner;
import com.globits.taskman.dto.ProjectDto;
import com.globits.taskman.dto.TaskOwnerDto;
import com.globits.taskman.repository.TaskOwnerRepository;
import com.globits.taskman.service.TaskOwnerService;
import com.mysql.jdbc.StringUtils;

@Service
@Transactional
public class TaskOwnerServiceImpl extends GenericServiceImpl<TaskOwner, Long> implements TaskOwnerService {
	@Autowired
	TaskOwnerRepository repository;
	@Autowired
	PersonRepository personRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	@Override
	public TaskOwner setValue(TaskOwnerDto dto, TaskOwner entity, String userName, LocalDateTime currentDate) {
		if (dto != null) {
			// Xử lý các trường đơn trước
			if (dto.getId() != null) {
				entity.setId(dto.getId());// trường hợp cập nhật dữ liệu
			}
			entity.setDisplayName(dto.getDisplayName());
			entity.setOwnerType(dto.getOwnerType());

			Person person = null;
			if (dto.getPerson() != null) {
				if (dto.getPerson().getId() != null) {
					person = personRepository.findOne(dto.getPerson().getId());
				}
			}
			entity.setPerson(person);

			Department department = null;
			if (dto.getDepartment() != null) {
				if (dto.getDepartment().getId() != null) {
					department = departmentRepository.findOne(dto.getDepartment().getId());
				}
			}
			entity.setDepartment(department);

			if (dto.getParent() != null) {
				TaskOwner parent = repository.findOne(dto.getParent().getId());
				entity.setParent(parent);
			}
		}
		return entity;
	}

	@Override
	public TaskOwnerDto getTaskOwner(Long id) {
		TaskOwner docCategory = repository.findOne(id);
		return new TaskOwnerDto(docCategory);
	}

	@Override
	public TaskOwnerDto saveTaskOwner(TaskOwnerDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		if (dto != null) {
			TaskOwner entity = null;
			if (dto.getId() != null) {// Có khả năng là update
				entity = repository.findOne(dto.getId());// Lấy dữ liệu từ database ra.
			}
			if (entity == null) {// trường hợp thêm mới
				entity = new TaskOwner();
				entity.setCreatedBy(currentUserName);
				entity.setCreateDate(currentDate);
			}
			entity = setValue(dto, entity, currentUserName, currentDate);
			entity = repository.save(entity);
			return new TaskOwnerDto(entity);
		}
		return null;
	}

	@Override
	public Boolean deleteTaskOwner(Long id) {
		TaskOwner doc = repository.findOne(id);
		if (doc != null) {
			repository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public TaskOwnerDto saveTaskOwnerFromPerson(PersonDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		TaskOwner owner = null;
		List<TaskOwner> entities = repository.getListTaskOwnerByPerson(dto.getId());
		if (entities != null && entities.size() > 0) {
			owner = entities.get(0);
		}
		Person person = personRepository.findOne(dto.getId());
		if (owner == null) {
			owner = new TaskOwner();
			owner.setCreateDate(currentDate);
			owner.setCreatedBy(currentUserName);
		}
		owner.setOwnerType(TaskManConstant.TaskOwnerTypeEnum.PersonalType.getValue());
		owner.setDisplayName(person.getDisplayName());
		owner.setPerson(person);
		owner = repository.save(owner);
		return new TaskOwnerDto(owner);
	}

	@Override
	public TaskOwnerDto saveTaskOwnerFromDepartment(DepartmentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		TaskOwner owner = null;
		List<TaskOwner> entities = repository.getListTaskOwnerByDepartment(dto.getId());
		if (entities != null && entities.size() > 0) {
			owner = entities.get(0);
		}

		Department department = departmentRepository.findOne(dto.getId());
		if (owner == null) {
			owner = new TaskOwner();
			owner.setCreateDate(currentDate);
			owner.setCreatedBy(currentUserName);
		}
		owner.setOwnerType(TaskManConstant.TaskOwnerTypeEnum.DepartmentType.getValue());
		owner.setDisplayName(department.getName());
		owner.setDepartment(department);
		owner = repository.save(owner);
		return new TaskOwnerDto(owner);
	}

	@Override
	public List<TaskOwnerDto> getListTaskOwnerDtoByUserAndRole(Long userId, Long roleId) {
		// TODO Auto-generated method stub
		return repository.getListTaskOwnerDtoByUserAndRole(userId, roleId);
	}

	@Override
	public List<TaskOwnerDto> getListTaskOwnerDtoByRole(Long roleId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		return getListTaskOwnerDtoByUserAndRole(modifiedUser.getId(), roleId);
	}

	@Override
	public List<TaskOwnerDto> getListCurrentTaskOwner() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
			List<TaskOwner> list = repository.getListTaskOwnerDtoByUser(modifiedUser.getId());
			ArrayList<TaskOwnerDto> ret = new ArrayList<TaskOwnerDto>();
			for (TaskOwner w : list) {
				ret.add(new TaskOwnerDto(w));
			}
			return ret;
		}
		return null;
	}

	@Override
	public List<TaskOwnerDto> getListTaskOwnerByRoleCode(String roleCode) {
		return repository.getListTaskOwnerByRoleCode(roleCode);
	}

	@Override
	public Page<TaskOwnerDto> searchTaskOwnerByName(int pageSize, int pageIndex, String text) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		if (text != null && !StringUtils.isNullOrEmpty(text)) {
			return repository.searchTaskOwnerByName(text, pageable);
		} else {
			return repository.getListTaskOwner(pageable);
		}
	}

	@Override
	public TaskOwnerDto getOneTaskOwnerFromPerson(Long personId) {
		Person person = personRepository.findOne(personId);
		if (person != null) {
			TaskOwner owner = null;
			List<TaskOwner> entities = repository.getListTaskOwnerByPerson(person.getId());
			if (entities != null && entities.size() > 0) {
				owner = entities.get(0);
			}
			return new TaskOwnerDto(owner);
		}
		return null;
	}

	@Override
	public TaskOwnerDto getOneTaskOwnerFromDepartment(Long departmentId) {
		Department department = departmentRepository.findOne(departmentId);
		if (department != null) {
			TaskOwner owner = null;
			List<TaskOwner> entities = repository.getListTaskOwnerByDepartment(department.getId());
			if (entities != null && entities.size() > 0) {
				owner = entities.get(0);
			}
			return new TaskOwnerDto(owner);
		}
		return null;
	}

	// get All Person for Task Owner
	@Override
	public Page<PersonDto> getListPerson(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		String query = "select new com.globits.core.domain.Person(p) from Person p";
		String queryCount = "select count(*) from Person ";
		Query q = manager.createQuery(query, Person.class);
		Query qCount = manager.createQuery(queryCount);
		q.setMaxResults(pageSize);
		int startPosition = (pageIndex - 1) * pageSize;
		q.setFirstResult(startPosition);
		List<PersonDto> content = q.getResultList();
		Long total = (Long) qCount.getSingleResult();
		Page<PersonDto> result = new PageImpl<PersonDto>(content, pageable, total);

		return result;
	}

	@Override
	public List<TaskOwnerDto> getListTaskOwnerFromPerson(Long personId) {
		// TODO Auto-generated method stub
		return repository.getTaskOwnerFromPerson(personId);
	}

	@Override
	public Page<TaskOwnerDto> getListTaskOwner(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTaskOwner(pageable);
	}

	@Override
	public Page<TaskOwnerDto> searchTaskOwnerByPersonName(int pageSize, int pageIndex, String text) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.searchTaskOwnerByPersonName(text, pageable);
	}

	@Override
	public boolean deleteMultipleTaskOwner(TaskOwnerDto[] dtos) {
		if (CommonUtils.isEmpty(dtos)) {
			return false;
		}
		for (TaskOwnerDto dto : dtos) {
			if (CommonUtils.isPositive(dto.getOwnerType(), true)) {
				continue;
			}
			TaskOwner entity = repository.findOne(dto.getId());
			if (entity != null) {
				repository.delete(entity);
			}
		}
		return true;
	}

	@Override
	public Page<TaskOwnerDto> searchTaskOwnerByNameAndProjectId(long projectId, int pageSize, int pageIndex,
			String text) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.searchTaskOwnerByNameAndProjectId(projectId, text, pageable);
	}

	@Override
	public Page<TaskOwnerDto> searchTaskOwnerByProjectId(long projectId, int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.searchTaskOwnerByProjectId(projectId, pageable);
	}

	@Override
	public Page<TaskOwnerDto> getListTreeGrid(int pageSize, int pageIndex) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return repository.getListTreeGrid(pageable);
	}

	@Override
	public Page<TaskOwnerDto> getTaskOwnerByTextSearchExcludingTaskOwner(String text, Long taskOwnerId, int pageIndex,
			int pageSize) {
		if(pageIndex > 1) {
			pageIndex--;
		}else {
			pageIndex = 0;
		}
		
		String sql = "select new com.globits.taskman.dto.TaskOwnerDto(tod) from TaskOwner tod where tod.parent is null and tod.id <> :taskOwnerId ";
		String sqlCount = "select count(tod) from TaskOwner tod where tod.parent is null and tod.id <> :taskOwnerId ";
		if(text != null) {
			sql += "and tod.displayName like :search ";
			sqlCount += "and tod.displayName like :search ";
		}
		sql += "order by tod.displayName desc";
		Query query = manager.createQuery(sql, TaskOwnerDto.class);
		Query queryCount = manager.createQuery(sqlCount);
		query.setParameter("taskOwnerId", taskOwnerId);
		queryCount.setParameter("taskOwnerId", taskOwnerId);
		if(text != null) {
			String search = "%"+ text +"%";
			query.setParameter("search", search);
			queryCount.setParameter("search", search);
		}
		
		int startPosition = pageIndex * pageSize;
		query.setFirstResult(startPosition);
		query.setMaxResults(pageSize);
		List<TaskOwnerDto> content = query.getResultList();
		Long total = (Long) queryCount.getSingleResult();
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<TaskOwnerDto> results = new PageImpl<TaskOwnerDto>(content, pageable, total);
		
		return results;
	}

}
