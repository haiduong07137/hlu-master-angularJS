package com.globits.letter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterOutDocument;
import com.globits.letter.dto.LetterOutDocumentDto;

@Repository
public interface LetterOutDocumentRepository extends JpaRepository<LetterOutDocument, Long>{

	@Query("select new com.globits.letter.dto.LetterOutDocumentDto(lod) from LetterOutDocument lod inner join "
			+ "Participate p on p.task.id=lod.task.id "
			+ "where p.id in (select p1.id from Participate p1 "
			+ "inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id "
			+ "where (sdtr.user.id=?1)) ")
	Page<LetterOutDocumentDto> getAllLetterOutDocument(Long userId, Pageable pageable);

//	@Query("select new com.globits.letter.dto.LetterOutDocumentDto(lod) from LetterOutDocument lod inner join "
//			+ "Participate p on p.task.id=lod.task.id "
//			+ "where p.id in (select p1.id from Participate p1 "
//			+ "inner join UserTaskOwner sdtr on p1.role.id=sdtr.role.id and p1.taskOwner.id=sdtr.taskOwner.id "
//			+ "where (sdtr.user.id=?1)) and p.currentState=0 and lod.id = ?2")
//	LetterOutDocumentDto getLetterOutById(Long userId, Long id);

	@Query("select new com.globits.letter.dto.LetterOutDocumentDto(d) from LetterOutDocument d where d.id=?1")
	LetterOutDocumentDto getLetterOutDocumentById(Long letterOutdocumentId);
	
	@Query("select new com.globits.letter.dto.LetterOutDocumentDto(d) from LetterOutDocument d where d.task.id=?1")
	LetterOutDocumentDto getletterOutDocumentByTaskId(Long taskId);
	
	@Query("select new com.globits.letter.dto.LetterOutDocumentDto(lod) from LetterOutDocument lod order by lod.deliveredDate DESC")
	Page<LetterOutDocumentDto> getAllLetter(Pageable pageable);

}
