package com.lakeqiu.user.service.impl;

import com.lakeqiu.common.enums.ExpectionEnum;
import com.lakeqiu.common.exception.LyException;
import com.lakeqiu.common.utils.NumberUtils;
import com.lakeqiu.user.mapper.UserMapper;
import com.lakeqiu.user.pojo.User;
import com.lakeqiu.user.service.UserService;
import com.lakeqiu.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lakeqiu
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone:";


    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();

        // 判断数据类型
        switch (type){
            // 用户名
            case 1:
                user.setUsername(data);
                break;
            // 手机号
            case 2:
                user.setPhone(data);
                break;
            // 无效数据类型
            default:
                throw new LyException(ExpectionEnum.INVALID_DATA_TYPE);
        }
        // 因为只需要查询有没有，所以不需要将数据查出来，只需要查出数据调数即可
        int count = userMapper.selectCount(user);
        // count==0为true，说明没有数据，该数据可用；如果为false，说明有该数据，说明不可用
        return count==0;
    }

    @Override
    public void sendCode(String phone) {
        // 生成存入redis的key
        String key = KEY_PREFIX + phone;

        // 生成6位长度验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        System.out.println("code-->" + code);

        // 发送验证码
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);
        // 保存验证码(存入redis),有效时长5分钟
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }


    @Override
    public void register(User user, String code) {
        // 从redis里取出验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        // 校验
        if (!StringUtils.equals(cacheCode, code)){
            throw new LyException(ExpectionEnum.INVALID_CODE_DATA);
        }

        // 生成盐值（对密码进行混淆用）,并存入数据库(使以后能够正确解析密码)
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        // 对密码进行md5加密
        String password = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(password);

        // 生成创建时间
        user.setCreated(new Date());

        // 存入数据库
        userMapper.insert(user);

    }

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        User record = new User();
        record.setUsername(username);

        // 查询用户信息
        User user = userMapper.selectOne(record);
        // 校验是否有这个用户
        if (null == user){
            // 没有这个用户或用户密码错误
            throw new LyException(ExpectionEnum.USER_PASSWORD_ERROR);
        }

        // 校验密码
        if (!StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password, user.getSalt()))){
            // 没有这个用户或用户密码错误
            throw new LyException(ExpectionEnum.USER_PASSWORD_ERROR);
        }

        // 用户名与密码正确
        return user;
    }
}
