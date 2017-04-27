package com.example.jzg.myapplication.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郑有权 on 2016/11/24.
 */

public class FileAccessI implements Serializable {

    RandomAccessFile oSavedFile;
    long nPos;


    public FileAccessI() throws IOException {
        this("", 0);
    }

    public FileAccessI(String sName, long nPos) throws IOException {
        oSavedFile = new RandomAccessFile(sName, "rw");//创建一个随机访问文件类，可读写模式
        this.nPos = nPos;
        oSavedFile.seek(nPos);
    }

    public synchronized int write(byte[] b, int nStart, int nLen) {
        int n = -1;
        try {
            oSavedFile.write(b, nStart, nLen);
            n = nLen;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
    }

    //每次读取102400字节
    public synchronized Detail getContent(long nStart) {
        Detail detail = new Detail();
        detail.b = new byte[102400];
        try {
            oSavedFile.seek(nStart);
            detail.length = oSavedFile.read(detail.b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detail;
    }


    public class Detail {

        public byte[] b;
        public int length;
    }

    //获取文件长度
    public long getFileLength() {
        Long length = 0l;
        try {
            length = oSavedFile.length();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return length;
    }

    /**
     * 文件切块
     *
     * @param fileName   原始文件
     * @param curSection    每段分割大小
     * @return
     */
    public static List<File> returnTempFile(String fileName, long curSection) {

        List<File> files = new ArrayList<>();
        if(TextUtils.isEmpty(fileName)){
            return  null;
        }
        File fileBig = new File(fileName);
        //如果原始文件不存在
        if(!fileBig.exists()){
            return  null;
        }
        long fileLength = fileBig.length();
        int chunks = (int) (fileLength / curSection + (fileLength % curSection > 0 ? 1 : 0));

        for (int i = 0; i < chunks; i++) {
            File file;
            file = new File(fileName + "_" + i+".tmp");
            //如果文件不存在则拆分
            if (!file.exists()) {
                try {
                    RandomAccessFile ras = new RandomAccessFile(fileName, "rw");
                    ras.seek(curSection * i);
                    byte[] buff = new byte[1024];
                    FileOutputStream tmpOut = new FileOutputStream(file, true);
                    //用于保存临时读取的字节数
                    int hasRead = 0;
                    long Read = curSection * i;
                    long end;
                    if (i != chunks - 1) {
                        end = curSection * i + curSection;
                    } else {
                        end = fileLength;
                    }

                    //循环读取插入点后的内容
                    while (Read < end) {
                        hasRead = ras.read(buff);
                        if (hasRead != -1) {
//                            String str = new String(buff, 0, hasRead);
                            // 将读取的数据写入临时文件中
                            tmpOut.write(buff, 0, hasRead);
                            Read += hasRead;
                        }
                        tmpOut.flush();
                    }
                    tmpOut.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            files.add(file);

        }

        return files;
    }
    /**
     * 文件切块，删除
     *
     * @param fileName   原始文件
     * @param curSection    每段分割大小
     * @return
     */
    public static void delTempFile(String fileName, long curSection) {

        List<File> files = new ArrayList<>();
        if(TextUtils.isEmpty(fileName)){
            return ;
        }
        File fileBig = new File(fileName);
        //如果原始文件不存在
        if(!fileBig.exists()){
            return ;
        }
        long fileLength = fileBig.length();
        int chunks = (int) (fileLength / curSection + (fileLength % curSection > 0 ? 1 : 0));
        for (int i = 0; i < chunks; i++) {
            File file;
            file = new File(fileName + "_" + i+".tmp");
            //如果文件不存在则拆分
            if (file.exists()) {
                file.delete();
            }
        }

    }


}

