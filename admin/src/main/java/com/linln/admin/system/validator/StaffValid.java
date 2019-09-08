package com.linln.admin.system.validator;

import com.linln.admin.system.domain.Staff;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author sumtdou
 * @date 2019/09/07
 */
@Data
public class StaffValid extends Staff implements Serializable {

    @NotEmpty(message = "名字不能为空")
    private String name;
}