package com.rckd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.rckd.R;
import com.rckd.base.BaseActivity;
import com.rckd.inter.HttpListener;
import com.rckd.utils.MD5;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by LiZheng on 2017/5/5 0005.
 */

public class TestActivityForSign extends BaseActivity implements View.OnClickListener {
    private static String tag = TestActivityForSign.class.getName();

    HashMap<String, Object> hashMap = new HashMap<>(); //存放sign之前的书局

    Request<String> request = null;
    String url = "http://192.168.2.201:8080/hr/hr/newHr.shtml?";
    //    String url = "http://192.168.2.201:8080/hr/hr/newHr.shtml?isnew=0&&townId=1170&secondCareerId=&thirdCareerId=&workYearId=&educationId=&salaryId=&timeRange=0";
    Button button;


    @Override
    protected int fragmentLayoutId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_for_sign);
        button = (Button) findViewById(R.id.button3);
        button.setClickable(true);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button3:
                request = NoHttp.createStringRequest(url, RequestMethod.GET);
                if (request != null) {
                    String key = "sdfkljfkadnmkgfzdmdfkagjkladfg";
//                    isnew=0&&townId=1170&secondCareerId=&thirdCareerId=&workYearId=&educationId=&salaryId=&timeRange=0
                    request.add("isnew", 0);
                    request.add("townId", 1170);
                    request.add("secondCareerId", "");
                    request.add("thirdCareerId", "");
                    request.add("workYearId", "");
                    request.add("educationId", "");
                    request.add("salaryId", "");
                    request.add("timeRange", 0);
//                    String pt = "timeRange0educationIdsecondCareerIdsalaryIdworkYearIdtownId1170isnew0thirdCareerId";
//                    String pt = "isnew0townId1170secondCareerIdthirdCareerIdworkYearIdeducationIdsalaryIdtimeRange0";

                    hashMap.put("isnew", 0);
                    hashMap.put("townId", 1170);
                    hashMap.put("secondCareerId", "");
                    hashMap.put("thirdCareerId", "");
                    hashMap.put("workYearId", "");
                    hashMap.put("educationId", "");
                    hashMap.put("salaryId", "");
                    hashMap.put("timeRange", 0);
                    String pt = MD5.sortRequest(hashMap);
                    Timber.e(tag+" pt = " +pt ,tag);
                    String pt1 = pt.toUpperCase();
                    Timber.e(tag+ " pt1 " + pt1,tag);
                    String md5 = MD5.hmacSign(pt1, key);
                    Timber.e(tag + " md5 = " + md5, tag);
                    request.add("sign", md5);
                    request(0, request, httpListener, true, true);
                }
        }
    }

    private HttpListener<String> httpListener = new HttpListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (response.getHeaders().getResponseCode() == 501) {

                makeText("501");
            } else if (RequestMethod.HEAD == response.request().getRequestMethod()) {
                // 请求方法为HEAD时没有响应内容
                makeText("head");
            } else if (response.getHeaders().getResponseCode() == 405) {
                makeText("405");

            } else if (response.getHeaders().getResponseCode() == 200) {
                makeText("请求 已经接受");
            }

            List<String> allowList = response.getHeaders().getValues("Allow");
            String allow = "服务器仅仅支持请求方法";
            if (allowList != null && allowList.size() > 0) {
                allow = String.format(Locale.getDefault(), allow, allowList.get(0));
            }
            button.setText(allow);
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            makeText("请求失败");
        }
    };


}
