package by.ales.android.yarg.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.ales.android.yarg.R;
import by.ales.android.yarg.utils.NumberUtils;
import by.ales.android.yarg.views.filters.DiscardNonChangingNumberFilter;
import by.ales.android.yarg.views.filters.MinMaxNumberFilter;

/**
 * Created by Ales on 30.03.2017.
 */
public class SeekbarFieldCompoundView extends RelativeLayout
        implements TextWatcher, View.OnFocusChangeListener, SeekBar.OnSeekBarChangeListener {
    public static final String TAG = "SeekbarFieldCView";


    private EditText editView;
    private TextView labelView;
    private SeekBar seekbarView;
    private int minValue;
    private int maxValue;
    private boolean isSyncingValues = false;

    public interface Listener {
        void onValueChanged(int n);
    }

    private List<Listener> valueChangedListeners;


    public SeekbarFieldCompoundView(Context context) {
        super(context);
        init(null);
    }

    public SeekbarFieldCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SeekbarFieldCompoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(this.getContext(), R.layout.view_seekbar_field, this);

        editView = (EditText) this.findViewWithTag("tag_seekbar_field_view_edit");
        editView.setId(View.generateViewId());
        editView.addTextChangedListener(this);
        editView.setOnFocusChangeListener(this);

        labelView = (TextView) this.findViewWithTag("tag_seekbar_field_view_label");
        labelView.setId(View.generateViewId());
        labelView.setLabelFor(editView.getId());

        seekbarView = (SeekBar) this.findViewWithTag("tag_seekbar_field_view_seekbar");
        seekbarView.setId(View.generateViewId());
        seekbarView.setOnSeekBarChangeListener(this);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editView.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, labelView.getId());
        params = (RelativeLayout.LayoutParams) seekbarView.getLayoutParams();
        params.addRule(RelativeLayout.RIGHT_OF, editView.getId());

        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.FieldWithLabel);
            String label = arr.getString(R.styleable.FieldWithLabel_label);
            if (label != null) {
                labelView.setText(label);
            }

            String minValueS = arr.getString(R.styleable.FieldWithLabel_minValue);
            if (minValueS == null) {
                minValueS = "1";
            }
            String maxValueS = arr.getString(R.styleable.FieldWithLabel_maxValue);
            if (maxValueS == null) {
                maxValueS = "100";
            }
            minValue = Integer.valueOf(minValueS);
            maxValue = Integer.valueOf(maxValueS);

            editView.setInputType(minValue < 0 ?
                    (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED) :
                    InputType.TYPE_CLASS_NUMBER);
            editView.setFilters(new InputFilter[] {
                    DigitsKeyListener.getInstance(minValue < 0, false), // order is important, using input type in layout adds filter to end, not to start
                    new MinMaxNumberFilter(minValue, maxValue, true)
            });
            seekbarView.setMax(maxValue - minValue);

            String value = arr.getString(R.styleable.FieldWithLabel_value);
            if (value != null) {
                editView.setText(value);
            }

            arr.recycle();
        }
    }

    private void notifyOnValueChanged(Integer value) {
        if (!isSyncingValues && valueChangedListeners != null && value != null) {
            for (Listener valueChangedListener : valueChangedListeners) {
                valueChangedListener.onValueChanged(value);
            }
        }
    }

    private static int getProgressFromValue(int value, int min) {
        return value - min;
    }

    private static int getValueFromProgress(int progress, int min) {
        return progress + min;
    }

    private void syncTextChanged(int value) {
        if (!isSyncingValues) {
            isSyncingValues = true;
            seekbarView.setProgress(getProgressFromValue(value, minValue));
            isSyncingValues = false;
        }
    }

    private void syncProgressValueChanged(int value) {
        if (!isSyncingValues) {
            isSyncingValues = true;
            editView.setText(String.valueOf(value));
            isSyncingValues = false;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
    @Override
    public void afterTextChanged(Editable s) {
        Integer val = NumberUtils.tryParseInt(s);
        if (val != null) {
            if (val > maxValue) {
                s.clear();
                s.append(String.valueOf(maxValue));
            } else if (val < minValue) {
                s.clear();
                s.append(String.valueOf(minValue));
            } else {
                syncTextChanged(val);
                notifyOnValueChanged(val);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.equals(editView)) {
            if (!hasFocus) {
                Integer val = NumberUtils.tryParseInt(editView.getText());
                if (val == null) {
                    editView.setText(String.valueOf(minValue));
                } else if (val < minValue) {
                    editView.setText(minValue);
                } else if (val > maxValue) {
                    editView.setText(String.valueOf(maxValue));
                }
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int value = getValueFromProgress(progress, minValue);
        if (value < minValue) {
            value = minValue;
        }
        if (value > maxValue) {
            value = maxValue;
        }
        syncProgressValueChanged(value);
        notifyOnValueChanged(value);
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }


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


    // TODO: Save state by ID - ID must persist between fragment recreations
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState state = new SavedState(superState);
        state.label = labelView.onSaveInstanceState();
        state.edit = editView.onSaveInstanceState();
        state.seekbar = seekbarView.onSaveInstanceState();

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
        seekbarView.onRestoreInstanceState(ss.seekbar);
    }

    public static class SavedState extends BaseSavedState {
        Parcelable label;
        Parcelable edit;
        Parcelable seekbar;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeParcelable(label, flags);
            out.writeParcelable(edit, flags);
            out.writeParcelable(seekbar, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private SavedState(Parcel in) {
            super(in);

            label = in.readParcelable(this.getClass().getClassLoader());
            edit = in.readParcelable(this.getClass().getClassLoader());
            seekbar = in.readParcelable(this.getClass().getClassLoader());
        }
    }

}
