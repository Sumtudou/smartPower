package com.linln.admin.system.service.impl;

import com.linln.admin.system.domain.Tag;
import com.linln.admin.system.repository.TagRepository;
import com.linln.admin.system.service.TagService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sumtudou
 * @date 2019/11/13
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Tag getById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Tag> getPageList(Example<Tag> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return tagRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param tag 实体对象
     */
    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return tagRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    public ArrayList<Tag> getAllTags(){
        return tagRepository.getAllTags();
    }

}