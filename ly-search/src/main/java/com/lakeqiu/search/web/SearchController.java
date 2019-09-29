package com.lakeqiu.search.web;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.search.pojo.Goods;
import com.lakeqiu.search.pojo.SearchRequest;
import com.lakeqiu.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lakeqiu
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 搜索功能
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest request){
        return ResponseEntity.ok(searchService.search(request));
    }
}
