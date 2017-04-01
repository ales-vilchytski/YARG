package by.ales.android.yarg.utils;

/**
 * Created by Ales on 01.04.2017.
 */
public class NumberUtils {

    public static Number tryParseNumber(CharSequence s) {
        Number n;
        try {
            n = Integer.parseInt(s.toString());
        } catch (NumberFormatException e) {
            try {
                n = Double.parseDouble(s.toString());
            } catch (NumberFormatException e1) {
                n = null;
            }
        }
        return n;
    }

    public static Number tryParseNumber(CharSequence s, Number defaultValue) {
        Number n = tryParseNumber(s);
        if (n == null) {
            n = defaultValue;
        }
        return n;
    }

    public static Integer tryParseInt(CharSequence s) {
        Integer n;
        try {
            n = Integer.parseInt(s.toString());
        } catch (NumberFormatException e) {
            n = null;
        }
        return n;
    }

    public static int tryParseInt(CharSequence s, int defaultValue) {
        Integer i = tryParseInt(s);
        if (i == null) {
            i = defaultValue;
        }
        return i;
    }
}
