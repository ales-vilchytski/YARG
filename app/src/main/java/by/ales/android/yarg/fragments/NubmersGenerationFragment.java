package by.ales.android.yarg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.ales.android.yarg.R;
import by.ales.android.yarg.data.GenerationParameters;
import by.ales.android.yarg.data.NumbersGenerationParameters;
import by.ales.android.yarg.data.NumbersGenerationResult;
import by.ales.android.yarg.data.ResultCallback;
import by.ales.android.yarg.fragments.listeners.GenerationParametersReadyListener;
import by.ales.android.yarg.utils.ActivityUtils;
import by.ales.android.yarg.views.NumberFieldCompoundView;
import by.ales.android.yarg.views.SeekbarFieldCompoundView;

public class NubmersGenerationFragment extends Fragment {
    public static final String TAG = "NumberGenerationF";

    private NumberFieldCompoundView fromView;
    private NumberFieldCompoundView toView;
    private SeekbarFieldCompoundView quantityView;
    private TextView resultView;
    private Button generateButton;
    private NumberFieldCompoundView decimalsView;

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
        decimalsView = (NumberFieldCompoundView) view.findViewById(R.id.fragment_numbers_generation_decimals_field_view);
        generateButton = (Button) view.findViewById(R.id.fragment_numbers_generation_generate_button_view);
        resultView = (TextView) view.findViewById(R.id.fragment_numbers_generation_result_view);

        fromView.addValueChangedListener(new NumberFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(Number n) {
                Log.d(TAG, "New FROM value: " + n.toString());
                debounceOnGenerationParameters(false);
            }
        });
        toView.addValueChangedListener(new NumberFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(Number n) {
                Log.d(TAG, "New TO value: " + n.toString());
                debounceOnGenerationParameters(false);
            }
        });
        quantityView.addValueChangedListener(new SeekbarFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(int n) {
                Log.d(TAG, "New Qnt value: " + n);
                debounceOnGenerationParameters(false);
            }
        });
        decimalsView.addValueChangedListener(new NumberFieldCompoundView.Listener() {
            @Override
            public void onValueChanged(Number n) {
                Log.d(TAG, "New DECIMALS value: " + n.toString());
                debounceOnGenerationParameters(false);
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debounceOnGenerationParameters(true);
            }
        });
        return view;
    }

    private void debounceOnGenerationParameters(final boolean hideKeyboard) {
        // TODO: delay/debounce!
        if (listener != null) {
            listener.onGenerationParametersReady(prepareGenerationParameters(), new ResultCallback<NumbersGenerationResult>() {
                @Override
                public void call(NumbersGenerationResult result) {
                    if (result == null) {
                        return;
                    }
                    Log.d(TAG, "Numbers generated!");
                    String results;
                    if (result.getParameters().getDecimals() == null || result.getParameters().getDecimals().equals(0)) {
                        List<Integer> ints = new ArrayList<>();
                        for (Number d: result.getNumberList()) {
                            ints.add(d.intValue());
                        }
                        results = ints.toString();
                    } else {
                        results = result.getNumberList().toString();
                    }
                    resultView.setText(results);
                    if (hideKeyboard) {
                        ActivityUtils.hideSoftKeyboard(getActivity());
                    }
                }
            });
        }
    }


    private GenerationParameters prepareGenerationParameters() {
        NumbersGenerationParameters params = new NumbersGenerationParameters();

        params.setType(GenerationParameters.Type.NUMBERS);
        params.setFrom(fromView.getValue());
        params.setTo(toView.getValue());
        params.setDecimals(decimalsView.getValue() != null ? decimalsView.getValue().intValue() : null);
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
