package com.example.pettelier_androidvol2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private EditText login_id,login_pw;
    private Button btn_login;
    private TextView tv_find,tv_join;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private FragmentManager fm;
    private Real_Main RM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        btn_login = findViewById(R.id.btn_login);
        tv_join = findViewById(R.id.tv_join);
        tv_find = findViewById(R.id.tv_find);
        Intent intent = getIntent();


        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),join.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        tv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),find_id.class);
                startActivity(intent);
            }
        });
    }

    private void sendRequest() {
        //RequestQueue ?????? ??????
        requestQueue = Volley.newRequestQueue(this);    // this==getApplicationContext();

        // ????????? ????????? ??????
        String url = "http://192.168.43.220:8081/web/andLogin.do";

        // ?????? : 172.30.1.28:8089
        // ?????? : 59.0.129.176:8081
        // ?????? : 210.223.239.212:8081
        // ?????? : 220.80.165.82:8081

        // 1.??????????????? ?????? ????????????

        // ????????? ????????? ????????? ?????? ??????  ????????????  4???(????????????(get,post),??????url??????, new ?????????(???????????????)-???????????????????????? ?????????)
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            // ?????????????????? ???????????? ???
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response.length()+"");         //???????????? ??? ?????????,
                if(response.length() > 0) {
                    //????????? ??????
                    // 0??? ???????????????
                    try {
                        JSONObject jsonObject = new JSONObject(response);   //response??? JSON????????? ?????? ??? ????????? ???????????? ?????????
                        String id = jsonObject.getString("mb_id");
                        String pw = jsonObject.getString("mb_pw");
                        String nick = jsonObject.getString("mb_nick");
                        String name = jsonObject.getString("mb_name");
                        String phone = jsonObject.getString("mb_phone");
                        String address = jsonObject.getString("mb_address");
                        String joindate = jsonObject.getString("mb_joindate");
                        String type = jsonObject.getString("mb_type");

                        //????????? ????????? After_Login_Main ?????? ??????,
                        // MemberVO ???????????? ?????????
                        //MemberVO vo = new MemberVO(id,pw,nick,name,phone,address,joindate,type);
                        loginCheck.info= new MemberVO(id,pw,nick,name,phone,address,joindate,type);
                        Log.v("check",loginCheck.info.getId());
                        if((loginCheck.info.getId()).equals("admin")){
                            Intent intent = new Intent(getApplicationContext(),Main_Page.class);
                            startActivity(intent);
                        }

                        else {
//                            fm.beginTransaction().replace(R.id.frame,RM).commit();

                            Intent intent = new Intent(getApplicationContext(), Main_Page.class);
                            //intent.putExtra("response", response);
                            startActivity(intent);
                            //Intent intent = new Intent(getApplicationContext(), Real_Main.class);

                            //startActivity(intent);
                            Toast.makeText(login.this, "???????????????", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    //???????????????
                    Toast.makeText(login.this, "???????????????", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener(){
            // ???????????? ?????? ????????? ??????
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override //response??? UTF8??? ??????????????? ????????????
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            // ?????? ???????????? ???????????? ??? ???????????? ???????????? ?????? - key,value
            @Override
            protected Map <String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String id = login_id.getText().toString();
                String pw = login_pw.getText().toString();


                params.put("mb_id", id);
                params.put("mb_pw",pw);
                // key?????? ???????????? ????????? name??? ????????????

                return params;
            }
        };
        stringRequest.setTag("main");       //????????? ??????????????????????????? ??????????????? ????????? (??????????????????)
        requestQueue.add(stringRequest);        //?????? ?????? add??? ????????? ????????????
    }
}