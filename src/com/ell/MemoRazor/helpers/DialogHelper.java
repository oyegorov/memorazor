package com.ell.MemoRazor.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.ell.MemoRazor.AppSettings;
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

    public static void messageBox(Context context, String message) {
        messageBox(context, context.getResources().getString(R.string.app_name), message);
    }

    public static void messageBox(Context context, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.common_ok, null).create();
        dialog.show();
    }

    public static void confirm(Context context, String message, final OnConfirmListener onConfirm) {
        confirm(context, context.getResources().getString(R.string.app_name), message, onConfirm);
    }

    public static void confirm(Context context, String title, String message, final OnConfirmListener onConfirm) {
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

    public static void requestInput(Context context, String message, final OnRequestInputListener onRequest) {
        requestInput(context, context.getResources().getString(R.string.app_name), message, onRequest);
    }

    public static void requestInput(Context context, String title, String message, final OnRequestInputListener onRequest) {
        requestInput(context, title, message, "", onRequest);
    }

    public static void requestInput(Context context, String title, String message, String initialData, final OnRequestInputListener onRequest) {
        final EditText input = new EditText(context);
        input.setText(initialData);
        input.setSingleLine();

        final AlertDialog dialog = new AlertDialog.Builder(context)
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
                }).create();
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                return true;
            }
        });

        dialog.show();
    }

    public static void showTip(Context context, final String tipKey, final String tipMessage) {
        if (!AppSettings.getBooleanSetting(tipKey, true))
            return;

        View checkBoxView = View.inflate(context, R.layout.alertdialog_checkbox, null);
        final CheckBox suppressTipsCheckbox = (CheckBox) checkBoxView.findViewById(R.id.suppress_tips_checkbox);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setMessage(tipMessage)
                .setView(checkBoxView)
                .setPositiveButton(R.string.common_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (suppressTipsCheckbox.isChecked()) {
                            AppSettings.suppressTips();
                        }
                    }
                }).show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        suppressTipsCheckbox.setTextColor(messageView.getTextColors());

        AppSettings.setBooleanSetting(tipKey, false);
    }
}
