package com.example.jzg.myapplication.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author: voiceofnet
 * email: pengkun@jingzhengu.com
 * phone:18101032717
 * @time: 2016/9/8 14:20
 * @desc:
 */
public class StringHelper {

    public static String getUrl(Map<String,String> params){
        int index = 0;
        StringBuffer sb = new StringBuffer();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String value = params.get(key);
            if(index==0){
                sb.append("?").append(key).append("=").append(value);
            }else{
                sb.append("&").append(key).append("=").append(value);
            }
            index++;
        }
        return sb.toString();
    }

    public static SpannableString getSpannableText(String text, int index) {
        SpannableString sp = new SpannableString(text);
        sp.setSpan(new ForegroundColorSpan(Color.RED), index, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }


    /**
     * L04_P08_A004_F01_D002_1.jpg ---->D002
     * @param st
     * @return
     */
    public static String cut(String st){
        String str ="";
        if(st.contains("_")){
            int ls = st.lastIndexOf("_");
            int ls1  = st.lastIndexOf("_", ls-1);
            str = st.substring(ls1+1, ls);

            System.out.println("ls--"+ls+"--ls1--"+ls1);
            System.out.println("str--"+str);
        }


        return str;

    }


    public static String subFile(String fileName){
        String str = "";
        if(fileName.contains(".jpg")){
            str  =  fileName.substring(0,fileName.indexOf(".jpg"));
        }
        return  str;
    }



}
