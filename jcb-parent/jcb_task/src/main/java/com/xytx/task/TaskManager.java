package com.xytx.task;


import com.xytx.service.PlantBaseInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("taskManager")
public class TaskManager {
    @DubboReference(interfaceClass = PlantBaseInfoService.class,version = "1.0")
    private PlantBaseInfoService plantBaseInfoService;

    /**
     * 更新首页信息
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePlantBase(){
        plantBaseInfoService.updatePlantBase();
    }

}
