package com.rckd.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.rckd.R;
import com.rckd.base.BaseActivity;
import com.rckd.bean.JsonBean;
import com.rckd.utils.GetJsonDataUtil;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.baidu.location.h.j.B;
import static com.baidu.location.h.j.t;

/**
 * Created by LiZheng on 2017/5/8 0008.
 */
/*
//发招聘贴
 */
public class BarJobWantActivity extends BaseActivity implements View.OnClickListener {

    private  static String tag=BarJobWantActivity.class.getName();
    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    //-------------------------
    View view;
    LayoutInflater inflater;
    TextView text1;
    TextView text2;
    //----------------
    Button left_btn;
    TextView title_text;
    Button right_btn;
    FrameLayout frameLayout;//framelayout真正显示加载页面

    //---------------------
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private  boolean isLoaded = false;

    FullJobView fullJobView;

    PartJobView partJobView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_sendbar_ac);
        //-------------------
        text1 = (TextView) findViewById(R.id.text1);
        text1.setVisibility(View.VISIBLE);
        text1.setText("全职招聘");
        text1.setOnClickListener(this);

        //------------------------
        text2 = (TextView) findViewById(R.id.text2);
        text2.setVisibility(View.VISIBLE);
        text2.setText("兼职招聘");
        text2.setOnClickListener(this);


        //---------------
        left_btn = (Button) findViewById(R.id.left_btn);
        left_btn.setVisibility(View.VISIBLE);
        left_btn.setOnClickListener(this);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setVisibility(View.VISIBLE);
        title_text.setText("请选择发布类型");

        //--------
        frameLayout =(FrameLayout) findViewById(R.id.frame);
        //--------------

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);//解析城市三联列表
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.left_btn:
                finish();
                break;
//全职招聘
            case R.id.text1:
                Timber.e(tag+" 全职招聘 ",tag);
                inflater = LayoutInflater.from(this);
                //在此可以判断是否为公司
                view = inflater.inflate(R.layout.fulljob, null);
                fullJobView= new FullJobView(view ,mContext);

                //view的相关事件可以写在此处
                fullJobView.onClick(view);
                frameLayout.addView(view);

                frameLayout.setVisibility(View.VISIBLE);
                title_text.setText("全职工作");
                //当利用帧布局加载后  ,需要将   原来的布局 隐藏掉
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                break;

//兼职招聘
            case R.id.text2:
                inflater = LayoutInflater.from(this);
                view = inflater.inflate(R.layout.partjob, null);
                partJobView =new PartJobView(view ,mContext);
                partJobView.onClick(view);
                frameLayout.addView(view);
                title_text.setText("兼职工作");
                frameLayout.setVisibility(View.VISIBLE);

                //当利用帧布局加载后  ,需要将   原来的布局 隐藏掉
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread==null){//如果已创建就不再重新创建子线程了

                        Toast.makeText(BarJobWantActivity.this,"开始解析数据",Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(BarJobWantActivity.this,"解析数据成功",Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(BarJobWantActivity.this,"解析数据失败",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };




    public   void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()+
                        options2Items.get(options1).get(options2)+
                        options3Items.get(options1).get(options2).get(options3);

                Toast.makeText(mContext,tx,Toast.LENGTH_SHORT).show();
                    Timber.e(tag+" fullJobView  ShowPickerView ");
                fullJobView.et3.setText(tx);

            }
        }).setTitleText("城市选择").setDividerColor(Color.BLACK).setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        pvOptions.show();
    }

    public void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        //此处循环算法特别重要
        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    //--------------------------fulljobview
    public class FullJobView  implements  View.OnClickListener{
        FullJobView  fullJobView;
        Context context;
        //-------------
        TextView et3; //城市列表
        TextView tv3;
        LinearLayout lin3;
        //-----------
        EditText et1;
        String nameJob="";
        TextView et5;
        Button bt;
        EditText et;
        String num="";
        TextView et2;

        TextView et4;
        String area="";
        TextView et12;
        EditText  textView;
        String con="";
        TextView et6;
        TextView et7;
        TextView et8;
        EditText et_a9;
        EditText et9;

        private View rootView;
        public   FullJobView (View rootView ,Context context){
            this.rootView = rootView;
            this.context=context;
            init();
        }


        //----------------
        private  void init(){
            //--------------
            et3=(TextView)   rootView.findViewById(R.id.et3);
            et3.setOnClickListener(this);
            tv3=(TextView)rootView.findViewById(R.id.tv3);
            tv3.setOnClickListener(this);
            lin3=(LinearLayout)rootView.findViewById(R.id.lin3);
            lin3.setOnClickListener(this);

            //-------------
            bt=(Button) rootView.findViewById(R.id.bt);
            bt.setOnClickListener(this);

            //
            et1=(EditText) rootView.findViewById(R.id.et1);
            et5=(TextView)rootView.findViewById(R.id.et5);
            et=(EditText)rootView.findViewById(R.id.et);
            //-------------
            et2=(TextView)rootView.findViewById(R.id.et2);
            et2.setOnClickListener(this);

            et4=(EditText) rootView.findViewById(R.id.et4);
            et12=(TextView) rootView.findViewById(R.id.et12);
            et12.setOnClickListener(this);
            textView=(EditText) rootView.findViewById(R.id.textView);
            et6=(TextView) rootView.findViewById(R.id.et6);
            et6.setOnClickListener(this);
            et7=(TextView) rootView.findViewById(R.id.et7);
            et7.setOnClickListener(this);






        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv3:
                case R.id.et3:
                case  R.id.lin3:
                    Timber.e(tag+" fullJobView onClick");
                    if (isLoaded){
                        ShowPickerView();
                    }else {
                        Toast.makeText(mContext,"数据暂未解析成功，请等待",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.bt:
                    nameJob=et1.getText().toString().trim();
                    if (nameJob==null || nameJob.isEmpty()){
                        makeText("此帖子不能没有职位哦");
                        return;
                    }
                    num=et.getText().toString().trim();
                    if (num==null || num.isEmpty()  || Integer.parseInt(num) <=0){
                        makeText("招聘人数不能少于1个人哦");
                        return;
                    }
                    area=et4.getText().toString().trim();
                    if (area.isEmpty() || area ==null){
                        makeText("工作区域不能为空");
                        return;
                    }
                    con=textView.getText().toString().trim();
                    if (con==null || con.isEmpty() ){
                        makeText("进行职位描述会吸引更多人才哦!!!");
                        return;
                    }
                    makeText("恭喜你,发帖成功!!!");
                    //----------------post请求注意编码放肆

            }

        }
    }








    //----------------------partjobview

    public  class  PartJobView implements  View.OnClickListener{
        FullJobView  fullJobView;
        Context context;
        TextView et3; //城市列表
        private View rootView;


        public   PartJobView (View rootView ,Context context){
            this.rootView = rootView;
            this.context=context;
            init();
        }

        private  void init(){
        }

        @Override
        public void onClick(View v) {

        }
    }




}



