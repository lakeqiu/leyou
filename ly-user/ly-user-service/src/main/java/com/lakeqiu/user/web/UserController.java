package com.lakeqiu.user.web;

import com.lakeqiu.user.pojo.User;
import com.lakeqiu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @author lakeqiu
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 校验数据是否可用（用户名，手机号）
     * @param data 数据
     * @param type 数据类型
     * @return 布尔值
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable String data, @PathVariable Integer type){
        return ResponseEntity.ok(userService.checkData(data, type));
    }

    /**
     * 发送验证码
     * @param phone 要发送的手机号
     * @return void
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @param code 验证码
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code") String code){
        // 因为@Valid校验完数据后还会执行这个函数，故加个判断，如果校验有误就抛出错误不执行
        if (result.hasFieldErrors()){
            throw new RuntimeException(result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.joining("|")));
        }
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名与密码查询用户信息
     * @param username 用户名
     * @param password 用户密码
     * @return 用户信息类
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(
            @RequestParam("username") String username,
            @RequestParam("password") String password){
        User user = userService.queryUserByUsernameAndPassword(username, password);
        return ResponseEntity.ok(user);

    }
}
