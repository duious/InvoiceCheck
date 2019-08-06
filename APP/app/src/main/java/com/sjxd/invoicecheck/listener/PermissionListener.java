package com.sjxd.invoicecheck.listener;

import java.util.List;

/**
 * @author zhangyl
 * <p>
 * 17-11-1
 */
public interface PermissionListener {
    void onGranted();

    void onDenied(List<String> deniedPermission);

    void onShouldShowRationale(List<String> deniedPermission);
}
