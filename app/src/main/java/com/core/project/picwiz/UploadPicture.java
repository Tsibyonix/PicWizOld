package com.core.project.picwiz;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class  UploadPicture extends AppCompatActivity {
    String photoLocation;
    Uri photoLocationURI;
    String photoLocationPath;
    String TAG = "log";
    Button upload;
    float imageViewX, imageViewY;
    public static Bitmap bitmap;
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
        upload= (Button) findViewById(R.id.uploadbtn);
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
                bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                //scale(ScalingLogic.CROP);
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new UploadTask().execute();

            }
        });

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
    private class UploadTask extends AsyncTask<Bitmap, Void, Void> {

        protected Void doInBackground(Bitmap... params) {
            if (params == null)
                return null;
            setProgress(0); //initial progress of the progress bar

             //storing the bitmap in a variable
            ByteArrayOutputStream stream = new ByteArrayOutputStream(); //creating an object of byteArrayoutput stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
            InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
            Log.d("image", "cant get the image ");
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                HttpPost httppost = new HttpPost(
                        "http://e-learningpoint.in/picwiz/uploadimage.php"); // server

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("PicWiz",
                        System.currentTimeMillis() + ".jpg", in);
                httppost.setEntity(reqEntity);

                Log.i(TAG, "request " + httppost.getRequestLine());
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    if (response != null)
                        Log.i(TAG, "response " + response.getStatusLine().toString());
                } finally {

                }
            } finally {

            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }



        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Toast.makeText(UploadPicture.this, "uploaded", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "onResume: " + this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }

    String mCurrentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;
    File photoFile = null;


}