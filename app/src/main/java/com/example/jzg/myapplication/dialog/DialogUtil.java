package com.example.jzg.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.jzg.myapplication.R;


/**
 * Created by 李波 on 2017/2/22.
 */
public class DialogUtil {

    private static Dialog dialog = null;

    public static void showDialog(Context context, String title, String content, boolean singleButton, final OrderDialogListener odl) {
        dialog = new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(R.layout.order_dialog);
        dialog.setCanceledOnTouchOutside(false);   //触摸dialog外 是否关闭dialog
        dialog.setCancelable(true);                //点击物理返回键是否关闭dialog

        //dialog关闭时的监听  -> 李波 on 2017/2/22.
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        //当我们要屏蔽 手机返回键不关闭dialog，但同时又要对 返回键做其它操作，那么就直接在dialog下复写onkey方法。
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    if (callback != null) {
//                        callback.backPressed();
//                    }
                }

                return false;
            }
        });

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        Button btConfirm = (Button) dialog.findViewById(R.id.btConfirm);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);

        if (singleButton) {
            btCancel.setVisibility(View.GONE);
            btConfirm.setText("关闭");
        }
        if (title == null || title.equals("null"))
            tvTitle.setVisibility(View.GONE);
        else
            tvTitle.setText(title);

        if (content != null)
            tvContent.setText(content);

        btConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                if (odl!=null)
                odl.confirm();
            }
        });
        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                if (odl!=null)
                odl.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 关闭对话框
     */
    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public interface OrderDialogListener {
        void cancel();
        void confirm();
    }

}