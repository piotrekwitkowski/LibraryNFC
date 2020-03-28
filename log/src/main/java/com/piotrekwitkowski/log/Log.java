package com.piotrekwitkowski.log;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

@SuppressLint("SetTextI18n")
public class Log {
    @SuppressLint("StaticFieldLeak")
    private static TextView logTextView;

    public static void setLogTextView(TextView tv) {
        logTextView = tv;
    }

    public static void i(final String tag, final String msg) {
        android.util.Log.i(tag, msg);
        new Handler(Looper.getMainLooper()).post(() -> logTextView.setText(logTextView.getText() + format(tag, msg)));
    }

    public static void reset(final String tag, final String msg) {
        android.util.Log.i(tag, msg);
        new Handler(Looper.getMainLooper()).post(() -> logTextView.setText(format(tag, msg)));
    }

    private static String format(String tag, String msg) {
        return tag + ": " + msg + '\n';
    }
}
