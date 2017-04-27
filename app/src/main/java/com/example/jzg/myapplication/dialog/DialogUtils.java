package com.example.jzg.myapplication.dialog;

        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import com.example.jzg.myapplication.R;


/**
 * Created by libo on 2017/4/27.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 确认 取消 title content 通用dialog
 *                 单例模式只存在一个dialog 避免 重复new dialog 导致一些bug
 */
public class DialogUtils {

    public static DialogUtils dialogUtil = new DialogUtils();

    private Dialog dialog = null;

    private boolean canceledOnTouchOutside;
    private boolean cancelable;
    private String cancle;
    private String confrim;
    private String title;
    private String content;

    private TextView tv_title;
    private TextView tv_content;
    private Button bt_cancle;
    private Button bt_confrim;
    private View vw_distance;
    private DialogDismissListener dialogDismissListener;
    private DialogCancleListener  dialogCancleListener;
    private DialogConfirmListener dialogConfirmListener;

    //此方法直接返回 避免了判断空在new的 线程安全问题
    public synchronized static DialogUtils getInstance(){
        return dialogUtil;
    }

    private void initDialogData(){
        canceledOnTouchOutside = false;
        cancelable = false;
        title = null;
        content = null;
        confrim = null;
        cancle = null;
        dialogConfirmListener = null;
        dialogCancleListener = null;
        dialogDismissListener = null;
    }

    private DialogUtils(){}

    public synchronized void onCreateDialog(Context context){
        initDialogData();

        if (dialog==null) {
            dialog = new Dialog(context, R.style.DialogStyle);

            dialog.setContentView(R.layout.dialog);
            tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            tv_content = (TextView) dialog.findViewById(R.id.tv_content);
            bt_cancle = (Button) dialog.findViewById(R.id.bt_cancle);
            bt_confrim = (Button) dialog.findViewById(R.id.bt_confrim);
            vw_distance = (View) dialog.findViewById(R.id.vw_distance);
        }
    }

    public void setCancle(String cancle) {
        this.cancle = setNoEmptyValue(cancle);
    }

    public void setConfrim(String confrim) {
        this.confrim = setNoEmptyValue(confrim);
    }

    public void setContent(String content) {
        this.content = setNoEmptyValue(content);
    }

    public void setTitle(String title) {
        this.title = setNoEmptyValue(title);
    }


    public String setNoEmptyValue(String content){
        String value = null;
        if (!TextUtils.isEmpty(content)&&!content.trim().equals(""))
            value = content;
        return value;
    }

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside){
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

    public void setCancelable(boolean cancelable){
        this.cancelable = cancelable;
    }

    public void setOnCancleListener(DialogCancleListener dialogListener){
        this.dialogCancleListener = dialogListener;
    }

    public void setOnConfrimListener(final DialogConfirmListener dialogListener){
        this.dialogConfirmListener = dialogListener;
    }

    public void setOnDismissListener(final DialogDismissListener dialogListener){
        this.dialogDismissListener = dialogListener;
    }

    /**
     * 显示对话框
     *
     */
    public void showDialog() {
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.setCancelable(cancelable);
        if (TextUtils.isEmpty(title))
            tv_title.setVisibility(View.GONE);
        else
            tv_title.setText(title);

        tv_content.setText(content);

        if (TextUtils.isEmpty(cancle)){
            bt_cancle.setVisibility(View.GONE);
            vw_distance.setVisibility(View.GONE);
        }else{
            bt_cancle.setText(cancle);
        }

        if (TextUtils.isEmpty(confrim)){
            bt_confrim.setVisibility(View.GONE);
            vw_distance.setVisibility(View.GONE);
        }else{
            bt_confrim.setText(confrim);
        }

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCancleListener!=null)
                    dialogCancleListener.cancle();
                dismissDialog();
            }
        });

        bt_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogConfirmListener!=null)
                    dialogConfirmListener.confirm();
                dismissDialog();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogs) {
//                dialog = null;
                if (dialogDismissListener!=null)
                    dialogDismissListener.onDismiss();
            }
        });

        dialog.show();
    }


    /**
     * 关闭对话框
     */
    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
//            dialog = null;
        }
    }

    public interface DialogConfirmListener{
        void confirm();
    }
    public interface DialogCancleListener{
        void cancle();
    }
    public interface DialogDismissListener{
        void onDismiss();
    }
}
