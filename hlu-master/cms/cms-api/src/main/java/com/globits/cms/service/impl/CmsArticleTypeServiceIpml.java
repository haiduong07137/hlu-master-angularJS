package com.globits.cms.service.impl;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.globits.cms.domain.CmsArticleType;
import com.globits.cms.dto.CmsArticleTypeDto;
import com.globits.cms.repository.CmsArticleTypeRepository;
import com.globits.cms.service.CmsArticleTypeService;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.security.domain.User;

@Service
public class CmsArticleTypeServiceIpml extends GenericServiceImpl<CmsArticleType, Long>
		implements CmsArticleTypeService {
	@Autowired
	CmsArticleTypeRepository articleTypeRepository;

	@Override
	public Page<CmsArticleTypeDto> getListArticleType(int pageIndex, int pageSize) {
		if (pageIndex > 0) {
			pageIndex--;
		} else {
			pageIndex = 0;
		}
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		return articleTypeRepository.getListArticleType(pageable);
	}

	@Override
	public CmsArticleTypeDto addArticleType(CmsArticleTypeDto dto) {
		CmsArticleType at = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}
		if (dto.getId() != null) {
			at = articleTypeRepository.findById(dto.getId());
		}

		if (at == null) {
			at = new CmsArticleType();
			at.setCreateDate(currentDate);
			at.setCreatedBy(currentUserName);
		}
		if (dto.getName() != null) {
			at.setName(dto.getName());
		}
		if (dto.getCode() != null) {
			at.setCode(dto.getCode());
		}
		if (dto.getDescription() != null) {
			at.setDescription(dto.getDescription());
		}
		at.setPriority(dto.getPriority());

		at = articleTypeRepository.save(at);

		return new CmsArticleTypeDto(at);
	}

	@Override
	public boolean removeList(List<CmsArticleTypeDto> dtos) {
		if (dtos != null && dtos.size() > 0) {
			for (CmsArticleTypeDto item : dtos) {
				remove(item.getId());
			}
		}
		return false;
	}

	@Override
	public CmsArticleTypeDto remove(Long id) {
		CmsArticleType at = null;
		if (id != null) {
			at = articleTypeRepository.findById(id);
			if (at != null) {
				articleTypeRepository.delete(at);
				return new CmsArticleTypeDto(at);
			}
		}
		return null;
	}

	@Override
	public CmsArticleTypeDto checkDuplicateCode(String code) {
		
		CmsArticleTypeDto viewCheckDuplicateCodeDto = new CmsArticleTypeDto();
		CmsArticleType au = null;
		List<CmsArticleType> list = articleTypeRepository.findListByCode(code);
		if(list != null && list.size() > 0) {
			au = list.get(0);
		}
		if(list == null) {
			viewCheckDuplicateCodeDto.setDuplicate(false);
		}else if(list != null && list.size() > 0) {
			viewCheckDuplicateCodeDto.setDuplicate(true);
			viewCheckDuplicateCodeDto.setDupCode(au.getCode());
			viewCheckDuplicateCodeDto.setDupName(au.getName());
		}
		return viewCheckDuplicateCodeDto;
	}
}
