package com.xytx.mapper;

import com.xytx.moder.UUser;

import java.util.Date;

public interface UUserMapper {
    /**
     * 查询用户数量
     * @return
     */
    int selectCount();

    UUser selectByPhone(String phone);

    void insertUser(UUser user);

    void updateLastLoginTime(String phone, Date lastLoginTime);


    int updateRealName(String phone, String name, String idCard);

    UUser selectUserByid(Integer uid);
}