package com.mk.wehire.User.models;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mk.wehire.R;

public class OpenBottomSheet {

    Context context;
    Dialog dialog;

    public OpenBottomSheet(Context context) {
        this.context = context;
    }

    public void openBottomSheet(String title,String msg){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.details_bottomsheet);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_round);

        TextView Title = dialog.findViewById(R.id.textTitle);
        TextView message = dialog.findViewById(R.id.textMesg);
        ImageView close = dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Title.setText(title);
        message.setText(msg);

        dialog.create();
        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void closeBottomSheet(){
        dialog.dismiss();
    }
}
