package com.wickedride.app.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wickedride.app.R;

/**
 * TODO: document your custom view class.
 */
public class IQProgressDialog extends DialogFragment {
    private String message = "";
    private Dialog dialog;
    private boolean _isShowing = false;

    public IQProgressDialog() {
    }

    public void init(Context context, String message) {
        // Load attributes

        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_progress);


        TextView label = (TextView) dialog.findViewById(R.id.progresslayout_text);
        label.setText(message);
        if (message.equals("")) {
            label.setVisibility(View.GONE);
        }
        //Try catch added to ensure the Leaking Window error does not occur
        try {
            // ProgressBar mProgress = (ProgressBar) dialog.findViewById(R.id.progresslayout_progress);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception e) {
            Log.i("asdasd", "asdasd");

        }
        _isShowing = true;
    }

    public boolean isShowing() {
        return _isShowing;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.DialogFragment#dismiss()
     */
    @Override
    public void dismiss() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
        }

        _isShowing = false;
    }

    public void cancel() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
        }
        _isShowing = false;
    }

}
