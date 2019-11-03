package com.linln.admin.system.controller.osm;

import com.linln.admin.system.domain.Staff;
import com.linln.admin.system.mapper.StaffMapper;
import com.linln.admin.system.repository.StaffRepository;
import com.linln.admin.system.service.StaffService;
import com.linln.admin.system.validator.StaffValid;
import com.linln.common.enums.StatusEnum;
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

import java.util.Date;
import java.util.List;

/**
 * @author sumtdou
 * @date 2019/09/07
 */
@Controller
@RequestMapping("/system/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private StaffRepository staffRepository;
    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:staff:index")
    public String index(Model model, Staff staff) {


       Long gg =  staffMapper.getCount();
       Long  ggg =  staffRepository.findSum();
        System.out.println("gggggg");
        System.out.println("个数为"+gg);
        System.out.println("个数为"+ggg);


        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("create_time", match -> match.contains());

        // 获取数据列表
        Example<Staff> example = Example.of(staff, matcher);
        Page<Staff> list = staffService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/staff/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:staff:add")
    public String toAdd() {
        return "/system/staff/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:staff:edit")
    public String toEdit(@PathVariable("id") Staff staff, Model model) {
        model.addAttribute("staff", staff);
        return "/system/staff/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:staff:add", "system:staff:edit"})
    @ResponseBody
    public ResultVo save(@Validated StaffValid staffValid) {
        System.out.println("在保存1了");
        // 复制保留无需修改的数据
        Staff staff= new Staff();
        if (staffValid.getId() != null) {
            staff= staffService.getById(staffValid.getId());
            //EntityBeanUtil.copyProperties(beStaff, staff);
        }else{
            staff.setCreate_time(new Date());
        }
        staff.setName(staffValid.getName());
        staff.setPhone(staffValid.getPhone());

        // 保存数据
        staffService.save(staff);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:staff:detail")
    public String toDetail(@PathVariable("id") Staff staff, Model model) {
        model.addAttribute("staff",staff);
        return "/system/staff/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("system:staff:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (staffService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}