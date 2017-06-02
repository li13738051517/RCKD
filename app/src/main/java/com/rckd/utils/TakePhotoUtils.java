package com.rckd.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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

import static android.R.attr.externalService;
import static android.R.attr.tag;
import static android.R.attr.width;
import static com.baidu.location.h.a.i;
import static com.baidu.location.h.j.b;
import static com.baidu.location.h.j.c;
import static com.baidu.location.h.j.t;
import static com.darsh.multipleimageselect.helpers.Constants.limit;

/**
 * Created by LiZheng on 2017/5/23 0023.
 */

public class TakePhotoUtils {
    private static String  tag=TakePhotoUtils.class.getName();
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
     *
     * @param takePhoto
     * @param cropTyeWithOwn 自带相册
     * @param correctYes 是否矫正
     * @return
     */
    public static TakePhoto configTakePhotoOption(TakePhoto takePhoto  , boolean cropTyeWithOwn ,boolean   correctYes){
        TakePhotoOptions.Builder builder=new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(cropTyeWithOwn);
        builder.setCorrectImage(correctYes);
        takePhoto.setTakePhotoOptions(builder.create());
        return takePhoto;
    }


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
     *
     * 压缩工具类
     * @param takePhoto
     * @param cropYes
     * @param cropTyeWithOwn
     * @param maxSize
     * @param width
     * @param height
     * @param reserveRaw
     * @param showProgressBar
     * @return
     */
    public static   TakePhoto configCompress(    TakePhoto takePhoto , boolean cropYes,
                                                 boolean cropTyeWithOwn, int maxSize,  int width, int height ,
                                                 boolean reserveRaw   , boolean showProgressBar){
        if (!cropYes){
            takePhoto.onEnableCompress(null,false);
        }
        CompressConfig config =null;
        //选择压缩工具模式  ,可以使用taketools
        if (cropTyeWithOwn){
            config=new CompressConfig.Builder()
                    .setMaxSize(maxSize)//最大体积
                    .setMaxPixel(width>=height? width:height)//最大长度
                    .enableReserveRaw(reserveRaw) //保留原件
                    .create();
        }else {
            //you can use Lu办Optition
            LubanOptions option=new LubanOptions.Builder()
                    .setMaxHeight(height)
                    .setMaxWidth(width)
                    .setMaxSize(maxSize)
                    .create();
            config=CompressConfig.ofLuban(option);
            config.enableReserveRaw(reserveRaw);
        }
        takePhoto.onEnableCompress(config,showProgressBar);
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



    public static CropOptions getCropOptions( boolean cropYes, boolean picIsAspect,   int width, int height ,   boolean cropTyeWithToolsOwn ){
        if (!cropYes){
            return null;
        }
        CropOptions.Builder builder=new CropOptions.Builder();
        if (picIsAspect){
            //是否按宽/高 裁剪 ,对使用   taketool自带 工具无效
            builder.setAspectX(width).setAspectY(height);
        }else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(cropTyeWithToolsOwn); //使用自带工具
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


    /**
     *
     * @param takePhoto
     * @param fileFromCamera
     * @param cropYes
     * @param limitNum
     * @param IsGallery
     * @param picIsAspect
     * @param width
     * @param height
     * @param cropTyeWithToolsOwn
     */

    //照相之前应该先进行照相机相关设置 ,以及配置 压缩图片 ,裁剪等 ,万能写法,以后直接调用
    public static void takePhotosAll(TakePhoto takePhoto,boolean fileFromCamera, boolean cropYes, int limitNum ,boolean IsGallery , boolean picIsAspect,   int width, int height ,   boolean cropTyeWithToolsOwn   ){
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs(); ///storage/emulated/0/temp
        Uri imageUri = Uri.fromFile(file);///storage/emulated/0/temp/1496301638934.jpg
        //判断图片来源 ,是否来源于相机
          if (fileFromCamera){
              Log.e(tag," fileFromCamera ==true ");
              if(cropYes){
                  Log.e(tag," fileFromCamera ==true , cropYes==true  ");
                  takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions(cropYes ,picIsAspect,width,  height ,  cropTyeWithToolsOwn));
              }else {
                  Log.e(tag," fileFromCamera ==true , cropYes==fasle  ");
                  takePhoto.onPickFromCapture(imageUri);
              }
          }
          //当图片来源不是相机时
          else{
              Log.e(tag," fileFromCamera ==false ");
                  if(limitNum>1){
                      Log.e(tag," fileFromCamera ==false,limitNum>1  ");
                      if(cropYes){
                          Log.e(tag," fileFromCamera ==false,limitNum>1 ,cropYes==true  ");
                          takePhoto.onPickMultipleWithCrop(limitNum,getCropOptions(cropYes ,picIsAspect,width,  height ,  cropTyeWithToolsOwn));
                      }else {
                          Log.e(tag," fileFromCamera ==false,limitNum>1 ,cropYes==fasle  ");
                          takePhoto.onPickMultiple(limitNum);
                      }
                      return; //跳出
                  }
              Log.e(tag," fileFromCamera ==false,limitNum<=1 ");
              //相册 fileFromCamera ==false,limitNum<1  ,IsGallery ==true ,cropYes == true
              if (IsGallery){
                  Log.e(tag," fileFromCamera ==false,limitNum<=1  ,IsGallery ==true ");
                  if (cropYes){
                      Log.e(tag," fileFromCamera ==false,limitNum<=1  ,IsGallery ==true ,cropYes == true");
                      takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions(cropYes ,picIsAspect,width,  height ,  cropTyeWithToolsOwn));
                  }
                  else{
                      Log.e(tag," fileFromCamera ==false,limitNum<=1  ,IsGallery ==true ,cropYes == false");
                      takePhoto.onPickFromGallery();
                  }
                  return;
              }
              //文件
              else {
                  Log.e(tag," fileFromCamera ==false,limitNum<1  ,IsGallery ==fasle ");
                  if(cropYes){
                      Log.e(tag," fileFromCamera ==false,limitNum<1  ,IsGallery ==fasle ,IsGallery ==fasle , cropYes ==true");
                      takePhoto.onPickFromDocumentsWithCrop(imageUri,getCropOptions(cropYes ,picIsAspect,width,  height ,  cropTyeWithToolsOwn));
                  }else {
                      Log.e(tag," fileFromCamera ==false,limitNum<1  ,IsGallery ==fasle ,IsGallery ==fasle , cropYes ==false");
                      takePhoto.onPickFromDocuments();
                  }
              }


          }


    }

    //相册
   public static void takePhotosPickFromGalleryWithCrop(TakePhoto takePhoto ) {
       File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
       if (!file.getParentFile().exists())file.getParentFile().mkdirs();
       Uri imageUri = Uri.fromFile(file);
       takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
   }




}
