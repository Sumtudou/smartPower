package com.linln.admin.system.domain;

import java.math.BigDecimal;
import java.util.zip.DeflaterOutputStream;
import lombok.Data;

@Data
public class TagSon {
    private String key;
    private String value;
    private Integer times = 0 ;
    private Double support;
    private Double confidence;
}
