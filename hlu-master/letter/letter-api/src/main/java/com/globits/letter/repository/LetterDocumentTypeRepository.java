package com.globits.letter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocumentType;
@Repository
public interface LetterDocumentTypeRepository extends JpaRepository<LetterDocumentType, Long> {
}
