package com.lakeqiu.common.vo;

import com.lakeqiu.common.enums.ExpectionEnum;
import lombok.Data;

/**
 * 封装返回的异常，使得用户体验好一些
 * @author lakeqiu
 */
@Data
public class ExpectionResult {
    private int code;
    private String msg;
    /**
     * 时间戳
     */
    private Long timestamp;

    public ExpectionResult(ExpectionEnum expectionEnum){
        this.code = expectionEnum.getCode();
        this.msg = expectionEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
