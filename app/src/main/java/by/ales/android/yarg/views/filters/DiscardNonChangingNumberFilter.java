package by.ales.android.yarg.views.filters;

import android.text.InputFilter;
import android.text.Spanned;

import by.ales.android.yarg.utils.NumberUtils;

/**
 * Created by Ales on 01.04.2017.
 */
public class DiscardNonChangingNumberFilter extends NumberFilterBase implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence newDest = createDest(source, start, end, dest, dstart, dend);
        Number newVal = NumberUtils.tryParseNumber(newDest);
        Number curVal = NumberUtils.tryParseNumber(dest);
        return filter(curVal, newVal);
    }

    public CharSequence filter(Number curVal, Number newVal) {
        if ((curVal == null && curVal == newVal) || curVal != newVal || !curVal.equals(newVal)) {
            return null;
        } else {
            return "";
        }
    }

}
