package by.ales.android.yarg.views.filters;

import android.text.Spanned;

/**
 * Created by Ales on 01.04.2017.
 */

public class NumberFilterBase {

    protected StringBuilder createDest(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder b = new StringBuilder();
        b.append(dest.subSequence(0, dstart))
                .append(source.subSequence(start, end))
                .append(dest.subSequence(dend, dest.length()));
        return b;
    }

}
