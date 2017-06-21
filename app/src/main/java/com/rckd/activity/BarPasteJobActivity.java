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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.rckd.pickers.AddressPickTask;
import com.rckd.utils.CallServer;
import com.rckd.utils.ScreenUtils;
import com.rckd.utils.TakePhotoUtils;
import com.rckd.view.PoupCamera;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.DatePicker;
import cn.addapp.pickers.picker.SinglePicker;
import timber.log.Timber;

import static com.baidu.location.h.j.B;
import static com.baidu.location.h.j.n;
import static com.baidu.location.h.j.t;
import static com.baidu.location.h.j.v;
import static com.rckd.R.id.ed1;
import static com.rckd.R.id.ed6;
import static com.rckd.R.id.left_btn;
import static com.rckd.R.id.lin;
import static com.rckd.R.id.right_btn;
import static com.rckd.R.id.textView;
import static com.rckd.R.id.text_ad;
import static com.rckd.R.id.text_ad4;
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

    //---------------
    @Nullable@BindView(R.id.text1) TextView tv1;
    @Nullable@BindView(R.id.text2) TextView tv2;
    @Nullable@BindView(R.id.frame) FrameLayout frameLayout;
    View view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paster_jobselect);
        ButterKnife.bind(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

        Button  leftBtn =ButterKnife.findById(this,R.id.left_btn);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=ButterKnife.findById(this,R.id.title_text);
        title.setVisibility(View.VISIBLE);
        title.setText("请选择发帖类型");

        Button rightBtn=ButterKnife.findById(this,R.id.right_btn);
        rightBtn.setVisibility(View.GONE);

        frameLayout.setVisibility(View.GONE);
//        //-----------------
        //----------
        // ------------拍照对象
        takePhoto=getTakePhoto();
        screenWidth= ScreenUtils.getScreenWidth(this);
        screenHeight=ScreenUtils.getScreenHeight(this);

    }







    //----------------
    @Nullable@BindView(R.id.left_btn) Button left_btn;
    @Nullable@BindView(R.id.title_text) TextView title_text;
    @Nullable@BindView(R.id.right_btn) Button right_btn;


    //----------------------    全职View
    GridView list_view;
    @Nullable@BindView(R.id.imageView3) Button photo;

    @Nullable@BindView(R.id.ed1) EditText editTextName;
    //-----------------------全职求职贴的信息


//    @Nullable@BindView(R.id.radioGroup)     RadioGroup group; //性别
//    String sex="";
//    @Nullable@BindView(R.id.ed3) EditText ed3;
//    @Nullable@BindView(R.id.ed4) EditText ed4;
//    @Nullable@BindView(R.id.ed5) EditText ed5;

    //------------
    @Nullable@BindView(R.id.ed6) TextView ed6;
    @Nullable@BindView(R.id.text6) TextView text6;
    @Nullable@BindView(R.id.lin6) LinearLayout lin6;
    //------------------
    @Nullable@BindView(R.id.ed7) TextView ed7;//期望薪资
    @Nullable@BindView(R.id.text7) TextView text7;
    @Nullable@BindView(R.id.lin7) LinearLayout lin7;
    //-------------------
//----------------------


//    @Nullable@BindView(R.id.ed8) EditText ed8;

    //----------------------
    @Nullable@BindView(R.id.ed9) TextView ed9;
    @Nullable@BindView(R.id.text9) TextView text9;
    @Nullable@BindView(R.id.lin9) LinearLayout lin9;


    @Nullable@BindView(R.id.textView) AppCompatEditText textView;
    String con="";

    @Nullable@BindView(R.id.button) Button button;
    //----------------------

    //类别选择器
    String[]  strOpt=  new String[]{"面议", "1000元以下" ,"1000-2000元" ,"2000-3000元" ,"3000-5000元","5000-8000元" ,"8000-12000元"  ,"12000-20000元" ,"200000元以上"};

//------------------------------------
    TextView text_ad4;
    TextView text_a5;
    TextView text_d5;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全职求职贴
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

                //----------------
                ed7.setOnClickListener(this);
                text7.setOnClickListener(this);
                lin7.setOnClickListener(this);

                //-------------
                ed6.setOnClickListener(this);
                text6.setOnClickListener(this);
                lin6.setOnClickListener(this);


                //-------------
                ed9.setOnClickListener(this);
                text9.setOnClickListener(this);
                lin9.setOnClickListener(this);
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
            //兼职求职贴
            case R.id.text2:
                view=  LayoutInflater.from(this).inflate(R.layout.lookpartjob,null);
                setContentView(view);
                //------------------------
                final EditText ed1=(EditText)view.findViewById(R.id.text_ad) ;
                final  EditText ed3=(EditText) view.findViewById(R.id.text_ad3);
                //------------------------------
                TextView text_ad=(TextView) view.findViewById(R.id.text_ad2);
                initTListener(text_ad);
                TextView text_tie2=(TextView)view.findViewById(R.id.text_tie2);
                initTListener(text_tie2);
                LinearLayout lin2 =(LinearLayout) view.findViewById(R.id.lin2);
                initTListener(lin2);
                //------------------------------
                final EditText   textView=(EditText) view.findViewById(R.id.textView);
                //--------------//地址选择器
                text_ad4 =(TextView) view.findViewById(R.id.text_ad4);
                initTListener(text_ad4);
                TextView text_tie4=(TextView) view.findViewById(R.id.text_tie4);
                initTListener(text_tie4);
                LinearLayout linearLayout4=(LinearLayout) view.findViewById(R.id.lin4);
                initTListener(linearLayout4);


                //---------------------
                text_a5 =(TextView) view.findViewById(R.id.text_a5);
                initTListener( text_a5);

                text_d5 =(TextView)view.findViewById(R.id.text_d5);
                initTListener(text_d5);

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
                        String ed1sStr=ed1.getText().toString().trim();
                        if (ed1sStr==null || ed1sStr.isEmpty()){
                            makeText("期望职位不能没有哦!!!");
                            return;
                        }
                        String money=ed3.getText().toString().trim();

                        if (money==null || money.isEmpty()  ){
                            makeText("期望薪资不能没有哦!!!");
                            return;
                        }
                        String con=textView.getText().toString().trim();
                        if ( con==null || con.isEmpty() ){
                            makeText("介绍自己会让简历浏览几率增加哦!!!");
                        }

                        makeText("发帖成功!!!");
                        //--------------------------------------------------------
                        //-----------------------------发帖成功后  ,跳转到我的帖子,兼职求职贴
                        startActivity(SeeMyPositionPartTimeActivity.class);
                        finish();

                    }
                });


                break;
            //---------------------------
            case R.id.lin6:
            case R.id.ed6:
            case R.id.text6:
                onConstellationPicker(R.id.lin6,strOpt);
                break;
            //----------
            case R.id.lin9:
            case R.id.ed9:
            case R.id.text9:
                onAddressPicker(R.id.lin9);
                break;

            //-------------
            case R.id.lin7:
            case R.id.ed7:
            case R.id.text7:
                onConstellationPicker(R.id.lin7,strOpt);
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
                String  name=  editTextName.getText().toString().trim();
                if (name.isEmpty() || name==null){
                    makeText("温馨提示:期望职位不能没有哦");
                    return;
                }
                String  worktype= ed6.getText().toString().trim();
                if (worktype==null || worktype.isEmpty()){
                    makeText("工作职位类别不能为空");
                    return;
                }
                String salary=ed7.getText().toString().trim();
                if (salary ==null || salary.isEmpty()){
                    makeText("工作薪水不能没有哦");
                    return;
                }
                String area=  ed9.getText().toString().trim();
                if (area.isEmpty()|| area==null){
                    makeText("工作地点不能没有哦");
                    return;
                }
                //请求
                //----------------------------------------------
                //-------------------------------------信息保存起来跳转到到下一个界面 ???
                Bundle bundle=new Bundle();
                bundle.putInt("type",0);//自定义传到完善资料处,别名0;
                //保证有值
                bundle.putString("positionApplied",name);
                bundle.putString("workType",worktype);
                bundle.putString("salary",salary);
                bundle.putString("area",area);
                startActivity(PrefectPersonData.class,bundle);
                finish();
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



    /**
     * 传入View ,传入需要的对象
     * @param strings
     */
    private void onConstellationPicker(final  int  id  , final String [] strings) {
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
                if (id==R.id.lin7 || id== R.id.text7||  id==R.id.ed7  ){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ed7.setText(item);
                        }
                    });
                }
            }
        });
        picker.show();
    }




    //------------------
    private void onAddressPicker(final int viewId) {
        com.rckd.pickers.AddressPickTask task = new  com.rckd.pickers.AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
               makeText("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    makeText(province.getAreaName() + city.getAreaName());
                    if (viewId== R.id.lin9 || viewId ==R.id.text9 || viewId==R.id.ed9){
                        ed9.setText(province.getAreaName() + city.getAreaName());
                    }

                    if (  viewId== R.id.lin4 || viewId== R.id.text_tie4|| viewId== R.id.text_ad4){
                        text_ad4.setText(province.getAreaName() + city.getAreaName());
                    }
                } else {
                    makeText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    if (viewId== R.id.lin9 || viewId ==R.id.text9 || viewId==R.id.ed9){
                        ed9.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    }
                    if (  viewId== R.id.lin4 || viewId== R.id.text_tie4|| viewId== R.id.text_ad4){
                        text_ad4.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    }
                }
            }
        });
        task.execute("贵州", "毕节", "纳雍");
    }



    private void initTListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.text_ad2:
                    case R.id.text_tie2:
                    case R.id.lin2:
                        //--------------------工作工种的选择器
                        break;
                    case R.id.lin4:
                    case R.id.text_tie4:
                    case R.id.text_ad4:
                        onAddressPicker(R.id.text_ad4);
                        break;

                    //-------------------
                    case R.id.text_a5:
                        onYearMonthDayPicker(R.id.text_a5);
                        break;
                    case R.id.text_d5:
                        onYearMonthDayPicker(R.id.text_d5);
                        break;

                    //----------------
                }
            }
        });

    }





    //-------------------------日期选择器
    private void onYearMonthDayPicker(final   int id) {
        final DatePicker picker = new DatePicker(this);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);

        picker.setRangeStart(year ,month , day);
        picker.setRangeEnd(year+100,month, day);
        picker.setSelectedItem(year+50, month, day);
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                makeText(year + "-" + month + "-" + day);
                if (id==R.id.text_a5){
                    text_a5.setText(year + "-" + month + "-" + day);
                }
                if (id==R.id.text_d5){
                    text_d5.setText(year + "-" + month + "-" + day);
                }

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

}
