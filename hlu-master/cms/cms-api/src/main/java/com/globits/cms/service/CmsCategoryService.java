package com.globits.cms.service;

import org.springframework.data.domain.Page;

import com.globits.cms.domain.CmsCategory;
import com.globits.cms.dto.CmsCategoryDto;
import com.globits.cms.dto.SearchDto;
import com.globits.core.service.GenericService;

public interface CmsCategoryService extends GenericService<CmsCategory, Long>{

	Page<CmsCategoryDto> getPageCmsCategoryDto(int pageIndex, int pageSize);

	CmsCategoryDto getCmsCategoryDtoByCmsCategoryId(Long categoryId);

	CmsCategoryDto saveCmsCategory(CmsCategoryDto dto, Long id);

	Boolean deleteCategoryById(Long id);

	Page<CmsCategoryDto> getCmsCategoryBySearch(SearchDto search);

	Page<CmsCategoryDto> getPageCmsCategoryDtoByPublishAPI(int pageIndex, int pageSize);

}
