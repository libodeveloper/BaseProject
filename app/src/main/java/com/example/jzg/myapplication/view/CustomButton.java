package com.example.jzg.myapplication.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jzg.myapplication.R;


/**
 * Created by 李波 on 2017/8/30.
 * 自定义button 类似开关按钮，点击左右滑动
 *
 * 用法示例：
 *
 *  customButton.setLeftAndRight("2.0订单","3.0订单");

    customButton.setAnimationListener(new CustomButton.AnimationListener() {
        @Override
        public void L2RanimationStart() {
        }

        @Override
        public void R2LanimationStart() {
        }

        @Override
        public void L2RanimationEnd() {
        switchFragment(1);
        }

        @Override
        public void R2LanimationEnd() {
        switchFragment(0);
        }
        });
 *
 */
public class CustomButton extends LinearLayout {


    TextView tvL,tvR,tvM; //分别为左边 ，右边 ，当前状态按钮  -> 李波 on 2017/8/30.
    ObjectAnimator oaAnimatorLtoR;
    ObjectAnimator oaAnimatorRtoL;
    Context context;
    AnimationListener animationListener;

    private int curPosition; //当前选中状态 left - 0 ，Right - 1  -> 李波 on 2017/8/30.

    View parent;
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        parent = View.inflate(context, R.layout.custom_button,this);
        tvL = (TextView) parent.findViewById(R.id.tvL);
        tvR = (TextView) parent.findViewById(R.id.tvR);
        tvM = (TextView) parent.findViewById(R.id.tvM);


        tvL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curPosition=0;
                toLeft(tvM);
            }
        });
        tvR.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curPosition=1;
                toRight(tvM);
            }
        });

    }


    /**
     * Created by 李波 on 2017/8/30.
     * 当前按钮从左边滑动到右边
     */
    private void toRight(View view){
        int parentW = parent.getWidth();
        int tvMW    = tvM.getWidth();
//        int x = SizeUtils.dp2px(context,80);
        int x = parentW - tvMW;
        oaAnimatorLtoR= ObjectAnimator.ofFloat(view, "translationX",x);
        oaAnimatorLtoR.setDuration(500);

        //开始动画之前首先取消当前动画，以免造成动画冲突 在viewpager联动，左右快速滑动时容易冲突  -> 李波 on // 2017/9/4.
        if (oaAnimatorRtoL!=null){
            oaAnimatorRtoL.cancel();
        }

        oaAnimatorLtoR.start();
        animationListener.L2RanimationStart();
        oaAnimatorLtoR.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
//                animationEndListener.L2RanimationStart();  无效？
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                String r = tvR.getText().toString();
                tvM.setText(r);
                animationListener.L2RanimationEnd();


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * Created by 李波 on 2017/8/30.
     * 当前按钮从右边滑动到左边
     */
    private void toLeft(View view){
        oaAnimatorRtoL= ObjectAnimator.ofFloat(view, "translationX",0);
        oaAnimatorRtoL.setDuration(500);

        if (oaAnimatorLtoR!=null){  //开始动画之前首先取消当前动画，以免造成动画冲突  -> 李波 on 2017/9/4.
            oaAnimatorLtoR.cancel();
        }

        oaAnimatorRtoL.start();
        animationListener.R2LanimationStart();
        oaAnimatorRtoL.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
//                animationEndListener.R2LanimationStart();  无效？
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                String L = tvL.getText().toString();
                tvM.setText(L);
                animationListener.R2LanimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    /**
     * Created by 李波 on 2017/8/30.
     * 初始化左右和当前button显示
     */
    public void setLeftAndRight(String l, String r){
        tvL.setText(l);
        tvM.setText(l);
        tvR.setText(r);
    }


    /**
     * Created by 李波 on 2017/8/30.
     * 对外提供联动方法
     */
    public void animatorL2R(){
        toRight(tvM);
    }

    public void animatorR2L(){
        toLeft(tvM);
    }

    /**
     * Created by 李波 on 2017/8/30.
     * 获取当前选中状态 left - 0，Right - 1
     */
    public int getCurPosition(){
        return curPosition;
    }

    public void setAnimationListener(AnimationListener animationListener){
        this.animationListener = animationListener;
    }

   public interface AnimationListener {
       void L2RanimationStart();
       void R2LanimationStart();
       void L2RanimationEnd();
       void R2LanimationEnd();
   }


}
