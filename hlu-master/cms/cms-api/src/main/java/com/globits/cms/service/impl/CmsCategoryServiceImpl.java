package com.globits.cms.service.impl;

import java.util.List;

import javax.persistence.Query;

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

import com.globits.cms.domain.CmsCategory;
import com.globits.cms.dto.CmsCategoryDto;
import com.globits.cms.dto.SearchDto;
import com.globits.cms.repository.CmsCategoryRepository;
import com.globits.cms.service.CmsCategoryService;
import com.globits.cms.utils.Utils;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.security.domain.User;

@Service
public class CmsCategoryServiceImpl extends GenericServiceImpl<CmsCategory, Long> implements CmsCategoryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsCategoryServiceImpl.class);
	@Autowired
	private CmsCategoryRepository cmsCategoryRepository;

	@Override
	public Page<CmsCategoryDto> getPageCmsCategoryDto(int pageIndex, int pageSize) {
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return cmsCategoryRepository.getPageCmsCategoryDto(pageable);
	}

	@Override
	public CmsCategoryDto getCmsCategoryDtoByCmsCategoryId(Long categoryId) {
		return cmsCategoryRepository.getCmsCategoryDtoByCmsCategoryId(categoryId);
	}

	@Override
	public CmsCategoryDto saveCmsCategory(CmsCategoryDto dto, Long id) {
		if (dto != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User modifiedUser = null;
			LocalDateTime currentDate = LocalDateTime.now();
			String currentUserName = "Unknown User";
			if (authentication != null) {
				modifiedUser = (User) authentication.getPrincipal();
				currentUserName = modifiedUser.getUsername();
			}
			CmsCategory cmsCategory = null;
			if (id != null) {
				cmsCategory = cmsCategoryRepository.findOne(id);
			} else {
				if (dto.getId() != null) {
					cmsCategory = cmsCategoryRepository.findOne(dto.getId());
				}
			}
			if (cmsCategory == null) {
				cmsCategory = new CmsCategory();
				cmsCategory.setCreateDate(currentDate);
				cmsCategory.setCreatedBy(currentUserName);
			}
			cmsCategory.setDescription(dto.getDescription());
			cmsCategory.setTitle(dto.getTitle());

			CmsCategory category = cmsCategoryRepository.save(cmsCategory);
			return new CmsCategoryDto(category);
		}
		return null;
	}

	@Override
	public Boolean deleteCategoryById(Long id) {
		if (id != null) {
			try {
				cmsCategoryRepository.delete(id);
				return true;
			} catch (Exception e) {
				LOGGER.debug("Lỗi xóa category theo id=" + id, e);
				return false;
			}
		}
		return null;
	}

	@Override
	public Page<CmsCategoryDto> getCmsCategoryBySearch(SearchDto search) {
		int pageIndex = search.getPageIndex();
		int pageSize = search.getPageSize();
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		String sql = "select new com.globits.cms.dto.CmsCategoryDto(c) from CmsCategory c where 1=1 ";
		String sqlCount = "select count(*) from CmsCategory c where 1=1 ";
		String whereHasTitle = " and c.title=:title";
		String whereHasDescription = " and c.description=:description";

		boolean isTitle = !Utils.isBlank(search.getDescription());
		boolean isDescription = !Utils.isBlank(search.getTitle());

		if (isTitle) {
			sql += whereHasTitle;
			sqlCount += whereHasTitle;
		}

		if (isDescription) {
			sql += whereHasDescription;
			sqlCount += whereHasDescription;
		}

		Query query = manager.createQuery(sql, CmsCategoryDto.class);
		Query queryCount = manager.createQuery(sqlCount);
		query.setMaxResults(pageSize);
		query.setFirstResult(pageIndex * pageSize);
		if (isTitle) {
			query.setParameter("title", search.getTitle());
			queryCount.setParameter("title", search.getTitle());
		}

		if (isDescription) {
			query.setParameter("title", search.getTitle());
			queryCount.setParameter("title", search.getTitle());
		}

		List<CmsCategoryDto> content = query.getResultList();
		Object object = queryCount.getSingleResult();
		Long total = 0L;
		if (object != null) {
			total = (Long) object;
		}
		Page<CmsCategoryDto> page = new PageImpl<CmsCategoryDto>(content, pageable, total);
		return page;
	}

	//hàm này cho client k check AdministrativeUnit by user login
	@Override
	public Page<CmsCategoryDto> getPageCmsCategoryDtoByPublishAPI(int pageIndex, int pageSize) {
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return cmsCategoryRepository.getPageCmsCategoryDto(pageable);
	}

}
