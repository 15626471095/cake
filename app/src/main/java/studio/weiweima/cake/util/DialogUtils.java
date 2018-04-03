package studio.weiweima.cake.util;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import java.util.function.Consumer;

public final class DialogUtils {

    private DialogUtils() {
    }

    public static void showDeleteConfirmDialog(Activity activity, Consumer<Void> onSure) {
        showConfirmDialog(activity, "姐姐你确定要删除吗？-.-#", onSure);
    }

    public static void showConfirmDialog(Activity activity, String message, Consumer<Void> onSure) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setTitle("warning")
                .setNeutralButton("我点错了", (dialog, id) -> dialog.dismiss())
                .setPositiveButton("删意已决>.<", (dialog, id) -> {
                    onSure.accept(null);
                }).show();
    }
}
