package com.technolifestyle.therealshabi.techlauncher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    LoginButton mLoginButton;
    CallbackManager mCallbackManager;
    TextView mDisplayTextView;
    ProfileTracker profileTracker;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Place this before Set Content View Method
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        mDisplayTextView = (TextView) findViewById(R.id.facebook_login_textView);
        mCallbackManager = CallbackManager.Factory.create();

        mLoginButton.setReadPermissions(Arrays.asList("user_posts","email","publish_actions","user_about_me","user_status"));

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getBaseContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                mDisplayTextView.setText("Hello "+loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Login Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "Error was Encountered", Toast.LENGTH_SHORT).show();

            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                accessToken = currentAccessToken;
                //mDisplayTextView.setText(accessToken.getToken());
                Log.d("Access Token",accessToken.getToken());
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.


        /*profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                if (currentProfile != null) {
                    mDisplayTextView.setText("Hello " + currentProfile.getFirstName());
                } else {
                    mDisplayTextView.setText("Hello User");
                }
            }
        };*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}
