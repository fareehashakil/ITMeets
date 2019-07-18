package com.example.itmeetsapp.signup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_username, tv_email, tv_phone;
    private Button buttonEdit, buttonSave;
    String getId;
    private Menu action;
    private static final String TAG = HomeActivity.class.getSimpleName(); //get info
    SharedPrefManager sharedPrefManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        if (SharedPrefManager.getInstance(this).isLoggeddIn()){
//            finish();
//            startActivity(new Intent(this, HomeActivity.class));
//            return;
//        }

        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_email = (TextView) findViewById(R.id.tv_email);

        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        buttonEdit.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

//        HashMap<String, String> user = sharedPrefManager.isLoggeddIn();
//        getId = user.get(sharedPrefManager.);

        tv_email.setText(SharedPrefManager.getInstance(this).getUserEmail());
        tv_username.setText(SharedPrefManager.getInstance(this).getUsername());
    }

    private void getUserDetail(){
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i =0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
                                    tv_username.setText(strName);
                                    tv_email.setText(strEmail);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Error Reading Detail"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Error Reading Detail"+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    private void onEdit(){
//        tv_username.setFocusableInTouchMode(true);
//        tv_email.setFocusableInTouchMode(true);
//
//        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(tv_username, InputMethodManager.SHOW_IMPLICIT);
//
//        findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
//        findViewById(R.id.buttonSave).setVisibility(View.VISIBLE);
//    }

    private void saveEditDetail(){
        final String name = this.tv_username.getText().toString().trim();
        final String email = this.tv_email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(ProfileActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
                                sharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                        jsonObject.getInt("user_id"),
                                        jsonObject.getString("user_name"),
                                        jsonObject.getString("email_address"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", name);
                params.put("email_address", email);
                params.put("user_id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume(){
        super.onResume();
        getUserDetail();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_ation, menu);
//
//        action = menu;
//        action.findItem(R.id.menuSave).setVisible(false);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.menuEdit:
//                tv_username.setFocusableInTouchMode(true);
//                tv_email.setFocusableInTouchMode(true);
//
//                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.showSoftInput(tv_username, InputMethodManager.SHOW_IMPLICIT);
//
//                action.findItem(R.id.menuEdit).setVisible(false);
//                action.findItem(R.id.menuSave).setVisible(true);
//
//                return true;
//
//            case R.id.menuSave:
//                saveEditDetail();
//                action.findItem(R.id.menuEdit).setVisible(true);
//                action.findItem(R.id.menuSave).setVisible(false);
//
//                tv_username.setFocusable(false);
//                tv_email.setFocusable(false);
//
//                tv_username.setFocusable(false);
//                tv_email.setFocusable(false);
//
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    @Override
    public void onClick(View v) {
        if(v==buttonEdit) {
            getUserDetail();
            tv_username.setFocusableInTouchMode(true);
            tv_email.setFocusableInTouchMode(true);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(tv_username, InputMethodManager.SHOW_IMPLICIT);

            findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
            findViewById(R.id.buttonSave).setVisibility(View.VISIBLE);
        }
        if (v==buttonSave){
            saveEditDetail();
            findViewById(R.id.buttonEdit).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonSave).setVisibility(View.INVISIBLE);
            tv_username.setFocusableInTouchMode(false);
            tv_email.setFocusableInTouchMode(false);
            tv_username.setFocusable(false);
            tv_email.setFocusable(false);
        }
    }


//
//    @Override
//    public void onClick(View v) {
//        if(v==buttonEdit) {
//            getUserDetail();
//
//            tv_username.setFocusableInTouchMode(true);
//            tv_email.setFocusableInTouchMode(true);
//
//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.showSoftInput(tv_username, InputMethodManager.SHOW_IMPLICIT);
//
//            findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
//            findViewById(R.id.buttonSave).setVisibility(View.VISIBLE);
//
//        }
//        if (v==buttonSave){
//            saveEditDetail();
//            findViewById(R.id.buttonEdit).setVisibility(View.VISIBLE);
//            findViewById(R.id.buttonSave).setVisibility(View.INVISIBLE);
//            tv_username.setFocusableInTouchMode(false);
//            tv_email.setFocusableInTouchMode(false);
//            tv_username.setFocusable(false);
//            tv_email.setFocusable(false);
//
//        }
//    }
}
