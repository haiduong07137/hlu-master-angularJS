package com.globits.document.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.globits.document.domain.DocumentCategory;
import com.globits.document.dto.DocumentCategoryDto;

public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
	@Query("select new com.globits.document.dto.DocumentCategoryDto(d) from DocumentCategory d")
	Page<DocumentCategoryDto> getAllDocumentCategory(Pageable pageable);
	
	@Query("select new com.globits.document.dto.DocumentCategoryDto(d) from DocumentCategory d where d.id=?1")
	DocumentCategoryDto getDocumentCategoryById(Long id);
	
	@Query("select d from DocumentCategory d where d.id=?1")
	DocumentCategory findById(Long id);
}
