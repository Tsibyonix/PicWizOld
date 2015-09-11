package com.core.project.picwiz;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        FrameLayout home_Screen_Layout = (FrameLayout) findViewById(R.id.home_Screen_Layout);

        String theme = getThemeName();

        RadioButton darkRadioButton = (RadioButton) findViewById(R.id.darkRadioButton);
        RadioButton lightRadioButton = (RadioButton) findViewById(R.id.lightRadioButton);

        RadioGroup themeSelection = (RadioGroup) findViewById(R.id.themeRadioGroup);

        themeSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.darkRadioButton:
                        break;
                    case R.id.lightRadioButton:
                        break;
                }
            }
        });
    }

    public String getThemeName()
    {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectionId = item.getItemId();

        switch (selectionId)
        {
            case R.id.action_back:
                Toast.makeText(Settings.this, getThemeName(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
