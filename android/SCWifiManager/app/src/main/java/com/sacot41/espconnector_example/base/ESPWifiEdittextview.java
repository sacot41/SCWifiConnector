package com.sacot41.espconnector_example.base;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.sacot41.espconnector_example.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Samuel on 2017-11-02.
 */

public class ESPWifiEdittextview extends CardView {

    @BindView(R.id.edittext_single_line_edittext)
    EditText editText;

    private TextView.OnEditorActionListener onEditorActionListener;

    public ESPWifiEdittextview(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public ESPWifiEdittextview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ESPWifiEdittextview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.view_round_edittext, this);
        ButterKnife.bind(this);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ESPWifiEdittextview);

        setTextSize(typedArray.getDimensionPixelSize(R.styleable.ESPWifiEdittextview_roundTextSize, -1));
        setHint(typedArray.getText(R.styleable.ESPWifiEdittextview_roundHint));
        setText(typedArray.getText(R.styleable.ESPWifiEdittextview_roundText));

        int imeOptions = typedArray.getInt(R.styleable.ESPWifiEdittextview_android_imeOptions, -1);
        int inputType = typedArray.getInt(R.styleable.ESPWifiEdittextview_android_inputType, -1);

        if (imeOptions != -1){
            editText.setImeOptions(imeOptions);
        }
        if (inputType != -1){
            Typeface typeface = editText.getTypeface();
            editText.setInputType(inputType);
        }

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (onEditorActionListener != null){
                    onEditorActionListener.onEditorAction(v, actionId, event);
                    return true;
                } else {
                    return false;
                }
            }
        });

        typedArray.recycle();
    }

    public String getText(){
        return editText != null ? editText.getText().toString() : "";
    }

    public EditText getEditText(){
        return editText;
    }

    public void setTextSize(float textSize){
        if (textSize > 0 && editText != null){
            editText.setTextSize(textSize);
        }
    }

    public void setHint(CharSequence hint) {
        if (editText != null) {
            editText.setHint(hint);
        }
    }

    public void setText(CharSequence text) {
        if (editText != null) {
            editText.setText(text);
        }
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener){
        onEditorActionListener = listener;
    }

    public void shakeView() {
        ObjectAnimator.ofFloat(this, "translationX", 0, 15, -15, 15, -15, 5, -5, 1, -1, 0)
                .setDuration(500)
                .start();
    }

}
