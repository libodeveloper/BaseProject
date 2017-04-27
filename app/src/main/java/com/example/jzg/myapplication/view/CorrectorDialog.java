package com.example.jzg.myapplication.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jzg.myapplication.R;
import com.example.jzg.myapplication.horizontalverticalscroll.ICorrector;
import com.example.jzg.myapplication.utils.MyToast;


/**
 * 车型纠错
 * Created by zealjiang on 2016/12/9 10:05.
 * Email: zealjiang@126.com
 */

public class CorrectorDialog implements ICorrector {

    private Activity activity;
    private Dialog dialog;
//    private CorrectorPresenter correctorPresenter;
    private String styleId;

    public CorrectorDialog(Activity activity) {
        this.activity = activity;
    }

    public void createDialog(){
        dialog = new Dialog(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.activity_corrector,null);
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
//        correctorPresenter = new CorrectorPresenter(this);
        final EditText etReason =  (EditText) view.findViewById(R.id.post_msg);

        CustomRippleButton crbCancel =  (CustomRippleButton) view.findViewById(R.id.crb_cancel);
        crbCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CustomRippleButton crbConfirm =  (CustomRippleButton) view.findViewById(R.id.crb_confirm);
        crbConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etReason.getText().toString().trim())){
                    Toast.makeText(activity, "请填写错误原因", Toast.LENGTH_SHORT).show();
                }else{
//                    correctorPresenter.reqCorrector(etReason.getText().toString(),styleId);
                }
            }
        });

    }

    public void setData(String styleId){
        this.styleId = styleId;
    }

    @Override
    public void succeed(String data) {
        MyToast.showShort("提交成功");
        dialog.dismiss();
    }



    @Override
    public void showError(String error) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void dismissLoading() {

    }
}
