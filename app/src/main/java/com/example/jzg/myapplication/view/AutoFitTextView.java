package com.example.jzg.myapplication.view;

/**
 * Created by libo on 2017/3/21.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 根据内容长度自动调整字体大小，达到显示完所有内容
 *                 注意：不要使用 wrap_content 不确定的属性，必须使用确定大小的属性 如：固定值 或者 match_parent、layout_weight
 */
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;

public class AutoFitTextView extends TextView {
    //unit px
    private static float DEFAULT_MIN_TEXT_SIZE = 5;
    private static float DEFAULT_MAX_TEXT_SIZE = 50;
    // Attributes
    private TextPaint testPaint;
    private float minTextSize;
    private float maxTextSize;

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        testPaint = new TextPaint();
        testPaint.set(this.getPaint());
        // max size defaults to the intially specified text size unless it is too small
        maxTextSize = this.getTextSize();  //获取textview设置的 字体大小 为默认的最大字号
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0&&textHeight>0) {
            //allow diplay rect 获取TextView 的宽高
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            int availableHeight = textHeight - this.getPaddingBottom() - this.getPaddingTop();
            //by the line calculate allow displayWidth
            int autoWidth = availableWidth;  //textview 容纳字符串的总长度
            float mult=1f;
            float add=0;
            if (Build.VERSION.SDK_INT>16)
            {
                mult=getLineSpacingMultiplier();
                add=getLineSpacingExtra();
            }else{
                //the mult default is 1.0f,if you need change ,you can reflect invoke this field;
            }
            // 给textview 的paint 设置 字体大小
            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            int oldline=1,newline=1;
            while ((trySize > minTextSize)) {
                //calculate text singleline width。
                int displayW = (int) testPaint.measureText(text); //传入内容字符串，根据之前设置的字体大小 计算出字符串占据的总长度 px
                //calculate text singleline height。
                int displaH=round(testPaint.getFontMetricsInt(null)*mult+add); //计算字符串的高度（代表一行的高度）

                //如果字符串总长度 大于 textview容纳的长度 就计算减小字体大小
                if (displayW < autoWidth) {
                    break;
                }

                //calculate maxLines  根据textview的高度 和 当前字体大小高度，计算textview能容纳多少行，进而计算textview容纳的总长度 autoWidth
                //减 1 ，规避精度问题，让 autoWidth 更小一点，使其多循环减小字体大小，避免出现 最后一行显示出来 但显示不全的问题（经测试已完美解决）
                newline = availableHeight / displaH-1;
                //if line change ,calculate new autoWidth
                if (newline > oldline) {
                    oldline=newline;
                    autoWidth = availableWidth * newline;
                    continue;
                }
                //try more small TextSize
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize); //设置字体大小然后再重新计算字符串长度

            }
            //setMultiLine
            if (newline>=2)
            {
                this.setSingleLine(false);
                this.setMaxLines(newline+1); //因其上面减了 1 ，所以最后要加上 1 ，让最后一行显示出来
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e("TagSizeChange","new("+w+","+h+") old("+oldw+""+oldh+")");
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w, h);
        }
    }
    //FastMath.round()
    public static int round(float value) {
        long lx = (long) (value * (65536 * 256f));
        return (int) ((lx + 0x800000) >> 24);
    }
}