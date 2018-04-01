package studio.weiweima.cake.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import studio.weiweima.cake.external.JsonObject;

public final class BitmapCache extends LruCache<String, Bitmap> {

    private static BitmapCache instance = new BitmapCache();

    public static BitmapCache getInstance() {
        return instance;
    }

    private Context context;

    private BitmapCache() {
        super(200);
    }

    public Bitmap getBitmap(Context context, String pictureUri, int width, int height) {
        this.context = context;
        return get(keyWithScale(pictureUri, width, height));
    }

    @Override
    protected Bitmap create(String key) {
        JsonObject o = new JsonObject(key);
        Bitmap bitmap = ImageUtils.getBitmap(context, o.getString("uri"), o.getInteger("width"), o.getInteger("height"));
        context = null;
        return bitmap;
    }

    private String keyWithScale(String key, int width, int height) {
        return new JsonObject().put("uri", key)
                .put("width", width)
                .put("height", height)
                .toString();
    }
}
