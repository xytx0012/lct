package com.xytx.mapper;

import com.xytx.moder.BBidInfo;
import com.xytx.pojo.BidInfoProduct;
import com.xytx.vo.ProductRecordVo;


import java.math.BigDecimal;
import java.util.List;
public interface BBidInfoMapper {
    /**
     * 总交易额
     * @return
     */
    BigDecimal selectSumMoney();

    List<BidInfoProduct> selectByProductId(Integer productId);

    List<ProductRecordVo> selectByUserId(Integer userId);


    List<BBidInfo> selectByProdId(Integer id);

    int selectCount(Integer userId);
}