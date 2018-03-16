package com.example.jzg.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.jzg.myapplication.view.SetPolyToPoly;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libo on 2018/1/9.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class TestSalceActivty extends AppCompatActivity {

    @BindView(R.id.poly)
    SetPolyToPoly poly;
    @BindView(R.id.point0)
    RadioButton point0;
    @BindView(R.id.point1)
    RadioButton point1;
    @BindView(R.id.point2)
    RadioButton point2;
    @BindView(R.id.point3)
    RadioButton point3;
    @BindView(R.id.point4)
    RadioButton point4;
    @BindView(R.id.group)
    RadioGroup group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_salce_test);
        ButterKnife.bind(this);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.point0: poly.setTestPoint(0); break;
                    case R.id.point1: poly.setTestPoint(1); break;
                    case R.id.point2: poly.setTestPoint(2); break;
                    case R.id.point3: poly.setTestPoint(3); break;
                    case R.id.point4: poly.setTestPoint(4); break;
                }
            }
        });

    }
}
