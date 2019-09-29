package com.lakeqiu.item.web;

import com.lakeqiu.item.pojo.Category;
import com.lakeqiu.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lakeqiu
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点id查询商品分类
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid){
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 根据多个id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryListByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }
}
