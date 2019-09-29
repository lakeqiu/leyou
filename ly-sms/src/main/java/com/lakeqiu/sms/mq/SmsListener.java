package com.lakeqiu.sms.mq;

import com.lakeqiu.sms.config.SmsProperties;
import com.lakeqiu.sms.utils.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lakeqiu
 */
@Component
public class SmsListener {
    @Autowired
    private SmsProperties props;

    @Autowired
    private SmsUtil smsUtil;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "ly.sms.verify.queue"),
            exchange = @Exchange(name = "ly.sms.exchange", type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"
    ))
    public void listenVerifyCode(Map<String, Object> msg) {
        if (msg == null) {
            return;
        }
        String phone = (String) msg.remove("phone");
        if (StringUtils.isBlank(phone)) {
            return;
        }
        smsUtil.sendSms(props.getSignName(), props.getVerifyCodeTemplate(), phone, msg);
    }
}
