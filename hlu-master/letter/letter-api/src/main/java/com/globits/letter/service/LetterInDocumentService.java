package com.globits.letter.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.letter.domain.LetterInDocument;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.ReportLetterByStepDto;
import com.globits.letter.dto.ResponseDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.letter.dto.UserRolesDto;
import com.globits.taskman.dto.ParticipateDto;
import com.globits.taskman.dto.TaskCommentDto;

public interface LetterInDocumentService extends GenericService<LetterInDocument, Long> {
	public LetterInDocumentDto saveLetterInDocument(LetterInDocumentDto dto);

	Page<LetterInDocumentDto> getAllDepartmentAndPersonalTask(Long staffId, Long flowId, Long stepId,
			Integer currentParticipateState, int pageSize, int pageIndex);

	Page<LetterInDocumentDto> getAllDepartmentAndPersonalTask(Long flowId, Long stepId, Integer currentParticipateState,
			int pageSize, int pageIndex);

	LetterInDocumentDto findDocumentById(Long id);

	public void forwardDocument(Long documentId, Long toStepId, Long toStaffId, Long roleId, TaskCommentDto commentDto);

	LetterInDocumentDto createLetterInDocument(LetterInDocumentDto dto, Long docClerkOwnerId, Long fowarderId);
	LetterInDocumentDto createLetterInDocument(LetterInDocumentDto dto, Long docClerkOwnerId, Long fowarderId, Long assignerId);
	Page<LetterInDocumentDto> getDocumentByStepIndex(int stepIndex, int pageIndex, int pageSize);

	Page<LetterInDocumentDto> getAllDepartmentByOwner(Long staffId, Long flowId, Long stepId,
			Integer currentParticipateState, int pageSize, int pageIndex);

	Page<LetterInDocumentDto> getAllDepartmentByOwner(Integer stepIndex, Integer currentParticipateState, int pageSize,
			int pageIndex);

	void assignTask(Long documentId, List<ParticipateDto> assignees);

	public LetterInDocumentDto getLetterInDocumentById(Long id);
	public LetterInDocumentDto assignProcess(LetterInDocumentDto dto, Long docClerkOwnerId, Long fowarderId, Long assignerId);

	LetterInDocumentDto forwardDocument(Long documentId, Long fowarderId);
	LetterInDocumentDto removeDocument(Long documentId);

	void fowardLetterInDocument(Long documentId, Long assignerId, boolean isMainProcess);

	public void processingLetterIn(Long documentId);

	Long getMaxDocumentId();

	public LetterInDocumentDto generateDtoLetterIn();

	public Page<LetterInDocumentDto> getLetterByString(int stepIndex, int currentParticipateState, SearchDocumentDto searchDocumentDto, int pageIndex, int pageSize);

	public List<UserRolesDto> getUserRoles();

	public LetterInDocumentDto getLetterInDocumentByTaskId(Long id);

	ResponseDto backToPreviousStep(Long documentId, TaskCommentDto commentDto);

	int saveListLetterInDocument(List<LetterInDocumentDto> dtos);

	public List<ReportLetterByStepDto> getReportLetterByStep();

	public void processingLetter(Long documentId, List<ParticipateDto> assignees);

	public void addStaff(Long documentId, List<ParticipateDto> listStaff);
	//hàm xuất excel công văn đến
	public List<LetterInDocumentDto> searchLetterInDto(SearchDocumentDto searchDto);
}
