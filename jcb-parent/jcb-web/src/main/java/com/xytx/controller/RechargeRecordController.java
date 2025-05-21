package com.xytx.controller;

import com.xytx.enums.RCode;
import com.xytx.service.RechargeRecordService;
import com.xytx.view.R;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class RechargeRecordController {
    @DubboReference(interfaceClass = RechargeRecordService.class,version = "1.0")
    private RechargeRecordService rechargeRecordService;
    /**
     * 根据用户id查最近充值记录,分页查
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("/recharge/records")
    public R queryPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestHeader("Uid") Integer userId){
        if(userId == null){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        if(pageNum == null || pageNum < 1){
            pageNum = 1;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 6;
        }
        return rechargeRecordService.queryPage(pageNum,pageSize,userId);
    }
}
