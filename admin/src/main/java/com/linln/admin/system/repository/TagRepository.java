package com.linln.admin.system.repository;

import com.linln.admin.system.domain.Tag;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sumtudou
 * @date 2019/11/13
 */
public interface TagRepository extends BaseRepository<Tag, Long> {

    @Query(value = "select * from osm_tag where status = 1  order by tagkey asc,tagvalue asc;",nativeQuery = true)
    ArrayList<Tag> getAllTags();

}