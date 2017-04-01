package by.ales.android.yarg.fragments.listeners;

import by.ales.android.yarg.data.GenerationParameters;
import by.ales.android.yarg.data.ResultCallback;

/**
 * Created by Ales on 29.03.2017.
 */

public interface GenerationParametersReadyListener {

    void onGenerationParametersReady(GenerationParameters data, ResultCallback generatedCallback);

}
