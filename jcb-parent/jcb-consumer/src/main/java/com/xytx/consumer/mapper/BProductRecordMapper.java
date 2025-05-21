package com.xytx.consumer.mapper;

import com.xytx.moder.BProductRecord;

import java.math.BigDecimal;


public interface BProductRecordMapper {
    BProductRecord selectProductDetilById(Integer productId);

    int updateLeftProductMoney(Integer productId, BigDecimal money);


    int updateStatus(Integer productId, Object productStatus);
}