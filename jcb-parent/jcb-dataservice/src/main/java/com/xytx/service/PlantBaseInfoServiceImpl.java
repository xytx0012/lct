package com.xytx.service;

import com.xytx.constants.RedisKey;
import com.xytx.mapper.BBidInfoMapper;
import com.xytx.mapper.BProductRecordMapper;
import com.xytx.mapper.UUserMapper;
import com.xytx.pojo.BaseInfo;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台基本信息service
 */
@DubboService(version = "1.0")
public class PlantBaseInfoServiceImpl implements PlantBaseInfoService{

    @Resource
    private UUserMapper userMapper;
    @Resource
    private BProductRecordMapper productRecordMapper;
    @Resource
    private BBidInfoMapper bidInfoMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public BaseInfo getPlantBaseInfo() {
        String sumBidMoney =(String) stringRedisTemplate.opsForHash().get(RedisKey.KEY_PLANTBASE,"sumBidMoney");
        String avgRete =  (String) stringRedisTemplate.opsForHash().get(RedisKey.KEY_PLANTBASE,"avgRete");
        String userCount = (String) stringRedisTemplate.opsForHash().get(RedisKey.KEY_PLANTBASE,"userCount");
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setHistoryAvgRate(new BigDecimal(avgRete));
        baseInfo.setRegisterUsers(Integer.parseInt(userCount));
        baseInfo.setSumBidMoney(new BigDecimal(sumBidMoney));
        return baseInfo;
    }

    @Override
    public void updatePlantBase() {
        BigDecimal avgRete = productRecordMapper.selectAvgRete();
        BigDecimal sumBidMoney=bidInfoMapper.selectSumMoney();
        int userCount = userMapper.selectCount();
        Map<Object, Object> entries = new HashMap<>();
        entries.put("sumBidMoney",sumBidMoney);
        entries.put("avgRete",avgRete);
        entries.put("userCount",userCount);
        stringRedisTemplate.opsForHash().putAll(RedisKey.KEY_PLANTBASE, entries);
    }
}
