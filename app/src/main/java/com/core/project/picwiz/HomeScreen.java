package com.core.project.picwiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class HomeScreen extends AppCompatActivity {
    public static final String SETTINGS_NAME = "MySettingsFile";
    int currentPicNumber;
    String TAG = "log";
    int PHOTO_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, Activity.MODE_PRIVATE);
        currentPicNumber = settings.getInt("currentPicNumber", 0);
        Log.i(TAG, String.valueOf(currentPicNumber));

        FrameLayout homeScreen = (FrameLayout) findViewById(R.id.home_Screen_Layout);

        //Floating Menu; API: https://github.com/Clans/FloatingActionButton
        final FloatingActionMenu floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        floatingActionMenu.setClosedOnTouchOutside(true);

        floatingActionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingActionMenu.isOpened()) {
                    Toast.makeText(HomeScreen.this, floatingActionMenu.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }
                floatingActionMenu.toggle(true);
            }
        });

        //FloatingActionButton
        FloatingActionButton floatingActionButton_Camera = (FloatingActionButton) findViewById(R.id.floatingActionButton_Camera);
        FloatingActionButton floatingActionButton_Gallery = (FloatingActionButton) findViewById(R.id.floatingActionButton_Gallery);

        floatingActionButton_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PicWiz/";
                File newDir = new File(dir);
                if(newDir.isDirectory()) {

                } else {
                    newDir.mkdirs();
                }

                currentPicNumber = currentPicNumber+1;
                String file = dir+currentPicNumber+".jpg";
                File newPic = new File(file);
                try {
                    newPic.createNewFile();
                } catch (IOException e) {
                    Log.i(TAG, "New image file not created");
                }

                Uri outputFileUri = Uri.fromFile(newPic);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, PHOTO_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        switch (selectedId)
        {
            case R.id.action_Settings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.action_MyProfile:
                break;
            case R.id.action_About:
                startActivity(new Intent(this, About.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop Called");
        Log.i(TAG, String.valueOf(currentPicNumber));
        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentPicNumber`", currentPicNumber);
        editor.apply();
    }
}
