package com.linln.admin.system.controller.osm;


import com.linln.admin.system.domain.Road;
import com.linln.admin.system.service.RoadService;
import com.linln.admin.system.validator.RoadValid;
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
 * @date 2019/11/04
 */
@Controller
@RequestMapping("/system/road")
public class RoadController {

    @Autowired
    private RoadService roadService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:road:index")
    public String index(Model model, Road road) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取数据列表
        Example<Road> example = Example.of(road, matcher);
        Page<Road> list = roadService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/road/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:road:add")
    public String toAdd() {
        return "/system/road/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:road:edit")
    public String toEdit(@PathVariable("id") Road road, Model model) {
        model.addAttribute("road", road);
        return "/system/road/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:road:add", "system:road:edit"})
    @ResponseBody
    public ResultVo save(@Validated RoadValid valid, Road road) {
        // 复制保留无需修改的数据
        if (road.getId() != null) {
            Road beRoad = roadService.getById(road.getId());
            EntityBeanUtil.copyProperties(beRoad, road);
        }

        // 保存数据
        roadService.save(road);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:road:detail")
    public String toDetail(@PathVariable("id") Road road, Model model) {
        model.addAttribute("road",road);
        return "/system/road/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:road:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (roadService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}