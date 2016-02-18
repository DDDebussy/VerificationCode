package com.example.verificationcode.http;

import com.example.verificationcode.listener.OnCodeListener;
import com.example.verificationcode.util.Constant;

/**
 * Created by 王宗贤 on 2016/1/22.
 */
public class LoginAction {
    /**
     * 获得验证码
     * @param num 电话号码
     * @param listener 结果的监听器
     */
    public static void doPostGetCode(String num,OnCodeListener listener){
        String url = Constant.GENERATE_CODE;
        String params="phoneNumber="+num+"&action=17&versionCode=11&channel=2";
        HttpURLConn.doPost(url, params, listener);
    }

    /**
     * 验证验证码
     * @param num
     * @param code
     * @param listener
     */
    public static void doPostConfrimCode(String num,String code,OnCodeListener listener){
        String url = Constant.LOGIN_URL;
        String params="versionName=2.1.4&systemVersion=5.0.2&versionCode=11&longitude=116.305157&mac=20:82:c0:1d:e5:c4&latitude=39.930223&" +
                "phoneNumber="+num+"&deviceName=Redmi Note 2&code="+code+"&channel=2";
        HttpURLConn.doPost(url, params, listener);
    }
}
