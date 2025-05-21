package com.xytx.service;

import com.xytx.mapper.UFinanceAccountMapper;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;


import java.math.BigDecimal;

@DubboService(interfaceClass = UFinanceAccountService.class,version = "1.0")
public class UFinanceAccountServiceImpl implements UFinanceAccountService {
    @Resource
    private UFinanceAccountMapper financeAccountMapper;
    @Override
    public BigDecimal queryMoneyByUid(Integer uid) {
        return financeAccountMapper.selectMoneyByUid(uid);
    }
}
