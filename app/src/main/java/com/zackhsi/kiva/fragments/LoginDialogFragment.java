package com.zackhsi.kiva.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container);
        ButterKnife.inject(this, view);

        // an attempt to make the dialogfragment appear faster
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                client = KivaApplication.getRestClient();
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                // Configure the client to use when opening URLs
                webView.setWebViewClient(new KivaWebviewClient());
                // Load the initial URL
                Log.d("WEBVIEW", "Original URL: " + getArguments().getString("url"));

                webView.loadUrl(getArguments().getString("url"));
            }
        };

        handler.postDelayed(r, 0);

        return view;
    }

    private class KivaWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("WEBVIEW", "Webview being directed to url: " + url);

            if (url.contains("/register?")) {
                url = url.replace("/register?", "/login?");
                view.loadUrl(url);
            }
            else if (url.startsWith(KivaApi.callbackUrl)) {

                client.authorize(Uri.parse(url), new OAuthBaseClient.OAuthAccessHandler() {
                    @Override
                    public void onLoginSuccess() {
                        Log.d("OAuth", "Successfully authorized with kiva!");
                        LoginDialogFragmentListener listener = (LoginDialogFragmentListener) getActivity();
                        listener.onFinishLoginDialog();
                        getDialog().dismiss();
                    }

                    @Override
                    public void onLoginFailure(Exception e) {
                        Log.d("OAuth", "Failed to authorize");

                        // For now, just launch the profile activity anyway
                        LoginDialogFragmentListener listener = (LoginDialogFragmentListener) getActivity();
                        listener.onFinishLoginDialog();

                        getDialog().dismiss();
                    }
                });
//                        getDialog().dismiss();
            } else {
                view.loadUrl(url);
            }

            return true;
        }
    }

    public interface LoginDialogFragmentListener {
        void onFinishLoginDialog();
    }
}
