package com.globits.letter.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.globits.letter.LetterConstant;
import com.globits.letter.dto.SearchTaskOwnerDto;
import com.globits.letter.service.TaskOwnerLetterService;
import com.globits.security.domain.User;
import com.globits.taskman.dto.TaskOwnerDto;

@Service
public class TaskOwnerLetterServiceImpl implements TaskOwnerLetterService {
	@Autowired
	EntityManager manager;

	@Override
	public List<TaskOwnerDto> getListTaskOwnerByRoleCode(String roleCode, SearchTaskOwnerDto search) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = null;
		User modifiedUser = null;
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			userId = modifiedUser.getId();
			ArrayList<Long> userInDepartment = new ArrayList<Long>();
			String sql = "";
			if (roleCode.equalsIgnoreCase(LetterConstant.ProcessRole)) {
				String sqlUserInDepartment = "Select u.id "
						+ " from tbl_user u "
						+ " inner join tbl_person p on u.id = p.user_id "
						+ " inner join tbl_staff s on p.id=s.id "
						+ " inner join  tbl_position_staff ps ON ps.staff_id = s.id "
						+ " left outer join tbl_department d on ps.department_id=d.id "
						+ " WHERE d.id IN ( "
						+ " Select d.id "
						+ " from tbl_user u inner join tbl_person p on u.id = p.user_id "
						+ " inner join tbl_staff s on p.id=s.id "
						+ " inner join  tbl_position_staff ps ON ps.staff_id = s.id "
						+ " left outer join tbl_department d on ps.department_id=d.id "
						+ "  WHERE u.id =:userId  and ps.current_position = 1) ";
				Query qUserInDepartment = manager.createNativeQuery(sqlUserInDepartment);
				qUserInDepartment.setParameter("userId", userId);
		        List<BigInteger> userInDepartmentResult = qUserInDepartment.getResultList();
		        for (BigInteger userIdSql : userInDepartmentResult) {
		        	userInDepartment.add(userIdSql.longValue());
		        }
		        sql += "select new com.globits.taskman.dto.TaskOwnerDto(w.taskOwner) from UserTaskOwner w where w.role.code=(:code) and w.user.id IN :userInDepartment";
			} else {
				sql += "select new com.globits.taskman.dto.TaskOwnerDto(w.taskOwner) from UserTaskOwner w where w.role.code=(:code)";
			}
			if(search != null && StringUtils.hasText(search.getName())) {
				sql+= " and lower(w.taskOwner.displayName) like :displayName ";
			}

			Query q = manager.createQuery(sql);
			q.setParameter("code", roleCode);
			if (roleCode.equalsIgnoreCase(LetterConstant.ProcessRole)) {
				q.setParameter("userInDepartment", userInDepartment);
			}
			if(search != null && StringUtils.hasText(search.getName())) {
				String converSearch = "%" + search.getName().toLowerCase() + "%";
				q.setParameter("displayName", converSearch);
			}
			List<TaskOwnerDto> list = q.getResultList();
			return list;
		}
		return null;
	}

}
