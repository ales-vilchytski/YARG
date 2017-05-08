package by.ales.android.yarg.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Ales on 08.05.2017.
 */

public class ActivityUtils {

    public static void hideSoftKeyboard(Activity a) {
        InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (a.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
