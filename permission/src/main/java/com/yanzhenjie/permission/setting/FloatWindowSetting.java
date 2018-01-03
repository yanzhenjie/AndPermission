package com.yanzhenjie.permission.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.yanzhenjie.permission.SettingService;
import com.yanzhenjie.permission.source.Source;

/**
 * Created by YanZhenjie on 2018/1/3.
 */
public class FloatWindowSetting implements SettingService {

    private static final String DEFAULT_PACKAGE_KEY = "package";
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Context mContext;

    public FloatWindowSetting(@NonNull Source source) {
        this.mContext = source.getContext();
    }

    @Override
    public void execute() {
        Intent intent;
        if (MARK.contains("huawei")) {
            intent = huaweiApi(mContext);
        } else if (MARK.contains("xiaomi")) {
            intent = xiaomiApi(mContext);
        } else if (MARK.contains("oppo")) {
            intent = oppoApi(mContext);
        } else if (MARK.contains("vivo")) {
            intent = vivoApi(mContext);
        } else if (MARK.contains("samsung")) {
            intent = samsungApi(mContext);
        } else if (MARK.contains("meizu")) {
            intent = meizuApi(mContext);
        } else if (MARK.contains("smartisan")) {
            intent = smartisanApi(mContext);
        } else {
            intent = defaultApi(mContext);
        }
        try {
            if (intent != null) {
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "发生异常", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cancel() {
    }

    /**
     * App details page.
     */
    @Nullable
    private static Intent defaultApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.fromParts(DEFAULT_PACKAGE_KEY, context.getPackageName(), null));
            return intent;
        }
        return null;
    }

    /**
     * Huawei cell phone Api23 the following method.
     */
    private static Intent huaweiApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity"));
            return intent;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity"));
            return intent;
        }
        return null;
    }

    /**
     * Xiaomi phone to achieve the method.
     */
    private static Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("extra_pkgname", context.getPackageName());
        return intent;
    }

    /**
     * Vivo phone to achieve the method.
     */
    private static Intent vivoApi(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.MainActivity"));
        intent.addCategory("android.intent.category.LAUNCHER");
        return intent;
    }

    /**
     * Oppo phone to achieve the method.
     */
    @Nullable
    private static Intent oppoApi(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity"));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.floatwindow.FloatWindowListActivity"));
        }
        return defaultApi(context);
    }

    /**
     * Meizu phone to achieve the method.
     */
    @Nullable
    private static Intent meizuApi(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    /**
     * Smartisan phone to achieve the method.
     */
    @Nullable
    private static Intent smartisanApi(Context context) {
        return defaultApi(context);
    }

    /**
     * Samsung phone to achieve the method.
     */
    @Nullable
    private static Intent samsungApi(Context context) {
        return defaultApi(context);
    }
}
