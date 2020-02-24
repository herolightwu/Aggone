package com.odelan.yang.aggone.Utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 3/6/2018.
 */

public class FileUtil {
    public static File fromBitmap(Context context, Bitmap bitmap) {
        try {
            File f = new File(context.getCacheDir(), "thumbnail_" + System.currentTimeMillis()/1000 + ".jpg");
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return  f;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
