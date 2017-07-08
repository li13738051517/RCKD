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
    Intent intent;
    static String tag = LoginActivity.class.getName();
    TelephonyManager phoneMgr;
    //titie
    @BindView(R.id.left_btn) Button left_btn;
    @BindView(R.id.title_text) TextView title_text;
    @BindView(R.id.right_btn) Button right_btn;

    //activity
    @BindView(R.id.text_phone) TextView text_phone;
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
                //判断是否已经注册,已经注册则跳转到正式登陆界面,没有,通过接口,获取返回值 true or fasle ,建议异步请求
                //----------------没有注册,去注册 ,实际由接口判断
                if (!AppConfig.isRegister){
                    AppConfig.phone = usertext.getText().toString().trim();
                    //注意正则表达式的正确性
//                    if (!HelpUtil.isMobileNO(phone)) {
//                        makeText("请输入正确的手机号码");
//                        return;
//                    }
                    Bundle bundle=new Bundle();
                    bundle.putString("phone" ,phone);
                    startActivityForResult(RegisterActivity.class ,bundle,600);
                    setResult(RESULT_CODE_BAR_AD,null);//为前面跳转的Fragment有所交代 ,更改登陆值
                    finish();
                }else {

                    //--------------------此处为已经注册过
                    AppConfig.phone = usertext.getText().toString().trim();
//                    if (!HelpUtil.isMobileNO(phone)) {
//                        makeText("请输入正确的手机号码");
//                        return;
//                    }
                    Bundle bundle=new Bundle();
                    bundle.putString("phone" ,phone);
                    startActivityForResult(LoginAccountActivity.class ,bundle ,REQUEST_CODE_LOGIN);//此处跳转到登陆病不结束掉当前界面
//                    startActivity(LoginAccountActivity.class ,bundle);
                    setResult(RESULT_CODE_BAR_AD,null);//为前面跳转的Fragment有所交代
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            //注册的范湖吗
            case RESULT_CODE_LOGIN:
               intent=getIntent();
                if (intent!=null){
                    Bundle bundle= intent.getExtras();
                    if (bundle!=null){
                        AppConfig.phone= bundle.get("phone").toString().trim();
                        AppConfig.password=bundle.get("pwd").toString().trim();
                        AppConfig.isLogin=true;
                    }
                }
                break;
            //登陆结果的返回码
            case 900:
               intent=getIntent();
                if (intent!=null){
                    Bundle bundle= intent.getExtras();
                    if (bundle!=null){
                        AppConfig.phone= bundle.get("phone").toString().trim();
                        AppConfig.password=bundle.get("pwd").toString().trim();
                        AppConfig.isLogin=true;
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.left_btn:
//                finish();
//                break;
////            case R.id.logeinbtn:
////                phone = usertext.getText().toString().trim();
////                //-------------------------------
////                //----------------------------------此处需要判断账号是否存在,账号若bu存在,则需要去注册账号,若已经存在
////                return;
//        }
//    }
}

