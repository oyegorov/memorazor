package com.ell.MemoRazor.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import com.ell.MemoRazor.R;

public class DialogHelper {
    public static interface OnConfirmListener {
        void onConfirm();
    }

    public static interface OnRequestInputListener {
        void onRequestInput(String input);
    }

    private DialogHelper() {
    }

    public static void MessageBox(Context context, String message) {
        MessageBox(context, context.getResources().getString(R.string.app_name), message);
    }

    public static void MessageBox(Context context, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.common_ok, null).create();
        dialog.show();
    }

    public static void Confirm(Context context, String message, final OnConfirmListener onConfirm) {
        Confirm(context, context.getResources().getString(R.string.app_name), message, onConfirm);
    }

    public static void Confirm(Context context, String title, String message, final OnConfirmListener onConfirm) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onConfirm.onConfirm();
                    }
                }).setNegativeButton(R.string.common_no, null).create();
        dialog.show();
    }

    public static void RequestInput(Context context, String message, final OnRequestInputListener onRequest) {
        RequestInput(context, context.getResources().getString(R.string.app_name), message, onRequest);
    }

    public static void RequestInput(Context context, String title, String message, final OnRequestInputListener onRequest) {
        RequestInput(context, title, message, "", onRequest);
    }

    public static void RequestInput(Context context, String title, String message, String initialData, final OnRequestInputListener onRequest) {
        final EditText input = new EditText(context);
        input.setText(initialData);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setView(input)
                .setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String data = input.getText().toString();
                        if (!data.equals("")) {
                            onRequest.onRequestInput(input.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }
}
