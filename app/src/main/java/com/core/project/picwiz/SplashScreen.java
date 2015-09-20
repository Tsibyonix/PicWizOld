package com.core.project.picwiz;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public static final String SETTINGS_NAME = "MySettingsFile";
    String username;
    String email;
    private ProgressDialog pDialog;
    public static String signup="http://picwiz.e-learningpoint.in/signup.php";
    public static String login="http://picwiz.e-learningpoint.in/signup.php";
    RequestQueue requestQueue;
    EditText usernameField;
    EditText emailField;
    Button accept;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    StringRequest strReq=null;


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
        settings = getSharedPreferences(SETTINGS_NAME, 0);
        editor = settings.edit();

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

                    if (usernameField.getText().toString().isEmpty() || emailField.getText().toString().isEmpty()) {
                        Toast.makeText(SplashScreen.this, "Fields are not filled", Toast.LENGTH_SHORT).show();
                    } else if (!emailPattern.matcher(emailField.getText().toString()).matches()) {
                        Toast.makeText(SplashScreen.this, "Incorrect email", Toast.LENGTH_SHORT).show();
                    } else {

                        pDialog = new ProgressDialog(SplashScreen.this);
                        pDialog.setCancelable(false);
                        final String tag_string_req = "req_register";

                        pDialog.setMessage("Registering ...");
                        //pDialog.show();

                            // Posting params to register url
                            pDialog.setMessage("Uploading");
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("tag", "register");
                            params.put("name", username);
                            params.put("email", email);
                            editor.putString("username", usernameField.getText().toString());
                            editor.putString("email", emailField.getText().toString());
                            editor.commit();
                            getUserInfo();



                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                signup, new JSONObject(params),
                                new com.android.volley.Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Log.d("TAG", response.toString());
                                        try {
                                            //Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                                            Toast.makeText(SplashScreen.this, "Thank you for your post", Toast.LENGTH_LONG).show();

                                            if (response.getBoolean("status")) {
                                                pDialog.dismiss();
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            Log.e("TAG", e.toString());
                                        }
                                        pDialog.dismiss();
                                    }
                                }, new com.android.volley.Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //VolleyLog.d("TAG", "Error: " + error.getMessage());
                                pDialog.dismiss();
                                //if (isNetworkProblem(error)) {
                                    Toast.makeText(SplashScreen.this, "Internet Problem", Toast.LENGTH_SHORT).show();

                                //}
                            }
                        }) {

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                          //  @Override
                           // public Map<String, String> getHeaders() throws AuthFailureError {
                          //      return getRequestHeaders();
                            //}
                        };

                        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        // Adding request to request queue
                        SendOnline.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);

                    }




                if(username==null||email==null)

                {

                }

                else

                {
                    Intent homeScreen = new Intent(SplashScreen.this, HomeScreen.class);
                    homeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeScreen);
                    finish();
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

