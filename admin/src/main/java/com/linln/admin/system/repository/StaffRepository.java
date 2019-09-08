package com.linln.admin.system.repository;

import com.linln.admin.system.domain.Staff;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author sumtdou
 * @date 2019/09/07
 */
public interface StaffRepository extends BaseRepository<Staff, Long> {
    @Query(value = "select count(*) from tb_staff where status = 1 ;",nativeQuery = true)
    Long findSum();

}