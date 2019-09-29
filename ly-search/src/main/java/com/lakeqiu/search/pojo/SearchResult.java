package com.lakeqiu.search.pojo;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Brand;
import com.lakeqiu.item.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lakeqiu
 */
@Data
public class SearchResult<Goods> extends PageResult<Goods> {

    private List<Brand> brands;
    private List<Category> categories;

    public SearchResult() {
    }

     //规格参数过滤条件
    private List<Map<String, Object>> specs;

    public SearchResult(Long total,
                        Long totalPage,
                        List<Goods> items,
                        List<Category> categories,
                        List<Brand> brands,
                        List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total,
                        Long totalPage,
                        List<Goods> items,
                        List<Category> categories,
                        List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SearchResult<?> that = (SearchResult<?>) o;
        return Objects.equals(brands, that.brands) &&
                Objects.equals(categories, that.categories) &&
                Objects.equals(specs, that.specs);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), brands, categories, specs);
    }
}