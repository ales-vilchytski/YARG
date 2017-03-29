package by.ales.android.yarg.data;

/**
 * Created by Ales on 29.03.2017.
 */

public class GenerationParameters {

    public enum Type {
        NUMBERS
    }

    private Type type;

    public GenerationParameters() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GenerationParameters{" +
                "type='" + type + '\'' +
                '}';
    }
}
