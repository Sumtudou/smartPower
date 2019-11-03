package com.linln.admin.system.domain;

import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sumtdou
 * @date 2019/11/02
 */
@Data
@Entity
@Table(name="osm_way")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class Way implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // nid
    private String nid;
    // 可视化
    private String visible;
    // 版本
    private Integer version;
    // 改变
    private String changeset;
    // 时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    // 使用者名字
    private String user;
    // 用户id
    private Integer uid;
    // 纬度
    private String lat;
    // 经度
    private String lon;
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
}