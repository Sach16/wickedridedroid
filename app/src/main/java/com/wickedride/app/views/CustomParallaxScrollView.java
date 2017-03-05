package com.wickedride.app.views;

import android.content.Context;
import android.util.AttributeSet;

import com.wickedride.app.interfaces.OnScrollViewListener;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;

/**
 * Created by Inkoniq-Admin on 11-Sep-15.
 */
public class CustomParallaxScrollView extends ParallaxScrollView {

    private OnScrollViewListener mOnScrollViewListener;
    public CustomParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomParallaxScrollView(Context context) {
        super(context);
    }

    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
        this.mOnScrollViewListener = onScrollViewListener;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }
}
