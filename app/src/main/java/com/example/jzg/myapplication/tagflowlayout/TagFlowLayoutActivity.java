package com.example.jzg.myapplication.tagflowlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.global.Constants;
import com.example.jzg.myapplication.tagflowlayout.widget.FlowLayout;
import com.example.jzg.myapplication.tagflowlayout.widget.TagFlowLayout;
import com.example.jzg.myapplication.tagflowlayout.widget.TagView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by libo on 2017/1/3.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 流布局示例
 */
public class TagFlowLayoutActivity extends AppCompatActivity {

    /**
     * item 横向填充满 TagFlowLayout 布局时的宽度
     */
    private int tagFlowLayoutItemMatchSize;
    /**
     * item 横向占据一半 TagFlowLayout 布局时的宽度
     */
    private int tagFlowLayoutItemHalfSize;
    /**
     * item 横向占据三分之一 TagFlowLayout 布局时的宽度
     */
    private int tagFlowLayoutItemOneThird;

    /**
     * Created by 李波 on 2017/1/20.
     * 注意本流布局不带滚动功能，如需滚动外套一个 Scrollview即可
     */
    private TagFlowLayout tagFlowLayout;
    private ArrayList<TagItemBean> list;
    private TagFlowLayoutAdapter tagFlowLayoutAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagflowlayout);
        tagFlowLayout = (TagFlowLayout) findViewById(R.id.tagFlowLayout);
        list = new ArrayList<>();

        Random ra =new Random();
        for (int i=0;i<100;i++){
//            System.out.println(ra.nextInt(1000000)+1);
            int random = ra.nextInt(1000000)+1;
            TagItemBean df = new TagItemBean(""+random);
            list.add(df);
        }

        tagFlowLayoutAdapter = new TagFlowLayoutAdapter(list,tagFlowLayout,this);
        tagFlowLayout.setAdapter(tagFlowLayoutAdapter);

        //反显数据，或者默认就要选中一些项，都是通过设置 位置角标来反显
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(3);
        set.add(5);
        tagFlowLayoutAdapter.setSelectedList(set);//反显通过adapter这个方法

        //-----start---------最终获取选中的哪些项------------------------------------
        //tagFlowLayout.getSelectedList() 会实时储存选中的项，但它储存的都是选中项对应的 position 位置角标
        HashSet<Integer> selecte = (HashSet<Integer>) tagFlowLayout.getSelectedList();
        Iterator its = selecte.iterator();
        String selecteItem = "";
        if (its.hasNext()) {
            while (its.hasNext()) {
                Integer i = (Integer) its.next();
                selecteItem = i + ",";
            }
        }
        //------end---------------------------------------------------------

        //item 点击监听
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(TagView view, int position, FlowLayout parent) {

                Toast.makeText(TagFlowLayoutActivity.this,"tagView = "+position,Toast.LENGTH_SHORT).show();

                TagView tagView = (TagView) tagFlowLayout.getChildAt(position);
                TextView tv = (TextView) tagView.getTagView();

//                if (tagFlowLayout == tflCarNameplate) {
//                    otherInformationModel.changeTagViewEnabled(position, CARNAMEP_CONTROL, tagV, tagFlowLayout);
//                } else if (tagFlowLayout == tflCarVin) {
//                    otherInformationModel.changeTagViewEnabled(position, CARVINS_CONTROL, tagV, tagFlowLayout);
//                }

                if (tagView.isChecked()){ //选中

                }else {//未选中

                }
                return true;
            }
        });

    }

    /**
     * 切换 TagFlowLayout 里 item 多选 或 单控选 的状态 （某一项选中后其他均不可选）
     *
     * @param position      当前点击的 item 角标
     * @param control       控制项的角标，checked--->true：其他变为不可选状态: false:其他变为可选状态
     * @param tagV          当前Item
     * @param tagFlowLayout 当前tagFlowLayout
     */
    public void changeTagViewEnabled(int position, int control, TagView tagV, TagFlowLayout tagFlowLayout) {
        if (position == control && tagV.isChecked()) {  //如果当前项等于控制项，且是选中状态

            for (int i = 0; i < tagFlowLayout.getChildCount(); i++) {  //遍历所有item
                if (i != control) {  //只要等于控制项的角标 全部置为不可点击状态
                    TagView tag = (TagView) tagFlowLayout.getChildAt(i);
                    tag.isEnabled = false;  //重要
                    tag.setChecked(false);

                    //设置item背景为灰色不可选状态
                    TextView tv = (TextView) tag.getTagView();
                    tv.setBackgroundResource(R.drawable.not_optional_bg1);

                    //清除其他所有选项，只留控制项
                    if (tagFlowLayout.getSelectedList().contains(i)) {
                        tagFlowLayout.getSelectedList().remove(i);
                    }
                }
            }
        } else if (position == control && !tagV.isChecked()) {  //如果当前等于控制项，并且非选中状态
            for (int i = 0; i < tagFlowLayout.getChildCount(); i++) { //遍历所有子项 全部置为可点击状态
                if (i != control) {
                    TagView tag = (TagView) tagFlowLayout.getChildAt(i);
                    tag.isEnabled = true;

                    //设置item背景为初始化的状态选择器
                    TextView tv = (TextView) tag.getTagView();
                    Drawable drawable = getResources().getDrawable(R.drawable.tag_bg1);
                    tv.setBackgroundDrawable(drawable);
                }
            }
        }
    }

    /**
     * 根据屏幕尺寸动态计算 TagFlowLayout 中不同情况的 Item 宽度：占满TagFlowLayout 、 一半、三分之一
     */
    public void calculationFlowLayoutItemWidth(Context context) {
        int rv_width_fragment_other = context.getResources().getDimensionPixelSize(R.dimen.fragment_other_rv_width);
        int tv_margin = context.getResources().getDimensionPixelSize(R.dimen.tv_margin);
        int fragment_other_information_view_width = context.getResources().getDimensionPixelSize(R.dimen.fragment_other_information_view_width);
        int fragment_other_information_padding = context.getResources().getDimensionPixelSize(R.dimen.fragment_other_information_padding);
        if (SysApplication.getScreenSize() != 0) {
            int padHorizontalwidth = Constants.ScreenWidth > Constants.ScreenHeight ? Constants.ScreenWidth : Constants.ScreenHeight;
            int tagFlowLayoutWidth = (padHorizontalwidth - rv_width_fragment_other - fragment_other_information_view_width) / 2;
            tagFlowLayoutItemMatchSize = tagFlowLayoutWidth - fragment_other_information_padding * 2 - tv_margin * 2;
            tagFlowLayoutItemHalfSize = tagFlowLayoutItemMatchSize / 2 - tv_margin;
            tagFlowLayoutItemOneThird = tagFlowLayoutItemMatchSize / 3 - tv_margin * 2;
        }
    }

    /**
     * 根据屏幕尺寸和需求动态适配 TagFlowLayout布局Item的宽度，如果计算宽度失败，就用布局里写死的 dp 值
     *
     * adapter 里的 getView中调用即可
     *
     * @param position
     * @param tv            TagFlowLayout 的 item
     * @param tagFlowLayout
     */
    public void adapterItemWidth(int position, TextView tv, TagFlowLayout tagFlowLayout,
                                 TagFlowLayout tflShowMileage,TagFlowLayout tflCarNameplate,TagFlowLayout tflCarColor) {
        if (tagFlowLayoutItemMatchSize != 0 && tagFlowLayoutItemHalfSize != 0 && tagFlowLayoutItemOneThird != 0) {

            if (tagFlowLayout == tflShowMileage) {
                tv.getLayoutParams().width = tagFlowLayoutItemMatchSize;
                tv.setSelected(true);
            } else if (tagFlowLayout == tflCarNameplate && position == 2) {
                tv.getLayoutParams().width = tagFlowLayoutItemMatchSize;
                tv.setSelected(true);
            } else if (tagFlowLayout == tflCarColor) {
                tv.getLayoutParams().width = tagFlowLayoutItemOneThird;
            } else {
                tv.getLayoutParams().width = tagFlowLayoutItemHalfSize;
            }

        }
    }

}
