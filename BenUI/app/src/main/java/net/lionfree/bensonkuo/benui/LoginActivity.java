package net.lionfree.bensonkuo.benui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    String userName = null;
                    try {
                        userName = object.getString("name");
                        Log.d("fb-username", userName);
                        goToMain(userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();

        }


        callbackManager = CallbackManager.Factory.create();
        setUpFacebookLogin();

    }

    private void goToMain(String userName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fbUserName",userName);
        startActivity(intent);

    }

    private void setUpFacebookLogin() {
        LoginButton loginBtn = (LoginButton) findViewById(R.id.login_button);
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // 只是為了拿到參數而已
        // callbackmanager會handle好一切(store token on device)
        // 之後只需要getCurrentAccessToken()即可
    }
}
