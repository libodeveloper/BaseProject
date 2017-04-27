/**
 * Project Name:JZGPingGuShi
 * File Name:StringUtil.java
 * Package Name:com.gc.jzgpinggushi.uitls
 * Date:2014-10-8下午2:18:50
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 */

package com.example.jzg.myapplication.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class StringUtil {

    //从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(String fileName, Context context) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
//            result = EncodeUtils.getString(buffer, "UTF-8");//你的文件的编码
//            result = EncodeUtils.base64Encode2String(buffer);
            result = new String(buffer, "UTF-8");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + " 星期" + mWay;
    }

    public static String getStringByUrlUTF8(String str)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "utf-8");
    }

    /**
     * 2015-12  --> 2015
     */
    public static int getYear4String(String yearStr) {
        int year = 0;
        try {
            if ("".equals(yearStr) || null == yearStr) {
                year = 0;
            } else {
                if (yearStr.indexOf("-") != -1) {
                    year = Integer.parseInt(yearStr.substring(0,
                            yearStr.indexOf("-")));
                } else {
                    year = 0;
                }
            }
        } catch (Exception e) {
            year = 0;
        }
        return year;
    }

    public static enum TimeType {
        YEAR, MONTH, DAY
    }

    public static int getCurrent(TimeType type) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int result = 0;
        switch (type) {
            case YEAR:
                result = calendar.get(Calendar.YEAR);
                break;
            case MONTH:
                result = calendar.get(Calendar.MONTH) + 1;
                break;
            case DAY:
                result = calendar.get(Calendar.DAY_OF_MONTH);
                break;
        }
        return result;
    }


    /**
     * 2015-03  --> 3
     */
    public static int getMonth4String(String yearStr) {
        int year = 0;
        try {
            if ("".equals(yearStr) || null == yearStr) {
                year = 0;
            } else {
                String[] datas = yearStr.split("-");
                if (datas.length > 0) {
                    year = Integer.valueOf(datas[1]);
                } else {
                    year = 0;
                }
            }
        } catch (Exception e) {
            year = 0;
        }
        return year;
    }

    /**
     * 2015-03-12  --> 12
     */
    public static int getDay4String(String yearStr) {
        int year = 0;
        try {
            if ("".equals(yearStr) || null == yearStr) {
                year = 0;
            } else {
                String[] datas = yearStr.split("-");
                if (datas.length > 1) {
                    year = Integer.valueOf(datas[2]);
                } else {
                    year = 0;
                }
            }
        } catch (Exception e) {
            year = 0;
        }
        return year;
    }

    public static String getPrice(String price) {
        int C2B = 0;
        try {
            C2B = (int) (Float.parseFloat(price) * 10000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (C2B > 0.01) {
            return String.valueOf(C2B);
        } else {
            return String.valueOf("");
        }
    }

    public static String getPrice1(String price) {
        int C2B = 0;
        try {
            C2B = (int) (Float.parseFloat(price) * 10000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (C2B > 0.01) {
            return price;
        } else {
            return String.valueOf("");
        }

    }

    public static int getIntPrice(String price) {
        int C2B = 0;
        try {
            C2B = (int) (Float.parseFloat(price) * 10000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (C2B > 0.01) {
            return C2B;
        } else {
            return 0;
        }

    }


    /**
     * 限制整数小数的位数
     *
     * @param integerNumber 限制整数位数 0 - 不限制
     * @param decimalNumber 限制小数位数
     * @param editText
     */
    public static void priceTextWatcher(final EditText editText, final int integerNumber, final int decimalNumber) {


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0) {
                    return;
                }
             //限制整数位数
                editText.setSelection(s.length());

                if (integerNumber!=0 && s.length() > integerNumber && !s.toString().contains(".")) {
                    editText.setText(editText.getText().toString().subSequence(0,
                            editText.getText().toString().length() - 1));
                    editText.setSelection(editText.getText().toString().length());
                    //Toast.makeText(context, "请输入小数点", Toast.LENGTH_SHORT).show();
                    return;

             //限制小数位数
                } else if (s.toString().contains(".")
                        && s.toString()
                        .substring(s.toString().lastIndexOf("."))
                        .length() > decimalNumber+1) {
                    editText.setText(s.toString().substring(0,
                            s.toString().lastIndexOf("."))
                            + s.toString()
                            .substring(s.toString().lastIndexOf("."))
                            .subSequence(0, decimalNumber+1));
                    editText.setSelection(s.length() - 1);
                    //Toast.makeText(context, "只能输入小数点后两位", Toast.LENGTH_SHORT).show();
                    return;


             //不允许开头输入0的整数除非是 0. 小数
                } else if ("0".equals(String.valueOf(s.charAt(0)))
                        && !s.toString().contains(".")) {
                    if (s.length() == 2
                            && "0".equals(String.valueOf(s.charAt(0)))) {
                        editText.setText("0");
                        editText.setSelection(1);
                        //	Toast.makeText(context, "请输入小数点", Toast.LENGTH_SHORT).show();
                        return;
                    }

             //不允许开头直接输入 小数点 .
                } else if (".".equals(String.valueOf(s.charAt(0)))) {
                    editText.setText("");
                    //Toast.makeText(context, "第一位不能为小数点", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 判断字符串日期的大小
     *
     * @param endTime   结束日期
     * @param startTime 开始如期
     * @return
     */
    public static Boolean TimeCompare(String endTime, String startTime) {
        //格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date overTime = CurrentTime.parse(endTime);
            Date beginTime = CurrentTime.parse(startTime);

            //判断是否大于两天
            if (((overTime.getTime() - beginTime.getTime())) >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
