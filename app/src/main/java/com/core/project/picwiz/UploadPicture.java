package com.core.project.picwiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import com.core.project.picwiz.Filters.GrayFilter;

import com.core.project.picwiz.Filters.OldFilter;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;

public class  UploadPicture extends AppCompatActivity {
    String photoLocation;
    Uri photoLocationURI;
    String photoLocationPath;
    private static String TAG = "log";
    private Bitmap changeBitmap = null;

    float imageViewX, imageViewY;

    TouchImageView imageView;
    FloatingActionButton upload;
    Bitmap outBitmap;
    Bitmap originalBitmap;

    int CurrentSavedPicNumber;

    //location
    public String latitude;
    public String longitude;

    //img
    Button rotation;
    Button crop_scale;
    Button brightness;
        SeekBar setBrightness;

    //bar
    android.support.v7.widget.Toolbar midBar;
    android.support.v7.widget.Toolbar bottomBar;
    CheckBox uploadCheck;
    EditText caption;
    TextView location;
    Space spaceTop;
    Space space_bottom;

    String currentScale = "crop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        imageView = (TouchImageView) findViewById(R.id.customImageView);
        upload = (FloatingActionButton) findViewById(R.id.upload);
        uploadCheck = (CheckBox) findViewById(R.id.uploadCheck);
        caption = (EditText) findViewById(R.id.captionField);
        location = (TextView) findViewById(R.id.location);
        spaceTop = (Space) findViewById(R.id.space_top);
        space_bottom = (Space) findViewById(R.id.space_bottom);
        //bar
        midBar = (android.support.v7.widget.Toolbar) findViewById(R.id.midBar);
        bottomBar = (android.support.v7.widget.Toolbar) findViewById(R.id.bottomBar);

        //img
        crop_scale = (Button) findViewById(R.id.crop_fit);
        rotation = (Button) findViewById(R.id.rotate);
        brightness = (Button) findViewById(R.id.brightness);
            setBrightness = (SeekBar) findViewById(R.id.setBrightness);

        //upload.setVisibility(View.GONE);
        //upload.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

        if(savedInstanceState == null) {
            Log.i(TAG, "Not a saved instance");
            Bundle extra = getIntent().getExtras();
            if(extra == null) {
                Log.i(TAG, "Extra is null");
                photoLocation = null;
            } else {
                Log.i(TAG, "uri set");
                photoLocation = extra.getString("selectedImagePath");
            }
        } else {
            photoLocation = (String) savedInstanceState.getSerializable("selectedImagePath");
        }

        photoLocationURI = Uri.parse(photoLocation);
        photoLocationPath = getRealPathFromURI(photoLocationURI);

        try {
            originalBitmap = MediaStore.Images.Media.getBitmap(UploadPicture.this.getContentResolver(),photoLocationURI);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ViewTreeObserver imageViewObserver = imageView.getViewTreeObserver();
        imageViewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageViewY = (float) imageView.getMeasuredHeight();
                imageViewX = (float) imageView.getMeasuredWidth();
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setImageURI(photoLocationURI);
                imageView.setImageBitmap(originalBitmap);
                getGeoLocation();
            }
        });

        crop_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentScale.contains("crop")) {
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    currentScale = "fit";
                } else {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    currentScale = "crop";
                }
            }
        });

        midBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //Toast.makeText(UploadPicture.this, "drag", Toast.LENGTH_SHORT).show();

                        if (!uploadCheck.isChecked()) {
                            uploadCheck.setChecked(false);
                        } else {
                            uploadCheck.setChecked(true);
                        }
                        break;
                }
                return true;
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadCheck.isChecked()) {
                    BitmapDrawable btmpDr = (BitmapDrawable) imageView.getDrawable();
                    outBitmap = btmpDr.getBitmap();

                    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PicWiz/";
                    File newDir = new File(dir);
                    if (newDir.isDirectory()) {

                    } else {
                        newDir.mkdirs();
                    }

                    Uri outputImageUri;
                    String file;
                    File newPic;
                    FileOutputStream fileOutputStream = null;

                    CurrentSavedPicNumber = +200;
                    file = dir + CurrentSavedPicNumber + ".jpg";
                    newPic = new File(file);

                    outputImageUri = Uri.fromFile(newPic);
                    try {
                        fileOutputStream = new FileOutputStream(newPic);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    outBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                } else {

                }

            }
        });

        uploadCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!uploadCheck.isChecked()) {
                    caption.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    spaceTop.setVisibility(View.VISIBLE);
                    space_bottom.setVisibility(View.VISIBLE);
                } else {
                    caption.setVisibility(View.GONE);
                    location.setVisibility(View.GONE);
                    spaceTop.setVisibility(View.GONE);
                    space_bottom.setVisibility(View.GONE);
                }
            }
        });

        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setBrightness.getVisibility() == View.VISIBLE) {
                    setBrightness.setVisibility(View.GONE);
                } else {
                    setBrightness.setVisibility(View.VISIBLE);
                }
            }
        });

        rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap tempBitmap = null;
                imageView.buildDrawingCache();
                tempBitmap = imageView.getDrawingCache();
                imageView.setImageBitmap(null);
                final Bitmap finalTempBitmap = Bitmap.createBitmap(tempBitmap);

                new AsyncTask<Void, Bitmap, Void>() {

                    Bitmap processedBitmap;

                    @Override
                    protected Void doInBackground(Void... params) {
                        Log.i("Filter", "Running");
                        processedBitmap = OldFilter.changeToOld(finalTempBitmap);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Log.i("Filter", "Done");
                        imageView.setImageBitmap(processedBitmap);
                    }
                }.execute();
                //imageView.setImageBitmap(tempBitmap);
            }
        });
    }

    void getGeoLocation() {
        location.setText("Fetching location...");
        new AsyncTask<Void, Void, Void>() {
            float[] latLong = new float[2];
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ExifInterface exifInterface = new ExifInterface(photoLocationPath);
                    //latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                    exifInterface.getLatLong(latLong);
                    latitude = String.valueOf(latLong[0]);
                    longitude = String.valueOf(latLong[1]);
                } catch (IOException e) {
                    Toast.makeText(UploadPicture.this, "Unable to get image geo tag, location with be set to current", Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                location.setText(latitude +", "+ longitude);
            }
        }.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] split;
        split = contentUri.toString().split("/");
        String finalPath;
        finalPath = "/";
        for(int i = 3; i <= split.length - 1; i++) {
            finalPath = finalPath.concat(split[i]);
            if(i != split.length - 1)
                finalPath = finalPath.concat("/");
        }
        return finalPath;
    }
}