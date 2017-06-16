package com.rckd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.rckd.inter.ISMSListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

/**
 * Created by LiZheng on 2017/6/14 0014.
 */
//部分国产手机对广播接收器管理
public class SMSReceiver  extends BroadcastReceiver{
    private static final String tag = SMSReceiver.class.getName();

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";


    ISMSListener mSMSListener;
    public SMSReceiver(ISMSListener ismsListener ){
        mSMSListener = ismsListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = getMessagesFromIntent(intent);
            for (SmsMessage message : messages) {
                //在这里你可以对放松的号码进行判断，只接受目标号码的验证码
                String msg = message.getDisplayMessageBody();
                String verifyCode = null;
                Pattern p = Pattern.compile("\\d{6}");//这里你可以更改数字来指定验证码的位数
                Matcher m = p.matcher(msg);
                while (m.find()) {
                    verifyCode = m.group();
                    break;
                }
                if (mSMSListener != null) {
                    mSMSListener.onSmsReceive(verifyCode);
                }
            }

        }
    }
    //----------------------------
    public  SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];
        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }


}
