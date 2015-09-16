package com.core.project.picwiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class  UploadPicture extends AppCompatActivity {
    String photoLocation;
    Uri photoLocationURI;
    String photoLocationPath;
    String TAG = "log";

    float imageViewX, imageViewY;

    TouchImageView imageView;
    TextView touch;
    TextView left;
    TextView top;
    TextView bottom;
    TextView right;
    TextView imageviewsize;
    TextView imageres;
    Button crop_scale;

    String currentScale = "crop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        imageView = (TouchImageView) findViewById(R.id.customImageView);
        imageres = (TextView) findViewById(R.id.touch);

        //debug
        imageviewsize = (TextView) findViewById(R.id.imageviewsize);
        imageres  = (TextView) findViewById(R.id.resolution);
        crop_scale = (Button) findViewById(R.id.crop_fit);

        final TextView cropres = (TextView) findViewById(R.id.cropres);
        final TextView path = (TextView) findViewById(R.id.path);
        final TextView left = (TextView) findViewById(R.id.left);
        final TextView top = (TextView) findViewById(R.id.top);
        final TextView bottom = (TextView) findViewById(R.id.bottom);
        final TextView right = (TextView) findViewById(R.id.right);

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

        path.setText(getRealPathFromURI(photoLocationURI));

        ViewTreeObserver imageViewObserver = imageView.getViewTreeObserver();
        imageViewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageViewY = (float) imageView.getMeasuredHeight();
                imageViewX = (float) imageView.getMeasuredWidth();
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageURI(photoLocationURI);

                //scale(ScalingLogic.CROP);
            }
        });

        crop_scale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentScale.contains("crop")) {
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    currentScale = "fit";
                } else {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    currentScale = "crop";
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




