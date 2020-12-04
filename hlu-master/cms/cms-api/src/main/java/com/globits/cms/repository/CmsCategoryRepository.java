package com.globits.cms.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.cms.domain.CmsCategory;
import com.globits.cms.dto.CmsCategoryDto;

@Repository
public interface CmsCategoryRepository extends JpaRepository<CmsCategory, Long>{

	@Query("select new com.globits.cms.dto.CmsCategoryDto(c) from CmsCategory c order by c.title")
	Page<CmsCategoryDto> getPageCmsCategoryDto(Pageable pageable);

	@Query("select new com.globits.cms.dto.CmsCategoryDto(c) from CmsCategory c where c.id = ?1 order by c.title")
	CmsCategoryDto getCmsCategoryDtoByCmsCategoryId(Long categoryId);

}
