package com.globits.letter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocBook;
import com.globits.letter.domain.LetterDocBookGroup;

@Repository
public interface LetterDocBookGroupRepository extends JpaRepository<LetterDocBookGroup, Long>  {
	@Query("select e from LetterDocBookGroup e   where  e.code=?1")
	List<LetterDocBookGroup> findListByCode(String code);
}
