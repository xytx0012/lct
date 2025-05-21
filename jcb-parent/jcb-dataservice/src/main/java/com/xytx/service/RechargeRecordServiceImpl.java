package com.xytx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xytx.constants.JCBConstant;
import com.xytx.mapper.BRechargeRecordMapper;
import com.xytx.mapper.UFinanceAccountMapper;
import com.xytx.moder.BRechargeRecord;
import com.xytx.view.R;
import com.xytx.vo.RechargeRecordsVo;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = RechargeRecordService.class,version = "1.0")
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Resource
    private BRechargeRecordMapper bRechargeRecordMapper;
    @Resource
    private UFinanceAccountMapper accountMapper;

    @Override
    public R queryPage(Integer pageNum, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<BRechargeRecord> bRechargeRecordList = bRechargeRecordMapper.selectPageByUid(userId);
        List<RechargeRecordsVo> rechargeRecordsVoList = new ArrayList<>();
        bRechargeRecordList.forEach(bRechargeRecord -> {
            RechargeRecordsVo rechargeRecordsVo = new RechargeRecordsVo();
            rechargeRecordsVo.setId(bRechargeRecord.getId());
            rechargeRecordsVo.setRechargeDate(bRechargeRecord.getRechargeTime());
            rechargeRecordsVo.setRechargeMoney(bRechargeRecord.getRechargeMoney());
            rechargeRecordsVo.setResult(bRechargeRecord.getRechargeStatus());
            rechargeRecordsVoList.add(rechargeRecordsVo);
        });
        PageInfo<RechargeRecordsVo> pageInfo = new PageInfo<>(rechargeRecordsVoList);
        return R.ok(pageInfo);
    }

    @Override
    public int addRechargeRecord(BRechargeRecord record) {
        return bRechargeRecordMapper.insertSelective(record);
    }

    @Transactional
    @Override
    public int handleKQNotify(String orderId, String payAmount, String payResult) {
        int result = 0;//订单不存在
        int rows =  0;
        //1.查询订单
        BRechargeRecord record =bRechargeRecordMapper.selectByRechargeNo(orderId);
        if(record != null ){
            if( record.getRechargeStatus() == JCBConstant.RECHARGE_STATUS_PROCESSING){
                //2.判断金额是否一致
                String fen = record.getRechargeMoney().multiply(new BigDecimal("100"))
                        .stripTrailingZeros().toPlainString();
                if( fen.equals(payAmount)){
                    //金额一致
                    if("10".equals(payResult)){
                        //成功
                        rows = accountMapper.updateAvailableMoneyByRecharge(record.getUid(),record.getRechargeMoney());
                        if(rows < 1 ){
                            throw new RuntimeException("充值更新资金账号失败");
                        }
                        //更新充值记录的状态
                        rows = bRechargeRecordMapper.updateStatus(record.getId(),JCBConstant.RECHARGE_STATUS_SUCCESS);
                        if( rows < 1) {
                            throw new RuntimeException("充值更新充值记录状态失败");
                        }
                        result  = 1;//成功
                    } else {
                        //充值失败
                        //更新充值记录的状态
                        rows = bRechargeRecordMapper.updateStatus(record.getId(),JCBConstant.RECHARGE_STATUS_FAIL);
                        if( rows < 1) {
                            throw new RuntimeException("充值更新充值记录状态失败");
                        }
                        result = 2;//充值结果是失败的
                    }
                } else {
                    result = 4;//金额不一样
                }
            } else {
                result = 3;//订单已经处理过了
            }
        }
        return result;
    }
}
