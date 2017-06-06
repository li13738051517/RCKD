package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.rckd.R;
import com.rckd.adpter.ImageAdapterTImage;
import com.rckd.base.BaseActivity;
import com.rckd.utils.ScreenUtils;
import com.rckd.utils.TakePhotoUtils;
import com.rckd.view.CustomPicker;
import com.rckd.view.PoupCamera;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import timber.log.Timber;

import static com.rckd.R.id.button;
import static com.rckd.R.id.left_btn;
import static com.rckd.R.id.lin1;
import static com.rckd.R.id.title_text;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//房屋出售
public class BarHouseSaleActivity extends BaseActivity implements  View.OnClickListener{

    private static  final String tag=BarHouseSaleActivity.class.getName();
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    //------------------------------
    View view;
    Button left_bt ,right_bt;
    TextView title;

    //------------------------
    TextView text_ad;
    TextView text_tie;
    LinearLayout lin1;

    //----------
    Button button;
    Button photo;
    GridView list_view;

    EditText text_ad2;
    String bar_title="";
    AppCompatEditText textView;
    String con="";

    String[]  opt =    new String[]{"我要买房", "我要售房", "房屋出租", "求租房屋"};


    //------------------
    //----takephto
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_house  ,null);
        //-----------------------
        left_bt =(Button) view.findViewById(R.id.in).findViewById(R.id.left_btn);
        left_bt.setVisibility(View.VISIBLE);
        left_bt.setOnClickListener(this);
        title=(TextView)   view.findViewById(R.id.in).findViewById(R.id.title_text);
        title.setVisibility(View.VISIBLE);
        title.setText("房屋出售");
        right_bt =(Button) view.findViewById(R.id.in).findViewById(R.id.right_btn);
        right_bt.setVisibility(View.GONE);
        //--------------------
        text_tie=(TextView) view.findViewById(R.id.text_tie);
        text_tie.setOnClickListener(this);
        text_ad =(TextView) view.findViewById(R.id.text_ad);
        text_ad.setOnClickListener(this);
        lin1=(LinearLayout)view.findViewById(R.id.lin1);
        lin1.setOnClickListener(this);

        //-------------

        button=(Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);
        photo=(Button)view.findViewById(R.id.imageView3);
        photo.setOnClickListener(this);


        list_view=(GridView) view.findViewById(R.id.list_view);


        //-----------------
        text_ad2=(EditText) view.findViewById(R.id.text_ad2);
        textView=(AppCompatEditText) view.findViewById(R.id.textView);
        setContentView(view);


        //-----------
        //------------拍照对象
        takePhoto=getTakePhoto();
        screenWidth= ScreenUtils.getScreenWidth(this);
        screenHeight=ScreenUtils.getScreenHeight(this);

        //-----------
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView3:
                makeText("即将拍照....");
                Timber.e(tag+" 即将拍照  " ,tag);
                poupCamera=new PoupCamera(this);
    //                cb=(CheckBox) poupCamera.getView().findViewById(R.id.checkBox);
    //                cb.setOnCheckedChangeListener(this);
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


            case  R.id.left_btn:
                finish();
                break;
            //--------------
            case R.id.text_tie:
            case  R.id.text_ad:
            case R.id.lin1:
                onAnimator(R.id.lin1,opt);
                break;
            //--------------
            case  R.id.button:
                bar_title=text_ad2.getText().toString().trim();
                con=textView.getText().toString().trim();
                if (bar_title.isEmpty() || bar_title==null){
                    makeText("帖子没有标题");
                    return;
                }
                if (con.isEmpty() || con ==null){
                    makeText("帖子没有内容");
                    return;
                }
                makeText("发帖成功!!!");

                break;
        }

    }

    private void onAnimator(final  int  id  , final String [] strings) {
        CustomPicker picker = new CustomPicker(this ,strings);
        picker.setOffset(1);//显示的条目的偏移量，条数为（offset*2+1）
        picker.setGravity(Gravity.BOTTOM);//居底
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int position, final String option) {
                makeText("index=" + position + ", item=" + option);
                //具体的结果在这里处理
                if (id==R.id.lin1 || id== R.id.text_tie||  id==R.id.text_ad  ){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_ad.setText(option);
                        }
                    });
                }
            }
        });
        picker.show();
    }



    //------------------------
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

                adapterTImage =new ImageAdapterTImage(BarHouseSaleActivity.this ,urls);
                Log.e(tag,"adapterTImage");

                list_view.setAdapter(adapterTImage);
                Log.e(tag,"setAdapter");
                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        makeText("你点击了 postion==" +position);
                    }
                });

                poupCamera.dismiss();
            }
        });
    }

}



