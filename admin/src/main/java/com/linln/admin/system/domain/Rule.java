package com.linln.admin.system.domain;

import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sumtudou
 * @date 2019/11/13
 */
@Data
@Entity
@Table(name="osm_rule")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class Rule implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    //数据长度
    private Integer data_len;

    private Double min_sup;

    private Double min_conf;

    private Integer min_sup_num;

    private Double confidence;

    private String first;

    private String second;

    private String name;

    private Integer no;

    private String type;

    private Byte status = StatusEnum.OK.getCode();
}