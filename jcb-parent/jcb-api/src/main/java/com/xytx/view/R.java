package com.xytx.view;

import com.xytx.enums.RCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class R implements Serializable {
    private int code;
    private String msg;
    private Object data;
    private String accessToken;


    public R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public R(Object data) {
        this.data = data;
    }

    public static R ok(Object data) {
        R r = new R(data);
        r.setRCode(RCode.SUCC);
        return r;
    }
    public static R ok(){
        R r = new R();
        r.setRCode(RCode.SUCC);
        return r;
    }

    public static R err(RCode rCode) {
        R r=new R();
        r.setRCode(rCode);
        return r;
    }

    public void setRCode(RCode rCode){
        this.code = rCode.getCode();
        this.msg=rCode.getText();
    }
}
