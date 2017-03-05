package com.wickedride.app.views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.wickedride.app.R;

public class WRProgressView extends ProgressBar {
    private static AnimationDrawable sWRProgressDrawable;

    public WRProgressView(Context context) {
        super(context);
        init();
    }

    public WRProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WRProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (sWRProgressDrawable == null) {
            Context appContext = getContext().getApplicationContext();
            sWRProgressDrawable = (AnimationDrawable) appContext.getResources().getDrawable(R.drawable.wr_progress);
        }
        this.setIndeterminateDrawable(sWRProgressDrawable);
    }
}
