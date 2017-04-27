package com.example.jzg.myapplication.horizontalverticalscroll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.AdmixedData;

import java.util.List;


/**
 *
 * @author zealjiang
 * @time 2016/12/29 15:38
 */
public class AdmixedLeftAdapter extends RecyclerView.Adapter {
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    private Context context;
    private List<AdmixedData.ShowDataBean> showDataList;

    public AdmixedLeftAdapter(Context context, List<AdmixedData.ShowDataBean> showDataList) {
        this.context = context;
        this.showDataList = showDataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType);
        return holder;
    }

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType) {

        View titleView = View.inflate(context, R.layout.left_item, null);
        View itemView = View.inflate(context, R.layout.admixed_item1, null);

        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_1:
                holder = new AdmixedLeftAdapter.ItemViewHolder(itemView);
                break;
            case TYPE_2:
                holder = new AdmixedLeftAdapter.TitleViewHolder(titleView);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_1:
                ((ItemViewHolder)holder).title.setText(showDataList.get(position).getPropertyName());
                break;
            case TYPE_2:
                ((TitleViewHolder)holder).title.setText(showDataList.get(position).getPropertyName());
                break;
            default:

                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if(showDataList.get(position).getDataType()==2){
            return TYPE_2;
        }else{
            return TYPE_1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return showDataList.size();
    }


    class TitleViewHolder extends RecyclerView.ViewHolder{

        public TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }


}
