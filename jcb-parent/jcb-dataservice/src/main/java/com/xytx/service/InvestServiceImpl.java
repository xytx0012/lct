package com.xytx.service;


import com.xytx.mapper.BBidInfoMapper;
import com.xytx.mapper.BIncomeRecordMapper;
import com.xytx.mapper.BProductRecordMapper;
import com.xytx.mapper.UFinanceAccountMapper;
import com.xytx.mqMassge.InvestMsg;
import org.apache.dubbo.config.annotation.DubboService;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@DubboService(interfaceClass = InvestService.class,version = "1.0")
public class InvestServiceImpl implements InvestService {
    @Resource
    private BProductRecordMapper bProductRecordMapper;
    @Resource
    private UFinanceAccountMapper uFinanceAccountMapper;
    @Resource
    private BBidInfoMapper bBidInfoMapper;
    @Resource
    private BIncomeRecordMapper bIncomeRecordMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Value("${my.exchangeName}")
    private String exchangeName;
    @Value("${my.topic}")
    private String topic;

    private static final DefaultRedisScript<Long> INVERT_SCRIPT = new DefaultRedisScript<>();
    static {
        INVERT_SCRIPT.setLocation(new ClassPathResource("invert.lua"));
        INVERT_SCRIPT.setResultType(Long.class);
    }


    /**
     * 投资初步方案
     * 优化方案描述如下:
     *1.乐观锁解决"超卖"问题
     * 2.synchronized 解决每人十次投资 (分布式集群下synchronized就解决不了)
     * 3.redis分布式锁解决分布式集群投资次数问题{
     *     1.若锁还未释放服务器宕机了,后面线程都被阻塞于是要给Key一个过期时间,
     *     2.当线程1业务还没完成,过期时间就到了,redis自动删除了Key,线程2此时拿到了key,
     *          然后线程1业务执行完进行释放锁,于是线程1把线程2的锁释放完了,发生误删问题
     *     3.给锁一个标识(线程ID),当作key对应的value存入redis,每次释放锁时检查是不是当前线程的锁,若是才释放,否则啥也不做
     *     4.分布式集群下线程ID可能重复,于是在线程ID前加入UUID,使得线程ID唯一
     *     5.若线程1检查完是自己的锁准备释放时因为某些原因发生了阻塞(如Jvm垃圾回收),
     *          这时候锁的时间到了redis主动删了,然后线程2拿到了锁,紧接这线程1恢复了,然后直接删掉了锁,于是又发生了误删问题
     *     6.用lua脚步保证检查锁是不是自己的和释放锁两个动作一致性,原子性 缺点(不可重入,不可重试只获取一次获取不到就结束了不像synchronized一样可以阻塞等待,redis主从一致性问题)
     *     7.使用Redisson框架(实现了很多分布式功能),解决了上述不可重入等问题?
     *     8.因为加了锁,所以性能差,
     * }
     * @param productId
     * @param money
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int inverstProduct(Integer productId, BigDecimal money,Integer userId) {
        //用户剩余金额是否充值若充值则扣减
        int update = uFinanceAccountMapper.updateMoney(userId,money);
        if(update<1){
            return 1;
        }
        Long r = stringRedisTemplate.execute(INVERT_SCRIPT
                     , Collections.emptyList()
                     ,productId.toString()
                      ,userId.toString()
                       ,money.toString());

        if (r==0){
            throw new RuntimeException("参数有误");
        }

        if(r==1){
            throw new RuntimeException("每人最多投资10次");
        }



       //消息队列完成下面:参数1 产品ID,参数2 用户ID,参数3 投资金额 参数4 是否生成收益计划是否更新产品状态
        System.err.println(r);
        InvestMsg investMsg = new InvestMsg();
        investMsg.setProductId(productId);
        investMsg.setMoney(money);
        investMsg.setUserId(userId);
        investMsg.setR(r);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(
                exchangeName,
                topic,
                investMsg,
                m -> {
                    // 可以设置消息属性
                    m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT); // 持久化消息
                    m.getMessageProperties().setPriority(5); // 设置优先级
                    return m;
                },
                correlationData
                );
        return 0;
    }


//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public int inverstProduct(Integer productId, BigDecimal money,Integer userId) {
//        //用户剩余金额是否充值若充值则扣减
//        int update = uFinanceAccountMapper.updateMoney(userId,money);
//        if(update<1){
//            return 1;
//        }
//
//        //查询产品信息
//        BProductRecord bProductRecord = bProductRecordMapper.selectProductDetilById(productId);
//        if(money.intValue()<bProductRecord.getBidMinLimit().intValue()){
//            throw new RuntimeException("投资金额不能小于产品最小投资金额");
//        }
//        if(money.intValue()>bProductRecord.getBidMaxLimit().intValue()){
//            throw new RuntimeException("投资金额不能大于产品最大投资金额");
//        }
//        if(money.intValue()>bProductRecord.getLeftProductMoney().intValue()){
//            throw new RuntimeException("产品剩余投资金额不足111111");
//        }
//
//        synchronized (this){
//            int count = bBidInfoMapper.selectCount(userId);
//            if(count>=500){
//                throw new RuntimeException("每个用户只能投资10次");
//            }
//            //更新剩余金额
//            int r = bProductRecordMapper.updateLeftProductMoney(productId,money);
//            if(r<1){
//                throw new RuntimeException("产品剩余投资金额不足");
//            }
//
//            //插入投资记录
//            BBidInfo bidInfo = new BBidInfo();
//            bidInfo.setBidStatus(1);
//            bidInfo.setBidMoney(money);
//            bidInfo.setUid(userId);
//            bidInfo.setBidTime(new Date());
//            bidInfo.setProdId(productId);
//            bBidInfoMapper.insertSelective(bidInfo);
//
//            //更新产品状态
//            if(money.intValue()==bProductRecord.getLeftProductMoney().intValue()){
//                bProductRecordMapper.updateStatus(productId,2);
//                BigDecimal dayRate = null; //日利率
//                BigDecimal cycle = null; //周期
//                Date incomeDate = null; //到期时间
//                BigDecimal income = null; //利息
//
//                BProductRecord productRecord = bProductRecordMapper.selectProductDetilById(productId);
//                //日利率
//                dayRate = productRecord.getRate().divide(new BigDecimal("360"),10, RoundingMode.HALF_UP)
//                        .divide(new BigDecimal("100"),10,RoundingMode.HALF_UP);
//                //计算周期
//                if(productRecord.getProductType()== JCBConstant.PRODUCT_TYPE_XINSHOUBAO){
//                    cycle = new BigDecimal(productRecord.getCycle());
//                    incomeDate = java.sql.Date.valueOf((
//                            new Date().toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate()
//                                    .plusDays(productRecord.getCycle())
//                    ));
//                }else {
//                    cycle = new BigDecimal(productRecord.getCycle()*30);
//                    incomeDate = java.sql.Date.valueOf((
//                            new Date().toInstant()
//                                    .atZone(ZoneId.systemDefault())
//                                    .toLocalDate()
//                                    .plusDays(productRecord.getCycle()*30)
//                    ));
//                }
//                //生成收益计划
//                List<BBidInfo> bidInfoList = bBidInfoMapper.selectByProdId(productId);
//                for(BBidInfo bid:bidInfoList){
//                    income = bid.getBidMoney().multiply(cycle).multiply(dayRate);
//                    BIncomeRecord incomeRecord = new BIncomeRecord();
//                    incomeRecord.setBidId(bid.getId());
//                    incomeRecord.setBidMoney(bid.getBidMoney());
//                    incomeRecord.setIncomeDate(incomeDate);
//                    incomeRecord.setIncomeStatus(JCBConstant.INCOME_STATUS_PLAN);
//                    incomeRecord.setProdId(bid.getId());
//                    incomeRecord.setIncomeMoney(income);
//                    incomeRecord.setUid(bid.getUid());
//                    bIncomeRecordMapper.insertSelective(incomeRecord);
//                }
//                bProductRecordMapper.updateStatus(productId, JCBConstant.RECHARGE_STATUS_FAIL);
//
//            }
//        }
//        return 0;
//    }
}
