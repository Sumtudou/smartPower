package com.linln.admin.system.service.impl;

import com.linln.admin.system.domain.Staff;
import com.linln.admin.system.repository.StaffRepository;
import com.linln.admin.system.service.StaffService;
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
 * @date 2019/09/07
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Staff getById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Staff> getPageList(Example<Staff> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return staffRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param staff 实体对象
     */
    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return staffRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}