package com.globits.letter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterInDocument;
import com.globits.letter.dto.LetterInDocumentDto;
@Repository
public interface LetterInDocumentRepository extends JpaRepository<LetterInDocument, Long> {
	@Query("select new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d where d.task.id=?1")
	public List<LetterInDocumentDto> getListDocumentDtoByTask(Long taskId);
	@Query("from LetterInDocument d where d.task.id=?1")
	public List<LetterInDocument> getListDocumentEntityByTask(Long taskId);
	
	@Query("select new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d where d.id=?1")
	public LetterInDocumentDto getLetterIndocumentDtoById(Long letterIndocumentId);
	
	@Query("select d from LetterInDocument d where d.id=?1")
	public LetterInDocument getLetterIndocumentById(Long letterIndocumentId);
	
	@Query("select new com.globits.letter.dto.LetterInDocumentDto(d) from LetterInDocument d where d.task.id=?1")
	public LetterInDocumentDto getletterInDocumentByTaskId(Long taskId);
}
