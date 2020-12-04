package com.globits.letter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.LetterDocSecurityLevel;
@Repository
public interface LetterDocSecurityLevelRepository extends JpaRepository<LetterDocSecurityLevel, Long> {
}
