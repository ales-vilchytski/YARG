package by.ales.android.yarg.views.filters;

import android.text.InputFilter;
import android.text.Spanned;

import by.ales.android.yarg.utils.NumberUtils;

/**
 * Created by Ales on 01.04.2017.
 */
public class MinMaxNumberFilter extends NumberFilterBase implements InputFilter {
    private int min;
    private int max;
    private boolean discardNonChangingNumbers;

    private DiscardNonChangingNumberFilter discardFilter;

    public MinMaxNumberFilter() { }

    public MinMaxNumberFilter(int minValue, int maxValue, boolean discardNonChangingNumbers) {
        this.min = minValue;
        this.max = maxValue;
        this.discardNonChangingNumbers = discardNonChangingNumbers;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (min < 0 && source.subSequence(start, end).toString().equals("-")) {
            return null;
        }
        CharSequence newDest = createDest(source, start, end, dest, dstart, dend);
        Integer newVal = NumberUtils.tryParseInt(newDest);
        Integer curVal = NumberUtils.tryParseInt(dest);
        if (discardFilter == null) {
            discardFilter = new DiscardNonChangingNumberFilter();
        }
        CharSequence result = discardFilter.filter(curVal, newVal);
        if (result == null && (newVal == null || newVal < min || newVal > max)) {
            result = "";
        }
        return result;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isDiscardNonChangingNumbers() { return discardNonChangingNumbers; }

    public void setDiscardNonChangingNumbers(boolean discardNonChangingNumbers) { this.discardNonChangingNumbers = discardNonChangingNumbers; }
}
