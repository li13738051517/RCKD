package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;
import com.rckd.utils.HelpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by LiZheng on 2017/5/6 0006.
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static String tag = LoginActivity.class.getName();
    TelephonyManager phoneMgr;
    //titie
    @BindView(R.id.left_btn)
    Button left_btn;
    @BindView(R.id.title_text)
    TextView title_text;
    @BindView(R.id.right_btn)
    Button right_btn;

    //activity
    @BindView(R.id.text_phone)
    Button text_phone;
    @BindView(R.id.usertext)
    EditText usertext;
    @BindView(R.id.logeinbtn)
    Button logeinbtn;
    @BindView(R.id.userbg)
    RelativeLayout userbg;
    String phone = "";

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        Timber.e(tag);
        Timber.e(tag + "  onCreate ", tag);
        phoneMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        title_text.setClickable(false);
        title_text.setText("");
        right_btn.setClickable(false);
        right_btn.setText("");
//        String userPhone = HelpUtil.getSharedPreferences(this, "sp", "userPhone");
        phone = usertext.getText().toString().trim();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.logeinbtn:
                if (!HelpUtil.isMobileNO(phone)) {
//                HelpUtil.showToast(getApplicationContext(), "请输入正确的手机号码");
                    makeText("请输入正确的手机号码");
                }
                return;
        }
    }
}

