package com.globits.letter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocumentUser;
@Repository
public interface LetterDocumentUserRepository extends JpaRepository<LetterDocumentUser, Long> {

}
