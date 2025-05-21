package com.xytx.consumer.mapper;
import com.xytx.moder.BBidInfo;
import java.util.List;

public interface BBidInfoMapper {


    void insertSelective(BBidInfo bidInfo);

    List<BBidInfo> selectByProdId(Integer id);

}