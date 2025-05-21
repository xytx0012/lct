package com.xytx.service.impl;


import com.xytx.constants.RedisKey;
import com.xytx.service.SmsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("smsCodeRegisImpl")
public class SmsCodeImpl implements SmsService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 注册时获取验证码,向手机号发送验证码
     * @param phone
     */
    @Override
    public void sendRegisSms(String phone) {

    }

    @Override
    public void sendLoginSms(String phone) {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        log.info(code+"");
        stringRedisTemplate.opsForValue().set(RedisKey.KEY_SMS_CODE_LOGIN+phone, code+"",5, TimeUnit.MINUTES);
    }

    @Override
    public void sendRealNameSms(String phone) {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        log.info(code+"");
        stringRedisTemplate.opsForValue().set(RedisKey.KEY_SMS_CODE_RealName+phone, code+"",5, TimeUnit.MINUTES);
    }
}
