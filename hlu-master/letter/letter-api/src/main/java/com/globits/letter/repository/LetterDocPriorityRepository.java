package com.globits.letter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocPriority;
@Repository
public interface LetterDocPriorityRepository extends JpaRepository<LetterDocPriority, Long> {
}
