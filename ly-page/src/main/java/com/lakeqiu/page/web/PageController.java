package com.lakeqiu.page.web;

import com.lakeqiu.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 加了@RestController返回结果会被当成json处理
 * @author lakeqiu
 */
@Controller
@RequestMapping("item")
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 获取商品详情
     * @param spuId
     * @param model
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(@PathVariable("id")Long spuId, Model model){
        // 获取模型数据
        Map<String, Object> attributes = pageService.loadModel(spuId);

        // 装填数据
        model.addAllAttributes(attributes);
        return "item";
    }
}
