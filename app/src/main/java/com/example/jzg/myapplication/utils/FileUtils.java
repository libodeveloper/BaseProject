package com.example.jzg.myapplication.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils
{

	public static String SDCARD_PAHT = Environment
			.getExternalStorageDirectory().getPath();

	public static String DCIMCamera_PATH = Environment
			.getExternalStorageDirectory() + "/DCIM/Camera/";

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用; false为不可用
	 */
	public static boolean isSDAvailable()
	{
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth, int newHeight)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}
	
	public static Bitmap ResizeBitmap(Bitmap bitmap, int scale)
	{
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(1/scale, 1/scale);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	public static String getNewFileName()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());

		return formatter.format(curDate);
	}

	/**
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @param bm
	 *            要保存的bitmap
	 * 
	 * @param name
	 *            保存的名字 可为null,就根据时间自定义一个文件名
	 * 
	 * @return 以“.jpg”格式保存至相册
	 * 
	 */
	public static Boolean saveBitmapToCamera(Context context, Bitmap bm,
			String name)
	{

		File file = null;

		if (name == null || name.equals(""))
		{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date curDate = new Date(System.currentTimeMillis());
			name = formatter.format(curDate) + ".jpg";
		}

		file = new File(DCIMCamera_PATH, name);
		if (file.exists())
		{
			file.delete();
		}

		try
		{
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;

		} catch (IOException e)
		{

			e.printStackTrace();
			return false;
		}

//		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//		Uri uri = Uri.fromFile(file);
//		intent.setData(uri);
//		context.sendBroadcast(intent);

		return true;
	}

	/**
	 * 
	 * @param bitmap
	 * @param destPath
	 * @param quality
	 */
	public static void writeImage(Bitmap bitmap, String destPath, int quality)
	{
		try
		{
			deleteFile(destPath);
			if (createFile(destPath))
			{
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
				{
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean createFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath
	 *            要删除的文件路径名
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (file.exists())
			{
				return file.delete();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
	 * 
	 * @param
	 */
	public static void deleteDirectory(File dir)
	{
		if (dir.isDirectory())
		{
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++)
			{
				deleteDirectory(listFiles[i]);
			}
		}
		dir.delete();
	}

	/**
	 * 获取文件的大小
	 *
	 * @param fileSize
	 *            文件的大小
	 * @return
	 */
	public static String FormetFileSize(int fileSize) {// 转换文件大小
	   DecimalFormat df = new DecimalFormat("#.00");
	   String fileSizeString = "";
	   if (fileSize < 1024) {
		       fileSizeString = df.format((double) fileSize) + "B";
		   } else if (fileSize < 1048576) {
		       fileSizeString = df.format((double) fileSize / 1024) + "K";
		   } else if (fileSize < 1073741824) {
		       fileSizeString = df.format((double) fileSize / 1048576) + "M";
		   } else {
		       fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		   }
	   return fileSizeString;
	}

	public static boolean saveFile(byte[] b, String outputFile) {
		boolean result = false;
		BufferedOutputStream stream = null;
		try {
			FileOutputStream fstream = new FileOutputStream(new File(outputFile));
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return result;
	}


	/**
	 2. * Try to return the absolute file path from the given Uri
	 3. *
	 4. * @param context
	 5. * @param uri
	 6. * @return the file path or null
	 7. */
public static String getRealFilePath( final Context context, final Uri uri ) {
	  if ( null == uri ) return null;
	   final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
		        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
		       data = uri.getPath();
		    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
		       Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
		        if ( null != cursor ) {
			           if ( cursor.moveToFirst() ) {
				               int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
				               if ( index > -1 ) {
					                    data = cursor.getString( index );
					               }
				           }
			           cursor.close();
			      }
		   }
	   return data;
	}


}
