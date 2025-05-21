package com.xytx.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xytx.enums.RCode;
import com.xytx.util.JWTUtils;
import com.xytx.view.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        boolean flag = false;
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }
        String jwt = request.getHeader("Authorization");
        String headerId = request.getHeader("Uid");
        jwt=jwt.substring(7);
        try {
            Integer id =  JWTUtils.readJwt(jwt);
            if(headerId!=null&&headerId.equals(String.valueOf(id))){
                return true;
            }
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }
        //返回json数据给前端
        R r= R.err(RCode.TOKEN_INVALID);
        //使用HttpServletResponse输出 json
        String respJson = JSONObject.toJSONString(r);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(respJson);
        out.flush();
        out.close();
        return false;
    }
}
