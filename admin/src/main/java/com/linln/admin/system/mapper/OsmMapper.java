package com.linln.admin.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface OsmMapper {
    //MainCount getMonthCount(@Param(value = "id") Long id, @Param(value = "date") String date);
    void truncateTable(@Param(value="tableName")String tableName);
}
