package com.xytx.controller;

import com.xytx.constants.RedisKey;
import com.xytx.enums.RCode;
import com.xytx.service.SmsService;
import com.xytx.util.CommonUtil;
import com.xytx.view.R;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sms")
public class SmsController {
    @Resource(name = "smsCodeRegisImpl")
    private SmsService smsService;
    @Resource
    StringRedisTemplate stringRedisTemplate;


    @GetMapping("/code/register")
    public R sendCodeRegis(@RequestParam(value = "phone",required = false) String phone){
        if(!CommonUtil.checkPhone(phone)){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }
        if(stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_REG+phone)){
            return R.ok(RCode.SMS_CODE_CAN_USE);
        }
        smsService.sendRegisSms(phone);
        return R.ok();
    }

    @GetMapping("/code/login")
    public R sendLoginCode(@RequestParam("phone") String phone){
        if(!CommonUtil.checkPhone(phone)){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }
        if(stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_LOGIN+phone)){
            return R.err(RCode.SMS_CODE_CAN_USE);
        }
        smsService.sendLoginSms(phone);
        return R.ok();
    }

    @GetMapping("/code/realname")
    public R sendRealNameCode(@RequestParam("phone") String phone){
        if(!CommonUtil.checkPhone(phone)){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }
        if(stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_LOGIN+phone)){
            return R.err(RCode.SMS_CODE_CAN_USE);
        }
        smsService.sendRealNameSms(phone);
        return R.ok();
    }
}
