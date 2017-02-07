package com.yuanyinguoji.video.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by wa on 2016/11/22.
 */

public class FileUtils {

    public static File getFile(){
        return new File(
                Environment.getExternalStorageDirectory(),
                System.currentTimeMillis()+".MP4");
//        return null;
    }

    public static String getVideoPath(Context context){
        File file = createFile(context);
        return file.getAbsolutePath();
    }

    private static File createFile(Context context){
        File file = new File(Environment.getExternalStorageDirectory(),context.getPackageName());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 返回码： 0 成功，1 文件不存在，2，路径为空，3 删除失败
     */
    public static int delFile(String filePath){
        if(TextUtils.isEmpty(filePath)){
            return 2;
        }
        File file = new File(filePath);
        if(file.isFile()){
             boolean isDel = file.delete();
            if(isDel){
                return 0;
            }else{
                return 3;
            }
        }
        return 1;
    }
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
