package com.anbui.elephant.verify;

import android.app.Activity;

import com.anbui.elephant.app.AppChecker;
import com.anbui.elephant.utils.IntentUtil;

public class ParamNotebookVerifier {
    // It is sufficiently safe because Param Notebook uses ActivityResultLauncher.
    public static boolean verify(Activity activity) {
        String sourcePackageName = IntentUtil.getCallingPackageName(activity, true);
        return sourcePackageName != null && sourcePackageName.equals(AppChecker.PARAM_NOTEBOOK_PACKAGE_NAME) && AppChecker.isParamNoteBook(activity);
    }
}
