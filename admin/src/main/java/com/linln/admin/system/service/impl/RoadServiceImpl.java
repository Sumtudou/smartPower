package com.linln.admin.system.service.impl;


import com.linln.admin.system.domain.Road;
import com.linln.admin.system.repository.RoadRepository;
import com.linln.admin.system.service.RoadService;
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
 * @date 2019/11/04
 */
@Service
public class RoadServiceImpl implements RoadService {

    @Autowired
    private RoadRepository roadRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Road getById(Long id) {
        return roadRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Road> getPageList(Example<Road> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return roadRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param road 实体对象
     */
    @Override
    public Road save(Road road) {
        return roadRepository.save(road);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return roadRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}