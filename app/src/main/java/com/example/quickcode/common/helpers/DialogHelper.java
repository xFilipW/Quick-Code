package com.example.quickcode.common.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

public class DialogHelper {

    public static void showAlertDialogErrorGeneral(Context context, int dialogTitle, int dialogMessage) {
        String title = context.getString(dialogTitle);
        String message = context.getString(dialogMessage);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok", (dialogInterface, i) -> dialogInterface.cancel())
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
        alertDialog.getWindow().setGravity(Gravity.CENTER);
    }

}
