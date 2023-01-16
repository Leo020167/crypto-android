package com.bitcnew.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtil {

    /**
     * 从 uri 中获取文件
     */
    public static File copyFileFromContentUri(Context context, Uri contentUri) {
        if (null == contentUri) {
            return null;
        }

        String fileName = null;
        long fileSize = -1;

        try {
            // 获取文件信息
            Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
            try {
                if (cursor == null) {
                    return null;
                }

                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                if (cursor.moveToFirst()) {
                    fileName = cursor.getString(nameIndex);
                    fileSize = cursor.getLong(sizeIndex);
                }
            } finally {
                if (null != cursor && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
            return null;
        }

        // 拷贝文件
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(contentUri, "r");
            if (null == parcelFileDescriptor) {
                return null;
            }

            try (FileInputStream inputStream =
                         new FileInputStream(parcelFileDescriptor.getFileDescriptor())) {
                return createTemporalFileFrom(context, inputStream, fileName);
            }
        } catch (FileNotFoundException e) {
            Log.e("", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取图片缓存路径
     *
     * @param context .
     */
    public static File getFileCacheDir(Context context) {
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dir = new File(context.getExternalFilesDir(null), "imageCache");
        } else {
            dir = new File(context.getFilesDir(), "imageCache");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 从 uri 中获取文件
     */
    public static File getFileFromContentUri(Context context, Uri contentUri) {
        if (contentUri == null) {
            return null;
        }

        File file = null;
        String filePath = null;
        String fileName = null;
        String[] filePathColumn =
                {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(contentUri, filePathColumn, null,
                null, null);
        try {
            if (null == cursor || !cursor.moveToFirst()) {
                return null;
            }

            int index;

            index = cursor.getColumnIndex(filePathColumn[0]);
            if (-1 != index) {
                filePath = cursor.getString(index);
            }

            index = cursor.getColumnIndex(filePathColumn[1]);
            if (-1 != index) {
                fileName = cursor.getString(index);
            }

            cursor.close();

            // 直接文件路径
            if (!TextUtils.isEmpty(filePath)) {
                file = new File(filePath);
            }

            // 判断直接文件路径是否可以直接访问, 否则拷贝文件到指定目录
            boolean copiedFile = false;
            if (null != file &&
                    (!file.exists() || file.length() <= 0 || TextUtils.isEmpty(filePath) ||
                            !file.canRead())) {
                // 拷贝文件
                filePath = getPathFromInputStreamUri(context, contentUri, fileName);
                copiedFile = true;
            }

            // 返回新的拷贝文件
            if (copiedFile && !TextUtils.isEmpty(filePath)) {
                file = new File(filePath);
            }

            // 从 contentProvider 拷贝文件
            if (null == file) {
                file = copyFileFromContentUri(context, contentUri);
            }
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return file;
    }

    /*
     * 拷贝文件
     */
    private static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                // 拷贝文件
                File file = createTemporalFileFrom(context, inputStream, fileName);
                filePath = file.getPath();
            } catch (Exception e) {
                Log.e("", e.getMessage(), e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    Log.e("", e.getMessage(), e);
                }
            }
        }

        return filePath;
    }

    /*
     * 拷贝文件
     */
    private static File createTemporalFileFrom(Context context, InputStream inputStream,
                                               String fileName)
            throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];
            // 自己定义拷贝文件路径
            File dir = getFileCacheDir(context);

            targetFile = new File(dir, fileName);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

}
