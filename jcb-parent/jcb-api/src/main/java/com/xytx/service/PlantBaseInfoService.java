package com.xytx.service;

import com.xytx.pojo.BaseInfo;

/**
 * 平台基本信息service
 */
public interface PlantBaseInfoService {
    /**
     * 计算利率,注册人数,累计成交金额
     */
    BaseInfo getPlantBaseInfo();

    void updatePlantBase();
}
