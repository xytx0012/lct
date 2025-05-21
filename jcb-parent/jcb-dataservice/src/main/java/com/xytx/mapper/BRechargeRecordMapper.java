package com.xytx.mapper;

import com.xytx.moder.BRechargeRecord;

import java.math.BigDecimal;
import java.util.List;

public interface BRechargeRecordMapper {
    /**
     * 统计累计成交金额
     * @return
     */
    BigDecimal selectSumMoney();

    List<BRechargeRecord> selectPageByUid(Integer userId);

    int insertSelective(BRechargeRecord record);

    BRechargeRecord selectByRechargeNo(String orderId);

    int updateStatus(Integer id, Object rechargeStatusSuccess);
}