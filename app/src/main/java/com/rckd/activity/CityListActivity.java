package com.rckd.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.rckd.R;
import com.rckd.bean.City;
import com.rckd.db.DBHelper;
import com.rckd.db.DBPingYinUtil;
import com.rckd.db.DataBaseHelper;
import com.rckd.view.LetterListView;
import com.tencent.smtt.utils.TbsLog;
import com.yanzhenjie.permission.AndPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import timber.log.Timber;

import static com.baidu.location.h.j.I;

/**
 * Created by LiZheng on 2017/5/4 0004.
 * http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/getloc ,开发文档
 */
public class CityListActivity extends com.rckd.base.BaseActivity implements View.OnClickListener, OnScrollListener, com.yanzhenjie.permission.PermissionListener {
    protected String[] strPression = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    String city="";//搜索的结果

    private static String tag = CityListActivity.class.getName();
    private BaseAdapter adapter;
    private ResultListAdapter resultListAdapter;
    private ListView personList;
    private ListView resultList;
    private TextView overlay; // 对话框首字母textview
    private LetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private ArrayList<City> allCity_lists; // 所有城市列表
    private ArrayList<City> city_lists;// 城市列表
    private ArrayList<City> city_hot;
    private ArrayList<City> city_result;
    private ArrayList<String> city_history;
    private EditText sh;
    private TextView tv_noresult;
    //    TencentLocationManager mLocationManager;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    private String currentCity; // 用于保存定位到的城市
    private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
    private boolean isNeedFresh;
    private DataBaseHelper helper;
    boolean flag = false;//仅用来记录定位 是否已经开启权限，默认fasle

    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose);
        Timber.e(tag + "  onCreate  start ", tag);
        findViewById(R.id.left_back).setOnClickListener(this);
        personList = (ListView) findViewById(R.id.list_view);
        allCity_lists = new ArrayList<City>();
        city_hot = new ArrayList<City>();
        city_result = new ArrayList<City>();
        city_history = new ArrayList<String>();
        resultList = (ListView) findViewById(R.id.search_result);
        sh = (EditText) findViewById(R.id.sh);
        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        helper = new DataBaseHelper(this);
        sh.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() == null || "".equals(s.toString())) {
                    letterListView.setVisibility(View.VISIBLE);
                    personList.setVisibility(View.VISIBLE);
                    resultList.setVisibility(View.GONE);
                    tv_noresult.setVisibility(View.GONE);
                } else {
                    city_result.clear();
                    letterListView.setVisibility(View.GONE);
                    personList.setVisibility(View.GONE);
                    getResultCityList(s.toString());
                    if (city_result.size() <= 0) {
                        tv_noresult.setVisibility(View.VISIBLE);
                        resultList.setVisibility(View.GONE);
                    } else {
                        tv_noresult.setVisibility(View.GONE);
                        resultList.setVisibility(View.VISIBLE);
                        resultListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        letterListView = (LetterListView) findViewById(R.id.MyLetterListView01);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
        personList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //注意 postition的值
                if (position >= 4) {

                    Toast.makeText(getApplicationContext(),
                            allCity_lists.get(position).getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        locateProcess = 1;
        personList.setAdapter(adapter);
        personList.setOnScrollListener(this);
        resultListAdapter = new ResultListAdapter(this, city_result);
        resultList.setAdapter(resultListAdapter);
        resultList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                city =city_result.get(position).getName().toString();
                makeText(city);
//                Toast.makeText(getApplicationContext(), city_result.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        initOverlay();
        cityInit();
        hotCityInit();
        hisCityInit();
        setAdapter(allCity_lists, city_hot, city_history);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mMyLocationListener = new MyLocationListener();
        if (isHaveAndPermission(strPression)) {
            //初始化监听函数
            mLocationClient.registerLocationListener(mMyLocationListener);
            //注册监听函数
             InitLocation();
            //开启定位，开启定位前每次都应当判断是否已经权限

            mLocationClient.start();
        } else {
            getPression(this, REQUEST_CODE_PERMISSION_LOCATION, strPression);
        }
        Timber.e(tag + "  onCreate  over ", tag);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           case R.id.left_back:
                Intent intent=new Intent();
                intent.putExtra("city",city); //"city" key ,vaue 后填写实际数据
                Bundle bundle=null;//可以使用bundle传递数据
                setResult(RESULT_CODE_CITY ,intent);
                finish();
                break;
        }
    }


    /**
     * 根据搜索框输入结果适配
     */
    private class ResultListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<City> results = new ArrayList<City>();

        public ResultListAdapter(Context context, ArrayList<City> results) {
            inflater = LayoutInflater.from(context);
            this.results = results;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(results.get(position).getName());
            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }


    /**
     * 插入数据
     *
     * @param name
     */
    public void InsertCity(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from recentcity where name = '" + name + "'", null);
        if (cursor.getCount() > 0) { //
            db.delete("recentcity", "name = ?", new String[]{name});
        }
        db.execSQL("insert into recentcity(name, date) values('" + name + "', "
                + System.currentTimeMillis() + ")");
        db.close();
    }

    /**
     * 配置定位的参数
     */
    private void InitLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

    }

    /**
     * 初始化城市列表值设置
     */
    private void cityInit() {
        Timber.e(tag + "  cityInit  start ", tag);
        City city = new City("定位", "0"); // 当前定位城市
        allCity_lists.add(city);
        city = new City("最近", "1"); // 最近访问的城市
        allCity_lists.add(city);
        city = new City("热门", "2"); // 热门城市
        allCity_lists.add(city);
        city = new City("全部", "3"); // 全部城市
        allCity_lists.add(city);
        city_lists = getCityList();//
        allCity_lists.addAll(city_lists);
        Timber.e(tag + "  cityInit  over ", tag);
    }

    /**
     * 热门城市
     */
    public void hotCityInit() {
        City city = new City("上海", "2");
        city_hot.add(city);
        city = new City("北京", "2");
        city_hot.add(city);
        city = new City("广州", "2");
        city_hot.add(city);
        city = new City("深圳", "2");
        city_hot.add(city);
        city = new City("武汉", "2");
        city_hot.add(city);
        city = new City("天津", "2");
        city_hot.add(city);
        city = new City("西安", "2");
        city_hot.add(city);
        city = new City("南京", "2");
        city_hot.add(city);
        city = new City("杭州", "2");
        city_hot.add(city);
        city = new City("成都", "2");
        city_hot.add(city);
        city = new City("重庆", "2");
        city_hot.add(city);
    }

    /**
     * 城市历史访问记录
     * 最近访问
     */
    private void hisCityInit() {
        Timber.e(tag + " hisCityInit start", tag);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from recentcity order by date desc limit 0, 3", null);
        while (cursor.moveToNext()) {
            city_history.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    @SuppressWarnings("unchecked")
    /**
     * 注意可以直接通过系统服务器接口调用所有的城市
     */
    private ArrayList<City> getCityList() {
        Timber.e(tag + "  getCityList start  ", tag);
        //以下可以考虑使用DBUtil 工具类进行优化
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<City>();
        try {
            Timber.e(tag + "  getCityList start  try {} ", tag);
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Timber.e(tag + " 执行sql语句 ", tag);
            Cursor cursor = db.rawQuery(" select * from city", null);//执行select 语句
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);
        return list;//list  vaule 374
    }

    @SuppressWarnings("unchecked")
    private void getResultCityList(String keyword) {
        DBHelper dbHelper = new DBHelper(this);
        try {
            dbHelper.createDataBase();
            //获得数据库的对象,之后回去执行DBHelper的 OnCreter()方法
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(" select *  from city where name like \"%" + keyword + "%\" or pinyin like \"%" + keyword + "%\"", null);
            City city;
            Log.e("info", "length = " + cursor.getCount());
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                city_result.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(city_result, comparator);
    }

    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    private void setAdapter(List<City> list, List<City> hotList,
                            List<String> hisCity) {
        adapter = new ListAdapter(this, list, hotList, hisCity);
        personList.setAdapter(adapter);
    }


    /**
     * 实现实位回调监听 ,百度这个回调接口查看源码  发现是放置在子线程操作的
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            Timber.e(tag + " city =  " + location.getCity(), tag);
            if (!isNeedFresh) {
                return;
            }
            isNeedFresh = false;
            String latitude = String.valueOf(location.getLatitude());//纬度 double
            String longitude = String.valueOf(location.getLongitude());//	经度 double
            String altitude = String.valueOf(location.getAltitude());//	海拔
//            String accuracy = String.valueOf(location.getAccuracy());//	精度
//
//            //REQ_LEVEL_ADMIN_AREA
            String nation = location.getCountry();//国家
            String province = location.getProvince(); //省
            String city = location.getCity();//市
            String district = location.getDistrict();//district	区
//            String town = tencentLocation.getTown();//  镇
//            String village = tencentLocation.getVillage();//村
//            String street = tencentLocation.getStreet();//街道
//            String streetNo = tencentLocation.getStreetNo();//门号
            if (location.getCity() == null) {
                locateProcess = 3; // 如果连地级市都尚未获取到,就证明定位失败
                CityListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        personList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
                return;
            } else {
                if (location.getDistrict() != null) {
                    currentCity = location.getDistrict().substring(0, location.getDistrict().length() - 1);
                    locateProcess = 2; // 定位成功
                    CityListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            personList.setAdapter(adapter);  //此处为子线程??? ,耗时操作
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    currentCity = location.getCity().substring(0, location.getCity().length() - 1);
                    locateProcess = 2; // 定位成功
                    CityListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            personList.setAdapter(adapter);  //此处为子线程??? ,耗时操作
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }


//            disposables.add(doBackService()
//                    // Run on a background thread
//                    .subscribeOn(Schedulers.io())
//                    // Be notified on the main thread
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(new DisposableObserver<String>() {
//                        @Override public void onComplete() {
//                            Log.d(tag, "onComplete()");
//                        }
//
//                        @Override public void onError(Throwable e) {
//                            Log.e(tag, "onError()", e);
//                        }
//
//                        @Override public void onNext(String string) {
//                            Log.d(tag, "onNext(" + string + ")");
//                        }
//                    }));

        }

//        private final CompositeDisposable disposables = new CompositeDisposable();  //创建可观察

//        //内部类
//       Observable<String> doBackService(){
//           return  Observable.defer(new Callable<ObservableSource<? extends String>>() {
//               @Override
//               public ObservableSource<? extends String> call() throws Exception {
//                   SystemClock.sleep(5000);
//                   return Observable.just("one", "two", "three", "four", "five");
//               }
//           });
//       }

        //热点
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            Timber.e(tag + i + "  " + s, tag);
            String resText = "";

            if (i == 0) {
                resText = "不是wifi热点, wifi:" + s;
            } else if (i == 1) {
                resText = "是wifi热点, wifi:" + s;
            } else if (i == -1) {
                resText = "未连接wifi";
            }
            Timber.e(tag + " resText = ", tag);
        }
    }


    public class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> list;
        private List<City> hotList;
        private List<String> hisCity;
        final int VIEW_TYPE = 5;

        public ListAdapter(Context context, List<City> list,
                           List<City> hotList, List<String> hisCity) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
            this.hotList = hotList;
            this.hisCity = hisCity;
            alphaIndexer = new HashMap<String, Integer>();
            sections = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                // 当前汉语拼音首字母
                String currentStr = getAlpha(list.get(i).getPinyi());
                // 上一个汉语拼音首字母，如果不存在为" "
                String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
                        .getPinyi()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(list.get(i).getPinyi());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 4 ? position : 4;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.frist_list_item, null);
                TextView locateHint = (TextView) convertView
                        .findViewById(R.id.locateHint);
                city = (TextView) convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateProcess == 2) {
                            Toast.makeText(getApplicationContext(), city.getText().toString(), Toast.LENGTH_SHORT).show();
                            //此处可以在增加一个布局
                        } else if (locateProcess == 3) {
                            locateProcess = 1;
                            personList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            mLocationClient.stop();
                            isNeedFresh = true;
                            InitLocation();
                            currentCity = "";
                            if (isHaveAndPermission(strPression)) {
                                mLocationClient.start();
                            } else {
                                getPression(CityListActivity.this, REQUEST_CODE_PERMISSION_LOCATION, strPression);
                            }
                        }
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView.findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText("当前定位城市");
                    city.setVisibility(View.VISIBLE);
                    city.setText(currentCity); //城市名称就是广德
                    mLocationClient.stop();
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新选择");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) { // 最近访问城市
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView rencentCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                rencentCity
                        .setAdapter(new HitCityAdapter(context, this.hisCity));
                rencentCity.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Toast.makeText(getApplicationContext(),
                                city_history.get(position), Toast.LENGTH_SHORT)
                                .show();

                    }
                });
                TextView recentHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                recentHint.setText("最近访问的城市");
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView hotCity = (GridView) convertView
                        .findViewById(R.id.recent_city);
                hotCity.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Toast.makeText(getApplicationContext(),
                                city_hot.get(position).getName(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
                hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
                TextView hotHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                hotHint.setText("热门城市");
            } else if (viewType == 3) {
                convertView = inflater.inflate(R.layout.total_item, null);
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView
                            .findViewById(R.id.alpha);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 1) {
                    holder.name.setText(list.get(position).getName());
                    String currentStr = getAlpha(list.get(position).getPinyi());
                    String previewStr = (position - 1) >= 0 ? getAlpha(list
                            .get(position - 1).getPinyi()) : " ";
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    class HotCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<City> hotCitys;

        public HotCityAdapter(Context context, List<City> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position).getName());
            return convertView;
        }
    }

    class HitCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<String> hotCitys;

        public HitCityAdapter(Context context, List<String> hotCitys) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
            this.hotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return hotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(hotCitys.get(position));
            return convertView;
        }
    }

    private boolean mReady;

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        Timber.e(tag + "  initOverlay  start ", tag);
        mReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
        Timber.e(tag + "  initOverlay  over ", tag);
    }

    private boolean isScroll = false;

    private class LetterListViewListener implements LetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }


    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "最近";
        } else if (str.equals("2")) {
            return "热门";
        } else if (str.equals("3")) {
            return "全部";
        } else {
            return "#";
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }

        if (mReady) {
            String text;
            String name = allCity_lists.get(firstVisibleItem).getName();
            String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
            if (firstVisibleItem < 4) {
                text = name;
            } else {
                text = DBPingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase();
            }
            overlay.setText(text);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }


    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------
    //以下跟权限有关，参看https://developer.android.com/，android  6.0以上均为运行时申请权限
    @Override
    public void onFailed(int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {

            case REQUEST_CODE_PERMISSION_LOCATION:
                Timber.e(tag + " onFailed " + " AppPressionCode.TenCentMap ", tag);
                makeText(tag + " 您没有给相应的权限！！！我们可能无法提供更好的服务给你");
                break;
        }
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING).show();

            // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingHandle = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
        }
    }


    @Override
    public void onSucceed(int requestCode, List<String> grantPermissions) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                makeText(tag + " 开始申请权限");
                Timber.e(tag + " onSucceed " + " AppPressionCode.TenCentMap", tag);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //int requestCode, int resultCode, Intent data 注意三个参数
        TbsLog.e(tag, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);
        //根据请求吗判断事务
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                makeText("欢迎回来");
                Timber.e(tag + " REQUEST_CODE_SETTING =" + REQUEST_CODE_SETTING, tag);
                break;
            }
            default:
                Timber.e(tag + "    requestCode   =" + REQUEST_CODE_SETTING, tag);
                break;
        }
    }
//---------------------------------------------------------------------------------------------------------------------


}
