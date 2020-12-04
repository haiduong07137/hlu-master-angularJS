package com.globits.cms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.cms.domain.CmsArticleType;
import com.globits.cms.dto.CmsArticleTypeDto;
import com.globits.core.service.GenericService;

public interface CmsArticleTypeService extends GenericService<CmsArticleType, Long> {

	Page<CmsArticleTypeDto> getListArticleType(int pageIndex, int pageSize);

	CmsArticleTypeDto addArticleType(CmsArticleTypeDto dto);

	CmsArticleType findById(Long id);

	boolean removeList(List<CmsArticleTypeDto> dtos);
	
	CmsArticleTypeDto remove(Long id);

	CmsArticleTypeDto checkDuplicateCode(String code);

}
