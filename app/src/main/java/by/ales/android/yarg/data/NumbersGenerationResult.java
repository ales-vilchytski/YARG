package by.ales.android.yarg.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ales on 01.04.2017.
 */

public class NumbersGenerationResult extends GenerationResult<NumbersGenerationParameters> {

    private List<Number> numberList;


    public NumbersGenerationResult() {
    }

    public List<Number> getNumberList() {
        return numberList;
    }

    public void setNumberList(List<Number> numberList) {
        this.numberList = numberList;
    }

    public void setNumbers(Number... numbers) {
        this.numberList = Arrays.asList(numbers);
    }
}
