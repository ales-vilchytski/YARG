package by.ales.android.yarg.data;

/**
 * Created by Ales on 01.04.2017.
 */

public class GenerationResult<T extends GenerationParameters> {

    private T parameters;

    public GenerationResult() {
    }

    public T getParameters() {
        return parameters;
    }

    public void setParameters(T parameters) {
        this.parameters = parameters;
    }
}
