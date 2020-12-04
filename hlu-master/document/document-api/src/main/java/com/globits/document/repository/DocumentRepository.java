package com.globits.document.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.document.domain.Document;
import com.globits.document.dto.DocumentDto;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	@Query("select new com.globits.document.dto.DocumentDto(d) from Document d where (d.isLimitedRead is null " 
			+ "or d.isLimitedRead=false)")
	Page<DocumentDto> getAllDocument(Pageable pageable);
	
	@Query("select new com.globits.document.dto.DocumentDto(d) from Document d where d.id=?1")
	DocumentDto getDocumentById(Long id);
}
