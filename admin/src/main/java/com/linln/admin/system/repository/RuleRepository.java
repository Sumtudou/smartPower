package com.linln.admin.system.repository;

import com.linln.admin.system.domain.Rule;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sumtudou
 * @date 2019/11/13
 */
public interface RuleRepository extends BaseRepository<Rule, Long> {

    @Query(value = "select * from osm_rule where no = ?1 and type = ?2",nativeQuery = true)
    ArrayList<Rule> getRules(Integer no ,String type);

}