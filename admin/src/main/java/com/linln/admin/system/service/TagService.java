package com.linln.admin.system.service;

import com.linln.admin.system.domain.Tag;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sumtudou
 * @date 2019/11/13
 */
public interface TagService {

    ArrayList<Tag> getAllTags();

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Tag> getPageList(Example<Tag> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Tag getById(Long id);

    /**
     * 保存数据
     * @param tag 实体对象
     */
    Tag save(Tag tag);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}