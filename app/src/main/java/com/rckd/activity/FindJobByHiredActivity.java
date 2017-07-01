package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;
import com.rckd.view.GridViewNoScroll;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LiZheng on 2017/6/30 0030.
 */
//让人聘我选择工作意向界面
public class FindJobByHiredActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_btn)
    Button leftBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_btn)
    Button rightBtn;
    @BindView(R.id.imageView3)
    Button imageView3;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.ed1)
    EditText ed1;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.text6)
    TextView text6;
    @BindView(R.id.ed6)
    TextView ed6;
    @BindView(R.id.lin6)
    LinearLayout lin6;
    @BindView(R.id.text7)
    TextView text7;
    @BindView(R.id.ed7)
    TextView ed7;
    @BindView(R.id.lin7)
    LinearLayout lin7;
    @BindView(R.id.text9)
    TextView text9;
    @BindView(R.id.ed9)
    TextView ed9;
    @BindView(R.id.lin9)
    LinearLayout lin9;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.ed5)
    TextView ed5;
    @BindView(R.id.lin5)
    LinearLayout lin5;
    @BindView(R.id.list_view)
    GridViewNoScroll listView;
    @BindView(R.id.textView)
    AppCompatEditText textView;
    @BindView(R.id.button)
    Button button;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_job_liepin);
        ButterKnife.bind(this);
        leftBtn.setVisibility(View.VISIBLE);
        leftBtn.setOnClickListener(this);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("工作意向");
        rightBtn.setVisibility(View.GONE);
        button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;

            case R.id.button:
                //---------------------
                //--------------------提交相关信息
                String postion=    ed1.getText().toString().trim();
                if (TextUtils.isEmpty(postion)){
                    makeText("意向职位不能没有哦");
                    return;
                }
                //----------------
                startActivity(PrefectPersonData.class);
                finish();

                break;
            default:
                break;


        }
    }

//    @OnClick({R.id.left_btn, R.id.imageView3, R.id.button})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.left_btn:
//                break;
//            case R.id.imageView3:
//                break;
//            case R.id.button:
//                break;
//        }
//    }
}
