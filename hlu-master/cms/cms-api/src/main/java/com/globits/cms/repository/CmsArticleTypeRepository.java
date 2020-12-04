package com.globits.cms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.globits.cms.domain.CmsArticleType;
import com.globits.cms.dto.CmsArticleTypeDto;

public interface CmsArticleTypeRepository extends JpaRepository<CmsArticleType, Long> {
	@Query("select u from CmsArticleType u where u.id = ?1")
	CmsArticleType findById(Long id);
	@Query("select new com.globits.cms.dto.CmsArticleTypeDto(cs) from CmsArticleType cs  ")
	Page<CmsArticleTypeDto> getListArticleType(Pageable pageable);
	@Query("select u from CmsArticleType u where u.code = ?1")
	List<CmsArticleType> findListByCode(String code);
}
