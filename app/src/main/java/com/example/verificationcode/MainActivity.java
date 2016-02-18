package com.example.verificationcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.verificationcode.http.HttpURLConn;
import com.example.verificationcode.http.LoginAction;
import com.example.verificationcode.listener.OnCodeListener;
import com.example.verificationcode.util.MyVolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button mBtnLogin;
    private EditText mEditPhoneNum;
    private EditText mEditPassword;
    private Button mBtnGetCode;
    private TextView mTextRequestMessage;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mTextRequestMessage.setText(msg.getData().getString("msg"));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnGetCode = (Button) findViewById(R.id.button_get_code);
        mBtnLogin = (Button) findViewById(R.id.button_login);
        mEditPassword = (EditText) findViewById(R.id.edittext_password);
        mEditPhoneNum = (EditText) findViewById(R.id.edittext_phone_num);
        mTextRequestMessage = (TextView) findViewById(R.id.text_screen_message);
        mBtnGetCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mEditPhoneNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mEditPassword.setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_get_code:
//                使用volley请求网络
//                getCode(mEditPhoneNum.getText().toString());
                LoginAction.doPostGetCode(mEditPhoneNum.getText().toString(), new OnCodeListener() {
                    @Override
                    public void code(String result) {
                        Log.d("date", "请求成功:" + result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            String returnCode = obj.optString("returnCode");
                            if (returnCode.equals("00")) {
                                Log.d("date", "请求数据成功" + returnCode);
                                Message msg = new Message();
                                msg.what = 0;
                                Bundle bundle = new Bundle();
                                bundle.putString("msg", obj.optString("returnMsg"));
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mTextRequestMessage.setText("");
                break;
            case R.id.button_login:
//                使用volley请求网络
//                login(mEditPhoneNum.getText().toString(),mEditPassword.getText().toString());
                LoginAction.doPostConfrimCode(mEditPhoneNum.getText().toString(), mEditPassword.getText().toString(), new OnCodeListener() {
                    @Override
                    public void code(String result) {
                        Log.d("date", "登陆返回的数据:" + result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            String returnCode = obj.optString("returnCode");
                            String code = obj.optString("code");
                            if (returnCode.equals("00")) {
                                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mTextRequestMessage.setText("");
                break;
        }
    }

    /**
     * 获取验证码
     *
     * @param num 手机号
     */
    private void getCode(final String num) {

        String url = "http://order2.m.qncloud.cn/nncpverify/getCode.action";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("date", "请求成功:" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String returnCode = obj.optString("returnCode");
                    if (returnCode.equals("00")) {
                        Toast.makeText(getApplicationContext(), "获取验证码成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("date", "请求失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("phoneNumber", num);
                map.put("action", "17");
                map.put("versionCode", "11");
                map.put("channel", "2");
                return map;
            }
        };
        MyVolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
    }

    /**
     * 验证登陆
     *
     * @param importCode 输入的密码
     * @param num        手机号
     */
    private void login(final String num, final String importCode) {
        String url = "http://order2.m.qncloud.cn/nncpverify/mobilelogin.action";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("date", "登陆返回的数据:" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String returnCode = obj.optString("returnCode");
                    String code = obj.optString("code");
                    if (returnCode.equals("00")) {
                        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("date", "登陆失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                //{versionName=2.1.4, systemVersion=5.0.2, versionCode=11, longitude=116.305157,
                // mac=20:82:c0:1d:e5:c4, latitude=39.930223, phoneNumber=18501900547, deviceName=Redmi Note 2, channel=2, code=123456}
                map.put("versionName", "2.1.4");
                map.put("systemVersion", "5.0.2");
                map.put("versionCode", "11");
                map.put("longitude", "116.305157");
                map.put("mac", "20:82:c0:1d:e5:c4");
                map.put("latitude", "39.930223");
                map.put("phoneNumber", num);
                map.put("deviceName", "Redmi Note 2");
                map.put("channel", "2");
                map.put("code", importCode);
                return map;
            }
        };
        MyVolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
    }
}
