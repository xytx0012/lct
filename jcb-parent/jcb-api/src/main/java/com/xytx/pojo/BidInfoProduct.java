package com.xytx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品详情和投资记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidInfoProduct implements Serializable {
    private Integer id;
    private String phone;
    private String bidTime;
    private BigDecimal bidMoney;
}
