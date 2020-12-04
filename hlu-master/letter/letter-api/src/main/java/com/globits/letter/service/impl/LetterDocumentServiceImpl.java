package com.globits.letter.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.globits.letter.dto.LetterDocumentDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.repository.LetterDocumentRepository;
import com.globits.letter.service.LetterDocumentService;
import com.globits.security.domain.User;
@Service
@Transactional
public class LetterDocumentServiceImpl implements LetterDocumentService{
	@Autowired
	private EntityManager manager;
	
	@Autowired
	LetterDocumentRepository letterDocumentRepository;
	@Override
	public Page<LetterDocumentDto> getListDocument(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		Page<LetterDocumentDto> page = letterDocumentRepository.getAllLetterDocument(pageable);
		return page;
	}
	@Override
	public Page<LetterDocumentDto> searchDocument(SearchDocumentDto searchDto, int pageIndex, int pageSize) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		String sql ="select new com.globits.letter.dto.LetterDocumentDto (d) from LetterDocument d where ((d.isLimitedRead =null or d.isLimitedRead = false)";
		sql +=" or (d.id in (select up.letterDocument.id from LetterDocumentUser up where up.user.id=:userId))) and d.docOriginCode is not null";
		
		String sqlCount ="select count(d.id) from LetterDocument d where ((d.isLimitedRead =null or d.isLimitedRead = false or d.docOriginCode is not null)";
		sqlCount +=" or (d.id in (select up.letterDocument.id from LetterDocumentUser up where up.user.id=:userId))) and d.docOriginCode is not null";
		
		if(searchDto!=null) {
			if(searchDto.getDocumentTypeId()!=null) {
				sql +=" and (d.letterDocumentType.id=:documentTypeId)";
				sqlCount +=" and (d.letterDocumentType.id=:documentTypeId)";
			}
			if(searchDto.getLetterDocFieldId()!=null) {
				sql +=" and (d.letterDocField.id=:letterDocFieldId)";
				sqlCount +=" and (d.letterDocField.id=:letterDocFieldId)";
			}
			if(searchDto.getIssueOrgan()!=null) {
				sql +=" and (d.issueOrgan.id=:issueOrganId)";
				sqlCount +=" and (d.issueOrgan.id=:issueOrganId)";
			}
			if(searchDto != null && StringUtils.hasText(searchDto.getOtherIssueOrgan())) {
				sql += " and (d.issueOrgan.name like :otherIssueOrgan)";
				sqlCount += " and (d.issueOrgan.name like :otherIssueOrgan)";
			}	
			if(StringUtils.hasText(searchDto.getOtherIssueOrgan())) {
				sql += " and (d.issueOrgan.name like :otherIssueOrgan)";
				sqlCount += " and (d.issueOrgan.name like :otherIssueOrgan)";
			}
			if(searchDto != null && StringUtils.hasText(searchDto.getText())) {
				sql += " and (d.docOriginCode like :search or d.briefNote like :search)";
				sqlCount += " and (d.docOriginCode like :search or d.briefNote like :search)";
			}	
			if(StringUtils.hasText(searchDto.getText())) {
				sql += " and (d.docOriginCode like :search or d.briefNote like :search)";
				sqlCount += " and (d.docOriginCode like :search or d.briefNote like :search)";
				System.out.println("text: " + searchDto.getText());
			}
			if(searchDto.getDateFrom()!=null && searchDto.getDateTo()==null) {
				sql += " and d.registeredDate >=:dateFrom";
				sqlCount +=" and d.registeredDate >=:dateFrom";
			}
			if(searchDto.getDateFrom()==null && searchDto.getDateTo()!=null) {
				sql += " and d.registeredDate <=:dateTo";
				sqlCount +=" and d.registeredDate <=:dateTo";
			}
			if(searchDto.getDateFrom()!=null && searchDto.getDateTo()!=null) {
				sql += " and d.registeredDate >=:dateFrom and d.registeredDate <=:dateTo";
				sqlCount +=" and d.registeredDate >=:dateFrom and d.registeredDate <=:dateTo";
			}
		}
		sql += " order by d.registeredDate desc";

		
		Query q = manager.createQuery(sql);
		q.setParameter("userId", modifiedUser.getId());
		
		Query qCount = manager.createQuery(sqlCount);
		qCount.setParameter("userId", modifiedUser.getId());
		
		if(searchDto!=null) {
			if(searchDto.getDocumentTypeId()!=null) {
				q.setParameter("documentTypeId", searchDto.getDocumentTypeId());
				qCount.setParameter("documentTypeId", searchDto.getDocumentTypeId());
			}
			if(searchDto.getLetterDocFieldId()!=null) {
				q.setParameter("letterDocFieldId", searchDto.getLetterDocFieldId());
				qCount.setParameter("letterDocFieldId", searchDto.getLetterDocFieldId());
			}
			if(searchDto.getIssueOrgan()!=null) {
				q.setParameter("issueOrganId", searchDto.getIssueOrgan());
				qCount.setParameter("issueOrganId", searchDto.getIssueOrgan());
			}
			if(searchDto != null && StringUtils.hasText(searchDto.getOtherIssueOrgan())) {
				String converSearch = "%"+searchDto.getOtherIssueOrgan()+"%";
				q.setParameter("otherIssueOrgan", converSearch);
				qCount.setParameter("otherIssueOrgan", converSearch);
			}	
			if(searchDto != null && StringUtils.hasText(searchDto.getText())) {
				String converSearch = "%"+searchDto.getText()+"%";
				q.setParameter("search", converSearch);
				qCount.setParameter("search", converSearch);
			}
			if(searchDto.getDateFrom()!=null && searchDto.getDateTo()==null) {
				q.setParameter("dateFrom", searchDto.getDateFrom());
				qCount.setParameter("dateFrom", searchDto.getDateFrom());
			}
			if(searchDto.getDateFrom()==null && searchDto.getDateTo()!=null) {
				q.setParameter("dateTo", searchDto.getDateTo());
				qCount.setParameter("dateTo", searchDto.getDateTo());
			}
			if(searchDto.getDateFrom()!=null && searchDto.getDateTo()!=null) {
				q.setParameter("dateFrom", searchDto.getDateFrom());
				qCount.setParameter("dateFrom", searchDto.getDateFrom());
				
				q.setParameter("dateTo", searchDto.getDateTo());
				qCount.setParameter("dateTo", searchDto.getDateTo());
			}
		}
		int startPosition = (pageIndex-1)*pageSize;
		q.setFirstResult(startPosition);
		q.setMaxResults(pageSize);
		List<LetterDocumentDto> list = q.getResultList();
		long total = (Long) qCount.getSingleResult();
		PageImpl<LetterDocumentDto> page = new PageImpl<LetterDocumentDto>(list, pageable, total);
		return page;
	}
	@Override
	public LetterDocumentDto getByDocumentById(Long documentId) {
		return letterDocumentRepository.getDocumentById(documentId);
	}
}
