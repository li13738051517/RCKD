package com.rckd.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.rckd.R;
import com.rckd.activity.BrowserActivity;
import com.rckd.activity.MainActivity;
import com.rckd.activity.SpaceImageDetailActivity;
import com.rckd.view.SquareCenterImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.rckd.R.id.imageView2;

/**
 * Created by LiZheng on 2017/6/2 0002.
 */
public class ImageAdapterTImage extends BaseAdapter {
    private Context context;
    private List<String> urls;//路径
    private Activity activity;

   private ImageAdapterTImage(){

    }
//    public ImageAdapterTImage(Context context , List<String> urls){
//        this.context=context;
//        this.urls=urls;
//    }

    public ImageAdapterTImage (Activity activity ,List<String> urls){
        this.activity=activity;
        this.urls=urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //----------------------
//        ImageView imageView=new ImageView(context);
//        Glide.with(context).load(new File(urls.get(position))).placeholder(R.mipmap.image_placholder).error(R.drawable.upload).into(imageView);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(250, 250));//此处需要适配,建议使用dimens 和屏幕大小比较
//        return imageView;
        //--------------------------2
        final SquareCenterImageView imageView = new SquareCenterImageView(activity.getApplicationContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(activity).load(new File(urls.get(position))).placeholder(R.mipmap.image_placholder).error(R.drawable.upload).into(imageView);
//        Glide.with(activity).load(urls.get(position)).placeholder(R.mipmap.image_placholder).error(R.drawable.upload).into(imageView);

//        ImageLoader.getInstance().displayImage(datas.get(position), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SpaceImageDetailActivity.class);
                intent.putExtra("images", (ArrayList<String>) urls);
                intent.putExtra("position", position);
                int[] location = new int[2];
                imageView.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);

                intent.putExtra("width", imageView.getWidth());
                intent.putExtra("height", imageView.getHeight());
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });

        return imageView;
    }
}
