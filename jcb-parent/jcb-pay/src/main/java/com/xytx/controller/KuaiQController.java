package com.xytx.controller;

import com.xytx.moder.UUser;
import com.xytx.service.KQService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/kq")
public class KuaiQController {
    @Resource
    private KQService kqService;

    @GetMapping("/rece/recharge")
    public String receFrontRechargeKQ(Integer uid,
                                      BigDecimal rechargeMoney,
                                      Model model){
        if(uid==null||uid<=0||rechargeMoney==null||rechargeMoney.doubleValue()<=0){
            return "err";
        }
        UUser user = kqService.queryUser(uid);
        if(user==null){
            return "err";
        }
        Map<String,String> data =kqService.generateFormData(uid,user.getPhone(),rechargeMoney);
        model.addAllAttributes(data);
        kqService.addRecharge(uid,rechargeMoney,data.get("orderId"));
        kqService.addOrderIdToRedis(data.get("orderId"));
        return "kqForm";
    }


    //接收快钱给商家的支付结果 , 快钱以get方式，发送请求给商家
    @GetMapping("/rece/notify")
    @ResponseBody
    public String payResultNotify(HttpServletRequest request){
        System.out.println("=================接收快钱的异步通知=============");
        kqService.kqNotify(request);
        return "<result>1</result><redirecturl>http://localhost:8080/</redirecturl>";
    }

    //从定时任务，调用的接口
    @GetMapping("/rece/query")
    @ResponseBody
    public String queryKQOrder(){
        kqService.handleQueryOrder();
        return "接收了查询的请求";

    }
}
