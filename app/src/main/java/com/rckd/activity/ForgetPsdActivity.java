package com.rckd.activity;

import com.rckd.R;
import com.rckd.application.AppConfig;
import com.rckd.base.BaseActivity;
import com.rckd.inter.ISMSListener;
import com.rckd.service.SMSReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.location.h.j.B;
import static com.baidu.location.h.j.g;
import static com.baidu.location.h.j.v;

/**
 * Created by LiZheng on 2017/6/14 0014.
 */
public class ForgetPsdActivity extends BaseActivity implements View.OnClickListener  ,ISMSListener{
    SMSReceiver mReceiver;
    static String tag=ForgetPsdActivity.class.getName();
    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;

    //------------------------
    @BindView(R.id.usertext) EditText usertext;
    String phoneNum="";
    @BindView(R.id.yanzhengtext) EditText yanzhengtext;
    String testNum; //验证码

    @BindView(R.id.codebtn) Button codebtn;

    //----------------

    @BindView(R.id.setpsdtext) EditText setpsdtext;
    String pwd;
    @BindView(R.id.cpasswordtext) EditText cpasswordtext;
    String pwdNext;
    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.subbtn) Button subbtn;//提交按钮

    @BindView(R.id.textView2) TextView textView2;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpsd);
        ButterKnife.bind(this);
        Intent intent =getIntent();
        if (intent!=null){
            Bundle bundle= intent.getExtras();
            if (bundle !=null){
                AppConfig.phone= bundle.getString("phone" , "13738051517");
                usertext.setText(AppConfig.phone);
            }
        }

        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("找回密码");
        right.setVisibility(View.GONE);
        subbtn.setOnClickListener(this);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());  //其实就这一句是关键

    }


    @Override
    protected void onStart() {
        super.onStart();

        //注册广播接收器
        mReceiver =new SMSReceiver(this);
        //-----------------添加action
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        //优先级设置为最高
        filter.setPriority(Integer.MAX_VALUE);
        //注册----------广播很短,禁止做任何耗时操作
        registerReceiver(mReceiver,filter);
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.codebtn:
                //请求发送验证码
                //需要添加发送短信权限 ,读取验证码等权限
                makeText("验证码已经发送");
                //可以考虑加上时间限制!!!(比如60s)
                testNum =yanzhengtext.getText().toString().trim();
                break;
            case R.id.left_btn:
                finish();
                break;
            case R.id.subbtn:
                //
                phoneNum=usertext.getText().toString().trim();
                //此处需要正则表达式判断
                if ( phoneNum==null ||  phoneNum.isEmpty()){
                    makeText("登陆账户不能没有");
                    return;
                }
                AppConfig.phone=phoneNum;
                testNum =yanzhengtext.getText().toString().trim();
                //正则表达式-------
                if (testNum==null || testNum.isEmpty()){
                    makeText("验证码输入不正确");
                    //
                    return;
                }
                pwd=setpsdtext.getText().toString().trim();
                if (  pwd==null ||   pwd.isEmpty()){
                    makeText("密码不能为空");
                    //
                    return;
                }
                pwdNext= cpasswordtext.getText().toString().trim();
                if (     pwdNext==null ||     pwdNext.isEmpty()){
                    makeText("密码不能为空");
                    //
                    return;
                }

                if (!pwdNext.equals(pwd)){
                    makeText("两次密码不匹配!!!");
                    return;
                }
                AppConfig.password=pwdNext;
                if (!checkBox.isChecked()){
                    makeText("您没有勾选同意协议书");
                }

                //确认修改后让当前界面结束
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);

    }

    //验证码的接收器
    @Override
    public void onSmsReceive(String verifyCode) {
        Toast.makeText(this, "验证码为：" + verifyCode, Toast.LENGTH_SHORT).show();
        //获取验证码并填写到EditText中，即可
        yanzhengtext.setText(verifyCode);
    }



}
