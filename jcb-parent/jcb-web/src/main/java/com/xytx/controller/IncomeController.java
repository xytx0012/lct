package com.xytx.controller;

import com.xytx.service.IncomeService;
import com.xytx.view.R;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class IncomeController {
    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incomeService;
    /**
     * 查询收益记录
     * @return
     */
    @GetMapping("/income/records")
    public R queryIncomeRecord(@RequestParam("pageNum") Integer pageNum
            , @RequestParam("pageSize") Integer pageSize
            , @RequestHeader("Uid") Integer userId){
        if(pageNum == null || pageNum < 1){
            pageNum = 1;
        }
        if(pageSize == null || pageSize < 1){
            pageSize = 6;
        }
        return incomeService.queryIncomeByUid(pageNum,pageSize,userId);
    }
}
