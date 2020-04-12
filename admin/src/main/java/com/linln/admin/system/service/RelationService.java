package com.linln.admin.system.service;

import com.linln.admin.system.domain.Relation;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sumtdou
 * @date 2019/11/02
 */
public interface RelationService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Relation> getPageList(Example<Relation> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Relation getById(Long id);

    /**
     * 保存数据
     */
    Relation save(Relation relation);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}