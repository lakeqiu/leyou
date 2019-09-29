package com.lakeqiu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 异常枚举类
 * @author lakeqiu
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExpectionEnum {
    BRAND_NOT_FOUND(404, "品牌不存在！ "),
    CATEGORY_NOT_FOUND(404, "商品分类不存在！"),
    SPEC_GROUP_NOT_FOUND(404, "规格组不存在！"),
    SPEC_PARAM_NOT_FOUND(404, "商品规格参数不存在！"),
    GOODS_NOT_FOUND(404, "商品不存在！"),
    GOODS_UPDATE_ERROR(500, "商品更新失败！"),
    SAVE_BRAND_ERROR(500, "新增品牌失败！"),
    SAVE_SPEC_GROUP_ERROR(500, "新增规格失败！"),
    SAVE_ERROR(500, "新增失败！"),
    INDEX_ERROR(500, "创建索引失败！"),
    SKU_NOT_FOUND(500,"SKU不存在！"),
    DELETE_ERROR(500, "删除失败！"),
    UPDATE_ERROR(500, "更新失败！"),
    ALTER_SPEC_GROUP_ERROR(500, "更新规格失败！"),
    DELETE_SPEC_GROUP_ERROR(500, "删除规格失败！"),
    INVALID_FILE_TYPE(400, "无效的文件类型！"),
    GOOD_ID_ERROR(400, "商品id不能为空！"),
    INVALID_DATA_TYPE(400, "无效的数据！"),
    INVALID_CODE_DATA(400, "无效的验证码！"),
    USER_PASSWORD_ERROR(400, "用户密码错误"),
    CREATE_TOKEN_ERROR(500, "生成token失败！"),
    CART_NOT_FOUND(500, "商品没有找到！"),
    UNAUTHORIZED(500, "未授权！"),
    RECEIVER_ADDRESS_NOT_FOUND(404, "收货地址不能为空！"),
    ;

    private int code;
    private String msg;
}
