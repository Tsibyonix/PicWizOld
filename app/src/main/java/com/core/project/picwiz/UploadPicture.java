package com.core.project.picwiz;

<<<<<<< HEAD
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
=======
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
>>>>>>> origin/master
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
<<<<<<< HEAD
import android.view.View;
=======
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
>>>>>>> origin/master
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadPicture extends AppCompatActivity {
=======
public class  UploadPicture extends AppCompatActivity {
>>>>>>> origin/master
    String photoLocation;
    Uri photoLocationURI;
    String photoLocationPath;
    String TAG = "log";
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri;

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
<<<<<<< HEAD
        ImageView imageView = (ImageView) findViewById(R.id.selectedImageView);
        Button upLoad = (Button) findViewById(R.id.uploadbtn);
        upLoadServerUri = "http://e-learningpoint.in/picwiz/get_image_picwiz.php";
=======
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

>>>>>>> origin/master
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

<<<<<<< HEAD
        imageView.setImageURI(null);
        imageView.setImageURI(photoLocationURI);
        upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(UploadPicture.this, "", "Uploading file...", true);

                uploadFile(photoLocationURI.getPath());


=======
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
>>>>>>> origin/master
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
<<<<<<< HEAD
    public int uploadFile(String sourceFileUri) {


        final String fileName = sourceFileUri;

        HttpURLConnection connection = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 5 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
            +photoLocation);



            return 0;

        }
        else
        {

            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true); // Allow Inputs
                connection.setDoOutput(true); // Allow Outputs
                connection.setUseCaches(false); // Don't use a Cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(connection.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name="+photoLocation+";filename="
                                + fileName + "" + lineEnd);

                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(UploadPicture.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(UploadPicture.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });


            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(UploadPicture.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e("Upload file to server Exception", "Exception : "+ e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }}
=======
}


>>>>>>> origin/master


