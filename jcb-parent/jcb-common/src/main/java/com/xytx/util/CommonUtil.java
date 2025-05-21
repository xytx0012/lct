package com.xytx.util;

public class CommonUtil {
    public static String phoneTuoMing(String phone){
        return phone.substring(0,3)+"******"+phone.substring(9,11);
    }

    public static boolean checkPhone(String phone){
       return !(phone.length()!=11||phone==null||!phone.matches("^1[3-9]\\d{9}$"));
    }
}
