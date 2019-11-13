package com.linln.admin.system.controller.osm;

import com.linln.admin.system.domain.Tag;
import com.linln.admin.system.service.TagService;
import com.linln.admin.system.validator.TagValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sumtudou
 * @date 2019/11/13
 */
@Controller
@RequestMapping("/system/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:tag:index")
    public String index(Model model, Tag tag) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Tag> example = Example.of(tag, matcher);
        Page<Tag> list = tagService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/tag/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:tag:add")
    public String toAdd() {
        return "/system/tag/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:tag:edit")
    public String toEdit(@PathVariable("id") Tag tag, Model model) {
        model.addAttribute("tag", tag);
        return "/system/tag/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:tag:add", "system:tag:edit"})
    @ResponseBody
    public ResultVo save(@Validated TagValid valid, Tag tag) {
        // 复制保留无需修改的数据
        if (tag.getId() != null) {
            Tag beTag = tagService.getById(tag.getId());
            EntityBeanUtil.copyProperties(beTag, tag);
        }

        // 保存数据
        tagService.save(tag);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:tag:detail")
    public String toDetail(@PathVariable("id") Tag tag, Model model) {
        model.addAttribute("tag",tag);
        return "/system/tag/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:tag:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (tagService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}