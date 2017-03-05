package com.wickedride.app.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.VolleyError;
import com.wickedride.app.R;
import com.wickedride.app.activities.BaseActivity;
import com.wickedride.app.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by Nitish Kulkarni on 02-Sep-15.
 */
public class WebFragment extends BaseFragment {

    public static final String TAG = "FAQ_FRAGMENT";

    @Override
    public String getSelfTag() {
        return TAG;
    }

    @Override
    public CharSequence getTitle(Resources r) {
        return r.getString(R.string.faq);
    }

    @InjectView(R.id.webview)
    WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.inject(this, mView);
        String url = getArguments().getString(Constants.WEB_URL);
        if(url.contains("tariff")){
            ((BaseActivity)getActivity()).setActionBarTitle(R.string.tariff);
        }else if(url.contains("faq")){
            ((BaseActivity)getActivity()).setActionBarTitle(R.string.faq);
        }else if(url.contains("about")){
            ((BaseActivity)getActivity()).setActionBarTitle(R.string.about);
        }else if(url.contains("review")){
            ((BaseActivity)getActivity()).setActionBarTitle(R.string.review);
        }else if(url.contains("contact")){
            ((BaseActivity)getActivity()).setActionBarTitle(R.string.contact_us);
        }
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(tel);
                    return true;
                } else if (url.startsWith("mailto:")) {
                    String body = "";
                    Intent mail = new Intent(Intent.ACTION_SEND);
                    mail.setType("application/octet-stream");
                    mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"reservations@wickedride.com"});
                    mail.putExtra(Intent.EXTRA_SUBJECT, "");
                    mail.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(mail);
                    return true;
                }else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
//        placeRequest(apiMethod, WebResult.class);

        return mView;
    }

//    @OnClick(R.id.close)
//    public void onClickListener(View view){
//
//    }


    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        getActivity().finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
//        final String mimeType = "text/html";
//        final String encoding = "UTF-8";
//        if(response!=null){
//            WebResult webResult = (WebResult) response;
//            mWebView.loadData(webResult.getData().getContent(), mimeType, encoding);
//        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
    }
}
