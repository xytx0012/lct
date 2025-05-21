package com.xytx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xytx.constants.JCBConstant;
import com.xytx.mapper.BBidInfoMapper;
import com.xytx.mapper.BIncomeRecordMapper;
import com.xytx.mapper.BProductRecordMapper;
import com.xytx.mapper.UFinanceAccountMapper;
import com.xytx.moder.BBidInfo;
import com.xytx.moder.BIncomeRecord;
import com.xytx.moder.BProductRecord;
import com.xytx.view.R;
import com.xytx.vo.IncomeRecordVo;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {
    @Resource
    private BIncomeRecordMapper bIncomeRecordMapper;
    @Resource
    private BProductRecordMapper bProductRecordMapper;
    @Resource
    private BBidInfoMapper bidInfoMapper;
    @Resource
    private BIncomeRecordMapper incomeRecordMapper;
    @Resource
    private UFinanceAccountMapper uFinanceAccountMapper;

    @Override
    public R queryIncomeByUid(Integer pageNum, Integer pageSize, Integer userId) {
        PageHelper.startPage(pageNum, pageSize);
        List<IncomeRecordVo> incomeRecordVoList = bIncomeRecordMapper.selectByUid(userId);
        PageInfo<IncomeRecordVo> pageInfo = new PageInfo<>(incomeRecordVoList);
        return R.ok(pageInfo);
    }
}
