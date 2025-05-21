package com.xytx.consumer.service;

import com.rabbitmq.client.Channel;
import com.xytx.constants.JCBConstant;
import com.xytx.moder.BBidInfo;
import com.xytx.moder.BIncomeRecord;
import com.xytx.moder.BProductRecord;
import com.xytx.mqMassge.InvestMsg;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class InvestConsumer {
    @Resource
    private com.xytx.consumer.mapper.BProductRecordMapper bProductRecordMapper;
    @Resource
    private com.xytx.consumer.mapper.BBidInfoMapper bBidInfoMapper;
    @Resource
    private com.xytx.consumer.mapper.BIncomeRecordMapper bIncomeRecordMapper;


    @RabbitListener(queues = "invest.queue")
    public void receive(InvestMsg investMsg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        try {
            Integer productId = investMsg.getProductId();
            Integer userId =  investMsg.getUserId();
            BigDecimal money= investMsg.getMoney();
            long c = investMsg.getR();

            //插入投资记录
            BBidInfo bidInfo = new BBidInfo();
            bidInfo.setBidMoney(money);
            bidInfo.setUid(userId);
            bidInfo.setBidStatus(JCBConstant.INVEST_STATUS_SUCC);
            bidInfo.setBidTime(new Date());
            bidInfo.setProdId(productId);
            bBidInfoMapper.insertSelective(bidInfo);

            if(c==3){
                BigDecimal dayRate = null; //日利率
                BigDecimal cycle = null; //周期
                Date incomeDate = null; //到期时间
                BigDecimal income = null; //利息

                BProductRecord productRecord = bProductRecordMapper.selectProductDetilById(productId);
                //日利率
                dayRate = productRecord.getRate().divide(new BigDecimal("360"),10, RoundingMode.HALF_UP)
                        .divide(new BigDecimal("100"),10,RoundingMode.HALF_UP);
                //计算周期
                if(productRecord.getProductType()== JCBConstant.PRODUCT_TYPE_XINSHOUBAO){
                    cycle = new BigDecimal(productRecord.getCycle());
                    incomeDate = java.sql.Date.valueOf((
                            new Date().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .plusDays(productRecord.getCycle())
                    ));
                }else {
                    cycle = new BigDecimal(productRecord.getCycle()*30);
                    incomeDate = java.sql.Date.valueOf((
                            new Date().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                    .plusDays(productRecord.getCycle()*30)
                    ));
                }
                //生成收益计划
                List<BBidInfo> bidInfoList = bBidInfoMapper.selectByProdId(productId);
                for(BBidInfo bid:bidInfoList){
                    income = bid.getBidMoney().multiply(cycle).multiply(dayRate);
                    BIncomeRecord incomeRecord = new BIncomeRecord();
                    incomeRecord.setBidId(bid.getId());
                    incomeRecord.setBidMoney(bid.getBidMoney());
                    incomeRecord.setIncomeDate(incomeDate);
                    incomeRecord.setIncomeStatus(JCBConstant.INCOME_STATUS_PLAN);
                    incomeRecord.setProdId(bid.getId());
                    incomeRecord.setIncomeMoney(income);
                    incomeRecord.setUid(bid.getUid());
                    bIncomeRecordMapper.insertSelective(incomeRecord);
                }
                bProductRecordMapper.updateStatus(productId, JCBConstant.RECHARGE_STATUS_FAIL);


                /**
                 * 生成返还计划
                 * 产品id为消息参数
                 * 根据到期时间设置消息的TTL
                 */

            }
            bProductRecordMapper.updateLeftProductMoney(productId,money);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            try {
                channel.basicNack(deliveryTag,false,true);
            }catch (Exception ex) {
                throw new RuntimeException("消息拒绝失败",ex);
            }
            throw new RuntimeException("处理投资消息失败", e);
        }

    }
}
