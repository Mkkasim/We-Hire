package com.mk.wehire.User.NetworkUtility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mk.wehire.R;

public class InternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String status = ConnectionClass.getNetworkInfo(context);

        if (status.equals("disconnected")){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialog = LayoutInflater.from(context).inflate(R.layout.retry_internet_dialog,null);
            builder.setView(dialog);

            TextView retry = dialog.findViewById(R.id.retry);

            AlertDialog aDialog = builder.create();
            aDialog.show();
            aDialog.setCancelable(false);

            aDialog.getWindow().setGravity(Gravity.BOTTOM);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aDialog.dismiss();
                    onReceive(context,intent);
                }
            });


        }
    }
}
