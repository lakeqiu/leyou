package com.lakeqiu.item.api;

import com.lakeqiu.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface CategoryApi {
    /**
     * 调用根据多个id查询商品分类
     * 不加ResponseEntity，只有200-300间的状态会接收成功，其他的会报错
     * @param ids
     * @return
     */
    @GetMapping("category/list/ids")
    List<Category> queryCategoryListByIds(@RequestParam("ids") List<Long> ids);
}
