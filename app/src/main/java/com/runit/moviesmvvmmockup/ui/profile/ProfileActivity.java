package com.runit.moviesmvvmmockup.ui.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.databinding.ActivityProfileBinding;
import com.runit.moviesmvvmmockup.ui.login.LoginActivity;
import com.runit.moviesmvvmmockup.ui.login.LoginViewModel;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle(getString(R.string.profile));
        setContentView(R.layout.activity_profile);

        ViewModelProviders.of(this).get(LoginViewModel.class).isUserLoggedIn().observe(this, (isLoggedIn) -> {
            if (isLoggedIn != null && isLoggedIn) {
                // Continue with loading
                init();
            } else {
                LoginActivity.startActivity(ProfileActivity.this);
            }
        });
    }

    private void init() {
        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setProfileViewModel(profileViewModel);
        profileViewModel.getProfile(ProfileActivity.this);
    }

    public static void startActivity(Context c) {
        c.startActivity(new Intent(c, ProfileActivity.class));
    }

}
