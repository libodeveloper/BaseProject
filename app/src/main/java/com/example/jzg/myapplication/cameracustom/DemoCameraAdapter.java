package com.example.jzg.myapplication.cameracustom;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.PictureItem;
import com.example.jzg.myapplication.utils.FrescoImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by 李波 on 2017/1/20.
 * recyclerView Adapter
 */
public class DemoCameraAdapter extends Adapter<DemoCameraAdapter.MyViewHolder> {

    private ArrayList<PictureItem> data;
    private LayoutInflater layoutInflater;
    private Context context;
    float size=0;
    float size_=0;
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setData(ArrayList<PictureItem> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public DemoCameraAdapter(Context context, ArrayList<PictureItem> data){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public DemoCameraAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(layoutInflater.inflate(R.layout.item_demo_camera,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final DemoCameraAdapter.MyViewHolder holder, final int position) {
        FrescoImageLoader.displayImage(context,holder.sdvPhoto,data.get(position).getPicPath());

        //整体item条目的监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                int pos = holder.getLayoutPosition();
                if (mOnItemClickLitener!=null)
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = holder.getLayoutPosition();
                if (mOnItemClickLitener!=null)
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                return true; //这里必须返回 true 否则事件会继续传递，也就是 onClick 也会执行
            }
        });

        //item条目里包含的控件单独设置的监听
//        holder.sdvPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = holder.getLayoutPosition();
//                Toast.makeText(context,pos+"tv== "+position,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //刷新单个 item 数据 前提是对应position上的数据源改变了
    public void notifyItem(int position,ArrayList<PictureItem> data){
        this.data = data;
        notifyItemChanged(position);
    }

    //添加动画
    public void addData(int position)
    {
//        data.add(position, "Insert One");
        notifyItemInserted(position);
    }

    //删除动画
    public void removeData(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MyViewHolder extends ViewHolder{
        public SimpleDraweeView sdvPhoto;
        public MyViewHolder(View itemView) {
            super(itemView);
            sdvPhoto = (SimpleDraweeView) itemView.findViewById(R.id.photo_img);
        }
    }

}
