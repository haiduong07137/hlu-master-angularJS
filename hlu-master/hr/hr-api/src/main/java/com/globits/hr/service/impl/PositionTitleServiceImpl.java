package com.globits.hr.service.impl;

import java.util.ArrayList;
import java.util.List;

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

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.PositionTitle;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.repository.PositionTitleRepository;
import com.globits.hr.service.PositionTitleService;
import com.globits.security.domain.User;

@Transactional
@Service
public class PositionTitleServiceImpl extends GenericServiceImpl<PositionTitle, Long> implements PositionTitleService {
	@Autowired
	private PositionTitleRepository positionTitleRepository;
	
	@Override
	public PositionTitleDto saveTitle(PositionTitleDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User modifiedUser = null;
		LocalDateTime currentDate = LocalDateTime.now();
		String currentUserName = "Unknown User";
		if (authentication != null) {
			modifiedUser = (User) authentication.getPrincipal();
			currentUserName = modifiedUser.getUsername();
		}

		PositionTitle title = null;
		if (dto.getId() != null && dto.getId() > 0) {
			title = positionTitleRepository.findOne(dto.getId());
		}
		if (title == null) {
			title = new PositionTitle();
			title.setCreateDate(currentDate);
			title.setCreatedBy(currentUserName);
		}
		title.setModifiedBy(currentUserName);
		title.setModifyDate(currentDate);
		if (dto.getCode() != null) {
			title.setCode(dto.getCode());
		}
		if (dto.getName() != null) {
			title.setName(dto.getName());
		}
		title.setDescription(dto.getDescription());
		title.setType(dto.getType());
		title = positionTitleRepository.save(title);
		return new PositionTitleDto(title);
	}

	@Override
	public PositionTitleDto getTitle(Long id) {
		PositionTitle entity = positionTitleRepository.findOne(id);

		if (entity == null) {
			return null;
		} else {
			PositionTitleDto dto = new PositionTitleDto(entity);
			return dto;
		}
	}

	@Override
	public Page<PositionTitleDto> getPage(int pageIndex, int pageSize) {
		Pageable pageable = new PageRequest(pageIndex, pageSize);
		Page<PositionTitle> _page = positionTitleRepository.findAll(pageable);

		List<PositionTitle> _content = _page.getContent();
		List<PositionTitleDto> content = new ArrayList<PositionTitleDto>();

		for (PositionTitle entity : _content) {
			PositionTitleDto dto = new PositionTitleDto(entity);
			content.add(dto);
		}

		return new PageImpl<>(content, pageable, _page.getTotalElements());
	}

	@Override
	public Boolean removeTitle(Long id) {
		PositionTitle title = positionTitleRepository.findOne(id);
		if (title != null) {
			positionTitleRepository.delete(title);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Boolean deleteMultiple(PositionTitleDto[] dtos) {
		boolean ret = true;

		if (dtos == null || dtos.length <= 0) {
			return ret;
		}
		ArrayList<PositionTitle> titles = new ArrayList<PositionTitle>();
		for (PositionTitleDto dto : dtos) {

			PositionTitle entity = positionTitleRepository.findOne(dto.getId());

			if (entity == null) {
				throw new RuntimeException();
			}
			titles.add(entity);
			
		}
		
		positionTitleRepository.delete(titles);//Sẽ chạy nhanh hơn delete ở trong vòng for

		return ret;
	}

}
