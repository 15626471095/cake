package studio.weiweima.cake.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public final class ImageUtils {

    private ImageUtils() {
    }

    /**
     * 按比例获取bitmap
     *
     * @param path
     * @param w
     * @param h
     * @return
     */
    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture仅仅获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public static Bitmap getBitmap(Context context, String pictureUri, int w, int h) {
        Bitmap bitmap = null;
        try {
            Bitmap origin = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(pictureUri));
            bitmap = Bitmap.createScaledBitmap(origin, w, h, true);
        } catch (Exception e) {
            Toast.makeText(context, "图片不存在！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return bitmap;
    }
}
