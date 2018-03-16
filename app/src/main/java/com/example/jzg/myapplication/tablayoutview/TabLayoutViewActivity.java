package com.example.jzg.myapplication.tablayoutview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.popWindow.PopwindowUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2018/3/16.
 *
 * @Email: libo@jingzhengu.com
 * @Description: tablayout + viewp + view 模式 （view：轻量级，界面简洁简单时不必费时费力用fragment）
 */
public class TabLayoutViewActivity extends AppCompatActivity {

    @BindView(R.id.bt)
    Button bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_view);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.bt)
    public void onViewClicked() {
        PopwindowUtils.popWindowBottomToTop(this,bt);
    }
}
