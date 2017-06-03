package com.rckd.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.rckd.R;
import com.rckd.adpter.GridAdapter;
import com.rckd.adpter.ImageAdapterPicasso;
import com.rckd.adpter.ImageAdapterTImage;
import com.rckd.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

//import static com.baidu.location.h.j.R;
//import static com.rckd.utils.TakePhotoUtils.configCompress;
//import static com.rckd.utils.TakePhotoUtils.configTakePhotoOption;

import com.rckd.utils.ScreenUtils;
import com.rckd.utils.TakePhotoUtils;
import com.rckd.view.PoupCamera;

import static com.rckd.R.id.gridview;
import static com.rckd.R.id.img;

/**
 * Created by LiZheng on 2017/5/8 0008.
 * https://github.com/crazycodeboy/TakePhoto 照相所需要使用到的第三方
 */
/*
广而告之
 */
public class BarAdActivity extends BaseActivity implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener{
    private static String tag = BarAdActivity.class.getName();
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    private boolean isShowDelete;
//    private List<BaseIcon> datas = new ArrayList<BaseIcon>();
    Button left_btn;
    TextView title_text;
    Button right_btn;
    View view;
    Button imageView3;//拍照发帖
    TextView text_tie; // 类别名称
    TextView text_ad;//
    TextView text_tie2;
    EditText text_ad2;
    Uri imageUri;//获取image的uri
    LinearLayout linearLayout;
    TakePhoto takePhoto;
    TextView textView;
    Button button;
    String test = "";//帖子标题
    String testArear = "";//发帖内容区域

    PoupCamera poupCamera;

    int screenWidth;
    int screenHeight;
    ArrayList<TImage> images;
//    private GridView gridView;
//    private GridAdapter gridAdapter;
//    private List<TImage> datas = new ArrayList<TImage>();

    GridView list_view;
    ImageAdapterTImage adapterTImage;
    ImageAdapterPicasso adapterPicasso;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getTakePhoto().onCreate(savedInstanceState);  //先让takephoto
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bar_ad);
        Timber.e(tag + " onCreate  start ");
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_bar_ad, null);
        // in  的 left title ,right
        left_btn = (Button) view.findViewById(R.id.in).findViewById(R.id.left_btn);
        left_btn.setVisibility(View.VISIBLE);
        left_btn.setOnClickListener(this);
        title_text = (TextView) view.findViewById(R.id.in).findViewById(R.id.title_text);
        title_text.setVisibility(View.VISIBLE);
        title_text.setText("便民广告");
        right_btn =(Button)view.findViewById(R.id.in).findViewById(R.id.right_btn);
        right_btn.setVisibility(View.GONE);
        //
        text_tie = (TextView) view.findViewById(R.id.text_tie);
        text_tie.setText("类别名称");
        text_ad = (TextView) view.findViewById(R.id.text_ad);
        text_ad.setText("广而告之");
        //类别选择  ,用Picker

        text_tie2 = (TextView) view.findViewById(R.id.text_tie2);
        text_tie2.setText("帖子标题");

        text_ad2 = (EditText) view.findViewById(R.id.text_ad2);

        imageView3 = (Button) view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);
        button=(Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);


        textView =(AppCompatEditText)view.findViewById(R.id.textView);
        //---------------------------
//        gridView = (GridView) view.findViewById(R.id.list_view); //初始化
//        gridView.setVisibility(View.GONE);//初始化默认不会显示

        //------------------
        linearLayout = (LinearLayout) view.findViewById(R.id.llImages);
//        linearLayout.setVisibility(View.GONE);
        list_view=(GridView) view.findViewById(R.id.list_view);

        // 显示 ,将布局中的内容显示
        setContentView(view);
        takePhoto=getTakePhoto();
//        DisplayMetrics dm = new DisplayMetrics();
//        //取得窗口属性
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        //窗口的宽度
//        screenWidth = dm.widthPixels;
//        //窗口高度
//         screenHeight = dm.heightPixels;
        screenWidth= ScreenUtils.getScreenWidth(this);
        screenHeight=ScreenUtils.getScreenHeight(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        getTakePhoto().onSaveInstanceState(outState);//防止app奔溃后收集信息
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }



    //解绑
    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
        Timber.e(tag + " onDestroy ", tag);
    }


//    CheckBox cb;
    TextView tv_camera;
    TextView tv_pic;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                finish();
//                defaultFinish();
                break;
            //点击拍照
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
                break;

            case R.id.button:
                test=text_ad2.getText().toString().trim();
                testArear=textView.getText().toString().trim();
                if (test.isEmpty() || test == null) {
                    makeText("温馨提示:帖子不能没有标题哦!");
                    return;
                }
                if (testArear.isEmpty() || testArear == null) {
                    makeText("温馨提示:帖子不能没有内容哦!");
                    return;
                }
                //---------------------post 请求  //url =?   &  &  &
                //此处图片集合images ,post请求全部带上
                break;
        }
    }




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

    private ArrayList<String> urls=new ArrayList<>();
//    int imgId[];
//    List<Map<String, Object>> listItems = new ArrayList<>();
//    HashMap<Integer,TImage> imgsObj=new HashMap<>();
    //照相成功
    @Override
    public void takeSuccess(TResult result) {
        //拍照操作成功成功后在此操作
        super.takeSuccess(result);
//        showImg(result.getImages());
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
                adapterTImage =new ImageAdapterTImage(BarAdActivity.this ,urls);
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



    //此处仅为演示效果
    private void showImg() {
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llImages);
//        View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
//        ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
//        ImageView imageView2 = (ImageView) view.findViewById(R.id.imgShow2);
//        ImageView imageView3 = (ImageView) view.findViewById(R.id.imgShow3);
//       imageView1.setLayoutParams(la);  //设置宽高
        for (int i = 0, j = images.size(); i < j - 1; i += 2) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
            ImageView imageView2 = (ImageView) view.findViewById(R.id.imgShow2);
            Glide.with(this).load(new File(images.get(i).getCompressPath())).into(imageView1);
            Glide.with(this).load(new File(images.get(i + 1).getCompressPath())).into(imageView2);
            linearLayout.addView(view);
        }
        if (images.size() % 2 == 1) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_show, null);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.imgShow1);
            Glide.with(this).load(new File(images.get(images.size() - 1).getCompressPath())).into(imageView1);
            linearLayout.addView(view);
        }



//        gridAdapter = new GridAdapterTImage(this, images);
//        gridView.setAdapter(gridAdapter);
//        gridView.setVisibility(View.VISIBLE);//初始化时,让其不可见,只有当添加图片上传成功后,可见
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //判断点击的是否是最后一个   ,如果是最后一个的话  ,需要 添加数据
//                Timber.e(tag+" position = " position +);
//                if (position == parent.getChildCount() - 1) {
////                    addDatas();
//                    makeText("你需要添加图片进来!!!");
//                }
//            }
//        });
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if (position < images.size()) {
//                    if (isShowDelete) {//删除图片显示时长按隐藏
//                        isShowDelete = false;
//                        gridAdapter.setIsShowDelete(isShowDelete);
//                    } else {//删除图片隐藏式长按显示
//                        isShowDelete = true;
//                        gridAdapter.setIsShowDelete(isShowDelete);
//                    }
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.isChecked()) {
            Toast.makeText(this, compoundButton.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //    private void addDatas() {
//        BaseIcon animalAdd = new BaseIcon("", R.drawable.ad);
//        datas.add(animalAdd);
//        gridAdapter.notifyDataSetChanged();
//    }

//    private void initDatas() {
//
//        BaseIcon animal0 = new BaseIcon("兔八哥", R.drawable.ad);
//        BaseIcon animal1 = new BaseIcon("眼镜蛇", R.drawable.ad);
//        BaseIcon animal2 = new BaseIcon("小金鱼", R.drawable.ad);
//        BaseIcon animal3 = new BaseIcon("千里马", R.drawable.ad);
//        BaseIcon animal4 = new BaseIcon("米老鼠", R.drawable.ad);
//        BaseIcon animal5 = new BaseIcon("大国宝", R.drawable.ad);
//        datas.add(animal0);
//        datas.add(animal1);
//        datas.add(animal2);
//        datas.add(animal3);
//        datas.add(animal4);
//        datas.add(animal5);
//    }

}
