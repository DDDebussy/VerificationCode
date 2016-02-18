package com.example.verificationcode.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by 王宗贤 on 2016/1/21.
 */
    public class MyVolleySingleton {
        private RequestQueue requestQueue;
        private static MyVolleySingleton mMySingleton;
        private MyVolleySingleton(Context context){
            requestQueue= Volley.newRequestQueue(context);
        }
        public synchronized static MyVolleySingleton getInstance(Context context){
            if(mMySingleton==null){
                mMySingleton=new MyVolleySingleton(context);
            }
            return mMySingleton;
        }

        public RequestQueue getRequestQueue() {
            return requestQueue;
        }
    }
