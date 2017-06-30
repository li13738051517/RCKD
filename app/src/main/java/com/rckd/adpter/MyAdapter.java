package com.rckd.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rckd.R;
import com.rckd.bean.UserNew;
import com.rckd.bean.UserOlde;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangCheng on 2017/6/27.
 */
public class MyAdapter extends BaseAdapter {

    private List<List<UserNew>> listNew=new ArrayList<>();//子数据3个一组(所有数据)
    private List<List<UserNew>> listNew2=new ArrayList<>();//子数据3个一组(用于显示的数据)
    private Context context;
    public MyAdapter(Context context, List<UserOlde> lists){
        this.context=context;
        //将原始数据转换成新数据
        initList(lists);
        MyNotifyDataSetChanged();
    }
    public void initList(List<UserOlde> lists){
        //一级数据补空
        if(lists.size()%3!=0){
            int z=3-lists.size()%3;
            for (int i=0;i<z;i++){
                List<String> lis=new ArrayList<>();
                lists.add(new UserOlde("",lis));
            }
        }
        for (int i=2;i<lists.size();i=i+3){
            List<UserNew> list=new ArrayList<>();
            listNew.add(list);
            for (int j=2;j>=0;j--){
                list.add(new UserNew(lists.get(i-j).getName(),true,""));
                if(lists.get(i-j).getChild().size()%3!=0){
                    int y=3-lists.get(i-j).getChild().size()%3;
                    for (int k=0;k<y;k++){
                        lists.get(i-j).getChild().add("");//二级数据补空
                    }
                }
                for (int m=2;m<lists.get(i-j).getChild().size();m=m+3){
                    List<UserNew> list1=new ArrayList<>();
                    listNew.add(list1);
                    for (int n=2;n>=0;n--){
                        list1.add(new UserNew(lists.get(i-j).getChild().get(m-n),false,lists.get(i-j).getName()));
                    }
                }
            }
        }

    }

    /**
     * 重写刷新
     */
    public void MyNotifyDataSetChanged(){
        listNew2.clear();
        String clin="";
        for (int i=0;i<listNew.size();i++){
            if(listNew.get(i).get(0).isOne()){
                listNew2.add(listNew.get(i));
                for (int j=0;j<listNew.get(i).size();j++){
                    if(listNew.get(i).get(j).isClin()){
                        clin=listNew.get(i).get(j).getName();//选中的一级目录名称
                    }
                }
            }else {
                if(!TextUtils.isEmpty(clin)){
                    if(clin.equals(listNew.get(i).get(0).getParent())){
                        listNew2.add(listNew.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return listNew2==null?0:listNew2.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View view = null;
        if (null == view) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_adapter, null);
            holder.tv0= (TextView) view.findViewById(R.id.tv_0);
            holder.tv1= (TextView) view.findViewById(R.id.tv_1);
            holder.tv2= (TextView) view.findViewById(R.id.tv_2);
            holder.view= (LinearLayout) view.findViewById(R.id.ll);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        for (int i=0;i<listNew2.get(position).size();i++){
            ((TextView)holder.view.getChildAt(i)).setText(listNew2.get(position).get(i).getName());
            if(listNew2.get(position).get(i).isOne()){
                holder.view.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.white));
                final int finalI = i;
                if(listNew2.get(position).get(i).isClin()){
                    holder.view.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                holder.view.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(listNew2.get(position).get(finalI).getName()))return;
                        if(listNew2.get(position).get(finalI).isClin()){
                            listNew2.get(position).get(finalI).setClin(false);
                        }else {
                            cleaClin();
                            listNew2.get(position).get(finalI).setClin(true);
                        }
                        MyNotifyDataSetChanged();
                    }
                });
            }else {
                final String clin=listNew2.get(position).get(i).getName();
                holder.view.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.green));
                final int finalI1 = i;
                holder.view.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(listNew2.get(position).get(finalI1).getName()))return;
                        Toast.makeText(context, "选中："+clin, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return view;
    }
    class ViewHolder{
        TextView tv0,tv1,tv2;
        LinearLayout view;
    }

    public void cleaClin(){
        for (int i=0;i<listNew2.size();i++){
            for (int j=0;j<listNew2.get(i).size();j++){
                if(listNew2.get(i).get(j).isClin()){
                    listNew2.get(i).get(j).setClin(false);
                }
            }
        }
    }
}
