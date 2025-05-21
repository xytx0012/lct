package com.xytx.service;

import com.xytx.moder.UUser;

public interface UserService {
    UUser queryUser(String phone);

    int addUser(String phone, String passWord);

    void setLastLoginTime(String phone);

    Boolean modifyRealname(String phone,String name,String idCard);

    UUser queryUserByid(Integer uid);

}
