package by.ales.android.yarg.utils;

import android.app.Activity;

import by.ales.android.yarg.data.ResultCallback;

/**
 * Created by Ales on 01.04.2017.
 */

public class ResultCallbackWrapper<T> implements ResultCallback<T> {

    private ResultCallback<T> callback;
    private Activity activity;

    public ResultCallbackWrapper(ResultCallback<T> callback, Activity activity) {
        this.callback = callback;
        this.activity = activity;
    }

    @Override
    public void call(final T result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.call(result);
            }
        });
    }
}
