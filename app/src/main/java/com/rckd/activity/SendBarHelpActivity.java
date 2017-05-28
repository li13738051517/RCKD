package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.adpter.GridAdapter;
import com.rckd.base.BaseActivity;
import com.rckd.bean.BaseIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiZheng on 2017/5/8 0008.
 * //打听求助
 */

public class SendBarHelpActivity extends BaseActivity implements  View.OnClickListener {
    String test = null;
    String testArear = null;

    private GridView gridView;
    private GridAdapter gridAdapter;
    private boolean isShowDelete;
    private List<BaseIcon> datas = new ArrayList<BaseIcon>();
    Button left_btn;
    TextView title_text;
    Button right_btn;

    View view;
    ImageView imageView3;//拍照发帖
    TextView text_tie; // 类别名称
    TextView text_ad;//
    TextView text_tie2;
    EditText text_ad2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_help_me, null);
        left_btn = (Button) view.findViewById(R.id.in).findViewById(R.id.left_btn);
        left_btn.setVisibility(View.VISIBLE);
        left_btn.setOnClickListener(this);
        title_text = (TextView) view.findViewById(R.id.in).findViewById(R.id.title_text);
        title_text.setVisibility(View.VISIBLE);
        title_text.setText("便民广告");
        text_tie = (TextView) view.findViewById(R.id.text_tie);
        text_tie.setText("类别名称");
        text_ad = (TextView) view.findViewById(R.id.text_ad);
        text_ad.setText("广而告之");

        text_tie2 = (TextView) view.findViewById(R.id.text_tie2);
        text_tie2.setText("帖子标题");

        text_ad2 = (EditText) view.findViewById(R.id.text_ad2);

        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);
//        test = text_ad2.getText().toString();
//        testArear = textView.getText().toString();
        gridView = (GridView) view.findViewById(R.id.list_view);
//        initDatas(); //实际上是由  上传图片后将图片加载到GridView中 ,在此之前  需要判断时候获取读取sd卡 ,或者  网络等相关权限
        gridAdapter = new GridAdapter(this, datas);
        gridView.setAdapter(gridAdapter);
        gridView.setVisibility(View.GONE);//初始化时,让其不可见,只有当添加图片上传成功后,可见
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断点击的是否是最后一个   ,如果是最后一个的话  ,需要 添加数据
                if (position == parent.getChildCount() - 1) {
//                    addDatas();
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < datas.size()) {
                    if (isShowDelete) {//删除图片显示时长按隐藏
                        isShowDelete = false;
                        gridAdapter.setIsShowDelete(isShowDelete);
                    } else {//删除图片隐藏式长按显示
                        isShowDelete = true;
                        gridAdapter.setIsShowDelete(isShowDelete);
                    }
                }
                return false;
            }
        });
        // 显示 ,将布局中的内容显示
        setContentView(view);
//        setContentView(R.layout.test_for_sign);
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (view.getId()) {
            case R.id.left_btn:
                finish();
//                defaultFinish();
                break;
            case R.id.imageView3:

            case R.id.button:
                if (test.isEmpty() || test == null) {
                    makeText("温馨提示:帖子不能没有标题哦!");
                } else if (testArear.isEmpty() || testArear == null) {
                    makeText("温馨提示:帖子不能没有内容哦!");
                } else {
                    makeText("发帖成功!");
                    finish();
                }

                break;
        }
    }
}
