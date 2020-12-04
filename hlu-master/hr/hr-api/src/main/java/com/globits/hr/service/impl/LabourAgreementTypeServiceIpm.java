package com.globits.hr.service.impl;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.hr.domain.LabourAgreementType;
import com.globits.hr.dto.LabourAgreementTypeDto;
import com.globits.hr.dto.SearchLabourAgreementTypeDto;
import com.globits.hr.repository.LabourAgreementTypeRepository;
import com.globits.hr.service.LabourAgreementTypeService;
import com.globits.security.domain.User;


@Transactional
@Service
public class LabourAgreementTypeServiceIpm extends GenericServiceImpl<LabourAgreementType, Long>  implements LabourAgreementTypeService{
	@Autowired
	LabourAgreementTypeRepository labourAgreementRepository;

	@Override
	public Page<LabourAgreementTypeDto> getPage(int pageSize, int pageIndex) {
		Pageable page = new PageRequest(pageIndex-1, pageSize);
		return labourAgreementRepository.getListPage(page);
	}

	@Override
	public LabourAgreementTypeDto saveLabourAgreementType(LabourAgreementTypeDto dto) {
		 Authentication authentication =
		 SecurityContextHolder.getContext().getAuthentication();
		 User modifiedUser = null;
		 LocalDateTime currentDate = LocalDateTime.now();
		 String currentUserName = "Unknown User";
		 if (authentication != null) {
			 modifiedUser = (User) authentication.getPrincipal();
			 currentUserName = modifiedUser.getUsername();
		 }
		 
		LabourAgreementType labourAgreement = null;
		if(dto!=null) {
			if(dto.getId()!=null) {//Nếu vào đây thì có nghĩa Id khác null, thì có nghĩa là dự kiến sẽ sửa bản ghi có id tương ứng.
				//Tìm bản ghi tương ứng với id từ cơ sở dữ liệu
				labourAgreement = labourAgreementRepository.findOne(dto.getId());
			}
			if(labourAgreement==null) {//Nếu vẫn không tìm thấy thì thêm mới
				labourAgreement = new LabourAgreementType();
				labourAgreement.setCreateDate(currentDate);
				labourAgreement.setCreatedBy(currentUserName);
			}
			
			labourAgreement.setModifiedBy(currentUserName);
			labourAgreement.setModifyDate(currentDate);
			
			//Đến đây thì labourAgreement khác null, thực hiện việc set dữ liệu nhận được từ client thông qua DTO vào entity
			labourAgreement.setCode(dto.getCode());
			labourAgreement.setName(dto.getName());
			labourAgreement = labourAgreementRepository.save(labourAgreement);
			return new LabourAgreementTypeDto(labourAgreement);
		}
		return null;
	}

	@Override
	public Boolean deleteLabourAgreementType(Long id) {
		LabourAgreementType labourAgreement = labourAgreementRepository.findOne(id);
		if(labourAgreement!=null) {
			labourAgreementRepository.delete(id);
			return true;
		}
		return false;
	}

	@Override
	public LabourAgreementTypeDto getLabourAgreementType(Long id) {
		LabourAgreementType labourAgreement = labourAgreementRepository.findOne(id);
		if(labourAgreement!=null) {
			return new LabourAgreementTypeDto(labourAgreement);
		}
		return null;
	}

	@Override
	public int deleteLabourAgreementTypes(List<LabourAgreementTypeDto> dtos) {
		if(dtos==null) {
			return 0;
		}
		int ret = 0;
		for(LabourAgreementTypeDto dto:dtos) {
			if(dto.getId()!=null) {
				labourAgreementRepository.delete(dto.getId());
			}
			ret++;
		}
		return ret;
	}

	@Override
	public Page<LabourAgreementTypeDto> searchLabourAgreementType(SearchLabourAgreementTypeDto dto, int pageIndex, int pageSize) {
		String name = '%'+dto.getName()+'%';
		String code = '%'+dto.getCode()+'%';
		Pageable pageable = new PageRequest(pageIndex-1, pageSize);
		return labourAgreementRepository.searchByPage(name, code, pageable);
	}
	

}

