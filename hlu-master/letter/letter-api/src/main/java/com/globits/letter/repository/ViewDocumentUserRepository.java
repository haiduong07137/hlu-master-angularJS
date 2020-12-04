package com.globits.letter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.letter.domain.ViewDocumentUser;
@Repository
public interface ViewDocumentUserRepository extends JpaRepository<ViewDocumentUser, Long> {
	@Query("select a from ViewDocumentUser a where a.user is not null and a.user.id = ?1 and a.letterDocument is not null and a.letterDocument.id = ?2")
	List<ViewDocumentUser> findByLetterAndUser(Long userId, Long documentId);
}
