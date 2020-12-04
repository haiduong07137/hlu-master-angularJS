package com.globits.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.cms.domain.CmsCategoryArticle;

@Repository
public interface CmsCategoryArticleRepository extends JpaRepository<CmsCategoryArticle, Long>{

	@Query("select ca from CmsCategoryArticle ca where ca.category is not null and ca.category.id = ?1 and ca.article is not null and ca.article.id = ?2")
	CmsCategoryArticle findByCategoryIdAndArticleId(Long categoryId, Long articleId);

}
