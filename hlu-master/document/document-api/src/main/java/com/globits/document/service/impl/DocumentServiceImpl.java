package com.globits.document.service.impl;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.springframework.util.StringUtils;

import com.globits.document.utils.Utils;
import com.globits.core.domain.FileDescription;
import com.globits.core.repository.FileDescriptionRepository;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.core.utils.CommonUtils;
import com.globits.document.domain.Document;
import com.globits.document.domain.DocumentAttachment;
import com.globits.document.domain.DocumentCategory;
import com.globits.document.domain.DocumentUser;
import com.globits.document.dto.DocumentAttachmentDto;
import com.globits.document.dto.DocumentDto;
import com.globits.document.dto.DocumentUserDto;
import com.globits.document.dto.SearchDto;
import com.globits.document.repository.DocumentAttachmentRepository;
import com.globits.document.repository.DocumentCategoryRepository;
import com.globits.document.repository.DocumentRepository;
import com.globits.document.repository.DocumentUserRepository;
import com.globits.document.service.DocumentService;
import com.globits.security.domain.User;
import com.globits.security.service.UserService;

@Service
public class DocumentServiceImpl extends GenericServiceImpl<Document, Long> implements DocumentService {
	@Autowired
	DocumentService documentService;
	@Autowired
	DocumentRepository documentRepository;
	@Autowired
	DocumentCategoryRepository documentCategoryRepository;
	@Autowired
	DocumentAttachmentRepository documentAttachmentRepository;
	@Autowired
	FileDescriptionRepository fileDescriptionRepository;
	@Autowired
	DocumentUserRepository documentUserRepository;
	@Autowired
	UserService userService;
	@Autowired
	EntityManager manager;
	
	@Override
	public Page<DocumentDto> getListDocument(int pageIndex, int pageSize) {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		String sql ="select new com.globits.document.dto.DocumentDto (d) from Document d where ((d.isLimitedRead =null or d.isLimitedRead = false)";
		sql +=" or (d.id in (select up.document.id from DocumentUser up where up.user.id=:userId)))";
		
		String sqlCount ="select count(d.id) from Document d where ((d.isLimitedRead =null or d.isLimitedRead = false)";
		sqlCount +=" or (d.id in (select up.document.id from DocumentUser up where up.user.id=:userId)))";
		
		Query q = manager.createQuery(sql);
		q.setParameter("userId", modifiedUser.getId());
		
		Query qCount = manager.createQuery(sqlCount);
		qCount.setParameter("userId", modifiedUser.getId());
		
		int startPosition = (pageIndex-1)*pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<DocumentDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<DocumentDto> page = new PageImpl<DocumentDto>(list, pageable, total);
		return page;
	}
	
	@Override
	public DocumentDto getByDocumentById(Long id) {
		return documentRepository.getDocumentById(id);
	}
	
	@Override
	public Boolean removeDocument(Long id) {
		if (id != null) {
			try {
				documentRepository.delete(id);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		return null;
	}
	
	@Override
	public DocumentDto createOrUpdate(DocumentDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		if (dto != null) {
			Document entity = null;
			if (dto.getId() != null)
				entity = documentRepository.findOne(dto.getId());
			if (entity == null) {
				entity = new Document();
				entity.setCreateDate(currentDate);
				entity.setCreatedBy(currentUserName);
			}
			if(dto.getTitle()!=null) {
				entity.setTitle(dto.getTitle());
			}
			if(dto.getDescription()!=null) {
				entity.setDescription(dto.getDescription());
			}
			if(dto.getDocCode()!=null) {
				entity.setDocCode(dto.getDocCode());
			}
			if(dto.getSummary()!=null) {
				entity.setSummary(dto.getSummary());
			}
			entity.setIsLimitedRead(dto.getIsLimitedRead());
			if (dto.getCategory() != null && dto.getCategory().getId() != null) {
				DocumentCategory documentCategory = null;
				documentCategory = documentCategoryRepository.findById(dto.getCategory().getId());
				entity.setCategory(documentCategory);
			}

			if (dto.getAttachments() != null && dto.getAttachments().size() > 0) {
				if (entity.getAttachments() != null) {
					entity.getAttachments().clear();
				} else {
					entity.setAttachments(new HashSet<DocumentAttachment>());
				}
				for (DocumentAttachmentDto _a : dto.getAttachments()) {

					DocumentAttachment a = null;
					if (CommonUtils.isPositive(_a.getId(), true)) {
						a = documentAttachmentRepository.findOne(_a.getId());
					}

					if (a == null) {
						a = new DocumentAttachment();
						a.setCreateDate(currentDate);
						a.setCreatedBy(currentUserName);
					} else {
						a.setModifyDate(currentDate);
						a.setModifiedBy(currentUserName);
					}
					a.setDocument(entity);
					if (_a.getFile() != null) {
						FileDescription file = new FileDescription();
						file.setId(_a.getFile().getId());
						if (_a.getFile().getId() == null) {
							file.setContentSize(_a.getFile().getContentSize());
							file.setContentType(_a.getFile().getContentType());
							file.setName(_a.getFile().getName());
							file.setFilePath(_a.getFile().getFilePath());
							
							file = fileDescriptionRepository.save(file);
						}
						a.setFile(file);
					}
					entity.getAttachments().add(a);
				}
			}
			if(dto.getUserPermission()!=null) {
				List<DocumentUser> userPermissions = new ArrayList<DocumentUser>();
				for (DocumentUserDto uDto: dto.getUserPermission()) {
					DocumentUser docUser = null;
					if (CommonUtils.isPositive(uDto.getId(), true)) {
						docUser = documentUserRepository.findOne(uDto.getId());
					}
					
					if (docUser == null) {
						docUser = new DocumentUser();
						docUser.setCreateDate(currentDate);
						docUser.setCreatedBy(currentUserName);
					} else {
						docUser.setModifyDate(currentDate);
						docUser.setModifiedBy(currentUserName);
					}
					docUser.setDocument(entity);
					if(uDto.getUser()!=null && uDto.getUser().getId()!=null) {
						User user = userService.findById(uDto.getUser().getId());
						docUser.setUser(user);
					}
					userPermissions.add(docUser);
				}
				if(entity.getUserPermission()==null) {
					entity.setUserPermission(new HashSet<DocumentUser>());
				}
				entity.getUserPermission().clear();
				entity.getUserPermission().addAll(userPermissions);
			}
			entity = documentRepository.save(entity);
			return new DocumentDto(entity);
		}

		return null;
	}

	@Override
	public Page<DocumentDto> searchDocumentBySearchDto(SearchDto search) {
		int pageIndex = search.getPageIndex();
		int pageSize = search.getPageSize();
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		String sql = "select distinct new com.globits.document.dto.DocumentDto(c) from Document c where 1=1 ";
		String sqlCount = "select distinct count(*) from Document c where 1=1 ";
		String whereHasCategoryId = " and c.category is not null and c.category.id = :categoryId";
		String whereHasTitle = " and c.Document.title like :title";
		boolean isCategoryId = search.getCategoryId() != null ? true: false;
		boolean isTitle = !Utils.isBlank(search.getTitle());

		if (isCategoryId) {
			sql += whereHasCategoryId;
			sqlCount += whereHasCategoryId;
		}
		
		if (isTitle) {
			sql += whereHasTitle;
			sqlCount += whereHasTitle;
		}

		Query query = manager.createQuery(sql, DocumentDto.class);
		Query queryCount = manager.createQuery(sqlCount);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageIndex * pageSize);
		
		if (isCategoryId) {
			query.setParameter("categoryId", search.getCategoryId());
			queryCount.setParameter("categoryId", search.getCategoryId());
		}
		
		if (isTitle) {
			query.setParameter("title", "%"+search.getTitle()+"%");
			queryCount.setParameter("title", "%"+search.getTitle()+"%");
		}

		List<DocumentDto> content = query.getResultList();
		Object object = queryCount.getSingleResult();
		Long total = 0L;
		if (object != null) {
			total = (Long) object;
		}
		Page<DocumentDto> page = new PageImpl<DocumentDto>(content, pageable, total);
		return page;
	}
}
