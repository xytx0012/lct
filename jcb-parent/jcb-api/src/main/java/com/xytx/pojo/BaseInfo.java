package com.xytx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 平台基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseInfo implements Serializable{
    //收益率平均值
    private BigDecimal historyAvgRate;
    /*累计成交金额*/
    private BigDecimal sumBidMoney;
    //平台注册人数
    private Integer registerUsers;

}
