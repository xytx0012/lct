package com.xytx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 返回给前端的充值记录
 */
public class RechargeRecordsVo implements Serializable {
    private Integer id;
    private Integer result;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rechargeDate;
    private BigDecimal rechargeMoney;
}
