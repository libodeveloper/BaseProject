package com.example.jzg.myapplication.horizontalverticalscroll;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jzg.myapplication.app.SysApplication;
import com.example.jzg.myapplication.base.BaseActivity;
import com.example.jzg.myapplication.bean.AdmixedData;
import com.example.jzg.myapplication.bean.NewStyle;
import com.example.jzg.myapplication.dialog.DialogUtil;
import com.example.jzg.myapplication.http.ResponseJson;
import com.example.jzg.myapplication.mvpview.IAdmixedInf;
import com.example.jzg.myapplication.utils.MyToast;
import com.example.jzg.myapplication.utils.StringUtil;
import com.example.jzg.myapplication.view.CarStyleConfirmDialog;
import com.example.jzg.myapplication.view.MyHorizontalItem;
import com.example.jzg.myapplication.view.MySyncHorizontalScrollView;
import com.example.jzg.myapplication.view.MyXRecyclerView;
import com.example.jzg.myapplication.view.ScrollBottomScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import com.example.jzg.myapplication.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by 李波 on 2017/3/23.
 * 横纵向 联动表
 */
public class AdmixedContrastActivity extends BaseActivity implements IAdmixedInf,MyHorizontalItem.MyCloseOnClickListener, MyHorizontalItem.MyOkOnClickListener, View.OnClickListener {


    LinearLayout llHrz;
    MySyncHorizontalScrollView mytableScrollview;
    XRecyclerView contentListViewLeft;
    XRecyclerView contentListViewRight;
    MySyncHorizontalScrollView rightContentHorscrollView;
    TextView tvShowAll;
    ScrollBottomScrollView scrollBottomScrollView;
//    @BindView(R.id.title_content)
//    TextView titleContent;
//    @BindView(R.id.title_return)
//    ImageView titleReturn;
//    @BindView(R.id.title)
//    RelativeLayout title;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.ll_hrz)
//    LinearLayout llHrz;
//    @BindView(R.id.mytable_scrollview)
//    MySyncHorizontalScrollView mytableScrollview;
//    @BindView(R.id.contentListViewLeft)
//    XRecyclerView contentListViewLeft;
//    @BindView(R.id.contentListViewRight)
//    XRecyclerView contentListViewRight;
//    @BindView(R.id.rightContentHorscrollView)
//    MySyncHorizontalScrollView rightContentHorscrollView;
//    @BindView(R.id.tv_show_all)
//    TextView tvShowAll;
//    @BindView(R.id.scrollBottomScrollView)
//    ScrollBottomScrollView scrollBottomScrollView;

    private ArrayList<String> styleids;
    private AdmixedData admixedData;
    private String IsDiff = "0";//1差异项 0全部
    private String styleIdsParam;//请求的styleid用|分割
//    private CarParamConfigPresenter carParamConfigPresenter;
    private AdmixedLeftAdapter admixedLeftAdapter;
    private AdmixedRightAdapter admixedRightAdapter;
    private List<AdmixedData.ShowDataBean> showDataList;
    private final int loadNum = 15;
    private Subscription subscribe;

    @Override
    protected Object createPresenter() {
        return null;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_admixed);
//        ButterKnife.bind(this);
//        carParamConfigPresenter = new CarParamConfigPresenter(this);
        Intent intent = getIntent();
        //请求的styleid用|分割
//        styleids = intent.getStringArrayListExtra("styleIds");
//        if (styleids == null || styleids.size() == 0) {
//            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
//            return;
//        }

        showDataList = new ArrayList<>();
        initView();
        startAlreadyListThread();
//       wait2s();
    }

    private void wait2s(){

        subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //----------test data--------------------
                        startAlreadyListThread();

                        //---------------------------------------
                    }
                });
    }

    @Override
    protected void setData() {

    }


    private String FilterData() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < styleids.size(); i++) {
            sb.append(styleids.get(i) + "|");
        }
        sb.deleteCharAt(sb.length()-1);
        styleIdsParam = sb.toString();
        return styleIdsParam;
    }


    private void initView() {

        llHrz = (LinearLayout) findViewById(R.id.ll_hrz);
        mytableScrollview = (MySyncHorizontalScrollView) findViewById(R.id.mytable_scrollview);
        contentListViewLeft = (MyXRecyclerView) findViewById(R.id.contentListViewLeft);
        contentListViewRight = (MyXRecyclerView) findViewById(R.id.contentListViewRight);
        rightContentHorscrollView = (MySyncHorizontalScrollView) findViewById(R.id.rightContentHorscrollView);
        tvShowAll = (TextView) findViewById(R.id.tv_show_all);
        //--------
        tvShowAll.setOnClickListener(this);
        OverScrollDecoratorHelper.setUpOverScroll(mytableScrollview);
        OverScrollDecoratorHelper.setUpOverScroll(rightContentHorscrollView);
        mytableScrollview.setmSyncView(rightContentHorscrollView);
        rightContentHorscrollView.setmSyncView(mytableScrollview);
        //syncScroll(contentListViewLeft,contentListViewRight);

    }

    private void initLeftRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contentListViewLeft.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contentListViewLeft.setLayoutManager(layoutManager);
        contentListViewLeft.setPullRefreshEnabled(true);
        contentListViewLeft.setLoadingMoreEnabled(true);

    }

    private void initRightRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        contentListViewRight.setNestedScrollingEnabled(false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contentListViewRight.setLayoutManager(layoutManager);
        contentListViewRight.setPullRefreshEnabled(false);
    }

    private void loadData() {

                        llHrz.removeAllViews();
                        for (int i = 0; i < admixedData.getCarData().size(); i++) {
                            MyHorizontalItem myHorizontalItem = new MyHorizontalItem(AdmixedContrastActivity.this);
                            myHorizontalItem.setPosition(i);
                            myHorizontalItem.setContent(admixedData.getCarData().get(i).getStyleName());

                            //测试 根据内容长度 固定宽高的 textview 自动缩减字体 大小 以达到显示所有内容
//                            if (i%3==0)
//                            myHorizontalItem.setContent("一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪" +
//                                    " Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI" +
//                                    " 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 " +
//                                    "2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI " +
//                                    "豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus....");
//                            else if (i%3==1)
//                                myHorizontalItem.setContent("一汽奥迪 Q5 2016款 2.0T 自动 40TFSI");
//                            else if (i%3==2)
//                                myHorizontalItem.setContent("一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus一汽奥迪 Q5 2016款 2.0T 自动 40TFSI 豪华型plus");

                            myHorizontalItem.setGuidePrice(admixedData.getCarData().get(i).getNowMsrp()+"万元");
                            llHrz.addView(myHorizontalItem);
                            myHorizontalItem.setMyCloseOnClickListener(AdmixedContrastActivity.this);
                            myHorizontalItem.setMyOkOnClickListener(AdmixedContrastActivity.this);
                        }
                        initRecyclerView();

    }

    private void initRecyclerView(){

        showDataList = admixedData.getShowData();
        initLeftRecyclerView();
        admixedLeftAdapter = new AdmixedLeftAdapter(AdmixedContrastActivity.this, showDataList);
        contentListViewLeft.setAdapter(admixedLeftAdapter);
        admixedLeftAdapter.notifyDataSetChanged();

        initRightRecyclerView();
        admixedRightAdapter = new AdmixedRightAdapter(AdmixedContrastActivity.this, showDataList,admixedData.getCarData().size());
        contentListViewRight.setAdapter(admixedRightAdapter);
        admixedRightAdapter.notifyDataSetChanged();

        //因数据量过大，绘制view太多，导致数据回来 dialog dismiss后 还会空白很长一段时间（绘制view），所以监听listview notifyData
        // 加载完数据后 再关闭 dialog
        contentListViewRight.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                if (contentListViewRight.getChildCount()!=0) {
                    dismissLoading();
                }
            }
        });
    }

    @Override
    public void onCloseClickListener(View view, int position) {
        DialogUtil.showDialog(this,"参配表","正在删除数据中，请稍后...",false,null);
        wait1s(position);
    }

    //等待一秒在执行，因为此段代码在主线程里执行所以，会把dialog直接冲掉，造成显示不出来。
    private void wait1s(final int position){
        subscribe = Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                        if(position<admixedData.getCarData().size()){
                            if(admixedData.getCarData().size()==1){//最后一个不能关闭
                                return;
                            }
                            //删除对应的车辆
                            admixedData.getCarData().remove(position);
                            //删除对应的view
                            llHrz.removeViewAt(position);
                            //重新设置车辆的position
                            for (int i = 0; i < admixedData.getCarData().size(); i++) {
                                ((MyHorizontalItem)llHrz.getChildAt(i)).setPosition(i);
                            }

                            //删除对应的图片
                            for (int i = 0; i < admixedData.getPhotoData().size(); i++) {
                                admixedData.getPhotoData().get(i).getPropertyValue().remove(position);
                            }
                            //删除对应的参数
                            for (int i = 0; i < admixedData.getShowData().size(); i++) {
                                if(admixedData.getShowData().get(i).getDataType()==1){
                                    admixedData.getShowData().get(i).getPropertyValue().remove(position);
                                }
                            }
                        }

                        admixedLeftAdapter.notifyDataSetChanged();
                        contentListViewLeft.scrollToPosition(0);
                        admixedRightAdapter = new AdmixedRightAdapter(AdmixedContrastActivity.this, showDataList,admixedData.getCarData().size());
                        contentListViewRight.setAdapter(admixedRightAdapter);
                        //---------------------------------------
                    }
                });
    }

    @Override
    public void onOkClickListener(View view, int position) {
        if (position < admixedData.getCarData().size()) {
            NewStyle newStyle = new NewStyle();
            newStyle.setName(admixedData.getCarData().get(position).getStyleName());
            newStyle.setNowMsrp(admixedData.getCarData().get(position).getNowMsrp()+"");
            newStyle.setStyleId(admixedData.getCarData().get(position).getStyleID()+"");

            String content = "您已选择" + newStyle.getName() + "款车型\n指导价" + newStyle.getNowMsrp() + "万,请确认这是否为您所需要的车型";
            CarStyleConfirmDialog carStyleConfirmDialog = new CarStyleConfirmDialog(AdmixedContrastActivity.this);
            carStyleConfirmDialog.setData(content,newStyle);
            carStyleConfirmDialog.createDialog();

        }
    }

    public void startAlreadyListThread() {
//        if (TextUtils.isEmpty(FilterData())) {
//            Toast.makeText(this, "请选择车型", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!SysApplication.networkAvailable){
//            MyToast.showShort(getResources().getString(R.string.check_net));
//            return;
//        }


//        carParamConfigPresenter.requestAdmixedData(styleIdsParam,IsDiff); TODO

        showLoading();

        //----------test data--------------------

        new Thread(){
            @Override
            public void run() {
                super.run();

                requestData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                });
            }
        }.start();




        //---------------------------------------

    }

    private void requestData() {
        String carType="";
        if (IsDiff.equals("0")) {
            carType = StringUtil.getFromAssets("cartypeall.txt",
                    AdmixedContrastActivity.this);
        }else if (IsDiff.equals("1")){
            carType = StringUtil.getFromAssets("cartypehide.txt", AdmixedContrastActivity
                    .this);
        }
        ResponseJson<AdmixedData> responseJson = new Gson().fromJson(carType, new TypeToken<ResponseJson<AdmixedData>>(){}.getType());
        AdmixedData admixedData = responseJson.getMemberValue();
        AdmixedContrastActivity.this.admixedData = admixedData;
    }


    public void goBack(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_all:
                if ("显示全部参配".equals(tvShowAll.getText().toString().trim())) {
                    tvShowAll.setText("隐藏相同配置");
                    tvShowAll.setBackgroundColor(getResources().getColor(R.color.red));
                    //要显示全部配置
                    IsDiff = "0";
                    startAlreadyListThread();
                } else {
                    tvShowAll.setText("显示全部参配");
                    tvShowAll.setBackgroundColor(getResources().getColor(R.color.common_btn_blue));
                    IsDiff = "1";
                    startAlreadyListThread();
                }
                break;
            case R.id.tv_zhaopianbidui:
                if(!SysApplication.networkAvailable){
                    MyToast.showShort(getResources().getString(R.string.check_net));
                    return;
                }
                if(admixedData==null){
                    MyToast.showShort("车辆数据不可为空");
                    return;
                }
//                Intent intent = new Intent(this, CarPicComparedActivity.class);
//                intent.putExtra(Constants.ADMIXEDCONTRASTACTIVITY, admixedData);
//                startActivityForResult(intent, 1);
                break;

            default:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe!=null)
        subscribe.unsubscribe();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (data != null) {
                    this.admixedData = (AdmixedData)data.getSerializableExtra("AdmixedData");
                    refresh();
                }
                break;
            default:

                break;
        }
    }

    private void refresh(){
        llHrz.removeAllViews();
        for (int i = 0; i < admixedData.getCarData().size(); i++) {
            MyHorizontalItem myHorizontalItem = new MyHorizontalItem(AdmixedContrastActivity.this);
            myHorizontalItem.setPosition(i);
            myHorizontalItem.setContent(admixedData.getCarData().get(i).getStyleName());
            myHorizontalItem.setGuidePrice(admixedData.getCarData().get(i).getNowMsrp()+"");
            llHrz.addView(myHorizontalItem);
            myHorizontalItem.setMyCloseOnClickListener(AdmixedContrastActivity.this);
            myHorizontalItem.setMyOkOnClickListener(AdmixedContrastActivity.this);
        }

        initRecyclerView();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        DialogUtil.showDialog(this,"参配表","正在加载中，请稍后...",false,null);
    }


    @Override
    public void succeed(AdmixedData admixedData) {
        if(admixedData!=null){
//            ShowDialogTool.hideProgressBar();
            this.admixedData = admixedData;
            loadData();
        }else {
           dismissLoading();
        }
    }
}
