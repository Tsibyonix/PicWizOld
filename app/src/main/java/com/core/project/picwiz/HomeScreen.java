/*
    HomeScreen runs after the splash screen.
    Contains the recycler view to show images on the go
    The floating add buttons gives you the option to choose the input method the upload a new pic
    1. The gallery option takes you to the gallery and shows your selection on the upload_activity
    2. The camera option helps you the click pics and upload them directly

    The top rite corner has a overflow menu which takes you to different activities like settings and profile
*/

package com.core.project.picwiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
    String username;
    String email;
    String TAG = "log";
    String currentTheme = "AppLiteTheme";
    Settings setting;

    //intent codes
    private static final int PHOTO_CODE = 1;
    private static final int GET_PHOTO = 0;

    //intent Extra
    public int UPLOAD_LATER;
    Uri outputFileUri;
    Intent galleryUploadPhotoIntent;

    FloatingActionMenu floatingActionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, Activity.MODE_PRIVATE);
        username = settings.getString("username", null);
        email = settings.getString("email", null);
        currentPicNumber = settings.getInt("currentPicNumber", 0);
        Log.i(TAG, String.valueOf(currentPicNumber));

        FrameLayout homeScreen = (FrameLayout) findViewById(R.id.home_Screen_Layout);

        //Floating Menu; API: https://github.com/Clans/FloatingActionButton
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatingActionMenu);
        floatingActionMenu.setClosedOnTouchOutside(true);

        floatingActionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                String file;
                File newPic;

                currentPicNumber = currentPicNumber+1;
                file = dir+currentPicNumber+".jpg";
                newPic = new File(file);

                while (newPic.exists()) {
                    currentPicNumber = currentPicNumber + 1;
                    file = dir+currentPicNumber+".jpg";
                    newPic = new File(file);
                }

//                try {
//                    newPic.createNewFile();
//                } catch (IOException e) {
//                    Log.i(TAG, "New image file not created");
//                }

                outputFileUri = Uri.fromFile(newPic);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(Intent.createChooser(cameraIntent, "CLICK PICTURE"), PHOTO_CODE);
            }
        });

        floatingActionButton_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT PICTURE"), GET_PHOTO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String selectedImagePath;

        if (resultCode == RESULT_OK && null != data) {
            switch(requestCode)
            {
                case GET_PHOTO:
                    Uri selectedImageURI = data.getData();
                    if(selectedImageURI == null)
                        Log.i(TAG, "NULL");

                    selectedImagePath = selectedImageURI.toString();
                    galleryUploadPhotoIntent = new Intent(this, UploadPicture.class);
                    galleryUploadPhotoIntent.putExtra("selectedImagePath", selectedImagePath);
                    startActivity(galleryUploadPhotoIntent);
                    break;

                case PHOTO_CODE:
                    Toast.makeText(HomeScreen.this, "camera activity", Toast.LENGTH_SHORT).show();
                    galleryUploadPhotoIntent = new Intent(this, UploadPicture.class);
                    galleryUploadPhotoIntent.putExtra("selectedImagePath", outputFileUri.toString());
                    startActivity(galleryUploadPhotoIntent);
            }
        } else {
            switch (requestCode){
                case PHOTO_CODE:
                    if(resultCode == RESULT_CANCELED) {
                        Log.i(TAG, "Intent Canceled");
                    }else {
                        Toast.makeText(HomeScreen.this, "camera activity", Toast.LENGTH_SHORT).show();
                        galleryUploadPhotoIntent = new Intent(this, UploadPicture.class);
                        galleryUploadPhotoIntent.putExtra("selectedImagePath", outputFileUri.toString());
                        startActivity(galleryUploadPhotoIntent);
                    }
                    break;
            }
        }
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
        floatingActionMenu.close(false);
        Log.i(TAG, "onStop Called");
        Log.i(TAG, String.valueOf(currentPicNumber));
        SharedPreferences settings = getSharedPreferences(SETTINGS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("currentPicNumber", currentPicNumber);
        editor.apply();
    }
}
