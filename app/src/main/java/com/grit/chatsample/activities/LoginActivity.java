package com.grit.chatsample.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.grit.chatsample.Interface.UserVerificationCallback;
import com.grit.chatsample.R;
import com.grit.chatsample.application;
import com.grit.chatsample.pojos.Users;
import com.thecode.aestheticdialogs.AestheticDialog;

public class LoginActivity extends AppCompatActivity {


    EditText username, password;
    Button login, signUp;
    TextInputLayout txtInLayoutUsername, txtInLayoutPassword;
//    CheckBox rememberMe;
    application app;
    SharedPreferences mPrefs;
    SpinKitView spin_kit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        app=(application) getApplicationContext();
        mPrefs = getSharedPreferences("ChatPrefs", MODE_PRIVATE);
        if(!mPrefs.getString("username", "").isEmpty()){
            Intent mIntent = new Intent(LoginActivity.this, UserActivity.class);
            startActivity(mIntent);
            finish();
        }
        spin_kit = findViewById(R.id.spin_kit);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        txtInLayoutUsername = findViewById(R.id.txtInLayoutUsername);
        txtInLayoutPassword = findViewById(R.id.txtInLayoutPassword);
//        rememberMe = findViewById(R.id.rememberMe);

        ClickLogin();

        //SignUp's Button for showing registration page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSignUp();
            }
        });

    }

    //This is method for doing operation of check login
    private void ClickLogin() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().trim().isEmpty()) {

                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    txtInLayoutUsername.setError("Username should not be empty");
                } else if (password.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    txtInLayoutPassword.setError("Password should not be empty");
                } else {
                    //Here you can write the codes for checking password
                    spin_kit.setVisibility(View.VISIBLE);
                    app.validateUserNamePassword(username.getText().toString(), new UserVerificationCallback() {
                        @Override
                        public void handleVerification(boolean success, String message, Users user) {
                            if(success){

                                if(user.isLoggedIn()){
                                    Snackbar snackbar = Snackbar.make(view, "User already login! please use other user",
                                            Snackbar.LENGTH_LONG);
                                    View snackbarView = snackbar.getView();
                                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                                    snackbar.show();
                                }else{
                                    String serverPassword = user.getPassword();
                                    if(serverPassword.equalsIgnoreCase(password.getText().toString())){
//                                    AestheticDialog.showFlashDialog(LoginActivity.this, "Success",
//                                            "User login successfully", AestheticDialog.SUCCESS);
                                        mPrefs.edit().putString("username", username.getText().toString()).apply();
                                        mPrefs.edit().putString("password", password.getText().toString()).apply();
                                        spin_kit.setVisibility(View.GONE);
                                        Intent mIntent = new Intent(LoginActivity.this, UserActivity.class);
                                        startActivity(mIntent);
                                        finish();

                                    }else{
                                        Snackbar snackbar = Snackbar.make(view, "Password not correct, please use correct password",
                                                Snackbar.LENGTH_LONG);
                                        View snackbarView = snackbar.getView();
                                        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                                        snackbar.show();
                                    }
                                }
                            }else{
                                Snackbar snackbar = Snackbar.make(view, "Username not exist, please sign up",
                                        Snackbar.LENGTH_LONG);
                                View snackbarView = snackbar.getView();
                                snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                                snackbar.show();
                            }
                        }
                    });
                }

//                if (rememberMe.isChecked()) {
//                    //Here you can write the codes if box is checked
//                } else {
//                    //Here you can write the codes if box is not checked
//                }

            }

        });

    }

    //The method for opening the registration page and another processes or checks for registering
    private void ClickSignUp() {
        Intent mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(mIntent);
    }
}
