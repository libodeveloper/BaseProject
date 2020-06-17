package com.example.jzg.myapplication.convenientbanner;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.BannerBean;
import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by jzg on 2015/12/24.
 */
public class ExtendedViewPager1Adapter extends PagerAdapter {
    private List<BannerBean> imageList;
    private Context context;
    PhotoDraweeView imageView;
    public ExtendedViewPager1Adapter(Context context, List<BannerBean> list) {
        this.context = context;
        this.imageList = list;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, null);
         imageView = (PhotoDraweeView) view.findViewById(R.id.photo_img);
        TextView title = (TextView) view.findViewById(R.id.title);
        loadResDrawablePic(context,imageView,imageList.get(position).getBannerPath());


        title.setText(imageList.get(position).getBannerName());

        container.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public static void loadResDrawablePic(Context context, PhotoDraweeView simpleDraweeView, int id) {
        Uri uri = Uri.parse("res://" +
                context.getPackageName() +
                "/" + id);
        simpleDraweeView.setPhotoUri(uri);
    }


    public PhotoDraweeView getImageView(){
        return imageView;
    }

}