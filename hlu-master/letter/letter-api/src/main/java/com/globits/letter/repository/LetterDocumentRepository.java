package com.globits.letter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocument;
import com.globits.letter.dto.LetterDocumentDto;
@Repository
public interface LetterDocumentRepository extends JpaRepository<LetterDocument, Long> {
	@Query("select new com.globits.letter.dto.LetterDocumentDto(d) from LetterDocument d where (d.isLimitedRead is null "
			+ "or d.isLimitedRead=false) order by d.registeredDate desc")
	Page<LetterDocumentDto> getAllLetterDocument(Pageable pageable);
	
	@Query("select new com.globits.letter.dto.LetterDocumentDto(d) from LetterDocument d where d.id=?1")
	LetterDocumentDto getDocumentById(Long documentId);
}
