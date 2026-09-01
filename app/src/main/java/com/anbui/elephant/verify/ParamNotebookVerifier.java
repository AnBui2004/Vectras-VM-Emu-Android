package com.anbui.elephant.verify;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.anbui.app.elephant.IElephantReceiverService4;
import com.anbui.app.elephant.IResultCallback;
import com.anbui.elephant.log.LogPrinter;

public class ParamNotebookVerifier {
    private static final String TAG = "ParamNotebookVerifier";

    public interface ParamNotebookVerifierCallback {
        void onResult(boolean isValid);
    }

    public static void verify(Context context, String key, ParamNotebookVerifierCallback callback) {
        final ServiceConnection[] connection = new ServiceConnection[1];

        connection[0] = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IElephantReceiverService4 elephant =
                        IElephantReceiverService4.Stub.asInterface(service);

                try {
                    elephant.verify(context.getPackageName(), key, new IResultCallback.Stub() {
                        @Override
                        public void onResult(boolean isValid) {
                            callback.onResult(isValid);
                            context.unbindService(connection[0]);
                        }
                    });
                } catch (RemoteException ignored) {

                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogPrinter.print(TAG, "serviceDisconnected");
            }
        };

        Intent intent = new Intent();
        intent.setClassName(
                "com.anbui.cqcm.app",
                "com.anbui.cqcm.app.elephant.ElephantReceiverService4"
        );

        boolean ok = context.bindService(intent, connection[0], Context.BIND_AUTO_CREATE);

        if (!ok) {
            callback.onResult(false);
            LogPrinter.print(TAG, "serviceFailedToConnect");
        }
    }
}
