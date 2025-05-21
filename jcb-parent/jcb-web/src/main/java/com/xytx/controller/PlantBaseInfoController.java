package com.xytx.controller;

import com.xytx.pojo.BaseInfo;
import com.xytx.service.PlantBaseInfoService;
import com.xytx.view.R;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")

public class PlantBaseInfoController {

    @DubboReference(interfaceClass = PlantBaseInfoService.class,version = "1.0")
    private PlantBaseInfoService plantBaseInfoService;

    /**
     * 页面信息
     * @return
     */
    @GetMapping("/plat/info")
    public R queryPlantBaseInfo(){
        BaseInfo baseInfo = plantBaseInfoService.getPlantBaseInfo();
        return R.ok(baseInfo);
    }
}
