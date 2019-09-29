package com.lakeqiu.common.advice;

import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.vo.ExpectionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 拦截service层所有异常，进行通用异常处理
 * @ControllerAdvice 加这个会拦截所有加@Controller的类的异常
 * @author lakeqiu
 */
@ControllerAdvice
public class CommonExpectionHandler {

    /**
     * 处理异常方法
     * @ExceptionHandler 要处理的异常类型
     * @param e 异常
     * @return
     */
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExpectionResult> handlerExpection(LyException e){
        // 获取枚举异常实例
        ExpectionEnum em = e.getExpectionEnum();
        // 对异常进行封装并返回
        return ResponseEntity.status(em.getCode()).body(new ExpectionResult(em));
    }
}
