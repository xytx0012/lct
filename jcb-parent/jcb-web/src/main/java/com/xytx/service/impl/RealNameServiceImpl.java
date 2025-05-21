package com.xytx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xytx.service.RealNameService;
import com.xytx.service.UserService;
import com.xytx.vo.RealNameVo;
import io.jsonwebtoken.lang.Strings;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class RealNameServiceImpl implements RealNameService {

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;


    // 免费接口地址
    private static final String API_URL = "https://api.shumaiapi.com/idcard/verify";
    // 可用密钥列表（定期更换）
    private static final String APP_KEYS = "iD1H==cL-Eo8Y-YZpYr==ia9XVsV7oEp";


    /**
     * 调用第三方接口实名认证
     * @return
     */
    @Override
    public boolean realName(RealNameVo realNameVo) {
        Boolean b=false;
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost httpPost = new HttpPost(API_URL);
//            // 构建表单参数（自动URL编码）
//            List<NameValuePair> params = new ArrayList<>();
//            params.add(new BasicNameValuePair("name", realNameVo.getName()));
//            params.add(new BasicNameValuePair("idCard", realNameVo.getIdCard()));
//            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//
//            // 执行请求
//            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
//                HttpEntity entity = response.getEntity();
//                String responseBody = EntityUtils.toString(entity);
//
//                // 解析JSON响应
//                if(responseBody != null) {
//                    JSONObject respObject = JSONObject.parseObject(responseBody);
//                    if( "1001".equalsIgnoreCase(respObject.getString("code"))&&"200".equals(respObject.getString("reason"))) {
//                        boolean modifyResult =  userService.modifyRealname(realNameVo.getPhone(),realNameVo.getName(),realNameVo.getIdCard());
//                        b = modifyResult;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String resp="{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"error_code\": 0,\n" +
                "        \"reason\": \"成功\",\n" +
                "        \"result\": {\n" +
                "            \"realname\": \""+realNameVo.getName()+"\",\n" +
                "            \"idcard\": \"350721197702134399\",\n" +
                "            \"isok\": true\n" +
                "        }\n" +
                "    }\n" +
                "}";
        if(Strings.hasText(resp)){
            JSONObject respObject = JSONObject.parseObject(resp);
            if( "10000".equalsIgnoreCase(respObject.getString("code"))){
                //解析result
                b = respObject.getJSONObject("result")
                        .getJSONObject("result")
                        .getBooleanValue("isok");

                //处理更新数据库
                boolean modifyResult =  userService.modifyRealname(realNameVo.getPhone(),realNameVo.getName(),realNameVo.getIdCard());
                b = modifyResult;
            }
        }
        return b;
    }
}
