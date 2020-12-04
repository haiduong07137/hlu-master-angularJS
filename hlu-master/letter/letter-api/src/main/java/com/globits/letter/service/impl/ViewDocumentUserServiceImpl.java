package com.globits.letter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
import org.springframework.util.StringUtils;

import com.globits.letter.domain.LetterDocument;
import com.globits.letter.domain.ViewDocumentUser;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.dto.ViewDocumentUserDto;
import com.globits.letter.repository.LetterDocumentRepository;
import com.globits.letter.repository.ViewDocumentUserRepository;
import com.globits.letter.service.ViewDocumentUserService;
import com.globits.security.domain.User;
import com.globits.security.dto.UserDto;
import com.globits.security.repository.UserRepository;
@Service
@Transactional
public class ViewDocumentUserServiceImpl implements ViewDocumentUserService{
	@Autowired
	ViewDocumentUserRepository viewDocumentUserRepository;
	@Autowired
	LetterDocumentRepository letterDocumentRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private EntityManager manager;

	@Override
	public ViewDocumentUser delete(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewDocumentUser findById(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ViewDocumentUser> getList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewDocumentUser save(ViewDocumentUser arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewDocumentUserDto addView(Long userId, Long documentId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		
			ViewDocumentUser viewDocumentUser = null;
			if (userId != null && documentId != null) {
				Object obj = viewDocumentUserRepository.findByLetterAndUser(userId, documentId);
				if(obj != null) {
					List<ViewDocumentUser>  listObj = (List<ViewDocumentUser>)obj;
					if(listObj.size() > 0) {
						viewDocumentUser = listObj.get(0);
					}
				}
			}
			if (viewDocumentUser == null) {
				viewDocumentUser = new ViewDocumentUser();
				viewDocumentUser.setCreateDate(currentDate);
				viewDocumentUser.setCreatedBy(currentUserName);
			}else {
				viewDocumentUser.setModifyDate(currentDate);
				viewDocumentUser.setModifiedBy(currentUserName);
			}
			
			if (userId != null) {
				User user = null;
				user = userRepository.findOne(userId);
				viewDocumentUser.setUser(user);
			}
			if (documentId != null) {
				LetterDocument letterDocument = null;
				letterDocument = letterDocumentRepository.findOne(documentId);
				viewDocumentUser.setLetterDocument(letterDocument);
			}

			viewDocumentUserRepository.save(viewDocumentUser);

		return null;
	}

	@Override
	public Page<ViewDocumentUserDto> searchUser(SearchDocumentDto searchDocumentDto, Long documentId, int pageIndex, int pageSize) {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		String sql ="select new com.globits.letter.dto.ViewDocumentUserDto (d) from ViewDocumentUser d where d.letterDocument.id =:documentId";
		String sqlCount ="select count(d.id) from ViewDocumentUser d where d.letterDocument.id =:documentId";
		
		if(searchDocumentDto!=null) {
			if(StringUtils.hasText(searchDocumentDto.getFullname())) {
				sql +=" and (d.user.person.displayName like :fullname)";
				sqlCount +=" and (d.user.person.displayName like :fullname)";
			}
		}
		
		sql+=" order by d.createDate desc";
		Query q = manager.createQuery(sql);
		Query qCount = manager.createQuery(sqlCount);
		
		if(searchDocumentDto!=null) {
			if(searchDocumentDto != null && StringUtils.hasText(searchDocumentDto.getFullname())) {
				String converSearch = "%"+searchDocumentDto.getFullname()+"%";
				q.setParameter("fullname", converSearch);
				qCount.setParameter("fullname", converSearch);
			}
		}
		q.setParameter("documentId", documentId);
		qCount.setParameter("documentId", documentId);
		int startPosition = (pageIndex-1)*pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<ViewDocumentUserDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<ViewDocumentUserDto> page = new PageImpl<ViewDocumentUserDto>(list, pageable, total);
		return page;
	}
}
