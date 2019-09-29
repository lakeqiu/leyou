package com.lakeqiu.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lakeqiu
 */
@Data
@Table(name = "tb_specification")
public class Specification {

    @Id
    private Long categoryId;
    private String specifications;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }
}
