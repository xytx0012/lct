package com.xytx.mapper;

import com.xytx.moder.BProductRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BProductRecordMapper {
    /**
     * 查询历史收益率平均值
     * @return
     */
    BigDecimal selectAvgRete();

    List<BProductRecord> selectByTypeLimit(Integer pType);

    BProductRecord selectProductDetilById(Integer productId);

    int updateLeftProductMoney(Integer productId, BigDecimal money);

    void updateSelled(Integer productId);

    List<BProductRecord> selectFullTimeProducts(Date beginDate, Date endDate);

    int updateStatus(Integer productId, Object productStatus);
}