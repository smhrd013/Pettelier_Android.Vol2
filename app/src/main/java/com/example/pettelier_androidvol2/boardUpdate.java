package com.example.pettelier_androidvol2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class boardUpdate extends AppCompatActivity {

    private Button btn_up_Board_cancel, btn_Board_update;
    private EditText up_board_title,up_board_content;
    private TextView up_writer;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    String seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_update);
        Intent intent = getIntent();
        BoardVO vo = (BoardVO) intent.getSerializableExtra("vo");

        seq = vo.getSeq();


        btn_Board_update = findViewById(R.id.btn_Board_update);
        btn_up_Board_cancel = findViewById(R.id.btn_up_Board_cancel);
        up_board_title = findViewById(R.id.up_board_title);
        up_board_content = findViewById(R.id.up_board_content);
        up_writer = findViewById(R.id.up_writer);
        if((loginCheck.info)!=null){
            up_writer.setText(loginCheck.info.getId());
        }

        up_board_content.setText(vo.getContent());
        up_board_title.setText(vo.getTitle());

        btn_Board_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBoardUpdate();
            }
        });

        btn_up_Board_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }

    public void sendBoardUpdate() {
        //RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this);    // this==getApplicationContext();

        // 서버에 요청할 주소
        String url = "http://172.30.1.28:8089/web/boardUpdate.do";

        // 1.객체만들고 요청 주소만듦

        // 요청시 필요한 문자열 객체 생성  매개변수  4개(통신방식(get,post),요청url주소, new 리스너(익명클래스)-응답시필요한부분 작성함)
        stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>(){
            // 응답데이터를 받아오는 곳
            // 응답시 데이터 받아오는 곳 - 통신이 잘됐다면 로그캣에서 확인하게출력함
            @Override
            public void onResponse(String response) {
                Log.v("resultValue",response);
                Log.v("resultValue", response.length()+"");         //응답글자 수 보여짐,
                if(response.length() > 0) {
                    Intent intent = new Intent(getApplicationContext(),Main_Page.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), " 수정 성공", Toast.LENGTH_SHORT).show();

                }else {
                    // 실패
                    Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            @Override //response를 UTF8로 변경해주는 소스코드
            protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return com.android.volley.Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return com.android.volley.Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            // 보낼 데이터를 저장하는 곳 해쉬맵에 저장해서 보냄 - key,value
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String title = up_board_title.getText().toString();
                String content = up_board_content.getText().toString();
                String board_seq = seq;

                params.put("board_title", title);
                params.put("board_content",content);
                params.put("board_seq",board_seq);
                // key값은 서버에서 지정한 name과 동일하게

                return params;
            }
        };
        stringRequest.setTag("main");       //구분자 어떤클라이언트에서 요청했는지 나타냄 (중요하지않음)
        requestQueue.add(stringRequest);        //실행 요청 add에 담으면 자동요청

    }
}