package by.ales.android.yarg.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.ales.android.yarg.data.NumbersGenerationParameters;
import by.ales.android.yarg.data.NumbersGenerationResult;
import by.ales.android.yarg.data.ResultCallback;

/**
 * Created by Ales on 01.04.2017.
 */
public class GenerationService extends Service {
    private static final String TAG = "GenerationService";

    private final IBinder mBinder = new LocalBinder();

    public GenerationService() {}

    public class LocalBinder extends Binder {
        public GenerationService getService() {
            return GenerationService.this;
        }
    }

    public void generateRandomNumbers(final NumbersGenerationParameters params, ResultCallback<NumbersGenerationResult> resultCallback) {
        NumbersGenerationResult result = new NumbersGenerationResult();
        result.setParameters(params);

        if (params.getFrom() == null || params.getTo() == null || params.getQuantity() == null
            || params.getFrom().doubleValue() > params.getTo().doubleValue()
            || params.getQuantity() == null || params.getQuantity().intValue() < 1) {

            Log.w(TAG, "Wrong numbers generation params: " + params);
            result = null;
        } else {
            List<Number> numbers = new ArrayList<>();
            Random rand = new Random();
            double from = params.getFrom().doubleValue();
            double to = params.getTo().doubleValue();
            Integer decimals = params.getDecimals();
            if (decimals == null) {
                decimals = 0;
            }
            for (int i = 0; i < params.getQuantity(); ++i) {
                double num = adjustNumbber(rand.nextDouble(), from, to);
                numbers.add(Math.ceil(num * Math.pow(10, decimals)) / Math.pow(10, decimals));
            }
            result.setNumberList(numbers);
        }

        resultCallback.call(result);
    }

    public double adjustNumbber(double rand, double from, double to) {
        return from + (rand * (to - from));
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}
