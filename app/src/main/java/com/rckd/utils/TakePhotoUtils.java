package com.rckd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import com.rckd.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.width;
import static com.baidu.location.h.a.i;
import static com.baidu.location.h.j.b;
import static com.baidu.location.h.j.c;
import static com.darsh.multipleimageselect.helpers.Constants.limit;

/**
 * Created by LiZheng on 2017/5/23 0023.
 */

public class TakePhotoUtils {
    Context context;
    TakePhoto takePhoto;
    //默认Construct
    public TakePhotoUtils(){
    }

    public TakePhotoUtils(Context context){
        this.context=context;
    }

    /**
     * 拍照
     */
    public static Uri takePhoto(Activity mActivity, int flag) throws IOException {
        //指定拍照intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = null;
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            String sdcardState = Environment.getExternalStorageState();
            File outputImage = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                outputImage = createImageFile(mActivity);
            } else {
                Toast.makeText(mActivity.getApplicationContext(), "内存异常", Toast.LENGTH_SHORT).show();
            }
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (outputImage != null) {
                imageUri = Uri.fromFile(outputImage);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                mActivity.startActivityForResult(takePictureIntent, flag);
            }
        }

        return imageUri;
    }


    /**
     * 创建图片文件
     * @param mActivity
     * @return
     * @throws IOException
     */
    public static  File createImageFile(Activity mActivity) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;//创建以时间命名的文件名称
        File storageDir = getOwnCacheDirectory(mActivity, "takephoto");//创建保存的路径
        File image = new File(storageDir.getPath(), imageFileName + ".jpg");
        if (!image.exists()) {
            try {
                //在指定的文件夹中创建文件
                image.createNewFile();
            } catch (Exception e) {
            }
        }

        return image;
    }


    /**
     * 根据目录创建文件夹
     * @param context
     * @param cacheDir
     * @return
     */
    public static File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        //判断sd卡正常挂载并且拥有权限的时候创建文件
        if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }


    /**
     * 检查是否有权限
     * @param context
     * @return
     */
    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

    /**
     *
     * @param takePhoto
     * @return
     */
    public static TakePhoto configTakePhotoOption(TakePhoto takePhoto ){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(false);
        takePhoto.setTakePhotoOptions(builder.create());
        return takePhoto;
    }
//


    /**
     * 压缩配置 ,先行将图片压缩
     * @param takePhoto
     * @param maxSize
     * @param width
     * @param height
     */
    public static   TakePhoto configCompress( TakePhoto takePhoto ,int maxSize,  int width, int height){
        CompressConfig config;
        config=new CompressConfig.Builder()
                .setMaxSize(maxSize)//最大体积
                .setMaxPixel(width>=height? width:height)//最大长度
                .enableReserveRaw(true) //保留原件
                .enablePixelCompress(true)
                .enableQualityCompress(true)
                .create();
        takePhoto.onEnableCompress(config,true);
        return takePhoto;
    }




    /**
     * 裁剪的相关配置
     * @param width  宽
     * @param height 高
     * @param aspect  按 宽/高
     */
    public static CropOptions getCropOptions(  int width, int height ,boolean aspect){
        CropOptions.Builder builder=new CropOptions.Builder();
        //是否按宽/高 裁剪 ,对使用   taketool自带 工具无效
        if(aspect){
            builder.setAspectX(width).setAspectY(height);
        }else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(true); //使用自带工具
        return builder.create();
    }

    public static CropOptions getCropOptions(){
        CropOptions.Builder builder=new CropOptions.Builder();
        //是否按宽/高 裁剪 ,对使用   taketool自带 工具无效
        builder.setOutputX(800).setOutputY(800);
        builder.setWithOwnCrop(true); //使用自带工具
        return builder.create();
    }


    /**
     * 拍照
     * @param takePhoto  从相机获取图片并裁剪
     */
    public static void takePhotosPickFromCaptureWithCrop(TakePhoto takePhoto ){
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
    }



    /**
     * 拍照
     * @param takePhoto 从相机获取图片(不裁剪)
     */
    public static void takePhotosPickFromCapture(TakePhoto takePhoto ){
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        takePhoto.onPickFromCapture(imageUri);
    }






}
