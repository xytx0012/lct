package com.xytx.service;

public interface SmsService {
    void sendRegisSms(String phone);

    void sendLoginSms(String phone);

    void sendRealNameSms(String phone);
}
