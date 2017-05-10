package com.example.jzg.myapplication.cameracustom;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.PictureItem;
import com.example.jzg.myapplication.utils.FrescoImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by 李波 on 2017/5/8.
 * 相机界面底部recycleview适配器
 */
public class PreviewAdapter extends CommonAdapter<PictureItem> {
    private int currPos = 0;
    private Context mContext;
    public PreviewAdapter(Context context, int layoutId, List<PictureItem> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    public void setCurrPos(int currPos) {
        this.currPos = currPos;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder holder, PictureItem pictureItem) {
        holder.setText(R.id.tv_name,pictureItem.getPicName());
        SimpleDraweeView ivPhoto = holder.getView(R.id.iv_photo);

        if(!TextUtils.isEmpty(pictureItem.getPicPath())){
//            if(pictureItem.getPicPath().startsWith("http")){
//                ivPhoto.setImageURI(Uri.parse(pictureItem.getPicPath()));
//            }else if(pictureItem.getPicPath().startsWith("/")){
//                ivPhoto.setImageURI(Uri.fromFile(new File(pictureItem.getPicPath())));
//            }
            FrescoImageLoader.displayImage(mContext,ivPhoto,pictureItem.getPicPath());
        }else{
            ivPhoto.setImageURI(Uri.parse(""));
        }
        RelativeLayout rootView = holder.getView(R.id.rl_rootView);
        if(holder.getItemPosition()==currPos){
            rootView.setBackgroundResource(R.drawable.border_corner_selected);
        }else{
            rootView.setBackgroundResource(0);
        }

    }

}
