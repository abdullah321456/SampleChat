package com.grit.chatsample.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.grit.chatsample.Interface.UserVerificationCallback;
import com.grit.chatsample.R;
import com.grit.chatsample.application;
import com.grit.chatsample.pojos.Users;
import com.thecode.aestheticdialogs.AestheticDialog;

public class RegisterActivity extends AppCompatActivity {


    EditText username, password, confirmpassword;
    Button register;
    TextInputLayout txtInLayoutUsername, txtInLayoutPassword, txtInLayoutConfirmPassword;
//    CheckBox rememberMe;
    application app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        app=(application) getApplicationContext();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        register = findViewById(R.id.register);

        txtInLayoutUsername = findViewById(R.id.txtInLayoutUsername);
        txtInLayoutPassword = findViewById(R.id.txtInLayoutPassword);
        txtInLayoutConfirmPassword = findViewById(R.id.txtInLayoutConfirmPassword);
//        rememberMe = findViewById(R.id.rememberMe);

        ClickRegister();
    }

    //This is method for doing operation of check login
    private void ClickRegister() {

        register.setOnClickListener(new View.OnClickListener() {
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
                }else if (confirmpassword.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    txtInLayoutConfirmPassword.setError("Confirm Password should not be empty");
                } else if(!confirmpassword.getText().toString().equalsIgnoreCase(password.getText().toString())) {
                    AestheticDialog.showFlashDialog(RegisterActivity.this, "Error",
                            "Confirm password is not correct", AestheticDialog.ERROR);
                }else{
                    app.checkIfUserExist(username.getText().toString(), new UserVerificationCallback() {
                        @Override
                        public void handleVerification(boolean success, String message, Users user) {

                            if(success){
                                app.registerUser(new Users(username.getText().toString(),
                                        password.getText().toString(),""));

                                AestheticDialog.showFlashDialog(RegisterActivity.this, "Success",
                                        "User register successfully! please login", AestheticDialog.SUCCESS);
//                                finish();
                            }else{
                                AestheticDialog.showFlashDialog(RegisterActivity.this, "Error",
                                        "User already exist", AestheticDialog.ERROR);
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
}
