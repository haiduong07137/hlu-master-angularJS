import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.globits.core.dto.FileDescriptionDto;
import com.globits.document.config.DatabaseConfig;
import com.globits.document.domain.DocumentCategory;
import com.globits.document.dto.DocumentAttachmentDto;
import com.globits.document.dto.DocumentCategoryDto;
import com.globits.document.dto.DocumentDto;
import com.globits.document.dto.DocumentUserDto;
import com.globits.document.service.DocumentCategoryService;
import com.globits.document.service.DocumentService;
import com.globits.security.dto.UserDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DatabaseConfig.class)
@Transactional(propagation = Propagation.SUPPORTS)
public class DocumentTest {
	@Autowired
	DocumentService documentService;
	
	@Autowired
	DocumentCategoryService documentCategoryService;
//	@Test
	public void testSaveCategory() {
		DocumentCategory cate = new DocumentCategory();
		cate.setCode("0001");
		cate.setName("Cate 1");
		documentCategoryService.save(cate);
	}
	@Test
	public void test() {
		DocumentCategoryDto cate = new DocumentCategoryDto();
		cate.setId(3L);
		DocumentDto dto = new DocumentDto();
		dto.setCategory(cate);
		
		dto.setDescription("Test Description");
		dto.setUserPermission(new ArrayList<DocumentUserDto>());
		DocumentUserDto duo= new DocumentUserDto();
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		duo.setUser(userDto);
		dto.getUserPermission().add(duo);
		
		dto.setAttachments(new ArrayList<DocumentAttachmentDto>());
		DocumentAttachmentDto dad = new DocumentAttachmentDto();
		FileDescriptionDto fileDto = new FileDescriptionDto();
		fileDto.setFilePath("D:\\test3.pdf");
		fileDto.setContentType("application/pdf");
		fileDto.setName("test3.pdf");
		dad.setFile(fileDto);
		dto.getAttachments().add(dad);
		
		documentService.createOrUpdate(dto);
		
	}
}
