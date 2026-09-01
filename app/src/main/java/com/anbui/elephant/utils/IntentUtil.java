package com.anbui.elephant.utils;

import android.app.Activity;
import android.net.Uri;

public class IntentUtil {
    public static String getCallingPackageName(Activity activity) {
        if (activity.getCallingActivity() != null)
            return activity.getCallingActivity().getPackageName();

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
