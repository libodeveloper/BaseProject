package com.example.jzg.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.example.jzg.myapplication.interfaces.ICurIndexListener;


/**
 * Created by libo on 2017/10/31.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 自定义可随手指移动的 TextView
 */
public class CustomMoveTextView extends TextView {


    int Screenwidth;
    int Screenheight;

    int maxLeft;
    int maxTop;
    private int lastX, lastY;

    private ICurIndexListener iCurIndexListener;

    private int customTextViewSize;   //标点大小

    private String type;              //标点缺陷类型  如：D = 凹陷......

    private String ascriptionId;      //标点缺陷归属Id，车轮 ， 车灯....

    //以按下抬起的时间间隔来判断是响应单击还是拖拽移动  -> 李波 on 2017/11/7.
    private long startTime = 0;
    private long endTime = 0;

    private boolean isClick;          //是否响应单击事件（要求单击事件和拖动事件互斥）

    public CustomMoveTextView(Context context) {
        super(context);
        Screenwidth = ScreenUtils.getScreenWidth();
        Screenheight = ScreenUtils.getScreenHeight();

    }

    /**
     * Created by 李波 on 2017/11/7.
     * 设置最大左边界 和 最大上边界，控制拖动时不超出指定区域
     */
    public void setMaxTopAndMaxLeft(int customTextViewSize,int marginTopAndBottom){
        this.customTextViewSize = customTextViewSize;
        maxTop  = Screenheight - customTextViewSize - marginTopAndBottom;
        maxLeft = Screenwidth - customTextViewSize;
    }

    public CustomMoveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setICurIndexListener(ICurIndexListener iCurIndexListener){
        this.iCurIndexListener = iCurIndexListener;
    }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            //获取当前view在容器里的角标，方便删除操作  -> 李波 on 2017/10/31.
            int  index=((ViewGroup)getParent()).indexOfChild(this);

            if (iCurIndexListener!=null)
            iCurIndexListener.curIndex(index);

        switch (event.getAction()) {
                       case MotionEvent.ACTION_DOWN:
                           isClick = false;
                           startTime = System.currentTimeMillis();
                           Log.i("TESTTEST", "ACTION_DOWN: "+startTime);
                           lastX = (int) event.getRawX();
                           lastY = (int) event.getRawY();
                           break;
                       case MotionEvent.ACTION_MOVE:
                           isClick = true;
                           int dx = (int) event.getRawX() - lastX;
                           int dy = (int) event.getRawY() - lastY;

                               int left = getLeft() + dx;
                               int top = getTop() + dy;
                               RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

                               layoutParams.height = customTextViewSize;
                               layoutParams.width = customTextViewSize;

                               //控制左边界  -> 李波 on 2017/11/7.
                               if (left <= 0)
                                   layoutParams.leftMargin = 0;
                               else if (left >= maxLeft)
                                   layoutParams.leftMargin = maxLeft;
                               else
                                   layoutParams.leftMargin = left;

                               //控制上边界
                               if (top <= 0)
                                   layoutParams.topMargin = 0;
                               else if (top >= maxTop)
                                   layoutParams.topMargin = maxTop;
                               else
                                   layoutParams.topMargin = top;

                               setLayoutParams(layoutParams);

                               lastX = (int) event.getRawX();
                               lastY = (int) event.getRawY();

                              break;
                       case MotionEvent.ACTION_UP:
                           endTime = System.currentTimeMillis();
                           Log.i("TESTTEST", "ACTION_UPUP: "+endTime);
                           long temp = endTime - startTime;
                           Log.i("TESTTEST", "ACTION_XXXX: "+temp);

                                      //当从点击到抬起小于多少毫秒的时候,则判断为点击,如果超过则不响应点击事件
                                     if ((endTime - startTime) < 100) {
                                          isClick = true;
                                      } else {
                                          isClick = false;
                                     }


                           lastX = (int) event.getRawX();
                           lastY = (int) event.getRawY();

                           //设置拖动时的删除区域，到达区域弹出删除框提醒  -> 李波 on 2017/11/7.
                           if (lastX>maxLeft-customTextViewSize && lastY>maxTop -customTextViewSize){
                               Log.i("TESTTEST", "到达删除区域" );
                               if (iCurIndexListener!=null)
                                   iCurIndexListener.isdelCurIndex();
                           }


                           if (isClick) { //super.onTouchEvent(event)不拦截响应单击事件
                               return super.onTouchEvent(event);
                           }else {
                               return true; //直接拦截掉，只响应拖动事件，不响应单击事件。
                           }

                      }
                    return super.onTouchEvent(event);

           }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAscriptionId() {
        return ascriptionId;
    }

    public void setAscriptionId(String ascriptionId) {
        this.ascriptionId = ascriptionId;
    }

}