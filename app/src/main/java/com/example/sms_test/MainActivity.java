package com.example.sms_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
//어플 실행 시 작동하는 함수
public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                    //        테스트용 코드 -- 팝업 알림
        PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        boolean bScreenOn = manager.isScreenOn();
        if(bScreenOn){
            Log.d(TAG, "화면 켜짐");
        }else{
            Log.d(TAG, "화면 꺼짐");
            Intent popup = new Intent(getApplicationContext(), SMS_Notification.class);
            popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(popup);
        }

        Intent displayIntent = getIntent();
        processIntent(displayIntent);
    }
//    값을 넘길 때 필요한 것 onCreate부분 -> Intent displayIntent = getIntent();
//    ProcessIntent(displayintent); 필요

//    그리고 onNewIntent(Intent intent)함수도 필요
//    processIntent(Intent displayIntent)에서 intent값을 받아오는 String sender, receivedDate, contents를 정의해준다.


       @Override
        protected void onNewIntent(Intent intent)
        {
            super.onNewIntent(intent);
            processIntent(intent);
        }


        private void processIntent(Intent displayIntent)
        {
            String sender = displayIntent.getStringExtra("sender");
        String receivedDate = displayIntent.getStringExtra("receivedDate");
        String contents = displayIntent.getStringExtra("contents");

        Log.d(TAG, "보낸이가 왔는가? " +  sender);
        Log.d(TAG, "보낸 날짜가 왔는가? " +  receivedDate);
        Log.d(TAG, "문자 내용이 왔는가? " +  contents);

        //보낸 사람이 있으면
//        if(sender != null)
//        {
//            btnTitle.setText(sender + "에서 문자 수신");
//            tvMsg.setText("[" + receivedDate + "]\n" + contents);
//        }
//        Log.d(TAG, sender);
        Button btn1 = (Button) findViewById(R.id.b1);
        Button btn2 = (Button) findViewById(R.id.b2);
        Button btn3 = (Button) findViewById(R.id.b3);

        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch(v.getId())
                {
                    case R.id.b1:
//                        버튼 1을 눌렀을 경우 (상)
                        Toast.makeText(MainActivity.this, "상" + sender, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.b2:
//                        버튼 2를 눌렀을 경우 (중)
                        Toast.makeText(MainActivity.this, "중" + receivedDate, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.b3:
//                        버튼 3을 눌렀을 경우 (하)
                        Toast.makeText(MainActivity.this, "하" + contents, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
//        권한 확인
        checkDangerousPermission();
    }

//    SMS 읽어오는 권한 확인 함수
    private void checkDangerousPermission()
    {
        String[] permissions = {Manifest.permission.RECEIVE_SMS};

        int permissionChechk = PackageManager.PERMISSION_GRANTED;
        for(int i = 0; i < permissions.length; i++)
        {
            permissionChechk = ContextCompat.checkSelfPermission(this, permissions[i]);
            if(permissionChechk == PackageManager.PERMISSION_DENIED)
            {
                break;
            }
        }

        if(permissionChechk == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]))
            {
                Toast.makeText(this, "권한 설명 필요함", Toast.LENGTH_LONG).show();
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

//    SMS 권한이 승인이 되어 있을 때 다시 체크 하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == 1)
        {
            for(int i = 0; i < permissions.length; i++)
            {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, permissions[i] + "권한 승인 완료", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, permissions[i] + "권한 승인 미완료", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}