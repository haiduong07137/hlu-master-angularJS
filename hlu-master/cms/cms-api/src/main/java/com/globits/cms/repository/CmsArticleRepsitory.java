package com.globits.cms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.cms.domain.CmsArticle;
import com.globits.cms.dto.CmsArticleDto;

@Repository
public interface CmsArticleRepsitory extends JpaRepository<CmsArticle, Long>{

	@Query("select new com.globits.cms.dto.CmsArticleDto(a, false) from CmsArticle a order by a.publishDate desc")
	Page<CmsArticleDto> getPageCmsArticleDto(Pageable pageable);

	@Query("select new com.globits.cms.dto.CmsArticleDto(a) from CmsArticle a where a.id = ?1")
	CmsArticleDto getCmsArticleDtoById(Long id);

}
