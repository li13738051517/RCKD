package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rckd.R.id.text_ad3;
import static com.tencent.smtt.utils.b.b;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//临时短工
public class SendBarTempJobActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView  title;
    @BindView(R.id.right_btn) Button right;
    @BindView(R.id.text_ad) TextView text_ad;
    @BindView(R.id.text_tie) TextView text_tie;
    @BindView(R.id.lin1) LinearLayout lin1;

    @BindView(R.id.text_ad2) EditText  text_ad2;
    String bar_title="";
    @BindView(R.id.text_ad3) EditText text_ad3;
    String job="";
    @BindView(R.id.textView) AppCompatEditText  textView;
    String con="";

    @BindView(R.id.button) Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_temp_work);
        ButterKnife.bind(this);
        bar_title= text_ad2.getText().toString().trim();
        job=text_ad3.getText().toString().trim();
        con=textView.getText().toString().trim();

    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;


            case R.id.button:
                makeText("正在发帖中");
                if (bar_title.isEmpty() || bar_title==null){
                    makeText("标题不能为空");
                    return;
                }
                if (job.isEmpty() || job==null){
                    makeText("不能没有职业哦");
                    return;
                }
                if (con.isEmpty()|| con==null){
                    makeText("贴在不能没有内容哦");
                }
//请求发送 .post
                break;
        }
    }
}
