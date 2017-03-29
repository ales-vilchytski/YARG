package by.ales.android.yarg.data;

/**
 * Created by Ales on 29.03.2017.
 */

public class NumbersGenerationParameters extends GenerationParameters {

    private Number from;
    private Number to;

    public NumbersGenerationParameters() {
    }

    public Number getFrom() {
        return from;
    }

    public void setFrom(Number from) {
        this.from = from;
    }

    public Number getTo() {
        return to;
    }

    public void setTo(Number to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "NumbersGenerationParameters{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
