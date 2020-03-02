package com.odelan.yang.aggone.Activity.Auth;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Dialog.ForgotPasswordDialog;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.TinyDB;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    private static final int GOOGLE_SIGN_IN = 9001;

    @BindView(R.id.edit_email)      EditText edit_email;
    @BindView(R.id.edit_password)   EditText edit_password;

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.odelan.yang.aggone",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:"+ Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
                Toast.makeText(getApplicationContext(), Base64.encodeToString(md.digest(),
                        Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

        tinyDB = new TinyDB(this);
        if (tinyDB.getBoolean(Constants.LOGIN_ISLOGGEDIN)) {
            edit_email.setText(tinyDB.getString(Constants.LOGIN_EMAIL));
            edit_password.setText(tinyDB.getString(Constants.LOGIN_PASSWORD));
            onClickGo();
        }


        // Facebook login
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                showToast("Cancel");
            }
            @Override
            public void onError(FacebookException error) {
                showToast(error.getMessage());
            }
        });

        // google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleAccount(account);
            } catch (ApiException e) {
                showToast(e.getLocalizedMessage());
            }
        }
    }

    void handleGoogleAccount(GoogleSignInAccount account) {
        String email = account.getEmail();
        String first_name = account.getGivenName();
        String last_name = account.getFamilyName();
        String photo = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";
        showProgress();
        API.loginWithSocial(this, email, first_name + " " + last_name, photo, new APICallback<User>() {
            @Override
            public void onSuccess(User response) {
                dismissProgress();
                AppData.user = response;
                if (response.sport_id < Constants.Football){
                    Intent intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                    intent.putExtra(Constants.SIGNUP_MODE, Constants.SIGNUP_SOCIAL);
                    intent.putExtra(Constants.EMAIL, response.email);
                    intent.putExtra(Constants.PASSWORD, "");
                    startActivity(intent);
                    finish();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    void handleFacebookAccessToken(final AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                (object, response) -> {
                    if (object != null) {
                        try {
                            String email = object.getString("email");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String photo = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                            showProgress();
                            API.loginWithSocial(LoginActivity.this, email, first_name + " " + last_name, photo, new APICallback<User>() {
                                @Override
                                public void onSuccess(User response) {
                                    dismissProgress();
                                    AppData.user = response;
                                    if (response.sport_id < Constants.Football){
                                        Intent intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                                        intent.putExtra(Constants.SIGNUP_MODE, Constants.SIGNUP_SOCIAL);
                                        intent.putExtra(Constants.EMAIL, response.email);
                                        intent.putExtra(Constants.PASSWORD, "");
                                        startActivity(intent);
                                        finish();
                                        return;
                                    }
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                @Override
                                public void onFailure(String error) {
                                    dismissProgress();
                                    showToast(error);
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("GraphRequest is failed.")
                                .setMessage("GraphicRequest is failed.")
                                .setPositiveButton(android.R.string.ok, null).show();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,link,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @OnClick(R.id.btn_go) void onClickGo() {
        final String email = edit_email.getText().toString();
        final String password = edit_password.getText().toString();
        if (email.isEmpty()) {
            showToast("Please input email");
            return;
        }
        if (isValidEmail(email) == false) {
            showToast("Invalid email");
            return;
        }
        if (password.isEmpty()) {
            showToast("Please input password");
            return;
        }

        showProgress();
        API.loginWithEmailAndPassword(this, email, password, new APICallback<User>() {
            @Override
            public void onSuccess(User response) {
                dismissProgress();
                tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, true);
                tinyDB.putString(Constants.LOGIN_EMAIL, edit_email.getText().toString());
                tinyDB.putString(Constants.LOGIN_PASSWORD, edit_password.getText().toString());
                AppData.user = response;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_signup) void onClickSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_show) void onClickShow() {
        edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @OnClick(R.id.btn_forgot_password) void onClickForgotPassword() {
        ForgotPasswordDialog dialog = new ForgotPasswordDialog(this);
        dialog.setListener(new ForgotPasswordDialog.Listener() {
            @Override
            public void onClickOK(String email) {
                showProgress();
                API.forgotPassword(email, new APICallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        dismissProgress();
                        showToast("Success");
                    }
                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        showToast(error);
                    }
                });
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    @OnClick(R.id.btn_facebook) void onClickFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @OnClick(R.id.btn_google) void onClickGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, GOOGLE_SIGN_IN);
    }
}