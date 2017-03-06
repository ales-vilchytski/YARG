package by.ales.android.yarg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.ales.android.yarg.R;

public class NubmersGenerationFragment extends Fragment {
    private static final String ARG_FIELD_NAME_KEY = "fieldName";
    private static final String ARG_INITIAL_VALUE = "initialValue";

    private int fieldNameKey;
    private String initialValue;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String text);
    }


    public NubmersGenerationFragment() {
    }

    public static NubmersGenerationFragment newInstance(String fieldNameKey, String initialValue) {
        NubmersGenerationFragment fragment = new NubmersGenerationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FIELD_NAME_KEY, fieldNameKey);
        args.putString(ARG_INITIAL_VALUE, initialValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fieldNameKey = getArguments().getInt(ARG_FIELD_NAME_KEY);
            initialValue = getArguments().getString(ARG_INITIAL_VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nubmers_generation, container, false);
        return view;
    }

    public void onTextChanged(String text) {
        if (listener != null) {
            listener.onFragmentInteraction(text);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
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
