package com.linln.admin.system.controller.osm;

import com.linln.admin.system.domain.Way;
import com.linln.admin.system.service.WayService;
import com.linln.admin.system.validator.WayValid;
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
 * @author sumtdou
 * @date 2019/11/02
 */
@Controller
@RequestMapping("/system/way")
public class WayController {

    @Autowired
    private WayService wayService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:way:index")
    public String index(Model model, Way way) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Way> example = Example.of(way, matcher);
        Page<Way> list = wayService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/way/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:way:add")
    public String toAdd() {
        return "/system/way/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:way:edit")
    public String toEdit(@PathVariable("id") Way way, Model model) {
        model.addAttribute("way", way);
        return "/system/way/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:way:add", "system:way:edit"})
    @ResponseBody
    public ResultVo save(@Validated WayValid valid, Way way) {
        // 复制保留无需修改的数据
        if (way.getId() != null) {
            Way beWay = wayService.getById(way.getId());
            EntityBeanUtil.copyProperties(beWay, way);
        }

        // 保存数据
        wayService.save(way);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:way:detail")
    public String toDetail(@PathVariable("id") Way way, Model model) {
        model.addAttribute("way",way);
        return "/system/way/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:way:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (wayService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }

    }
}