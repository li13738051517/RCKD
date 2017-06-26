package com.rckd.activity;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.location.h.j.n;

/**
 * Created by LiZheng on 2017/6/24 0024.
 */
//修改手机密码
public class ChangePhonePasswordActivity extends BaseActivity implements View.OnClickListener {

    @Nullable@BindView(R.id.left_btn) Button left;
    @Nullable@BindView(R.id.title_text) TextView title;
    @Nullable@BindView(R.id.right_btn) Button right;
    @Nullable@BindView(R.id.user_phone) EditText userPhone;
    @Nullable@BindView(R.id.user__orpwd) EditText user__orpwd;
    @Nullable@BindView(R.id.setpsdtext) EditText user_pwd_new;
    @Nullable@BindView(R.id.cpasswordtext) EditText user_pwd_com;
    @Nullable@BindView(R.id.subbtn) Button sbbtn;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepwd);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("修改密码");
        right.setVisibility(View.GONE);
        sbbtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
            case R.id.subbtn:
                String phone=userPhone.getText().toString().trim();
                if (phone==null || phone.isEmpty()){
                    makeText("手机号不能没有哦");
                    return;
                }
                String orginPwd=user__orpwd.getText().toString().trim();
                if (orginPwd==null || orginPwd.isEmpty()){
                    makeText("原密码不能没有哦");
                    return;
                }
                String newpwd=user_pwd_new.getText().toString().trim();
                if (newpwd==null || newpwd.isEmpty()){
                    makeText("新密码不能为空");
                    return;
                }
                String newpwdcom=user_pwd_com.getText().toString().trim();
                if (newpwdcom==null|| newpwdcom.isEmpty()){
                    makeText("密码不能为空!!!");
                    return;
                }
                if (!newpwdcom.equals(newpwd)){
                    makeText("两次密码不一致!!!");
                    return;
                }
                //------------------------------
                finish();
                break;

        }
    }
}
