package com.linln.admin.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StaffMapper {
	//MainCount getMonthCount(@Param(value = "id") Long id, @Param(value = "date") String date);

	Long getCount();

}
