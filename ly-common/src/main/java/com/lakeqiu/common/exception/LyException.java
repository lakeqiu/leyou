package com.lakeqiu.common.exception;

import com.lakeqiu.common.enums.ExpectionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 * java自带的异常类只能接收字符串，
 * 所以用来接收自己想要可以接收的值
 * @author lakeqiu
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LyException extends RuntimeException{
    private ExpectionEnum expectionEnum;

}
