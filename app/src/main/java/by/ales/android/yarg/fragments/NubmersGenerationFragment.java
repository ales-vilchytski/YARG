package by.ales.android.yarg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import by.ales.android.yarg.R;
import by.ales.android.yarg.data.GenerationParameters;
import by.ales.android.yarg.data.NumbersGenerationParameters;
import by.ales.android.yarg.data.NumbersGenerationResult;
import by.ales.android.yarg.data.ResultCallback;
import by.ales.android.yarg.fragments.listeners.GenerationParametersReadyListener;
import by.ales.android.yarg.views.NumberFieldCompoundView;
import by.ales.android.yarg.views.SeekbarFieldCompoundView;

public class NubmersGenerationFragment extends Fragment {
    public static final String TAG = "NumberGenerationF";

    private NumberFieldCompoundView fromView;
    private NumberFieldCompoundView toView;
    private SeekbarFieldCompoundView quantityView;
    private EditText resultView;

    private GenerationParametersReadyListener listener;


    public NubmersGenerationFragment() {
    }

    public static NubmersGenerationFragment newInstance() {
        NubmersGenerationFragment fragment = new NubmersGenerationFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nubmers_generation, container, false);
        fromView = (NumberFieldCompoundView) view.findViewById(R.id.fragment_numbers_generation_from_field_view);
        toView = (NumberFieldCompoundView) view.findViewById(R.id.fragment_numbers_generation_to_field_view);
        quantityView = (SeekbarFieldCompoundView) view.findViewById(R.id.fragment_numbers_generation_quantity_field_view);
        resultView = (EditText) view.findViewById(R.id.fragment_numbers_generation_result_view);

        fromView.addValueChangedListener(new NumberFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(Number n) {
                Log.d(TAG, "New FROM value: " + n.toString());
                debounceOnGenerationParameters();
            }
        });
        toView.addValueChangedListener(new NumberFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(Number n) {
                Log.d(TAG, "New TO value: " + n.toString());
                debounceOnGenerationParameters();
            }
        });
        quantityView.addValueChangedListener(new SeekbarFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(int n) {
                Log.d(TAG, "New Qnt value: " + n);
                debounceOnGenerationParameters();
            }
        });
        return view;
    }

    private void debounceOnGenerationParameters() {
        // TODO: delay/debounce!
        if (listener != null) {
            listener.onGenerationParametersReady(prepareGenerationParameters(), new ResultCallback<NumbersGenerationResult>() {
                @Override
                public void call(NumbersGenerationResult result) {
                    if (result == null) {
                        return;
                    }
                    Log.d(TAG, "Numbers generated!");
                    resultView.setText(result.getNumberList().toString());
                }
            });
        }
    }

    private GenerationParameters prepareGenerationParameters() {
        NumbersGenerationParameters params = new NumbersGenerationParameters();

        params.setType(GenerationParameters.Type.NUMBERS);
        params.setFrom(fromView.getValue());
        params.setTo(toView.getValue());
        params.setQuantity(quantityView.getValue());

        return params;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GenerationParametersReadyListener) {
            listener = (GenerationParametersReadyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
