package com.lakeqiu.item.api;

import com.lakeqiu.item.pojo.SpecGroup;
import com.lakeqiu.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lakeqiu
 */
public interface SpecificationApi {
    /**
     * 根据组id查询
     * 这里是get请求后带参数？xx=xx，所以用@RequestParam取参数
     * @param gid 组id
     * @return 参数集合
     */
    @GetMapping("spec/params")
   List<SpecParam> queryParamByGid(
            @RequestParam(value = "gid", required = false)Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "searching", required = false)Boolean searching
    );

    /**
     * 查询规格参数组，及组内参数
     * @param cid
     * @return
     */
    @GetMapping("spec/group")
    List<SpecGroup> queryGroupByCid(@RequestParam("cid") Long cid);
}
