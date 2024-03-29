package com.linln.admin.system.service.impl;

import com.linln.admin.system.domain.Node;
import com.linln.admin.system.repository.NodeRepository;
import com.linln.admin.system.service.NodeService;
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
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeRepository nodeRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Node getById(Long id) {
        return nodeRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Node> getPageList(Example<Node> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return nodeRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param node 实体对象
     */
    @Override
    public Node save(Node node) {
        return nodeRepository.save(node);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return nodeRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}