package com.lakeqiu.item.web;

import com.lakeqiu.item.mapper.SpecificationMapper;
import com.lakeqiu.item.pojo.SpecGroup;
import com.lakeqiu.item.pojo.SpecParam;
import com.lakeqiu.item.pojo.Specification;
import com.lakeqiu.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lakeqiu
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格组
     * @param cid 分类id
     * @return 规格组集合
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecificationByCategoryId(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(specificationService.querySpecificationByCategoryId(cid));
    }



    /**
     * 保存新增查询规格
     * @param specGroup 查询规格信息
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecification(@RequestBody SpecGroup specGroup){
        specificationService.saveSpecification(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更改规格信息
     * @param specGroup 规格信息
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateSpecification(@RequestBody SpecGroup specGroup){
        specificationService.updateSpecification(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除规格信息
     * @param id 规格信息id
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecificationById(@PathVariable("id") Long id){
        specificationService.deleteSpecificationById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据组id查询
     * 这里是get请求后带参数？xx=xx，所以用@RequestParam取参数
     * @param gid 组id
     * @return 参数集合
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamByGid(
            @RequestParam(value = "gid", required = false)Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "searching", required = false)Boolean searching
            ){
        return ResponseEntity.ok(specificationService.queryParam(gid, cid, searching));
    }

    /**
     * 新增商品规格参数
     * @param specParam 商品规格参数类
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveParam(@RequestBody SpecParam specParam){
        specificationService.saveParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 修改商品规格参数
     * @param specParam 商品规格参数类
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam){
        specificationService.updateParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除商品规格参数
     * @param id 商品规格参数id
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable("id") Long id){
        specificationService.deleteParam(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 查询规格参数组，及组内参数
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }
}
