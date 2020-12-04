package com.globits.letter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocumentAttachment;
@Repository
public interface LetterDocumentAttachmentRepository extends JpaRepository<LetterDocumentAttachment, Long> {
}
