package com.core.project.picwiz;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public static final String SETTINGS_NAME = "MySettingsFile";
    String username;
    String email;

    EditText usernameField;
    EditText emailField;
    Button accept;

    boolean isRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView helloText = (TextView) findViewById(R.id.hello);
        usernameField = (EditText) findViewById(R.id.username);
        emailField = (EditText) findViewById(R.id.email);
        accept = (Button) findViewById(R.id.accept);

        usernameField.setVisibility(View.GONE);
        emailField.setVisibility(View.GONE);
        accept.setVisibility(View.GONE);
        helloText.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

        getUserInfo();

        if(username == null || email == null) {
            helloText.setText("Create New Account");
            usernameField.setVisibility(View.VISIBLE);
            emailField.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);

            final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(SplashScreen.this).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String possibleEmail = account.name;
                    emailField.setText(possibleEmail);
                }
            }
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    if(usernameField.getText().toString().isEmpty() || emailField.getText().toString().isEmpty()) {
                        Toast.makeText(SplashScreen.this, "Fields are not filled", Toast.LENGTH_SHORT).show();
                    } else if (!emailPattern.matcher(emailField.getText().toString()).matches()) {
                        Toast.makeText(SplashScreen.this, "Incorrect email", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putString("username", usernameField.getText().toString());
                        editor.putString("email", emailField.getText().toString());
                        editor.commit();
                        getUserInfo();
                        if(username == null || email == null) {

                        } else {
                            Intent homeScreen = new Intent(SplashScreen.this, HomeScreen.class);
                            homeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(homeScreen);
                            finish();
                        }
                    }
                }

            });
        } else {
            Intent homeScreen = new Intent(this, HomeScreen.class);
            homeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeScreen);
            finish();
        }
    }

    void getUserInfo() {
        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, Activity.MODE_PRIVATE);
        username = settings.getString("username", null);
        email = settings.getString("email", null);
    }
}
