package com.vectras.vterm;

import android.app.Activity;
import android.content.Intent;

import com.termux.app.TermuxActivity;
import com.vectras.vm.R;
import com.vectras.vm.settings.SettingsData;
import com.vectras.vm.utils.DialogUtils;

public class TerminalManager {
    public static void openTerminal(Activity activity) {
        if (SettingsData.terminalWarning(activity)) {
            activity.startActivity(new Intent(activity, TermuxActivity.class));
        } else {
            DialogUtils.threeDialog(
                    activity,
                    activity.getString(R.string.warning),
                    activity.getString(R.string.terminal_warning_content),
                    activity.getString(R.string.accept),
                    activity.getString(R.string.accept_and_dont_show_again),
                    activity.getString(R.string.close),
                    true,
                    R.drawable.warning_48px,
                    true,
                    () -> activity.startActivity(new Intent(activity, TermuxActivity.class)),
                    () -> {
                        SettingsData.terminalWarning(activity, true);
                        activity.startActivity(new Intent(activity, TermuxActivity.class));
                    },
                    null,
                    null
            );
        }
    }
}
