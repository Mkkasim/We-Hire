package com.mk.wehire.User.models;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.airbnb.lottie.LottieAnimationView;
import com.mk.wehire.R;

public class WeHireLoader {

    Context context;
    Dialog dialog;

    public WeHireLoader(Context context) {
        this.context = context;
    }

    public void ShowDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loader_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LottieAnimationView loader = dialog.findViewById(R.id.lottie_loader);
        dialog.create();
        dialog.show();
    }

    public void DismissDialog(){
        dialog.dismiss();
    }
}
