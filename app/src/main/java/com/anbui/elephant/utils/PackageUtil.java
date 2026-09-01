package com.anbui.elephant.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

public class PackageUtil {
    public static Signature[] getSignatures(Context context, String packageName) {
        try {
            return getSignatures(context.getPackageManager().getPackageInfo(packageName, Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? PackageManager.GET_SIGNING_CERTIFICATES : PackageManager.GET_SIGNATURES));
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static Signature[] getArchiveSignatures(Context context, String archivePath) {
        return getSignatures(context.getPackageManager().getPackageArchiveInfo(archivePath, Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? PackageManager.GET_SIGNING_CERTIFICATES : PackageManager.GET_SIGNATURES));
    }

    public static Signature[] getSignatures(PackageInfo packageInfo) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (packageInfo.signingInfo == null) return null;

                if (packageInfo.signingInfo.hasMultipleSigners()) {
                    return packageInfo.signingInfo.getApkContentsSigners();
                } else {
                    return packageInfo.signingInfo.getSigningCertificateHistory();
                }
            } else {
                return packageInfo.signatures;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
