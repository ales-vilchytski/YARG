package by.ales.android.yarg.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.ales.android.yarg.R;
import by.ales.android.yarg.utils.NumberUtils;
import by.ales.android.yarg.views.filters.DiscardNonChangingNumberFilter;
import by.ales.android.yarg.views.filters.MinMaxNumberFilter;


/**
 * Created by Ales on 05.03.2017.
 */

public class NumberFieldCompoundView extends RelativeLayout implements TextWatcher {

    private TextView labelView;
    private EditText editView;
    private String prevValue;

    public interface Listener {
        void onValueChanged(Number n);
    }

    private List<Listener> valueChangedListeners;

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

        editView = (EditText) this.findViewWithTag("tag_number_field_view_edit");
        editView.setId(View.generateViewId());
        editView.addTextChangedListener(this);
        editView.setFilters(new InputFilter[] {
                new DiscardNonChangingNumberFilter()
        });

        labelView = (TextView) this.findViewWithTag("tag_number_field_view_label");
        labelView.setId(View.generateViewId());
        labelView.setLabelFor(editView.getId());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editView.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, labelView.getId());

        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.FieldWithLabel);
            String label = arr.getString(R.styleable.FieldWithLabel_label);
            if (label != null) {
                labelView.setText(label);
            }

            String value = arr.getString(R.styleable.FieldWithLabel_value);
            if (value != null) {
                editView.setText(value);
            }
            arr.recycle();
        }
    }

    public Number getValue() {
        return NumberUtils.tryParseNumber(editView.getText());
    }

    public void setValue(Number v) {
        editView.setText(v.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        prevValue = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (valueChangedListeners != null) {
            Number n = NumberUtils.tryParseNumber(s);
            if (n != null) {
                for (Listener valueChangedListener : valueChangedListeners) {
                    valueChangedListener.onValueChanged(n);
                }
            }
        }
    }

    // TODO: Save state by ID - ID must persist between fragment recreations
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState state = new SavedState(superState);
        state.label = labelView.onSaveInstanceState();
        state.edit = editView.onSaveInstanceState();

        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (!(savedState instanceof SavedState)) {
            super.onRestoreInstanceState(savedState);
            return;
        }
        SavedState ss = (SavedState) savedState;
        super.onRestoreInstanceState(ss.getSuperState());

        labelView.onRestoreInstanceState(ss.label);
        editView.onRestoreInstanceState(ss.edit);
    }

    public static class SavedState extends BaseSavedState {

        Parcelable label;
        Parcelable edit;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeParcelable(label, flags);
            out.writeParcelable(edit, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new NumberFieldCompoundView.SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new NumberFieldCompoundView.SavedState[size];
            }
        };

        private SavedState(Parcel in) {
            super(in);

            label = in.readParcelable(this.getClass().getClassLoader());
            edit = in.readParcelable(this.getClass().getClassLoader());
        }
    }

    public void addValueChangedListener(Listener listener) {
        if (valueChangedListeners == null) {
            valueChangedListeners = new ArrayList<>();
        }
        valueChangedListeners.add(listener);
    }

    public void removeValueChangedListener(Listener listener) {
        if (valueChangedListeners != null) {
            valueChangedListeners.remove(listener);
        }
    }

}
