
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
 * @date 2019/11/04
 */
@Data
@Entity
@Table(name = "osm_relation")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class Relation implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // id
    private String nid;

    private String tagkey;

    private String tagvalue;

    private Byte status = StatusEnum.OK.getCode();
}