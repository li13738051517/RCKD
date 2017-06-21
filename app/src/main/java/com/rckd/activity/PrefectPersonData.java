package com.rckd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.rckd.R;
import com.rckd.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.inflate;
import static com.baidu.location.h.j.r;
import static com.baidu.location.h.j.v;
import static com.rckd.R.id.button;
import static com.rckd.R.id.imageView2;
import static com.rckd.R.id.imageView3;

/**
 * Created by LiZheng on 2017/6/12 0012.
 */
//完善个人资料
public class PrefectPersonData extends BaseActivity implements View.OnClickListener {
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
    //----------------------
    @BindView(R.id.ll_image) LinearLayout ll_image;
    @BindView(R.id.imageView3) Button imageView3;
    @BindView(R.id.tv_camera) TextView tv_camera;

    //-----------
    @BindView(R.id.lin1) LinearLayout lin1;
    @BindView(R.id.text_tie) TextView text_tie;
    @BindView(R.id.text_ad) EditText text_ad;
    //-------------------
    @BindView(R.id.lin2) LinearLayout lin2;
    @BindView(R.id.text_tie2) TextView text_tie2;
    @BindView(R.id.text_ad2) TextView text_ad2;
    //------------------
    @BindView(R.id.text_tie3) TextView ttext_tie3;
    @BindView(R.id.text_ad3)TextView text_ad3;
    @BindView(R.id.lin3) LinearLayout lin3;
    //--------------------
    @BindView(R.id.lin4) LinearLayout lin4;
    @BindView(R.id.text_tie4) TextView text_tie4;
    @BindView(R.id.text_ad4) TextView text_ad4;
    //---------------
   @BindView(R.id.lin5) LinearLayout lin5;
   @BindView(R.id.text_tie5) TextView text_tie5;
   @BindView(R.id.text_ad5) EditText text_ad5;
    //---------------
    @BindView(R.id.textView) EditText textView;
    @BindView(R.id.tv) TextView tv;
    @BindView(R.id.frame_layout) FrameLayout frame_layout;
    @BindView(R.id.button) Button button;



    //----------------------------------------------
    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.right_btn) Button right;

    View view;

    boolean flag=false;//布局显示隐藏的开关

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistntState);
//    }

    Intent  intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_perfect);
        ButterKnife.bind(this);
        //------------------
        intent=getIntent();
        if (intent!=null){
            Bundle bundle= intent.getExtras();
            if (bundle!=null){
                int type=  bundle.getInt("type");
                if (type==0){
                    String  name  = bundle.getString("positionApplied");//期望职位
                    String  worktype  = bundle.getString("workType");//工作职位类别
                    String salary=   bundle.getString("salary");
                    String area=    bundle.getString("area");
                }
            }
        }
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title.setVisibility(View.VISIBLE);
        title.setText("完善个人资料");
        imageView3.setOnClickListener(this);
        tv.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_btn:
                finish();
                break;
            case R.id.tv:
                //--------------------------------添加各种视图,报矿
                view= LayoutInflater.from(PrefectPersonData.this).inflate(R.layout.person_data ,null);
                frame_layout.addView(view);
                break;

            //保存资料
            case R.id.button:
                //部分资料可由接口获得
                String personName= text_ad.getText().toString().trim();
                if(personName.isEmpty()|| personName==null){
                    makeText("姓名不能为空");
                    return;
                }
                String personSex=text_ad2.getText().toString();
                if (personSex.isEmpty()|| personSex==null){
                    makeText("性别有误");
                    return;
                }

                String birthday= text_ad3.getText().toString().trim();
                if (birthday.isEmpty()|| birthday==null){
                    return;
                }
                String  area=text_ad4.getText().toString().trim();

                String workYear= text_ad5.getText().toString().trim();
                if (workYear==null || workYear.isEmpty()){
                    makeText("工作年限不能为空");
                    return;
                }
                //------------------------------------------
                //当点击完善简历更多时,单独开个子线程页面
                startActivity(SeeMyPositionFullTimeActivity.class);
                finish();
                break;
        }
    }
}
