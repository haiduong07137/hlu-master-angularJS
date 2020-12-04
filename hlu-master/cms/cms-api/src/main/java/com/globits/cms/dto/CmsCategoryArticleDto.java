package com.globits.cms.dto;

import com.globits.cms.domain.CmsArticle;
import com.globits.cms.domain.CmsCategory;

public class CmsCategoryArticleDto {
	private Long id;
	private CmsCategory category;
	private CmsArticle article;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CmsCategory getCategory() {
		return category;
	}
	public void setCategory(CmsCategory category) {
		this.category = category;
	}
	public CmsArticle getArticle() {
		return article;
	}
	public void setArticle(CmsArticle article) {
		this.article = article;
	}
	public CmsCategoryArticleDto() {
		super();
	}
	
}
