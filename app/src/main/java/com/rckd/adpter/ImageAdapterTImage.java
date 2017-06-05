package com.rckd.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.rckd.R;

import java.io.File;
import java.util.List;

import static com.rckd.R.id.imageView2;

/**
 * Created by LiZheng on 2017/6/2 0002.
 */
public class ImageAdapterTImage extends BaseAdapter {
    private Context context;
    private List<String> urls;//路径

    public ImageAdapterTImage(Context context , List<String> urls){
        this.context=context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView=new ImageView(context);
        Glide.with(context).load(new File(urls.get(position))).placeholder(R.mipmap.image_placholder).error(R.drawable.upload).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(250, 250));//此处需要适配,建议使用dimens 和屏幕大小比较
        return imageView;
    }
}
