package com.rckd.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.rckd.R;
import com.rckd.adpter.GridAdapter;
import com.rckd.base.BaseActivity;
import com.rckd.bean.BaseIcon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

//import static com.baidu.location.h.j.R;
import static com.rckd.R.id.image;
import static com.rckd.R.id.left_btn;
import static com.rckd.R.id.textView;
import static com.rckd.utils.TakePhotoUtils.configCompress;
import static com.rckd.utils.TakePhotoUtils.configTakePhotoOption;

import com.rckd.R;
import com.rckd.utils.TakePhotoUtils;

/**
 * Created by LiZheng on 2017/5/8 0008.
 * https://github.com/crazycodeboy/TakePhoto 照相所需要使用到的第三方
 */
/*
广而告之
 */
public class SendBarAdActivity extends BaseActivity implements View.OnClickListener {
    private static String tag = SendBarAdActivity.class.getName();
    //    View view;
    //头部title
//    @BindView(left_btn)
//    Button button;
//    @BindView(R.id.title_text)
//    TextView title;
//    @BindView(R.id.text_ad)
//    TextView text_ad;
//    @BindView(R.id.text_ad2)
//    EditText text_ad2;
//    @BindView(R.id.textView)
//    EditText textView;
//    @BindView(R.id.imageView2)
//    TextView imageView2;
//    @BindView(R.id.button)
//    Button button2;
//    private Unbinder unbinder;

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
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
    Button imageView3;//拍照发帖
    TextView text_tie; // 类别名称
    TextView text_ad;//
    TextView text_tie2;
    EditText text_ad2;
    Uri imageUri;//获取image的uri
    LinearLayout linearLayout;
    TakePhoto takePhoto;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getTakePhoto().onCreate(savedInstanceState);  //先让takephoto
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sendbarad);
        Timber.e(tag + " onCreate  start ");
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_sendbarad, null);
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

        imageView3 = (Button) view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);
//        test = text_ad2.getText().toString();
//        testArear = textView.getText().toString();
        gridView = (GridView) view.findViewById(R.id.list_view);
//        initDatas(); //
        gridAdapter = new GridAdapter(this, datas);
        gridView.setAdapter(gridAdapter);
        gridView.setVisibility(View.GONE);//初始化时,让其不可见,只有当添加图片上传成功后,可见
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断点击的是否是最后一个   ,如果是最后一个的话  ,需要 添加数据
                if (position == parent.getChildCount() - 1) {
                    addDatas();
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
        linearLayout = (LinearLayout) view.findViewById(R.id.llImages);
        // 显示 ,将布局中的内容显示
        setContentView(view);
        takePhoto=getTakePhoto();
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

    private void addDatas() {
        BaseIcon animalAdd = new BaseIcon("", R.drawable.ad);
        datas.add(animalAdd);
        gridAdapter.notifyDataSetChanged();
    }

//    private void initDatas() {
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


    //解绑
    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
        Timber.e(tag + " onDestroy ", tag);
    }

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

              TakePhotoUtils.configCompress(takePhoto ,10*1024  ,800 ,800);
              TakePhotoUtils.configTakePhotoOption(takePhoto);
//                TakePhotoUtils.takePhotos(takePhoto);
                TakePhotoUtils. takePhotosPickFromCaptureWithCrop(takePhoto);

//                /**
//                 * 使用takephoto
//                 * @param takePhoto
//                 * @param pickByTakeCamear 是否使用照相机
//                 * @param crop 是否裁剪
//                 * @param limit 数量限制
//                 * @param maxSize   大小
//                 * @param width 宽
//                 * @param height 高
//                 * @param cropOwn  使用系统自带工具
//                 * @param aspect  是否按 宽/高
//                 * @param pickWithOwn 是否使用takephoto自带相册
//                 * @param correctYes 是否使用纠正 拍照图片旋转角度
//                 * @param       compressWithOwn  压缩工具
//                 */
                //带哦用拍照功能
                Timber.e(tag+" //带哦用拍照功能  " ,tag);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Timber.e(tag +" run  on ----------" );
//                        TakePhotoUtils.takePhotos(getTakePhoto() ,true ,true ,5,true ,100*1024,800,800,true,false,true,false,false ,true ,true);
//                    }
//                });

                break;

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




    //------------------------------------------------------------
//    //以下是拍照相关内容
//    private InvokeParam invokeParam;
//    private TakePhoto takePhoto;
//    @Override
//    public void takeSuccess(TResult result) {
//        Log.i(tag,"takeSuccess：" + result.getImage().getCompressPath());
//    }
//    @Override
//    public void takeFail(TResult result,String msg) {
//        Log.i(tag, "takeFail:" + msg);
//    }
//    @Override
//    public void takeCancel() {
//        Log.i(tag, getResources().getString(R.string.msg_operation_canceled));
//    }
//    @Override
//    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
//        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
//        if(PermissionManager.TPermissionType.WAIT.equals(type)){
//            this.invokeParam=invokeParam;
//        }
//        return type;
//    }
//
//    /**
//     *  获取TakePhoto实例
//     * @return
//     */
//    public TakePhoto getTakePhoto(){
//        if (takePhoto==null){
//            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
//        }
//        return takePhoto;
//    }
////权限问题,takephoto自行解决,无需关心内部实现问题
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        com.jph.takephoto.permission.PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
//    }


    //-----------------------------------------------------------


    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    ArrayList<TImage> images;
    @Override
    public void takeSuccess(TResult result) {
        //拍照操作成功成功后在此操作
        super.takeSuccess(result);
//        showImg(result.getImages());
        images= result.getImages();
       SendBarAdActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showImg();
            }
        });

    }


    //此处仅为演示效果
    private void showImg() {
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llImages);
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
    }


}
