package com.anbui.elephant.utils;

import android.app.Activity;
import android.net.Uri;

public class IntentUtil {
    public static String getCallingPackageName(Activity activity) {
        return getCallingPackageName(activity, false);
    }

    // If the device is running a custom rom, it could be tampered with and rendered completely useless.
    public static String getCallingPackageName(Activity activity, boolean isTruthNeeded) {
        // Work with ActivityResultLauncher.
        if (activity.getCallingActivity() != null)
            return activity.getCallingActivity().getPackageName();

        if (isTruthNeeded) return null;

        // Unreliable and susceptible to forgery.
        Uri referrer = activity.getReferrer();
        if (referrer != null) {
            if ("android-app".equals(referrer.getScheme())) {
                return referrer.getAuthority();
            }

            return referrer.toString().replace("android-app://", "");
        }

        return null;
    }
}
