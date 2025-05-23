package com.xytx.moder;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Database Table Remarks:
 *   收益记录表
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table jcb..b_income_record
 */
@Data
public class BIncomeRecord implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   用户ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.uid
     *
     * @mbg.generated
     */
    private Integer uid;

    /**
     * Database Column Remarks:
     *   产品ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.prod_id
     *
     * @mbg.generated
     */
    private Integer prodId;

    /**
     * Database Column Remarks:
     *   投标记录ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.bid_id
     *
     * @mbg.generated
     */
    private Integer bidId;

    /**
     * Database Column Remarks:
     *   投资金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.bid_money
     *
     * @mbg.generated
     */
    private BigDecimal bidMoney;

    /**
     * Database Column Remarks:
     *   到期时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.income_date
     *
     * @mbg.generated
     */
    private Date incomeDate;

    /**
     * Database Column Remarks:
     *   收益金额
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.income_money
     *
     * @mbg.generated
     */
    private BigDecimal incomeMoney;

    /**
     * Database Column Remarks:
     *   收益状态（0未返，1已返）
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jcb..b_income_record.income_status
     *
     * @mbg.generated
     */
    private Integer incomeStatus;
}