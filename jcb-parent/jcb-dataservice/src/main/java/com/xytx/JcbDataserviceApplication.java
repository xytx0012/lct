package com.xytx;

import com.xytx.constants.RedisKey;
import com.xytx.mapper.BBidInfoMapper;
import com.xytx.mapper.BProductRecordMapper;
import com.xytx.mapper.UUserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@MapperScan("com.xytx.mapper")
public class JcbDataserviceApplication  {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(JcbDataserviceApplication.class, args);
        StringRedisTemplate stringRedisTemplate = applicationContext.getBean(StringRedisTemplate.class);
        BProductRecordMapper productRecordMapper = applicationContext.getBean(BProductRecordMapper.class);
        BigDecimal avgRete = productRecordMapper.selectAvgRete();
        BBidInfoMapper bidInfoMapper = applicationContext.getBean(BBidInfoMapper.class);
        BigDecimal sumBidMoney=bidInfoMapper.selectSumMoney();
        UUserMapper userMapper = applicationContext.getBean(UUserMapper.class);
        Integer userCount = userMapper.selectCount();
        stringRedisTemplate.opsForHash().put(RedisKey.KEY_PLANTBASE, "avgRete",avgRete.toString());
        stringRedisTemplate.opsForHash().put(RedisKey.KEY_PLANTBASE, "userCount",userCount.toString());
        stringRedisTemplate.opsForHash().put(RedisKey.KEY_PLANTBASE, "sumBidMoney",sumBidMoney.toString());
    }

}
