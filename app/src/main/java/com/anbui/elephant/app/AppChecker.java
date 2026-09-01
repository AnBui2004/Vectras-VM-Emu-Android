package com.anbui.elephant.app;

import android.content.Context;
import android.content.pm.Signature;

import com.anbui.elephant.utils.PackageUtil;
import com.anbui.elephant.utils.SignatureUtil;

public class AppChecker {
    public static final String PARAM_NOTEBOOK_PACKAGE_NAME = "com.anbui.cqcm.app";
    public static final String PARAM_NOTEBOOK_SIGNATURE = "54C8AFF047890C8C7A9570BAACA2A36DA1357581BEB9886FFD001A7A8C21F4C4";
    public static final String PARAM_NOTEBOOK_SIGNATURE_PLAY_STORE = "803101169A3BA7C0488F654BFFD008B83A45DF270E7CB06E590F985A40E446F4";

    public static boolean isParamNoteBook(Context context) {
        Signature[] signatures = PackageUtil.getSignatures(context, PARAM_NOTEBOOK_PACKAGE_NAME);

        if (signatures == null) return false;

        String sha256 = new SignatureUtil().getSha256(signatures[0].toByteArray());
        return sha256.equals(PARAM_NOTEBOOK_SIGNATURE) || sha256.equals(PARAM_NOTEBOOK_SIGNATURE_PLAY_STORE);
    }
}
