package com.rckd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.application.AppConfig;
import com.rckd.base.BaseActivity;
import com.rckd.utils.HelpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.rckd.application.AppConfig.phone;

/**
 * Created by LiZheng on 2017/5/6 0006.
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    static String tag = LoginActivity.class.getName();
    TelephonyManager phoneMgr;
    //titie
    @BindView(R.id.left_btn) Button left_btn;
    @BindView(R.id.title_text) TextView title_text;
    @BindView(R.id.right_btn) Button right_btn;

    //activity
    @BindView(R.id.text_phone) Button text_phone;
    @BindView(R.id.usertext) EditText usertext;
    @BindView(R.id.logeinbtn) Button logeinbtn;
    @BindView(R.id.userbg) RelativeLayout userbg;

    View view;//加载
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CODE_BAR_AD,null);
                finish();
            }
        });
        title_text.setVisibility(View.VISIBLE);
        title_text.setText("登陆/注册");
        right_btn.setVisibility(View.GONE);


        //------------
        phoneMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        logeinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.phone = usertext.getText().toString().trim();
                if (!HelpUtil.isMobileNO(phone)) {
                    makeText("请输入正确的手机号码");
                    return;
                }
                AppConfig.isLogin=true;
                Bundle bundle=new Bundle();
                bundle.putString("phone" ,phone);
                startActivityForResult(LoginAccountActivity.class ,bundle ,REQUEST_CODE_LOGIN);//此处跳转到登陆病不结束掉当前界面
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case RESULT_CODE_LOGIN:
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.left_btn:
//                finish();
//                break;
//            case R.id.logeinbtn:
//                phone = usertext.getText().toString().trim();
//
//                return;
        }
    }
}

