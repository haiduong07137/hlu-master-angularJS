package com.globits.taskman.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.taskman.domain.UserTaskOwner;
import com.globits.taskman.dto.UserTaskOwnerDto;
@Repository
public interface UserTaskOwnerRepository  extends JpaRepository<UserTaskOwner, Long> {
	@Query("select new com.globits.taskman.dto.UserTaskOwnerDto(d) from UserTaskOwner d")
	public Page<UserTaskOwnerDto> getListUserTaskOwner(Pageable pageable);
	@Query("from UserTaskOwner uto where uto.user.id=?1")
	public List<UserTaskOwner> getListTaskUserByOwnerId(Long userId);
}

