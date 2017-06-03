package com.rckd.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rckd.R;
import com.rckd.bean.BaseIcon;

import java.util.List;

/**
 * Created by LiZheng on 2017/5/22 0022.
 * 自定义adapter 实现动态添加删除功能
 */
public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<BaseIcon> datas;
    private boolean isShowDelete;
    public GridAdapter(Context context,List<BaseIcon> datas){
        this.context=context;
        this.datas=datas;
    }


    //返回子项的个数
    @Override
    public int getCount() {
        return (datas.size()+1);
    }
    //返回子项对应的对象
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    //返回子项的下标
    @Override
    public long getItemId(int position) {
        return position;
    }
    //返回子项视图
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.bar_item_grild,null);
            viewHolder=new ViewHolder();
            viewHolder.animalImage=(ImageView)view.findViewById(R.id.iv);
            viewHolder.animalName=(TextView)view.findViewById(R.id.tv_phne);
            viewHolder.deleteImage=(ImageView)view.findViewById(R.id.delete_markView);
            view.setTag(viewHolder);//设置tag
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();//由tag获取对象
        }
        if(position<datas.size()) {
           BaseIcon animal= (BaseIcon) getItem(position);
            viewHolder.animalName.setText(animal.getiName());
            viewHolder.animalImage.setImageResource(animal.getiId());
            viewHolder.deleteImage.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);//根据标识位isShowDelete决定是否显示删除图片按钮
            if (isShowDelete) {
                viewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datas.remove(position);
                        setIsShowDelete(false);
                    }
                });
            }
        }else{
            viewHolder.animalName.setText("点击添加");
            viewHolder.animalImage.setImageResource(R.drawable.upload);
            viewHolder.deleteImage.setVisibility(View.GONE);
        }
        return view;
    }
    //创建ViewHolder类
    class ViewHolder{
        ImageView animalImage,deleteImage;
        TextView animalName;
    }
    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }
}
