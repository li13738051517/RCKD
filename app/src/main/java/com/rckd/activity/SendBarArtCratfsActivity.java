package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.location.h.j.B;
import static com.baidu.location.h.j.r;
import static com.baidu.location.h.j.t;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//匠工约定
public class SendBarArtCratfsActivity extends BaseActivity implements  View.OnClickListener {
    @BindView(R.id.left_btn) Button left;
    @BindView( R.id.title_text) TextView  title_text;
    @BindView(R.id.text_ad) TextView text_ad; //类别选择
    @BindView(R.id.text_ad2)  EditText text_ad2;
    @BindView(R.id.textView) AppCompatEditText textView;//

    @BindView(R.id.button) Button button;
    String title =null;
    String area =null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_art);
        ButterKnife.bind(this);
        title_text.setVisibility(View.VISIBLE);
        title=text_ad2.getText().toString().trim();
        area=textView.getText().toString().trim();

    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.left_btn:
                finish();
                break;

            case R.id.button:
                if (title.isEmpty() || title ==null){
                    makeText("帖子不能没有主题哦!!!");
                    return;
                }else if (area ==null || area.isEmpty() ){
                    makeText("帖子不能没有内容哦!!!");
                    return;
                }else {
                    makeText("恭喜 ,发帖成功!!!");

                }
                break;
        }

//        super.onClick(v);
    }
}
