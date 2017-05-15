package com.rckd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.common.BitMatrix;

public class HelpUtil {

    /**
     * 验证是否是手机号
     *
     * @param mobileNO
     * @return
     */
    public static boolean isMobileNO(String mobileNO) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
//        Pattern p = Pattern.compile("0?(13|14|15|18)[0-9]{9}");
        // ^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$
        // ^((13[0-9])|(15[^4,//D])|(18[0,5-9]))\\d{8}$
        Matcher m = p.matcher(mobileNO);
        return m.matches();
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 转换图片成圆形        
     *
     * @param bitmap 传入Bitmap对象    
     * @return          
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 根据字符串获取二维码
     *
     * @param imagesrc
     * @return
     */

//	public static Bitmap Create2DCode(String code) throws Exception {
//		// 生成二维码矩阵，编码时指定大小，不要生成了图片以后再进行缩放，这样会导致模糊
//		BitMatrix matrix = new MultiFormatWriter().encode(code,
//				BarcodeFormat.QR_CODE, 150, 150);
//		int width = matrix.getWidth();
//		int height = matrix.getHeight();
//		// 二维矩阵转为一维像素数组，也就是一直横着排
//		int[] pixels = new int[width * height];
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				if (matrix.get(x, y)) {
//					pixels[y * width + x] = 0xff000000;
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Config.ARGB_8888);
//		// 通过像素组生成bitmap
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//
//		return bitmap;
//
//	}

    /**
     * 根据字符串获取二维码
     *
     * @param
     * @return
     */

    public static float getMax(float[] d) {
        float max;
        for (int i = 0; i < d.length - 1; i++) { // 最多做n-1趟排序
            for (int j = 0; j < d.length - i - 1; j++) { // 对当前无序区间score[0......length-i-1]进行排序(j的范围很关键，这个范围是在逐步缩小的)
                if (d[j] < d[j + 1]) { // 把小的值交换到后面
                    float t = d[j];
                    d[j] = d[j + 1];
                    d[j + 1] = t;
                }
            }
        }
        max = d[0];
        return max;
    }

    /**
     * 获取SharedPreference中的单个数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String getSharedPreferences(Context context, String shName,
                                              String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                shName, 0);
        String value = sharedPreferences.getString(key, "");
        return value;

    }

    /**
     * 保存sp文件数据boolean型
     *
     * @param context
     * @param shName
     * @param key
     * @param value
     */
    public static void setBooleanSharedPreferences(Context context,
                                                   String shName, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                shName, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取sp文件中的boolean型数据
     *
     * @param context
     * @param shName
     * @param key
     * @return
     */
    public static boolean getBooleanSharedPreferences(Context context,
                                                      String shName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                shName, 0);
        boolean value = sharedPreferences.getBoolean(key, true);
        return value;
    }

    /*-----------取出http返回数据中的有用信息--------------*/
    public static String dataFormat(String body) {
        String str1 = "<string xmlns=\"http://tempuri.org/\">";
        String str2 = "</string>";
        String str3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        if (body.contains(str1) && body.contains(str2) && body.contains(str3)) {
            return body.replace(str1, "").replace(str2, "").replace(str3, "")
                    .trim();
        } else {
            return null;
        }
    }

//    public static void showToast(Context context, String showText) {
//        Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
//    }

    /**
     * 把应用对应的信息放到Map中
     *
     * @param mActivity
     * @return
     */
    public Map<String, ResolveInfo> getShareList(Activity mActivity) {
        final Map<String, ResolveInfo> appInfo = new HashMap<String, ResolveInfo>();
        List<ResolveInfo> appList = getShareTargets(mActivity);
        if (appList.size() > 0) {
            for (int i = 0; i < appList.size(); i++) {
                ResolveInfo tmp_ri = (ResolveInfo) appList.get(i);
                ApplicationInfo apinfo = tmp_ri.activityInfo.applicationInfo;
                String tmp_appName = apinfo.loadLabel(
                        mActivity.getPackageManager()).toString();
                System.out.println("tmp_appName=" + tmp_appName + ";tmp_ri="
                        + tmp_ri);
                if (tmp_appName.equals("微博")) {
                    appInfo.put(tmp_appName, tmp_ri);
                }
                if (tmp_appName.equals("微信")) {
                    appInfo.put(tmp_appName, tmp_ri);
                }
            }

        }
        return appInfo;
    }

    /**
     * 获取手机中所有可以分享的应用列表
     *
     * @param activity
     * @return
     */
    public static List<ResolveInfo> getShareTargets(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pm = activity.getPackageManager();
        return pm.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
    }

    public static boolean isContainApp(Activity context, String packName) {
        List<ResolveInfo> list = getShareTargets(context);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo resolveInfo = list.get(i);
            ApplicationInfo apinfo = resolveInfo.activityInfo.applicationInfo;
            String pack = apinfo.packageName;
            System.out.println("pack=" + pack);
            if (packName.equals(pack)) {
                return true;
            }
        }

        return false;
    }


    public static void startApp(Context context, String packageName) {
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }


    public static File compressPath(Bitmap bitmap, String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        int be = (int) (options.outHeight / (float) 200);
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取sd卡父文件路劲
     *
     * @return
     */
    public static String getSDPath() {
        String path = "";
        String sdPath = "";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // 为true的话，sd卡存在
            sdPath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(sdPath);
            path = file.getParentFile().getPath();
            Log.i("4", "path:" + path);
        } else {
            path = "/";
        }
        return path;
    }

    public static double getTimeValue(String time) throws Exception {
        double d = 0;
        String[] s = time.split(":");
        if (!s[1].startsWith("0")) {
            d = Double.parseDouble(s[0]) + Double.parseDouble(s[1]) / 60;
        } else {
            if (s[1].equals("00")) {
                d = Double.parseDouble(s[0]);
            } else {
                d = Double.parseDouble(s[0])
                        + Double.parseDouble(s[1].substring(1, 2)) / 60;
            }
        }

        return d;
    }

    /**
     * 获取List<double[]>中最大值
     *
     * @param
     * @return
     */
    public static double getDMax(List<double[]> dataList) {
        double max = Double.MIN_VALUE;
        for (double[] ds : dataList) {
            for (double d : ds) {
                if (d > max) {
                    max = d;
                }
            }
        }
        return max;
    }

    /**
     * 获取List<double[]>数组中最小值
     *
     * @param
     * @return
     */
    public static double getDMin(List<double[]> dataList) {
        double min = Double.MAX_VALUE;
        for (double[] ds : dataList) {
            for (double d : ds) {
                if (d < min) {
                    min = d;
                }
            }
        }
        return min;
    }

    /**
     * 获取当前日期
     *
     * @param
     * @return
     * @throws Exception
     */

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return year + "年" + month + "月" + day + "日";
    }

    /**
     * 获取当前时间
     *
     * @return
     */

    public static String getTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String time = "";
        if (minute < 10) {
            time = hour + ":" + "0" + minute;
        } else {
            time = hour + ":" + minute;
        }
        return time;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取当前时间的数值
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long getTime(String time) throws ParseException {
        long l = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (time != null && !time.equals("")) {
                Date date = df.parse(time);
                l = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return l;
    }


    //在此处做测试
//    public static void  main(String args[]){
//
//    }

}
