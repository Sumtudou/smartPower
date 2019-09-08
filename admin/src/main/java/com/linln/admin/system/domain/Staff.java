package com.linln.admin.system.domain;

import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * @author sumtdou
 * @date 2019/09/07
 */
@Data
@Entity
@Table(name="tb_staff")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class Staff implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 电话
    private String phone;
    private String name;
    // 创建时间
    @CreatedDate
    @Column(name="create_time")
    private Date create_time;
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
}