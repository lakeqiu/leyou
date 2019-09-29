package com.lakeqiu.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lakeqiu
 */
@Table(name = "tb_spec_param")
@Data
public class SpecParam {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    /**
     * 告诉通用mapper，写sql语句时，numeric字段要写成这个`numeric`
     */
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}
