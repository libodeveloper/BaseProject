package com.example.jzg.myapplication.tagflowlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.tagflowlayout.widget.FlowLayout;
import com.example.jzg.myapplication.tagflowlayout.widget.TagAdapter;
import com.example.jzg.myapplication.tagflowlayout.widget.TagFlowLayout;

import java.util.List;

/**
 * Created by 李波 on 2017/1/20.
 * tagFlowLayout 流布局的适配器
 */
public class TagFlowLayoutAdapter extends TagAdapter<TagItemBean> {
    private TagFlowLayout layout;
    private Context context;

//    public TagFlowLayoutAdapter(String[] params, Context context) {
//        super(params);
//        this.context = context;
//    }
//
//    public TagFlowLayoutAdapter(String[]  params,TagFlowLayout layout, Context context) {
//        super(params);
//        this.layout = layout;
//        this.context = context;
//    }

    public TagFlowLayoutAdapter(List<TagItemBean> params, TagFlowLayout layout, Context context) {
        super(params);
        this.layout = layout;
        this.context = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, TagItemBean s) {
        TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tv_tag,layout,false);

        //动态适配屏幕设置item宽度
//        otherInformationModel.adapterItemWidth(position, tv, tagFlowLayout,tflShowMileage,tflCarNameplate,tflCarColor);

        tv.setText(s.getIemtName()); //设置item项的名称  -> 李波 on 2016/11/24.
        return tv;
    }
}
