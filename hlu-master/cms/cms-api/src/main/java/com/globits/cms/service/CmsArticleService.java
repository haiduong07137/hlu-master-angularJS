package com.globits.cms.service;

import org.springframework.data.domain.Page;

import com.globits.cms.domain.CmsArticle;
import com.globits.cms.dto.CmsArticleDto;
import com.globits.cms.dto.SearchDto;
import com.globits.core.service.GenericService;

public interface CmsArticleService extends GenericService<CmsArticle, Long>{

	Page<CmsArticleDto> getPageCmsArticleDto(int pageIndex, int pageSize);

	CmsArticleDto getCmsArticleDtoById(Long id);

	CmsArticleDto saveCmsArticle(CmsArticleDto dto, Long id);

	Boolean deleteCmsArticleById(Long id);

	Page<CmsArticleDto> searchArticleServiceBySearchDto(SearchDto search);
	
	void seeNews(Long id);

	Page<CmsArticleDto> getPageCmsArticleOrderByViewDESC(int pageIndex, int pageSize);

	CmsArticleDto getCmsArticleDtoByIdByPublishAPI(Long id);

	Page<CmsArticleDto> searchArticleServiceBySearchDtoByPublishAPI(SearchDto searchDto);
}
