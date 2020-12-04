package com.globits.cms.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.globits.cms.domain.CmsArticle;
import com.globits.cms.domain.CmsArticleType;
import com.globits.cms.domain.CmsCategory;
import com.globits.cms.domain.CmsCategoryArticle;
import com.globits.cms.dto.CmsArticleDto;
import com.globits.cms.dto.CmsCategoryArticleDto;
import com.globits.cms.dto.SearchDto;
import com.globits.cms.repository.CmsArticleRepsitory;
import com.globits.cms.repository.CmsArticleTypeRepository;
import com.globits.cms.repository.CmsCategoryArticleRepository;
import com.globits.cms.service.CmsArticleService;
import com.globits.cms.service.CmsCategoryService;
import com.globits.cms.utils.Utils;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.security.domain.User;

@Service
public class CmsArticleServiceImpl extends GenericServiceImpl<CmsArticle, Long> implements CmsArticleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsArticleServiceImpl.class);
	@Autowired
	private CmsArticleRepsitory cmsArticleRepsitory;
	@Autowired
	private CmsArticleTypeRepository cmsArticleTypeRepository;
	
	@Autowired
	private CmsCategoryService cmsCategoryService;

	@Autowired
	private CmsCategoryArticleRepository cmsCategoryArticleRepository;

	@Override
	public Page<CmsArticleDto> getPageCmsArticleDto(int pageIndex, int pageSize) {
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return cmsArticleRepsitory.getPageCmsArticleDto(pageable);
	}

	@Override
	public CmsArticleDto getCmsArticleDtoById(Long id) {
		return cmsArticleRepsitory.getCmsArticleDtoById(id);
	}

	@Override
	public CmsArticleDto saveCmsArticle(CmsArticleDto dto, Long id) {
		if (dto != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User modifiedUser = null;
			LocalDateTime currentDate = LocalDateTime.now();
			String currentUserName = "Unknown User";
			if (authentication != null) {
				modifiedUser = (User) authentication.getPrincipal();
				currentUserName = modifiedUser.getUsername();
			}

			CmsArticle cmsArticle = null;
			if (id != null) {
				cmsArticle = cmsArticleRepsitory.findOne(id);
			} else {
				if (dto.getId() != null) {
					cmsArticle = cmsArticleRepsitory.findOne(dto.getId());
				}
			}
			if (cmsArticle == null) {
				cmsArticle = new CmsArticle();
				cmsArticle.setCreateDate(currentDate);
				cmsArticle.setCreatedBy(currentUserName);
				
				cmsArticle.setModifiedBy(currentUserName);
				cmsArticle.setModifyDate(currentDate);
			}else {
				cmsArticle.setModifiedBy(currentUserName);
				cmsArticle.setModifyDate(currentDate);
			}
			if(dto.getPublishDate() != null) {
				cmsArticle.setPublishDate(dto.getPublishDate());
			}
			if (dto.getView() != null && dto.getView() > 0) {
				cmsArticle.setView(dto.getView());
			}
			else {
				dto.setView(0);
			}
			if (dto.getArticleType() != null && dto.getArticleType().getId() != null) {
				CmsArticleType articleType = null;
				articleType = cmsArticleTypeRepository.findById(dto.getArticleType().getId());
				cmsArticle.setArticleType(articleType);
			}
			cmsArticle.setContent(dto.getContent());
			cmsArticle.setSummary(dto.getSummary());
			cmsArticle.setTitle(dto.getTitle());
			cmsArticle.setTitleImageUrl(dto.getTitleImageUrl());
			if (dto.getCategories() != null) {
				Set<CmsCategoryArticle> categoryArticles = new HashSet<CmsCategoryArticle>();
				for (CmsCategoryArticleDto cmsCategoryArticleDto : dto.getCategories()) {
					if (cmsCategoryArticleDto != null) {
						CmsCategoryArticle categoryArticle = null;
						if (cmsCategoryArticleDto.getCategory() != null && cmsCategoryArticleDto.getCategory().getId() != null) {
							Long articleId = cmsCategoryArticleDto.getArticle() != null && cmsCategoryArticleDto.getArticle().getId() != null ? cmsCategoryArticleDto.getArticle().getId(): null;
							categoryArticle = cmsCategoryArticleRepository.findByCategoryIdAndArticleId(cmsCategoryArticleDto.getCategory().getId(), articleId);
						}
						if(categoryArticle == null) {
							categoryArticle = new CmsCategoryArticle();
							categoryArticle.setCreateDate(currentDate);
							categoryArticle.setCreatedBy(currentUserName);
						}
						categoryArticle.setArticle(cmsArticle);
						CmsCategory category = null;
						category = cmsCategoryService.findById(cmsCategoryArticleDto.getCategory().getId());
						if(category != null) {
							categoryArticle.setCategory(category);
							categoryArticles.add(categoryArticle);
						}
					}
				}
				if (cmsArticle.getCategories() == null) {
					cmsArticle.setCategories(new HashSet<CmsCategoryArticle>());
				}
				cmsArticle.getCategories().clear();
				cmsArticle.getCategories().addAll(categoryArticles);

			}
			try {
				cmsArticleRepsitory.save(cmsArticle);
			} catch (Exception e) {
				LOGGER.error("Failure Save cmsArticle!", e);
			}
		}
		return null;
	}

	@Override
	public Boolean deleteCmsArticleById(Long id) {
		if (id != null) {
			try {
				cmsArticleRepsitory.delete(id);
				return true;
			} catch (Exception e) {
				LOGGER.error("Delete article have id=" + id + " is failured", e);
				return false;
			}
		}
		return null;
	}

	@Override
	public Page<CmsArticleDto> searchArticleServiceBySearchDto(SearchDto search) {
		int pageIndex = search.getPageIndex();
		int pageSize = search.getPageSize();
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		String sql = "select distinct new com.globits.cms.dto.CmsArticleDto(c.article) from CmsCategoryArticle c where 1=1 ";
		String sqlCount = "select distinct count(*) from CmsCategoryArticle c where 1=1 ";
		String whereHasCategoryId = " and c.category is not null and c.category.id = :categoryId";
		String whereHasTitle = " and c.article.title like :title";
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
		
		sql+=" order by c.article.id desc";

		Query query = manager.createQuery(sql, CmsArticleDto.class);
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

		List<CmsArticleDto> content = query.getResultList();
		Object object = queryCount.getSingleResult();
		Long total = 0L;
		if (object != null) {
			total = (Long) object;
		}
//		if (content.size() > 0) {
//		  Collections.sort(content, new Comparator<CmsArticleDto>() {
//		      @Override
//		      public int compare(final CmsArticleDto object1, final CmsArticleDto object2) {
//		          return object2.getPublishDate().compareTo(object1.getPublishDate());
//		      }
//		  });
//		}
		Page<CmsArticleDto> page = new PageImpl<CmsArticleDto>(content, pageable, total);
		return page;
	}

	@Override
	public void seeNews(Long id) {
		if (id != null) {
			try {
				CmsArticle cmsArticle = cmsArticleRepsitory.findOne(id);
				if (cmsArticle != null) {
					if (cmsArticle.getView() == null || cmsArticle.getView() <= 0) {
						cmsArticle.setView(0);
					}
					cmsArticle.setView(cmsArticle.getView() + 1);
					cmsArticleRepsitory.save(cmsArticle);
				}
			} catch (Exception e) {
				LOGGER.error("Error up view in news=" + id + " is failured", e);
			}
		}
	}

	@Override
	public Page<CmsArticleDto> getPageCmsArticleOrderByViewDESC(int pageIndex, int pageSize) {
		String sqlCount = "select count(news) from CmsArticle as news where (1=1)";
		String sql = "select new com.globits.cms.dto.CmsArticleDto(news, false) from CmsArticle as news where (1=1) ORDER BY news.view DESC ";

		Query q = manager.createQuery(sql, CmsArticleDto.class);
		Query qCount = manager.createQuery(sqlCount);

		int startPosition = (pageIndex - 1) * pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<CmsArticleDto> entities = q.getResultList();
		long count = (long) qCount.getSingleResult();
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		Page<CmsArticleDto> result = new PageImpl<CmsArticleDto>(entities, pageable, count);

		return result;
	}

	//hàm này cho client k check AdministrativeUnit by user login
	@Override
	public CmsArticleDto getCmsArticleDtoByIdByPublishAPI(Long id) {
		return cmsArticleRepsitory.getCmsArticleDtoById(id);
	}

	//hàm này cho client k check AdministrativeUnit by user login
	@Override
	public Page<CmsArticleDto> searchArticleServiceBySearchDtoByPublishAPI(SearchDto search) {
		int pageIndex = search.getPageIndex();
		int pageSize = search.getPageSize();
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		String sql = "select distinct new com.globits.cms.dto.CmsArticleDto(c.article) from CmsCategoryArticle c where 1=1 ";
		String sqlCount = "select distinct count(*) from CmsCategoryArticle c where 1=1 ";
		String whereHasCategoryId = " and c.category is not null and c.category.id = :categoryId";
		String whereHasTitle = " and c.article.title like :title";
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
		
		sql+=" order by c.article.id desc";

		Query query = manager.createQuery(sql, CmsArticleDto.class);
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

		List<CmsArticleDto> content = query.getResultList();
		Object object = queryCount.getSingleResult();
		Long total = 0L;
		if (object != null) {
			total = (Long) object;
		}
		Page<CmsArticleDto> page = new PageImpl<CmsArticleDto>(content, pageable, total);
		return page;
	}

}
