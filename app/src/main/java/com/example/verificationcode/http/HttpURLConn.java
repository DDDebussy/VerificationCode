package com.example.verificationcode.http;

import android.util.Log;

import com.example.verificationcode.listener.OnCodeListener;
import com.example.verificationcode.util.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 王宗贤 on 2016/1/22.
 */
public class HttpURLConn {


    /**
     * POST请求
     *
     * @param url
     * @param params
     * @param listener
     */
    public static void doPost(final String url, final String params, final OnCodeListener listener) {
        Log.d("date", "url:   " + url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter out = null;
                BufferedReader reader = null;

                try {
                    URL url1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) url1.openConnection();//强制转型
                    //HttpURLConnection进行控制
                    //设置连接超时时间
                    connection.setConnectTimeout(30000);
                    //读取超时时间
                    connection.setReadTimeout(30000);
                    //设置编码格式
                    // 设置接受的数据类型
                    connection.setRequestProperty("Accept-Charset", "GBK");
                    // 设置可以接受序例化的java对象
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    //客户端输出部分
                    //设置 URL 请求的方法
                    connection.setRequestMethod("POST");
                    //设置客户端可以给服务器提交数据，默认是false的,POST时必须改成true
                    connection.setDoOutput(true);
                    //设置可以读取服务器返回的内容，默认为true，不写也可以
                    connection.setDoInput(true);
                    //post方法不允许使用缓存
                    connection.setUseCaches(false);
                    Log.d("date", "params:   " + params);
//            connection.getOutputStream().write(params.getBytes());
                    out = new PrintWriter(connection.getOutputStream());
                    out.print(params);
                    out.flush();

                    InputStream is = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is, "GBK"));
                    String line = reader.readLine();
//            新建stringbuffer对象
                    StringBuffer stringBuffer = new StringBuffer();
                    while (line != null) {
                        stringBuffer.append(line);
                        Log.d("读取服务器的内容", " " + line);
                        line = reader.readLine();
                    }
                    listener.code(stringBuffer.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }
}
