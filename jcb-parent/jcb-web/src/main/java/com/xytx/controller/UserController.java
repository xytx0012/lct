package com.xytx.controller;


import com.xytx.constants.RedisKey;
import com.xytx.enums.RCode;
import com.xytx.moder.UUser;
import com.xytx.service.RealNameService;
import com.xytx.service.UFinanceAccountService;
import com.xytx.service.UserService;
import com.xytx.util.CommonUtil;
import com.xytx.util.JWTUtils;
import com.xytx.view.R;
import com.xytx.vo.RealNameVo;
import com.xytx.vo.UserCenterVo;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class UserController {
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;

    @DubboReference(interfaceClass = UFinanceAccountService.class,version = "1.0")
    private UFinanceAccountService financeAccountService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RealNameService realNameService;

    @GetMapping("/user/phone/exists")
    public R queryUserByPhone(@RequestParam(value = "phone",required = false) String phone){
        if(!CommonUtil.checkPhone(phone)){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }
        UUser user = userService.queryUser(phone);
        if(user != null){
            return R.err(RCode.PHONE_EXISTS);
        }
        return R.ok(RCode.PHONE_EXISTS);
    }

    @PostMapping("/user/register")
    public R registerUser(@RequestParam("phone") String phone
            ,@RequestParam("pword") String passWord
            ,@RequestParam("scode") String code) {

        if(!CommonUtil.checkPhone(phone)){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }

        if (passWord==null||passWord.length()!=32){
            return R.err(RCode.PHONE_LOGIN_PASSWORD_INVALID);
        }

        if(!stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_REG+phone)){
            return R.err(RCode.SMS_CODE_INVALID);
        }

        if(!stringRedisTemplate.opsForValue().get(RedisKey.KEY_SMS_CODE_REG+phone).equals(code)){
            return R.err(RCode.SMS_CODE_INVALID);
        }

        int count = userService.addUser(phone,passWord);
        return count>0?R.ok():R.err(RCode.PHONE_EXISTS);
    }

    @PostMapping("/user/login")
    public R login(@RequestParam("phone") String phone
            ,@RequestParam("pword") String passWord
            ,@RequestParam("scode") String code){
        if(!CommonUtil.checkPhone(phone)){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }
        if (passWord==null||passWord.length()!=32){
            return R.err(RCode.PHONE_LOGIN_PASSWORD_INVALID);
        }
        if(!stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_LOGIN+phone)){
            return R.err(RCode.SMS_CODE_INVALID);
        }
        if(!code.equals(stringRedisTemplate.opsForValue().get(RedisKey.KEY_SMS_CODE_LOGIN+phone))){
            return R.err(RCode.SMS_CODE_INVALID);
        }

        UUser user = userService.queryUser(phone);
        if(user==null){
            return R.err(RCode.UNKOWN);
        }
        userService.setLastLoginTime(phone);
        String jwt = JWTUtils.createJwt(user.getId());
        R r=R.ok();
        r.setAccessToken(jwt);
        Map<String,Object> map=new HashMap<>();
        map.put("uid",user.getId());
        map.put("phone",user.getPhone());
        map.put("name",user.getName());
        r.setData(map);
        return r;
    }


    @PostMapping("/user/realname")
    public R realName(@RequestBody RealNameVo realNameVo){
        if(!CommonUtil.checkPhone(realNameVo.getPhone())){
            return R.err(RCode.PHONE_FORMAT_ERR);
        }
        if(realNameVo.getCode()==null||realNameVo.getCode().length()!=4||!stringRedisTemplate.hasKey(RedisKey.KEY_SMS_CODE_RealName+realNameVo.getPhone())){
            return R.err(RCode.SMS_CODE_INVALID);
        }
        if(realNameVo.getName()==null||realNameVo.getIdCard()==null){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        UUser user = userService.queryUser(realNameVo.getPhone());
        if(user==null){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        if(user.getName()!=null){
            return R.err(RCode.REALNAME_RETRY);
        }
        //调用第三方接口进行实名认证;
        boolean b = realNameService.realName(realNameVo);
        return b?R.ok():R.err(RCode.REALNAME_FAIL);
    }

    @GetMapping("/user/usercenter")
    public R userCenter(@RequestHeader(name = "Uid",required = false)Integer uid){
        if(uid==null){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        UUser user = userService.queryUserByid(uid);
        if(user==null){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        BigDecimal money = financeAccountService.queryMoneyByUid(uid);
        UserCenterVo userCenterVo = new UserCenterVo();
        userCenterVo.setPhone(user.getPhone());
        userCenterVo.setName(user.getName());
        userCenterVo.setHeaderImage(user.getHeaderImage());
        if(user.getLastLoginTime()!=null){
           userCenterVo.setLastLoginTime(user.getLastLoginTime());
        }
        userCenterVo.setMoney(money);
        return R.ok(userCenterVo);
    }

}
