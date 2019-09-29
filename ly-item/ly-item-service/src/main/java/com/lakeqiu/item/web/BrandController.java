package com.lakeqiu.item.web;

import com.lakeqiu.common.vo.PageResult;
import com.lakeqiu.item.pojo.Brand;
import com.lakeqiu.item.service.BrandService;
import com.lakeqiu.item.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lakeqiu
 */
@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param page 当前页
     * @param rows 一页显示数量
     * @param sortBy 是否排序（默认false）,可传可不传
     * @param desc 排序方式 false：ASC ； true：DESC
     * @param key 过滤条件 可传可不传
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key

    ){
        PageResult<Brand> brandPageResult = brandService.queryBrandByPage(page, rows, sortBy, desc, key);
        return ResponseEntity.ok(brandPageResult);
    }

    /**
     * 新增品牌功能
     * @param brand 品牌信息类
     * @param cids 品牌所属分类
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(@RequestBody Brand brand, @RequestParam("cids")List<Long> cids){
        brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新品牌
     * @param brandVo
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(BrandVo brandVo) {
        brandService.updateBrand(brandVo);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除品牌
     * @param bid
     * @return
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long bid) {
        brandService.deleteBrand(bid);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据cid查询这个分类下所有品牌
     * @param cid 分类cid
     * @return 品牌集合
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        return ResponseEntity.ok(brandService.queryBrandsByCid(cid));
    }

    /**
     * 根据id查询商品品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        return ResponseEntity.ok(brandService.queryById(id));
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }
}
