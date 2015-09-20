package com.core.project.picwiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public static final String SETTINGS_NAME = "MySettingsFile";
    String username;
    String email;
    EditText usernameField;
    EditText emailField;
    Button check;
    Button checkEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        usernameField = (EditText) findViewById(R.id.usernameField);
        emailField = (EditText) findViewById(R.id.emailField);
        check = (Button) findViewById(R.id.check);
        checkEmail = (Button) findViewById(R.id.checkEmail);

//        FrameLayout home_Screen_Layout = (FrameLayout) findViewById(R.id.home_Screen_Layout);
        final SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        username = settings.getString("username", null);
        usernameField.setText(username);
        email = settings.getString("email", null);
        emailField.setText(email);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameField.getText().toString().isEmpty()) {
                    Toast.makeText(Settings.this, "Cannot change username to empty", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("username", usernameField.getText().toString());
                    editor.commit();
                    Toast.makeText(Settings.this, "Username changed to " + usernameField.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailField.getText().toString().isEmpty()) {
                    Toast.makeText(Settings.this, "Cannot change email to empty", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("email", emailField.getText().toString());
                    editor.commit();
                    Toast.makeText(Settings.this, "Email changed to " + emailField.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getCurrentTheme(){
        PackageInfo packageInfo;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            return getResources().getResourceEntryName(themeResId);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }

    public void setTheme(Activity activity, int resID) {
        activity.setTheme(resID);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
}
