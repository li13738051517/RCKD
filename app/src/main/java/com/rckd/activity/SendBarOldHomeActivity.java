package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//二手之家
public class SendBarOldHomeActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
    @BindView(R.id.left_btn)
    Button left;
    @BindView(R.id.title_text)
    TextView tite_text;
    @BindView(R.id.text_ad)
    TextView text_ad;
    @BindView(R.id.text_tie)
    TextView text_tie;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.lin1)
    LinearLayout lin1;

    @BindView(R.id.text_ad2) EditText text_ad2;
    String bar_title="";
    @BindView(R.id.textView) AppCompatEditText textView;
    String con="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_home);
        ButterKnife.bind(this);
        bar_title=text_ad2.getText().toString().trim();
        con=textView.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {

        switch ( v.getId())
        {

            case R.id.left_btn:
                finish();
                break;

            case  R.id.text_ad:
            case R.id.text_tie:
            case R.id.lin1:
                makeText("点击类别,正在匹配");

                break;
            case R.id.button:
                if (bar_title==null || bar_title.isEmpty()){
                    makeText("帖子不能没有标题哦");
                    return;
                }

                if (con== null || con.isEmpty()){
                    makeText("帖子不能没有内容");
                    return;
                }
                //post 请求
                makeText("发帖成功");
                finish();
                break;
        }

    }
}
