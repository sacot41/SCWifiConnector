package com.sacot41.espconnector_example.base;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by Samuel on 2017-10-31.
 */

@Deprecated
public class ESPWifiButton extends CardView {

    public ESPWifiButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ESPWifiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ESPWifiButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setClickable(true);

        try {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));
        } catch (Exception e){
            Log.d(getClass().getSimpleName(), "could not set button background to ripple drawable: " + e.getMessage());
        }
    }

}
