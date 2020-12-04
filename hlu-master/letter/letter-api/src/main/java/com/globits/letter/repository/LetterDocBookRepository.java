package com.globits.letter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocBook;
@Repository
public interface LetterDocBookRepository extends JpaRepository<LetterDocBook, Long> {
	@Query("select e from LetterDocBook e   where  e.code=?1")
	List<LetterDocBook> findListByCode(String code);

	@Query("select e from LetterDocBook e where e.docBookGroup.id=?1")
	List<LetterDocBook> getAllByGroupId(Long groupId);
}
