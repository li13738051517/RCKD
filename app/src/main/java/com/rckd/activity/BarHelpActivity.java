package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.rckd.R;
import com.rckd.adpter.GridAdapter;
import com.rckd.adpter.ImageAdapterTImage;
import com.rckd.base.BaseActivity;
import com.rckd.bean.BaseIcon;
import com.rckd.utils.ScreenUtils;
import com.rckd.utils.TakePhotoUtils;
import com.rckd.view.PoupCamera;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.baidu.location.h.j.B;
import static com.baidu.location.h.j.v;
import static com.rckd.R.id.list_view;
import static com.rckd.R.id.tv_camera;

/**
 * Created by LiZheng on 2017/5/8 0008.
 * //打听求助
 */
public class BarHelpActivity extends BaseActivity implements  View.OnClickListener {
    private  static String tag=BarHelpActivity.class.getName();

    //-----------
    Button left_btn;
    TextView title_text;
    Button right_btn;
    //------------

    View view;
    ImageView imageView3;//拍照发帖
    TextView text_tie; // 类别名称
    TextView text_ad;//
    TextView text_tie2;
    EditText text_ad2;
    //
    TextView textView;
    String test = "";
    String testArear = "";

    GridView list_view;
//    private GridAdapter gridAdapter;
//    private boolean isShowDelete;
//  11  private List<BaseIcon> datas = new ArrayList<BaseIcon>();

    Button button;
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
    private ArrayList<String> urls=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_help_me, null);

        //-------------
        left_btn = (Button) view.findViewById(R.id.in).findViewById(R.id.left_btn);
        left_btn.setVisibility(View.VISIBLE);
        left_btn.setOnClickListener(this);
        title_text = (TextView) view.findViewById(R.id.in).findViewById(R.id.title_text);
        title_text.setVisibility(View.VISIBLE);
        title_text.setText("打听求助");
        right_btn=(Button)view.findViewById(R.id.in).findViewById(R.id.right_btn);
        right_btn.setVisibility(View.GONE);

        //-----------------
        text_tie = (TextView) view.findViewById(R.id.text_tie);
        text_tie.setText("类别名称");
        text_ad = (TextView) view.findViewById(R.id.text_ad);
        text_ad.setText("打听求助");

        text_tie2 = (TextView) view.findViewById(R.id.text_tie2);
        text_tie2.setText("帖子标题");
        text_ad2 = (EditText) view.findViewById(R.id.text_ad2);

        //-------拍照

        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);
//        test = text_ad2.getText().toString();
//        testArear = textView.getText().toString();
        //拍照之后加载的视图
        list_view = (GridView) view.findViewById(R.id.list_view);
//        initDatas(); //实际上是由  上传图片后将图片加载到GridView中 ,在此之前  需要判断时候获取读取sd卡 ,或者  网络等相关权限
//        gridAdapter = new GridAdapter(this, datas);
//        gridView.setAdapter(gridAdapter);
//        gridView.setVisibility(View.GONE);//初始化时,让其不可见,只有当添加图片上传成功后,可见
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //判断点击的是否是最后一个   ,如果是最后一个的话  ,需要 添加数据
//                if (position == parent.getChildCount() - 1) {
////                    addDatas();
//                }
//            }
//        });
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if (position < datas.size()) {
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

        textView =(AppCompatEditText)view.findViewById(R.id.textView);
        button=(Button) view.findViewById(R.id.button);
        button.setOnClickListener(this);
        imageView3 = (ImageView) view.findViewById(R.id.imageView3);
        imageView3.setOnClickListener(this);

        // 显示 ,将布局中的内容显示
        setContentView(view);
//        setContentView(R.layout.test_for_sign);

        //以下部分在布局视图加载之后再去在家,一面占用资源
        takePhoto=getTakePhoto();
        screenWidth= ScreenUtils.getScreenWidth(this);
        screenHeight=ScreenUtils.getScreenHeight(this);
    }

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
//                defaultFinish();
                break;
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

            case R.id.button:
                test=text_ad2.getText().toString().trim();
                testArear =textView.getText().toString().trim();

                if (test.isEmpty() || test == null) {
                    makeText("温馨提示:帖子不能没有标题哦!");
                    return;
                }
                if (testArear.isEmpty() || testArear == null) {
                    makeText("温馨提示:帖子不能没有内容哦!");
                    return;
                }
                makeText("发帖成功!");
                break;
        }
    }


    //------------------------拍照

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
                adapterTImage =new ImageAdapterTImage(BarHelpActivity.this ,urls);
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

}
