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
 * Created by LiZheng on 2017/6/29 0029.
 */
//人才入库
public class TalentPersonActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

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
    @BindView(R.id.textView)
    AppCompatEditText textView;

    @BindView(R.id.left_btn)
    Button left;
    @BindView(R.id.title_text)
    TextView title;
    @BindView(R.id.right_btn)
    Button right;

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_job_liepin);
        ButterKnife.bind(this);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("工作意向");
        right.setVisibility(View.GONE);
        //        right.setOnClickListener(null);
        button.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        //使用外部类和接口方式 是为了 让共有部分实现相同的逻辑
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.button:
                //--------------------------
                //----------------------------请求之后 ,结束掉当前   Activity
                startActivity(PrefectPersonData.class);
                finish();
                break;
        }
    }

}
