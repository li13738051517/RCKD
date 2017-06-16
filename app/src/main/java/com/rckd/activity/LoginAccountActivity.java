package com.rckd.activity;

import com.rckd.R;
import com.rckd.application.AppConfig;
import com.rckd.base.BaseActivity;
import com.yanzhenjie.nohttp.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hp.hpl.sparta.xpath.XPath.get;
import static com.loopj.android.http.AsyncHttpClient.log;
import static com.rckd.R.id.city;
import static com.rckd.application.AppConfig.phone;

/**
 * Created by LiZheng on 2017/6/14 0014.
 */

public class LoginAccountActivity extends BaseActivity implements View.OnClickListener {
    static String tag=LoginAccountActivity.class.getName();
    //当布局中对应Fragment是,可让其绑定相应的fragent ,通过此方法
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;

    //-----------------------
    @BindView(R.id.usertext) EditText usertext;
    @BindView(R.id.psdbg) EditText psdbg;
    @BindView(R.id.logeinbtn) Button logeinbtn;
    @BindView(R.id.forgetpsd) Button forgetpsd;
    Intent intent;
    String  phoneNum="";
    String pwd="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        if (intent!=null){
             Bundle bundle= intent.getExtras();
              if (bundle !=null){
                  AppConfig.phone= bundle.getString("phone" , "13738051517");
                  usertext.setText(AppConfig.phone);
            }
        }

        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("登陆");
        right.setVisibility(View.GONE);
        //-------------
        logeinbtn.setOnClickListener(this);
        forgetpsd.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //忘记密码
            case R.id.forgetpsd:
                phoneNum=usertext.getText().toString().trim();
                //此处需要正则表达式判断
                if ( phoneNum==null ||  phoneNum.isEmpty()){
                    makeText("登陆账户不能没有");
                    return;
                }
                AppConfig.phone=phoneNum;
                Bundle bundle =new Bundle();
                bundle.putString("phone",AppConfig.phone);
                startActivity(ForgetPsdActivity.class ,bundle);
                break;
            case  R.id.left_btn:
                 intent=new Intent();
//                intent.putExtra("city",city); //"city" key ,vaue 后填写实际数据
                setResult(RESULT_CODE_LOGIN ,intent);
                finish();
                break;
            case R.id.logeinbtn:
                phoneNum=usertext.getText().toString().trim();
                //此处需要正则表达式判断
                if ( phoneNum==null ||  phoneNum.isEmpty()){
                    makeText("登陆账户不能没有");
                    return;
                }
                AppConfig.phone=phoneNum;
                pwd=psdbg.getText().toString().trim();
                //此处炫耀正则表达式判断
                if (pwd==null || pwd.isEmpty()){
                    makeText("密码不能没有");
                    return;
                }
                AppConfig.password= pwd;
                intent=new Intent();
                setResult(RESULT_CODE_LOGIN ,intent);
                finish();

                break;
        }
    }
}
