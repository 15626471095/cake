package studio.weiweima.cake.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public final class FileUtils {
    private FileUtils() {
    }

    public static void writeTo(Context context, String file, String content) {
        FileOutputStream os = null;
        try {
            File f = new File(context.getFilesDir(), file);
            if (!f.exists()) {
                f.createNewFile();
            }
            os = new FileOutputStream(f);
            os.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFrom(Context context, String file) {
        InputStream in = null;
        try {
            String result = "";
            File f = new File(context.getFilesDir(), file);
            if (f.exists()) {
                in = new FileInputStream(f);
                int lenght = in.available();
                //创建byte数组
                byte[] buffer = new byte[lenght];
                //将文件中的数据读到byte数组中
                in.read(buffer);
                return new String(buffer, "utf-8");
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
