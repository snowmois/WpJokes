package com.leeson.wpjokes.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {
    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();

        //获取手机连接管理对象（包括wifi，net等连接的管理）
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            //获取Network 对象
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

            if (networkInfos != null && networkInfos.length > 0) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

