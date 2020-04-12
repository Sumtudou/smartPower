package com.linln.admin.system.service.impl;

import com.linln.admin.system.domain.Relation;
import com.linln.admin.system.repository.RelationRepository;
import com.linln.admin.system.service.RelationService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sumtdou
 * @date 2019/11/02
 */
@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationRepository relationRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Relation getById(Long id) {
        return relationRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Relation> getPageList(Example<Relation> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return relationRepository.findAll(example, page);
    }

    /**
     * 保存数据
     */
    @Override
    public Relation save(Relation relation) {
        return relationRepository.save(relation);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return relationRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}