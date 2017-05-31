package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.base.BaseActivity;
import com.rckd.view.CustomPicker;

import java.util.Locale;
import java.util.zip.Inflater;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;

import static com.rckd.R.id.button;
import static com.rckd.R.id.left_btn;
import static com.rckd.R.id.title_text;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//房屋出售
public class SendBarHouseSaleActivity extends BaseActivity implements  View.OnClickListener{
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    Button left ,right;
    View view;
    TextView title;

    TextView text_ad;
    Button button;


    EditText text_ad2;
    String bar_title="";
    AppCompatEditText textView;
    String con="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_house  ,null);
        left =(Button) view.findViewById(R.id.in).findViewById(R.id.left_btn);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title=(TextView)   view.findViewById(R.id.in).findViewById(R.id.title_text);
        title.setVisibility(View.VISIBLE);

        right =(Button) view.findViewById(R.id.in).findViewById(R.id.right_btn);
        right.setVisibility(View.GONE);
        text_ad =(TextView) view.findViewById(R.id.text_ad);
        text_ad.setOnClickListener(this);
        view.findViewById(R.id.text_tie).setOnClickListener(this);
        view.findViewById(R.id.lin1).setOnClickListener(this);
        button=(Button) view.findViewById(R.id.button);
        text_ad2=(EditText) view.findViewById(R.id.text_ad2);
        bar_title=text_ad2.getText().toString().trim();



        textView=(AppCompatEditText) view.findViewById(R.id.textView);
        con=textView.getText().toString().trim();

        setContentView(view);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.left_btn:
                finish();
                break;
            case R.id.text_tie:
            case  R.id.text_ad:
            case R.id.lin1:
                onAnimator();
                break;
            case  R.id.button:
                if (bar_title.isEmpty() || bar_title==null){
                    makeText("帖子没有标题");
                    return;
                }
                if (con.isEmpty() || con ==null){
                    makeText("帖子没有内容");
                    return;
                }


                break;
        }

    }

    private void onAnimator() {
        String[]  string=    new String[]{"我要买房", "我要售房", "房屋出租", "求组房屋"};
        CustomPicker picker = new CustomPicker(this ,string);
        picker.setOffset(1);//显示的条目的偏移量，条数为（offset*2+1）
        picker.setGravity(Gravity.BOTTOM);//居底
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int position, String option) {
                makeText("index=" + position + ", item=" + option);
                text_ad.setText(option);
                textView.setText(option);
            }
        });
        picker.show();
    }



}



