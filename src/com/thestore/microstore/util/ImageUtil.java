package com.thestore.microstore.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class ImageUtil {
	private static final String SDCARD_CACHE_IMG_PATH = Environment.getExternalStorageDirectory().getPath() + "/vdian/images/";
	protected static final String TAG = "ImageUtil";

	/**
	 * 保存图片到SD卡
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer) throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdir();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 从SD卡加载图片
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Bitmap loadImage(final String imagePath, final String imgUrl, final ImageCallback callback) {
		Bitmap bitmap = getImageFromLocal(imagePath);
		if (bitmap != null) {
			return bitmap;
		} else {// 从网上加载
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.obj != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						callback.loadImage(bitmap, imagePath);
					}
				}
			};

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						URL url = new URL(imgUrl);
						URLConnection conn = url.openConnection();
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 8192) {};
						Bitmap bitmap = BitmapFactory.decodeStream(bis);
						Message msg = handler.obtainMessage();
						msg.obj = bitmap;
						handler.sendMessage(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			ThreadPoolManager.getInstance().addTask(runnable);
		}
		return null;
	}

	// 返回图片存到sd卡的路径
	public static String getCacheImgPath() {
		return SDCARD_CACHE_IMG_PATH;
	}

	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 
	 * @author Mathew
	 * 
	 */
	public interface ImageCallback {
		public void loadImage(Bitmap bitmap, String imagePath);
	}
	
	
	/**
	 * 文件拷贝
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public static int copySdcardFile(String fromFile, String toFile)
    {
       try
        {        
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) != -1)
			{
			    fosto.write(bt, 0, c);
			}
			
			fosto.close();
			fosfrom.close();
			return 0;           
       } catch (Exception ex)
       {      		
            return -1;
       }
    }
		
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	    
	    if (isKitKat){
	    	 if (DocumentsContract.isDocumentUri(context, uri)) {
	 	        // ExternalStorageProvider
	 	        if (isExternalStorageDocument(uri)) {
	 	            final String docId = DocumentsContract.getDocumentId(uri);
	 	            final String[] split = docId.split(":");
	 	            final String type = split[0];

	 	            if ("primary".equalsIgnoreCase(type)) {
	 	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	 	            }

	 	            // TODO handle non-primary volumes
	 	        }
	 	        // DownloadsProvider
	 	        else if (isDownloadsDocument(uri)) {

	 	            final String id = DocumentsContract.getDocumentId(uri);
	 	            final Uri contentUri = ContentUris.withAppendedId(
	 	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	 	            return getDataColumn(context, contentUri, null, null);
	 	        }
	 	        // MediaProvider
	 	        else if (isMediaDocument(uri)) {
	 	            final String docId = DocumentsContract.getDocumentId(uri);
	 	            final String[] split = docId.split(":");
	 	            final String type = split[0];

	 	            Uri contentUri = null;
	 	            if ("image".equals(type)) {
	 	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	 	            } else if ("video".equals(type)) {
	 	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	 	            } else if ("audio".equals(type)) {
	 	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	 	            }

	 	            final String selection = "_id=?";
	 	            final String[] selectionArgs = new String[] {
	 	                    split[1]
	 	            };

	 	            return getDataColumn(context, contentUri, selection, selectionArgs);
	 	        }
	 	    }
	    }
	    	    
	    // MediaStore (and general)
	   if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

}
