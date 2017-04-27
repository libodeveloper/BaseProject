package com.example.jzg.myapplication.recyclerview;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView.*;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.view.AutoFitTextView;
import com.example.jzg.myapplication.view.CustomTextView;

import java.util.ArrayList;

/**
 * Created by 李波 on 2017/1/20.
 * recyclerView Adapter
 */
public class RvAdapter extends Adapter<RvAdapter.MyViewHolder> {

    private ArrayList<String> data;
    private LayoutInflater layoutInflater;
    private Context context;
    float size=0;
    float size_=0;
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public RvAdapter(Context context,ArrayList<String> data){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(layoutInflater.inflate(R.layout.rv_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RvAdapter.MyViewHolder holder, final int position) {
            holder.tv1.setText(data.get(position));


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
            holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getLayoutPosition();
                Toast.makeText(context,pos+"tv== "+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //刷新单个 item 数据 前提是对应position上的数据源改变了
    public void notifyItem(int position){
        notifyItemChanged(position);
    }

    //添加动画
    public void addData(int position)
    {
        data.add(position, "Insert One");
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
        public TextView tv1;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
        }
    }

}
