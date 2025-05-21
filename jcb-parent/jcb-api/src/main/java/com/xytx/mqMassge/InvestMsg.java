package com.xytx.mqMassge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestMsg implements Serializable {
    private Integer productId;
    private Integer userId;
    private BigDecimal money;
    private Long r;
}
