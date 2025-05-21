package com.xytx.service;

import com.xytx.mapper.UFinanceAccountMapper;
import com.xytx.mapper.UUserMapper;
import com.xytx.moder.UFinanceAccount;
import com.xytx.moder.UUser;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.util.Date;

@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UUserMapper userMapper;
    @Resource
    private UFinanceAccountMapper financeAccountMapper;

    @Value("${jcb.config.password-salt}")
    private String passwordSalt;

    @Override
    public UUser queryUser(String phone) {
        UUser user = userMapper.selectByPhone(phone);
        return user;
    }

    @Transactional
    @Override
    public int addUser(String phone, String passWord) {
        UUser user = userMapper.selectByPhone(phone);
        if(user!=null){
            return -1;
        }

        passWord = DigestUtils.md5Hex(passWord+passwordSalt);
        user = new UUser();
        user.setPhone(phone);
        user.setLoginPassword(passWord);
        user.setAddTime(new Date());
        userMapper.insertUser(user);

        UFinanceAccount financeAccount = new UFinanceAccount();
        financeAccount.setUid(user.getId());
        financeAccount.setAvailableMoney(new BigDecimal("0"));
        financeAccountMapper.insertFinanceAccount(financeAccount);

        return 1;
    }

    @Override
    public void setLastLoginTime(String phone) {
        userMapper.updateLastLoginTime(phone, new Date());
    }

    @Override
    public Boolean modifyRealname(String phone, String name, String idCard) {
        int update = userMapper.updateRealName(phone,name,idCard);
        return update > 0;
    }

    @Override
    public UUser queryUserByid(Integer uid) {
        return userMapper.selectUserByid(uid);
    }
}
