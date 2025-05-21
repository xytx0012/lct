package com.xytx.controller;

import com.xytx.constants.RedisKey;
import com.xytx.enums.RCode;
import com.xytx.moder.UUser;
import com.xytx.service.InvestService;
import com.xytx.service.UserService;
import com.xytx.util.CommonUtil;
import com.xytx.view.R;
import com.xytx.view.RankView;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1")
public class InvestController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @DubboReference(interfaceClass = InvestService.class,version = "1.0")
    private InvestService investService;

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;

    /**
     * 投资排行榜
     * @return
     */
    @GetMapping("/invest/rank")
    public R showInvestRank(){
        Set<ZSetOperations.TypedTuple<String>> s = stringRedisTemplate.boundZSetOps(RedisKey.KEY_INVEST_RANK).reverseRangeWithScores(0,2);
        List<RankView> rankViewList = new ArrayList<>();
        s.forEach((entry) -> {
            rankViewList.add(new RankView(CommonUtil.phoneTuoMing(entry.getValue()),entry.getScore()));
        });
        return R.ok(rankViewList);
    }


    /**
     * 投资产品
     * @param productId
     * @param money
     * @param userId
     * @return
     */
    @PostMapping("/invest/product")
    public R investProduct(@RequestParam("productId") Integer productId
            ,@RequestParam("money") BigDecimal money
            ,@RequestHeader("Uid") Integer userId){
        if (productId==null || money==null||money.intValue()%100!=0||money.intValue()<100){
            return R.err(RCode.REQUEST_PARAM_ERR);
        }
        int count=investService.inverstProduct(productId,money,userId);
        if (count==1){
            return R.err(RCode.UNKOWN);
        }
        modifyInvestRank(userId,money);
        return R.ok();
    }



    /*更新投资排行榜*/
    private void modifyInvestRank(Integer uid,BigDecimal money){
        UUser user  = userService.queryUserByid(uid);
        if(user != null){
            //更新redis中的投资排行榜
            String key = RedisKey.KEY_INVEST_RANK;
            stringRedisTemplate.boundZSetOps(key).incrementScore(
                    user.getPhone(),money.doubleValue());
        }
    }
}
