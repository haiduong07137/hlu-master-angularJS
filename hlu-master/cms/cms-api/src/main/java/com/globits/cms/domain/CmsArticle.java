package com.globits.cms.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;
import com.globits.core.domain.PersonAddress;

@Entity
@Table(name = "tbl_cms_article")
@XmlRootElement
public class CmsArticle extends BaseObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7697226998038807928L;
	@Column(name="content",columnDefinition = "NVARCHAR(MAX)")
	private String content;
	@Column(name="title")
	private String title;//Tiêu đề bài báo
	@Column(name="summary")
	private String summary;//Tóm tắt nội dung
	@Column(name="title_image_url")
	private String titleImageUrl;//Đường dẫn đến File ảnh tiêu đề bài báo (nếu có)

	@Column(name="publish_date")
	private Date publishDate;	//Ngày đăng tin

	@Column(name="view_detail")
	private Integer view;	//lượt xem
	
	@OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CmsCategoryArticle> categories;//Danh sách các chủ đề mà bài báo này thuộc về
	@ManyToOne
	@JoinColumn(name="article_type_id")
	private CmsArticleType articleType;//Loại bài báo (tin nóng, tin thông thường, ...)
	
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
	public Set<CmsCategoryArticle> getCategories() {
		return categories;
	}
	public void setCategories(Set<CmsCategoryArticle> categories) {
		this.categories = categories;
	}
	public CmsArticleType getArticleType() {
		return articleType;
	}
	public void setArticleType(CmsArticleType articleType) {
		this.articleType = articleType;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public Integer getView() {
		return view;
	}
	public void setView(Integer view) {
		this.view = view;
	}
	
}
