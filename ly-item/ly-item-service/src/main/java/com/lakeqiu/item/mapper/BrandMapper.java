package com.lakeqiu.item.mapper;

import com.lakeqiu.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 品牌信息通用mapper接口
 * @author lakeqiu
 */
public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long> {
    /**
     * 新增品牌与商品分类的中间表数据
     * @param cid 商品分类
     * @param bid 品牌
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据cid查询这个分类下所有品牌
     * @param cid cid
     * @return 品牌集合
     */
    @Select("SELECT b.id, b.`name`, b.image, b.letter FROM tb_category_brand cb INNER JOIN tb_brand b " +
            "ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);


    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteCategoryBrandByBid(Long bid);

    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{cid})")
    List<Brand> queryBrandByCid(Long cid);

}
