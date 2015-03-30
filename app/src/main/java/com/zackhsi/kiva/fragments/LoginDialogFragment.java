package com.zackhsi.kiva.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zackhsi.kiva.KivaApi;
import com.zackhsi.kiva.KivaApplication;
import com.zackhsi.kiva.KivaClient;
import com.zackhsi.kiva.R;
import com.zackhsi.kiva.oauth.OAuthBaseClient;

import org.scribe.model.Token;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zackhsi on 3/28/15.
 */
public class LoginDialogFragment extends DialogFragment {

    private KivaClient client;

    @InjectView(R.id.webview)
    WebView webView;

    public LoginDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static LoginDialogFragment newInstance(String url) {
        LoginDialogFragment frag = new LoginDialogFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = KivaApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.inject(this, view);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        webView.setWebViewClient(new KivaWebviewClient());
        // Load the initial URL
        Log.d("OAuth", "URL: " + getArguments().getString("url"));
        webView.loadUrl(getArguments().getString("url"));

        return view;
    }

    private class KivaWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}