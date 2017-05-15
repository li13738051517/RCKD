package com.rckd.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by LiZheng on 2017/3/17 0017.
 * 获取网络状态
 */
public class NetWorkUtil {


    /**
     * 检测网络状态
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是wifi
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * 是否是2G网络
     * @param context
     * @return
     */
    public static boolean isNetwork2G(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_MOBILE) {// 手否是手机网络
            //移动联通电信2g
            if (ni.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                    || ni.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                    || ni.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

//
//    // 公用http get 获取网络数据方法
//    public static String getData(String url) throws Exception {
//        HttpClient httpClient = null;
//        try {
//            StringBuilder sb = new StringBuilder();
//            httpClient = new DefaultHttpClient();
//            System.out.println("u" + url);
//            HttpGet httpGet = new HttpGet(url);
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            if (httpEntity != null) {
//                InputStream instream = httpEntity.getContent();
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(instream));
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//                return sb.toString();
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            if (httpClient != null) {
//                httpClient.getConnectionManager().shutdown();
//            }
//        }
//    }
//
//    // 公用http post 获取网络数据方法
//    public static String postData(String url, List<NameValuePair> params)
//            throws Exception {
//        String result = null;
//        HttpPost httpPost = new HttpPost(url);
//        System.out.println("u=" + url);
//        String p = "";
//        for (int i = 0; i < params.size(); i++) {
//            p += params.get(i).getName() + "=" + params.get(i).getValue() + "&";
//        }
//        System.out.println("params=" + p);
//        HttpClient httpClient = new DefaultHttpClient();
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            if (httpResponse.getEntity() != null) {
//                result = EntityUtils.toString(httpResponse.getEntity());
//                System.out.println("result===" + result);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//    // 公用http post 获取网络数据方法
//    public static String postImage(File file) throws Exception {
//        String result = "";
//        HttpClient httpclient = new DefaultHttpClient();
//        try {
//            HttpPost httppost = new HttpPost(Constant.httpurl+"upLoad.shtml?");
//
//
//            FileBody bin = new FileBody(file);
//            MultipartEntity reqEntity = new MultipartEntity();
//            reqEntity.addPart("file", bin);
//
//
//            httppost.setEntity(reqEntity);
//            HttpResponse response = httpclient.execute(httppost);
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == HttpStatus.SC_OK) {
//                HttpEntity resEntity = response.getEntity();
//                // httpclient自带的工具类读取返回数据
//                // System.out.println(EntityUtils.toString(resEntity));
//                result = EntityUtils.toString(resEntity);
//                // EntityUtils.consume(resEntity);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            try {
//                httpclient.getConnectionManager().shutdown();
//            } catch (Exception ignore) {
//            }
//        }
//        System.out.println("result = "+result);
//        return result;
//    }

}
