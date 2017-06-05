package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.rckd.R;
import com.rckd.adpter.ImageAdapterTImage;
import com.rckd.base.BaseActivity;
import com.rckd.utils.ScreenUtils;
import com.rckd.utils.TakePhotoUtils;
import com.rckd.view.PoupCamera;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import timber.log.Timber;

import static com.rckd.R.drawable.photo;
import static com.rckd.R.id.list_view;
import static com.rckd.R.id.text_ad3;
import static com.rckd.R.id.title_text;
import static com.tencent.smtt.utils.b.b;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//临时短工
public class BarTempJobActivity extends BaseActivity implements View.OnClickListener {
    private static  final  String tag=BarTempJobActivity.class.getName();
    @BindView(R.id.left_btn) Button left;
    @BindView(R.id.title_text) TextView   title_text;
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
    @BindView(R.id.imageView3) Button photo;

    @BindView(R.id.list_view) GridView list_view;//拍照结果显示列表

    //---------------

    PoupCamera poupCamera;
    TextView tv_camera;
    TextView tv_pic;
    int screenWidth;
    int screenHeight;
    TakePhoto takePhoto;
    //----------
    ImageAdapterTImage adapterTImage;
    ArrayList<TImage> images;
    ArrayList<String> urls=new ArrayList<>();

    //类别选择器
    String[]  strOpt=   new String[]{"我是短工", "我找短工"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_work);
        ButterKnife.bind(this);

        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        title_text.setVisibility(View.VISIBLE);
        right.setVisibility(View.GONE);
        title_text.setText("临时短工");
        button.setOnClickListener(this);
        photo.setOnClickListener(this);


        //------------拍照对象
        takePhoto=getTakePhoto();
        screenWidth= ScreenUtils.getScreenWidth(this);
        screenHeight=ScreenUtils.getScreenHeight(this);


        //-----------
        text_tie.setOnClickListener(this);
        text_ad.setOnClickListener(this);
        lin1.setOnClickListener(this);


    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case   R.id.text_tie:
            case   R.id.text_ad:
            case   R.id.lin1:
//                makeText("正在加载数据,请稍后!!!");
                Timber.e(tag+" opt ",tag);
                onConstellationPicker(R.id.lin1,strOpt);
                break;


            case R.id.imageView3:
                makeText("即将拍照....");
                Timber.e(tag+" 即将拍照  " ,tag);
                poupCamera=new PoupCamera(this);
                tv_camera=(TextView)  poupCamera.getView().findViewById(R.id.tv_camera); //从相机
                tv_pic=(TextView) poupCamera.getView().findViewById(R.id.tv_camera_ku); //从 图库
                tv_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //相机
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Timber.e(tag+"  tv_camera  " ,tag);
                                TakePhotoUtils.configCompress(takePhoto ,true ,true ,10*1024 ,screenWidth ,screenHeight ,true ,true);
                                TakePhotoUtils.configTakePhotoOption(takePhoto ,true ,false);
                                TakePhotoUtils.takePhotosAll(takePhoto ,true ,true ,0 ,true,false ,screenWidth ,screenHeight,true);
                            }
                        });

                    }
                });
                tv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Timber.e(tag+"   tv_pic  " ,tag);
                                //相册,-------------Bug
                                TakePhotoUtils.configCompress(takePhoto ,true ,true ,10*1024 ,screenWidth ,screenHeight ,true ,true);
                                TakePhotoUtils.configTakePhotoOption(takePhoto ,true ,false);
                                TakePhotoUtils.takePhotosAll(takePhoto ,false ,true ,5 ,true,false ,screenWidth ,screenHeight ,true);
                            }
                        });
                    }
                });
                poupCamera.showPopupWindow();
                //这句话很重要,这就好比你创建了对象,但是却没有明显的使用它一样
                break;

            case R.id.left_btn:
                finish();
                break;


            case R.id.button:
//                makeText("正在发帖中");
                bar_title= text_ad2.getText().toString().trim();
                job=text_ad3.getText().toString().trim();
                con=textView.getText().toString().trim();
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
                    return;
                }
//请求发送 .post
                makeText("发帖成功");
                break;
        }
    }


    //------------------------------------takephoto



    //-----------------使用拍照的相关结果  ----------------
    //权限问题前面已经解决好了,你无需在做处理
    //取消照相
    @Override
    public void takeCancel() {
        super.takeCancel();
        poupCamera.dismiss();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        poupCamera.dismiss();
    }

    //照相成功
    @Override
    public void takeSuccess(TResult result) {
        //拍照操作成功成功后在此操作
        super.takeSuccess(result);
        images= result.getImages(); //获取图片的数组集合
        if(images.size()==0){
            return;
        }
        for (int i=0;i<images.size();i++){
            urls.add(images.get(i).getCompressPath());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                showImg();
//                adapterPicasso=new ImageAdapterPicasso(BarAdActivity.this, urls);
                adapterTImage =new ImageAdapterTImage(mContext ,urls);
                Log.e(tag,"adapterTImage");
//                list_view.setAdapter(adapterPicasso);
                list_view.setAdapter(adapterTImage);
                Log.e(tag,"setAdapter");
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        makeText("你点击了 postion==" +position);
                    }
                });
//                linearLayout.setVisibility(View.GONE);
                poupCamera.dismiss();
            }
        });
    }


    //---------------------------------



    //--------------------------option
    /**
     * 传入View ,传入需要的对象
     * @param strings
     */
    public void onConstellationPicker(final  int  id  , final String [] strings) {
        SinglePicker<String> picker = new SinglePicker<>(this,strings);
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText("请选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(13);
        picker.setSelectedTextColor(0xFFEE0000);
        picker.setUnSelectedTextColor(0xFF999999);
        LineConfig config = new LineConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(180);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(0);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, final String item) {
                makeText("index=" + index +"\n"+" item=" + item);
                if (id==R.id.lin1 || id== R.id.text_tie||  id==R.id.text_ad  ){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_ad.setText(item);
                        }
                    });
                }
            }
        });
        picker.show();
    }

}