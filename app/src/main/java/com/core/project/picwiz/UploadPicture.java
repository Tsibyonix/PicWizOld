package com.core.project.picwiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.w3c.dom.Text;

public class  UploadPicture extends AppCompatActivity {
    String photoLocation;
    Uri photoLocationURI;
    String photoLocationPath;
    String TAG = "log";

    float imageViewX, imageViewY;

    TouchImageView imageView;
    Button crop_scale;
    FloatingActionButton upload;
    Bitmap outBitmap;
    CheckBox uploadCheck;
    EditText caption;
    TextView location;
    Space spaceTop;
    Space space_bottom;

    //bar
    android.support.v7.widget.Toolbar midBar;
    android.support.v7.widget.Toolbar bottomBar;

    String currentScale = "crop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        imageView = (TouchImageView) findViewById(R.id.customImageView);
        crop_scale = (Button) findViewById(R.id.crop_fit);
        upload = (FloatingActionButton) findViewById(R.id.upload);
        uploadCheck = (CheckBox) findViewById(R.id.uploadCheck);
        caption = (EditText) findViewById(R.id.captionField);
        location = (TextView) findViewById(R.id.location);
        spaceTop = (Space) findViewById(R.id.space_top);
        space_bottom = (Space) findViewById(R.id.space_bottom);
        //bar
        midBar = (android.support.v7.widget.Toolbar) findViewById(R.id.midBar);
        bottomBar = (android.support.v7.widget.Toolbar) findViewById(R.id.bottomBar);

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

        final ViewTreeObserver imageViewObserver = imageView.getViewTreeObserver();
        imageViewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageViewY = (float) imageView.getMeasuredHeight();
                imageViewX = (float) imageView.getMeasuredWidth();
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageURI(photoLocationURI);
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
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                        //Toast.makeText(UploadPicture.this, "drag", Toast.LENGTH_SHORT).show();

                        if(!uploadCheck.isChecked()) {
                            uploadCheck.setChecked(false);
                        }
                        else {
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
                imageView.buildDrawingCache();
                outBitmap = imageView.getDrawingCache();

                //image save logic
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




