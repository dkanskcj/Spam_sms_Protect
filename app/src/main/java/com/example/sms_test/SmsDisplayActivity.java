package com.example.sms_test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
// MyReceiver에서 intent 인자 값을 받아서 문자 내용을 보여주는 함수
public class SmsDisplayActivity extends AppCompatActivity
{
    //객체 선언
    Button btnTitle, btnClose;
    TextView tvMsg;
    private static final String TAG = "SmsDisplayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sms_receiver);
        setContentView(R.layout.sms_receiver);
        //객체 초기화
        tvMsg = findViewById(R.id.tvMsg);
        btnTitle = findViewById(R.id.btnTitle);
        btnClose = findViewById(R.id.btnClose);

        //btnClose 기능 : 창 닫기
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                수동 검사 기준으로 UI를 짠다면 여기에 버튼 클릭 시 서버로 보내는 함수를 적으면 될 것 같음.
                finish();
            }
        });

        //onCreate에 있으면 새로 받은 문자가 갱신이 안된다.
        //인텐트 받기
        Intent displayIntent = getIntent();
        processIntent(displayIntent);
    }

    //새 문자를 받을때(이미 창이 만들어져 있어서 onCreate가 작동을 안할 때, 새 Intent를 받을 때) 작동
    //매개 변수에는 자동으로 갱신되는 인텐트가 들어간다.
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    //intent 값을 받아서 내용을 TextView에 출력하는 메서드
    @SuppressLint("SetTextI18n")
    private void processIntent(Intent displayIntent)
    {
//        sender == 번호, contents == 문자 내용
        String sender = displayIntent.getStringExtra("sender");
        String receivedDate = displayIntent.getStringExtra("receivedDate");
        String contents = displayIntent.getStringExtra("contents");
//        URL 커넥션 할 때 문자 내용이랑 번호
        Log.d(TAG, "보낸이를 받았는가? " + sender);
        Log.d(TAG, "날짜를 받았는가? " + receivedDate);
        Log.d(TAG, "문자 내용을 받았는가? " + contents);


        //보낸 사람이 있으면
        if(sender != null)
        {
            btnTitle.setText(sender);

            tvMsg.setText("전화번호 : " + sender + "\n" + "문자 내용 : " + contents);
        }
    }
}
