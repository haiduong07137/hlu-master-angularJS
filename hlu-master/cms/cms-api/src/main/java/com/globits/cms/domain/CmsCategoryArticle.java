package com.globits.cms.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.globits.core.domain.BaseObject;

@Entity
@Table(name = "tbl_cms_category_article")
@XmlRootElement
public class CmsCategoryArticle extends BaseObject{
	private static final long serialVersionUID = 2524470603565127170L;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private CmsCategory category;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "article_id")
	private CmsArticle article;
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
	
}
