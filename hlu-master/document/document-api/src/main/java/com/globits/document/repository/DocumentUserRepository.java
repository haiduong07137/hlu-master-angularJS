package com.globits.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.document.domain.DocumentUser;

@Repository
public interface DocumentUserRepository extends JpaRepository<DocumentUser, Long>{

}
