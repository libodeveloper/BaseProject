package com.example.jzg.myapplication.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.bean.NewStyle;
import com.example.jzg.myapplication.utils.MyToast;

import org.greenrobot.eventbus.EventBus;

/**
 * 车型确认
 * Created by zealjiang on 2016/12/8 20:23.
 * Email: zealjiang@126.com
 */

public class CarStyleConfirmDialog {

    private Activity activity;
    private Dialog dialog;
    private String content;
    private NewStyle newStyle;

    public CarStyleConfirmDialog(Activity activity) {
        this.activity = activity;
    }

    public void createDialog(){
        dialog = new Dialog(activity, R.style.MyDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.chexing_view,null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        init(view);
		/*
         * 将对话框的大小按屏幕大小的百分比设置
         */
        Window dialogWindow = dialog.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    private void init(View view){
        TextView tvMsg =  (TextView) view.findViewById(R.id.post_msg);
        if(content!=null){
            tvMsg.setText(content);
        }

        CustomRippleButton crbCancel =  (CustomRippleButton) view.findViewById(R.id.crb_cancel);
        crbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //取消确认的品牌、车型、车系
/*                newStyle.setCancel(true);
                EventBus.getDefault().post(newStyle);*/
            }
        });

        CustomRippleButton crbConfirm =  (CustomRippleButton) view.findViewById(R.id.crb_confirm);
        crbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存确认的品牌、车型、车系
                EventBus.getDefault().post(newStyle);
                newStyle.setCancel(false);
                dialog.dismiss();
            }
        });

        CustomRippleButton customRippleButton =  (CustomRippleButton) view.findViewById(R.id.jiucuo);
        customRippleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newStyle==null){
                    MyToast.showShort("车型参数不可为空");
                }
                CorrectorDialog correctorDialog = new CorrectorDialog(activity);
                correctorDialog.setData(newStyle.getStyleId());
                correctorDialog.createDialog();
            }
        });
    }

    public void setData(String content, NewStyle newStyle){
        this.content = content;
        this.newStyle = newStyle;
    }
}
