package com.example.sms_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
// 문자 수신 시 데이터를 받아서 Display로 보내주는 함수
public class MyReceiver extends BroadcastReceiver
//public class MyReceiver extends AppCompatActivity
{
    private static final String TAG = "MyReceiver";

    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onReceiver : 호출됨");

        Bundle bundle = intent.getExtras();

        SmsMessage[] messages = parseSmsMessage(bundle);

        if(messages != null && messages.length > 0)
        {
            Log.d(TAG, "onReceiver : SMS를 수신하였습니다.");

            String sender = messages[0].getOriginatingAddress();
            Log.d(TAG, "보낸이 : " + sender);

            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.d(TAG, "받은 시각 : " + receivedDate);

            String contents = messages[0].getMessageBody();
            Log.d(TAG, "문자 내용 : " + contents);

//            클래스명.class에 값 보내는 부분
//            Intent displayIntent = new Intent(context, SmsDisplayActivity.class);
            Intent displayIntent = new Intent(context, MainActivity.class);
            displayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP|
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            Log.d(TAG, "Test");

//            //        테스트용 코드 -- 팝업 알림
//            PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
//            boolean bScreenOn = manager.isScreenOn();
//            if(bScreenOn){
//                Log.d("Popup", "화면 켜짐");
//            }else{
//                Log.d("Popup", "화면 꺼짐");
//                Intent popup = new Intent(getApplicationContext(), SMS_Notification.class);
//                popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(popup);
//            }
            displayIntent.putExtra("sender", sender);
            displayIntent.putExtra("receivedDate", dateFormat.format(receivedDate));
            displayIntent.putExtra("contents", contents);
            context.startActivity(displayIntent);

            Log.d(TAG, "성공적으로 전송 완료.");

//            String action = intent.getAction();
//            if(action.equals(Intent.ACTION_BOOT_COMPLETED))
//            {
//                Intent new_ac = new Intent(context, SmsDisplayActivity.class);
//                new_ac.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(new_ac);
//            }
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle)
    {
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i = 0; i < objs.length; i++)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            }
            else
            {
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
            }
        }
        return messages;
    }
}