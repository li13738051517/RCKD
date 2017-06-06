package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.h.j;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.litesuits.common.io.StringCodingUtils;
import com.rckd.R;
import com.rckd.adpter.ImageAdapterTImage;
import com.rckd.base.BaseActivity;
import com.rckd.utils.ScreenUtils;
import com.rckd.utils.TakePhotoUtils;
import com.rckd.view.PoupCamera;

import java.security.PublicKey;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.baidu.location.h.j.B;
import static com.rckd.R.id.ed1;
import static com.rckd.R.id.left_btn;
import static com.rckd.R.id.right_btn;
import static com.rckd.R.id.textView;
import static com.rckd.R.id.text_ad;
import static com.rckd.R.id.title_text;
import static com.rckd.R.id.tv1;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
//发简历找工作
public class BarPasteJobActivity extends BaseActivity implements View.OnClickListener{
    private  static  final String tag=BarPasteJobActivity.class.getName();
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }
//
//    String test = null;
//    String testArear = null;
//
//    private GridAdapter gridAdapter;
//    private boolean isShowDelete;
//    private List<BaseIcon> datas = new ArrayList<BaseIcon>();
//    View view;
//    ImageView imageView3;//拍照发帖
//    TextView text_tie; // 类别名称
//    TextView text_ad;//
//    TextView text_tie2;
//    EditText text_ad2;
//    Uri imageUri;//获取image的uri

    //------------------take photo
    //---------------

    PoupCamera poupCamera;
    TextView tv_camera;
    TextView tv_pic;
    //-----------屏幕
    int screenWidth;
    int screenHeight;
    TakePhoto takePhoto;
    //----------
    ImageAdapterTImage adapterTImage;
    ArrayList<TImage> images;
    ArrayList<String> urls=new ArrayList<>();
    //类别选择器
    String[]  strOpt=  new String[]{"寻找女友", "寻找男友"};
    //---------------
    @BindView(R.id.text1) TextView tv1;
    @BindView(R.id.text2) TextView tv2;
    @Nullable@BindView(R.id.frame) FrameLayout frameLayout;
    View view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paster_jobselect);
        ButterKnife.bind(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        frameLayout.setVisibility(View.GONE);
//        //-----------------
        //----------
        // ------------拍照对象
        takePhoto=getTakePhoto();
        screenWidth= ScreenUtils.getScreenWidth(this);
        screenHeight=ScreenUtils.getScreenHeight(this);
        //-----------
//        gridView = (GridView) findViewById(R.id.list_view);
//        initDatas(); //实际上是由  上传图片后将图片加载到GridView中 ,在此之前  需要判断时候获取读取sd卡 ,或者  网络等相关权限
//        gridAdapter = new GridAdapter(this, datas);
//        gridView.setAdapter(gridAdapter);
//        gridView.setVisibility(View.GONE);//初始化时,让其不可见,只有当添加图片上传成功后,可见
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //判断点击的是否是最后一个   ,如果是最后一个的话  ,需要 添加数据
//                if (position == parent.getChildCount() - 1) {
//                    addDatas();
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

        //绑定一个匿名监听器

    }




//    private void addDatas() {
//        BaseIcon animalAdd = new BaseIcon( R.drawable.ad ,"大国宝");
//        datas.add(animalAdd);
//        gridAdapter.notifyDataSetChanged();
//    }
//
//    private void initDatas() {
//        BaseIcon animal0 = new BaseIcon(R.drawable.ad ,"兔八哥") ;
//        BaseIcon animal1 = new BaseIcon(R.drawable.ad ,"眼镜蛇" );
//        BaseIcon animal2 = new BaseIcon( R.drawable.ad,"小金鱼");
//        BaseIcon animal3 = new BaseIcon( R.drawable.ad, "千里马");
//        BaseIcon animal4 = new BaseIcon(R.drawable.ad, "米老鼠" );
//        BaseIcon animal5 = new BaseIcon( R.drawable.ad ,"大国宝");
//        datas.add(animal0);
//        datas.add(animal1);
//        datas.add(animal2);
//        datas.add(animal3);
//        datas.add(animal4);
//        datas.add(animal5);
//    }


    //----------------
    @Nullable@BindView(R.id.left_btn) Button left_btn;
    @Nullable@BindView(R.id.title_text) TextView title_text;
    @Nullable@BindView(R.id.right_btn) Button right_btn;


    //----------------------    全职View
    GridView list_view;
    @Nullable@BindView(R.id.imageView3) Button photo;

    @Nullable@BindView(ed1) EditText editTextName;
    String name="";
    @Nullable@BindView(R.id.radioGroup)     RadioGroup group; //性别
    String sex="";
    @Nullable@BindView(R.id.ed3) EditText ed3;
    @Nullable@BindView(R.id.ed4) EditText ed4;
    @Nullable@BindView(R.id.ed5) EditText ed5;
    @Nullable@BindView(R.id.ed6) EditText ed6;
    @Nullable@BindView(R.id.ed7) EditText ed7;

    @Nullable@BindView(R.id.ed8) EditText ed8;
    @Nullable@BindView(R.id.ed9) EditText ed9;

    @Nullable@BindView(R.id.textView) EditText textView;
    String con="";

    @Nullable@BindView(R.id.button) Button button;
    //----------------------


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text1:
                frameLayout.setVisibility(View.VISIBLE);
//                setContentView(R.layout.lookfullforjob);
                view=  LayoutInflater.from(this).inflate(R.layout.lookfullforjob,null);
                list_view=(GridView) view.findViewById(R.id.list_view);
                setContentView(view);
//                frameLayout.addView(view);
                ButterKnife.bind(this,view);
                //-------------
                left_btn.setVisibility(View.VISIBLE);
                left_btn.setOnClickListener(this);
                title_text.setVisibility(View.VISIBLE);
                title_text.setText("发全职求职贴");
                right_btn.setVisibility(View.GONE);
                button.setOnClickListener(this);
                photo.setOnClickListener(this);
//                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup arg0, int arg1) {
//                        // TODO Auto-generated method stub
//                        //获取变更后的选中项的ID
//                        int radioButtonId = arg0.getCheckedRadioButtonId();
//                        //根据ID获取RadioButton的实例
//                        RadioButton rb = (RadioButton)findViewById(radioButtonId);
//                        //更新文本内容，以符合选中项
//                        //将这个值存贮起来
//                        sex=rb.getText().toString().trim();
//                    }
//                });
                break;
            case R.id.text2:
                view=  LayoutInflater.from(this).inflate(R.layout.lookpartjob,null);
                setContentView(view);

                EditText ed1=(EditText)view.findViewById(text_ad) ;
                final String ed1sStr=ed1.getText().toString().trim();


                //------------------
                Button left =(Button) view.findViewById(R.id.include).findViewById(R.id.left_btn);
                TextView title=(TextView ) view.findViewById(R.id.include).findViewById(R.id.title_text);
                title.setVisibility(View.VISIBLE);
                title.setText("发求职兼职贴");
                Button right=(Button ) view.findViewById(R.id.include).findViewById(R.id.right_btn);
                right.setVisibility(View.GONE);
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                list_view=(GridView) view.findViewById(R.id.list_view);
                //-------------------------------
                Button photo=(Button)view.findViewById(R.id.imageView3);
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhotoOrPic();
                    }
                });


                Button button=(Button)view.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        makeText("发帖成功!!!");
                    }
                });


                break;



            case R.id.left_btn:
                finish();
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
            case R.id.button:
                name=  editTextName.getText().toString().trim();

                if (name.isEmpty() || name==null){
                    makeText("温馨提示:期望职位不能没有哦");
                    return;
                }



//                if (test.isEmpty() || test == null) {
//                    makeText("温馨提示:帖子不能没有标题哦!");
//                    return;
//                }
//                if (testArear.isEmpty() || testArear == null) {
//                    makeText("温馨提示:帖子不能没有内容哦!");
//                    return;
//                }
                //请求
                break;

        }
    }


    private void takePhotoOrPic(){
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
    }

    //-------------------------------------------
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
                adapterTImage =new ImageAdapterTImage(BarPasteJobActivity.this ,urls);
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
}