/*
 * Copyright (c) 2014 Inkoniq
 * All Rights Reserved.
 * @since July 20, 2014 
 * @author BalaKJ
 */
package com.wickedride.app.interfaces;

/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
import com.wickedride.app.views.CustomParallaxScrollView;

public interface OnScrollViewListener {

    void onScrollChanged( CustomParallaxScrollView v, int l, int t, int oldl, int oldt );

}
