package com.globits.cms.dto;

import java.util.List;

public class SearchDto {
	private String title;
	private String description;
	private String summary;
	private List<Long> ids;
	private Long cmsArticleTypeId;
	private Long articleId;
	private Long categoryId;
	private int pageIndex;
	private int pageSize;
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public Long getCmsArticleTypeId() {
		return cmsArticleTypeId;
	}
	public void setCmsArticleTypeId(Long cmsArticleTypeId) {
		this.cmsArticleTypeId = cmsArticleTypeId;
	}
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	
	
}
