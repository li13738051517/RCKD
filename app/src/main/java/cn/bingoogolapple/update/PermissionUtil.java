package cn.bingoogolapple.update;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import static cn.bingoogolapple.update.AppUtil.sApp;


class PermissionUtil {
    private PermissionUtil() {
    }

    /**
     * 获取FileProvider的auth
     *
     * @return
     */
    static String getFileProviderAuthority() {
        try {
            for (ProviderInfo provider : sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), PackageManager.GET_PROVIDERS).providers) {
                if (FileProvider.class.getName().equals(provider.name) && provider.authority.endsWith(".bga_update.file_provider")) {
                    return provider.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return null;
    }

    /**
     * 授予对某个意向的 uri 授权
     *
     * @param intent 要授予访问某个 uri 的 intent 意向
     * @param uri    要授予权限的 uri
     */
    static void grantUriPermission(Intent intent, Uri uri) {
        List<ResolveInfo> resolvedIntentActivities = sApp.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;
            sApp.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    /**
     * 撤销对某个文件夹的读写权限
     *
     * @param dir
     */
    static void revokeUriPermission(File dir) {
        if (dir == null || dir.listFiles() == null || dir.listFiles().length == 0) {
            return;
        }

        String fileProviderAuthority = getFileProviderAuthority();
        if (TextUtils.isEmpty(fileProviderAuthority)) {
            return;
        }

        for (File file : dir.listFiles()) {
            revokeUriPermission(FileProvider.getUriForFile(sApp, fileProviderAuthority, file));
        }
    }

    /**
     * 撤销对某个 uri 的读写权限
     * 注意:在页面中应当有相应对呗撤销 uri  读写权限的  处理  ,如 用户撤销 允许定位的权限后  ,应当有防止app 奔溃  的处理
     *  * @param uri
     */
    static void revokeUriPermission(Uri uri) {
        if (uri == null) {
            return;
        }

        sApp.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
