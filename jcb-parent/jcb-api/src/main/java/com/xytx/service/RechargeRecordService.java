package com.xytx.service;

import com.xytx.moder.BRechargeRecord;
import com.xytx.view.R;


public interface RechargeRecordService {
    R queryPage(Integer pageNum, Integer pageSize, Integer userId);

    int addRechargeRecord(BRechargeRecord record);

    int handleKQNotify(String orderId, String payAmount, String payResult);
}
