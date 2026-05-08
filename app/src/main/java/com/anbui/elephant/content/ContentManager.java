package com.anbui.elephant.content;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.anbui.app.elephant.IElephantReceiverService3;
import com.anbui.app.elephant.IResultContentCallback;
import com.anbui.elephant.log.LogPrinter;

public class ContentManager {
    private static final String TAG = "ContentManager";

    public interface ContentManagerCallback {
        void onResult(String[] urls);
    }

    public static void getUrls(Context context, String id, ContentManagerCallback callback) {
        final ServiceConnection[] connection = new ServiceConnection[1];

        connection[0] = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IElephantReceiverService3 elephant =
                        IElephantReceiverService3.Stub.asInterface(service);

                try {
                    elephant.getContentUrls(context.getPackageName(), id, new IResultContentCallback.Stub() {
                        @Override
                        public void onResult(String[] urls) {
                            callback.onResult(urls);
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
                "com.anbui.app",
                "com.anbui.app.elephant.ElephantReceiverService3"
        );

        boolean ok = context.bindService(intent, connection[0], Context.BIND_AUTO_CREATE);

        if (!ok) {
            callback.onResult(null);
            LogPrinter.print(TAG, "serviceFailedToConnect");
        }
    }
}
