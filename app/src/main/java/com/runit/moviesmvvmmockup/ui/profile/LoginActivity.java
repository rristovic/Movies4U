package com.runit.moviesmvvmmockup.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.databinding.ActivityLoginBinding;
import com.runit.moviesmvvmmockup.ui.main.MainActivity;
import com.runit.moviesmvvmmockup.utils.UIUtil;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private WebView mWebView;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setLoginViewModel(mViewModel);

        mWebView = findViewById(R.id.wv_web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mViewModel.onPageLoaded(url);
            }
        });

        mViewModel.isUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn != null && isLoggedIn) {
                MainActivity.startActivity(LoginActivity.this);
                LoginActivity.this.finish();
            } else {
                mViewModel.getLoginPage().observe(this, loginPage -> {
                    if (loginPage != null && !loginPage.isEmpty()) {
                        mWebView.loadUrl(loginPage);
                    } else {
                        UIUtil.showToast(LoginActivity.this, getString(R.string.network_error));
                    }
                });
            }
        });
    }
}
