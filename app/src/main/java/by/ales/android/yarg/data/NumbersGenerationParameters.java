package by.ales.android.yarg.data;

/**
 * Created by Ales on 29.03.2017.
 */
public class NumbersGenerationParameters extends GenerationParameters {

    private Number from;
    private Number to;
    private Integer decimals;
    private Integer quantity;

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

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "NumbersGenerationParameters{" +
                "from=" + from +
                ", to=" + to +
                ", quantity=" + quantity +
                '}';
    }

}
