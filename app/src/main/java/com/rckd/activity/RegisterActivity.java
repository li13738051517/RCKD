package com.rckd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.application.AppConfig;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LiZheng on 2017/7/4 0004.
 */
//注册界面
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.left_btn)
    Button leftBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    Button rightBtn;
    @BindView(R.id.usertext)
    EditText usertext;

    @BindView(R.id.yanzhengtext)
    EditText yanzhengtext;
    @BindView(R.id.codebtn)
    Button codebtn;
    @BindView(R.id.setpsdtext)
    EditText setpsdtext;
    @BindView(R.id.passwordbg)
    RelativeLayout passwordbg;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.passwordbg3)
    LinearLayout passwordbg3;
    @BindView(R.id.subbtn)
    Button subbtn;
//    @BindView(R.id.line)
//    LinearLayout line;
//    @BindView(R.id.titleView)
//    RelativeLayout titleView;
    @BindView(R.id.userbg)
    RelativeLayout userbg;
    @BindView(R.id.psdbg)
    RelativeLayout psdbg;
    @BindView(R.id.cpasswordtext)
    EditText cpasswordtext;
    @BindView(R.id.cpasswordbg)
    RelativeLayout cpasswordbg;
    @BindView(R.id.textView2)
    TextView textView2;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.forgetpsd, null);
        setContentView(view);
        ButterKnife.bind(this);
        //--------------------------
        Intent intent=getIntent();
        if (intent!=null){
            Bundle bundle= intent.getExtras();
            if (bundle !=null){
                AppConfig.phone= bundle.getString("phone");
                usertext.setText(AppConfig.phone);
            }
        }
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("注册");
        rightBtn.setVisibility(View.GONE);
        cpasswordbg.setVisibility(View.GONE);
        subbtn.setText("确认");
        onViewClicked(view);
    }

    @OnClick({R.id.left_btn, R.id.codebtn ,R.id.subbtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                setResult(RESULT_CODE_LOGIN,null);
                finish();
                break;
            case R.id.codebtn:
                break;
            case R.id.subbtn:
                //-----------------------------一系列判断
                if (!checkBox.isChecked()){
                    makeText("您没有勾选注册协议书");
                    return;
                }
                //---------------------注册成功后
                //-------------
                AppConfig.isRegister=true;
                Intent intent=new Intent();
                Bundle bundle =new Bundle();
                bundle.putString("phone" ,"13738051517");
                bundle.putString("pwd","li1234567890");
                intent.putExtras(bundle);
                startActivity(LoginAccountActivity.class ,bundle);
                setResult(RESULT_CODE_LOGIN,intent);
                finish();
                break;
        }
    }
}
