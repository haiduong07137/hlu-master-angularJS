package com.globits.letter.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.globits.core.service.GenericService;
import com.globits.letter.domain.LetterOutDocument;
import com.globits.letter.dto.LetterInDocumentDto;
import com.globits.letter.dto.LetterOutDocumentDto;
import com.globits.letter.dto.ReportLetterByStepDto;
import com.globits.letter.dto.ResponseDto;
import com.globits.letter.dto.SearchDocumentDto;
import com.globits.taskman.dto.TaskCommentDto;

public interface LetterOutDocumentService extends GenericService<LetterOutDocument, Long>  {

	LetterOutDocumentDto createLetterOutDocument(LetterOutDocumentDto dto);

	Page<LetterOutDocumentDto> getAllLetterOutDocumentByCurrentUser(int pageIndex, int pageSize);

	LetterOutDocumentDto getLetterOutDocumentById(Long id);

	Long getMaxDocumentId();

	LetterOutDocumentDto generateDtoLetterOut();

	Page<LetterOutDocumentDto> getLetterByString(String search, int pageIndex, int pageSize);

	Page<LetterOutDocumentDto> getAllLetterOutDocumentBy(int stepIndex, Integer currentParticipateState, int pageIndex, int pageSize);

	LetterOutDocumentDto getLetterOutDocumentByTaskId(Long id);
	public Page<LetterOutDocumentDto> getLetterByString(int stepIndex, int currentParticipateState, SearchDocumentDto searchDocumentDto, int pageIndex, int pageSize);

	int saveListLetterOutDocument(List<LetterOutDocumentDto> letterOutDocumentDto);

	LetterOutDocumentDto forwardLetterOutDocument(LetterOutDocumentDto dto, Long docClerkOwnerId, Long draftersId, Long fowarderId);

	void transferLetterOutDocument(Long documentId, Long leaderId);

	void signLetterOutDocument(Long documentId);

	ResponseDto backToPreviousStep(Long documentId, TaskCommentDto commentDto);

	LetterOutDocumentDto transferLetterOutDocument1(LetterOutDocumentDto dto, Long docClerkOwnerId, Long fowarderId,
			Long leaderId);

	List<ReportLetterByStepDto> getReportLetterByStep();

//	Page<LetterOutDocumentDto> getAllLetter(int pageIndex, int pageSize);

	LetterOutDocumentDto saveLetterOut(LetterOutDocumentDto dto);
	//hàm xuất excel công văn đi
	public List<LetterOutDocumentDto> searchLetterOutDto(SearchDocumentDto searchDto);
}
