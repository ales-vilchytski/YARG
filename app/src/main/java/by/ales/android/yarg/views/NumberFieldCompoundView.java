package by.ales.android.yarg.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.ales.android.yarg.R;


/**
 * Created by Ales on 05.03.2017.
 */

public class NumberFieldCompoundView extends LinearLayout implements TextWatcher {

    public interface Listener {
        void onValueChanged(Number n);
    }

    private Listener valueChangedListener;

    public NumberFieldCompoundView(Context context) {
        super(context);
        init(null);
    }

    public NumberFieldCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumberFieldCompoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        inflate(this.getContext(), R.layout.view_number_field, this);

        EditText editView = (EditText) this.findViewById(R.id.number_field_view_edit);
        editView.addTextChangedListener(this);

        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.NumberFieldCompoundView);
            String label = arr.getString(R.styleable.NumberFieldCompoundView_label);
            TextView labelView = (TextView) this.findViewById(R.id.number_field_view_label);
            if (label != null) {
                labelView.setText(label);
            }

            String value = arr.getString(R.styleable.NumberFieldCompoundView_value);
            if (value != null) {
                editView.setText(value);
            }

            arr.recycle();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (valueChangedListener != null) {
            Number n;
            // TODO: support other types?
            try {
                n = Integer.parseInt(s.toString());
            } catch (NumberFormatException e) {
                n = null;
            }
            valueChangedListener.onValueChanged(n);
        }
    }
}
