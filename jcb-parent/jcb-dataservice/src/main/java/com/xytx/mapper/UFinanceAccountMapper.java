package com.xytx.mapper;

import com.xytx.moder.UFinanceAccount;

import java.math.BigDecimal;

public interface UFinanceAccountMapper {


    void insertFinanceAccount(UFinanceAccount financeAccount);

    BigDecimal selectMoneyByUid(Integer uid);

    int updateMoney(Integer userId, BigDecimal money);

    int updateAvailableMoneyByIncomeBack(Integer uid, BigDecimal bidMoney, BigDecimal incomeMoney);

    int updateAvailableMoneyByRecharge(Integer uid, BigDecimal rechargeMoney);
}