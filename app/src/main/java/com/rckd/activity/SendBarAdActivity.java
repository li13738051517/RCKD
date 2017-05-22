package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rckd.R.id.left_btn;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */

public class SendBarAdActivity extends BaseActivity implements View.OnClickListener {
    private static String tag = SendBarAdActivity.class.getName();
//    View view;
    //头部title
    @BindView(left_btn)
    Button button;
    @BindView(R.id.title_text)
    TextView title;
    @BindView(R.id.text_ad)
    TextView text_ad;
    @BindView(R.id.text_ad2)
    EditText text_ad2;
    @BindView(R.id.textView)
    EditText textView;
    @BindView(R.id.imageView2)
    TextView imageView2;
    @BindView(R.id.button)
    Button button2;
//    private Unbinder unbinder;


    String test = null;
    String testArear = null;


    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendbarad);
        ButterKnife.bind(this);
        title.setText("便民广告");
        test = text_ad2.getText().toString();
        testArear = textView.getText().toString();

    }


    //解绑
    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.button:
                if (test.isEmpty() || test == null) {
                    makeText("温馨提示:帖子不能没有标题哦!");
                } else if (testArear.isEmpty() || testArear == null) {
                    makeText("温馨提示:帖子不能没有内容哦!");
                } else {
                    makeText("发帖成功!");
                }
                break;
        }
    }
}
