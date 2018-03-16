package com.example.jzg.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.jzg.myapplication.view.SingleTouchView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libo on 2018/1/9.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class TestActivty extends AppCompatActivity {
    @BindView(R.id.SingleTouchView)
    com.example.jzg.myapplication.view.SingleTouchView SingleTouchView;
    @BindView(R.id.chSave)
    CheckBox checkBox;
    @BindView(R.id.cbVisible)
    CheckBox cbVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        SingleTouchView.setEditable(false);
                        SingleTouchView.invalidate();
                    }else {
                        SingleTouchView.setEditable(true);
                        SingleTouchView.invalidate();
                    }
            }
        });
        cbVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        SingleTouchView.setImageDegree(0);
                        SingleTouchView.setImageScale(1f);
                        SingleTouchView.setVisibility(View.VISIBLE);
                    }else {
                        SingleTouchView.setVisibility(View.GONE);
                    }
            }
        });


    }
}
