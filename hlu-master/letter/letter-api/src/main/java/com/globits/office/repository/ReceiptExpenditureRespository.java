package com.globits.office.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.globits.office.domain.ReceiptExpenditure;
@Repository
public interface ReceiptExpenditureRespository extends JpaRepository<ReceiptExpenditure, Long> {
	
}
