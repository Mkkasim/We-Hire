package com.mk.wehire.User.NetworkUtility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionClass {

    public static String getNetworkInfo(Context context){

        String status = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo infos = connectivityManager.getActiveNetworkInfo();

        if (infos!=null){
            status = "connect";
            return status;
        }else {
            status = "disconnected";
            return status;
        }

    }

}
