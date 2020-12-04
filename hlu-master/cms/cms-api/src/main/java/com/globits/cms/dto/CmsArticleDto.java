package com.globits.cms.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import com.globits.cms.domain.CmsArticle;
import com.globits.cms.domain.CmsCategory;
import com.globits.cms.domain.CmsCategoryArticle;

public class CmsArticleDto {
	private Long id;
	private String content;
	private String title;//Tiêu đề bài báo
	private String summary;//Tóm tắt nội dung
	private String titleImageUrl;//Đường dẫn đến File ảnh tiêu đề bài báo (nếu có)
	
	private List<CmsCategoryArticleDto> categories;//Danh sách các chủ đề mà bài báo này thuộc về
	private CmsArticleTypeDto articleType;//Loại bài báo (tin nóng, tin thông thường, ...)
	private Date publishDate;	//Ngày đăng tin
	private Integer view;	//lượt xem

	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTitleImageUrl() {
		return titleImageUrl;
	}
	public void setTitleImageUrl(String titleImageUrl) {
		this.titleImageUrl = titleImageUrl;
	}
	
	public List<CmsCategoryArticleDto> getCategories() {
		return categories;
	}
	public void setCategories(List<CmsCategoryArticleDto> categories) {
		this.categories = categories;
	}
	public CmsArticleTypeDto getArticleType() {
		return articleType;
	}
	public void setArticleType(CmsArticleTypeDto articleType) {
		this.articleType = articleType;
	}
	
	public Integer getView() {
		return view;
	}
	public void setView(Integer view) {
		this.view = view;
	}
	public CmsArticleDto() {
		super();
	}
	
	public CmsArticleDto(CmsArticle entity) {
		if(entity != null) {
			this.id = entity.getId();
			this.content = entity.getContent();
			this.summary = entity.getSummary();
			this.title = entity.getTitle();
			this.titleImageUrl = entity.getTitleImageUrl();
			this.articleType = new CmsArticleTypeDto(entity.getArticleType());
			this.categories = new ArrayList<CmsCategoryArticleDto>();
			this.publishDate = entity.getPublishDate();
			if (entity.getView() != null && entity.getView() > 0) {
				this.view = entity.getView();
			}
			else {
				this.view = 0;
			}
			if(entity.getCategories() != null) {
				for(CmsCategoryArticle categoryArticle : entity.getCategories()) {
					if(categoryArticle != null && categoryArticle.getCategory() != null) {
						if(categoryArticle != null) {
							CmsCategoryArticleDto categoryArticleDto = new CmsCategoryArticleDto();
							if(categoryArticle.getCategory() != null) {
								categoryArticleDto.setCategory(categoryArticle.getCategory());
								this.categories.add(categoryArticleDto);
							}
						}
					}
				}
			}
		}
	}

	public CmsArticleDto(CmsArticle entity, boolean simple) {
		if(entity != null) {
			this.id = entity.getId();
			this.summary = entity.getSummary();
			this.title = entity.getTitle();
			this.titleImageUrl = entity.getTitleImageUrl();
			this.publishDate = entity.getPublishDate();
			if (entity.getView() != null && entity.getView() > 0) {
				this.view = entity.getView();
			}
			else {
				this.view = 0;
			}
		}
	}
	
}
