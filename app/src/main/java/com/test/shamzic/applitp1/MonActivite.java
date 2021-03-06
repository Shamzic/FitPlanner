package com.test.shamzic.applitp1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MonActivite extends AppCompatActivity implements View.OnClickListener {


  private EditText editTextEmail;
  private EditText editTextPassword;
  private Button buttonSignup ;
  private TextView textViewSignin;

  private ProgressDialog progressDialog;

  private FirebaseAuth firebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_mon_activite);

    firebaseAuth = FirebaseAuth.getInstance();

    if(firebaseAuth.getCurrentUser() != null) {
      // profile activity here
      finish();
      startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
    }

    editTextEmail = (EditText) findViewById(R.id.editTextEmail);
    editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    textViewSignin = (TextView) findViewById(R.id.textViewSignin);

    buttonSignup = (Button) findViewById(R.id.buttonSignup);

    progressDialog = new ProgressDialog(this);

    buttonSignup.setOnClickListener(this);
    textViewSignin.setOnClickListener(this);


    ConnectivityManager connMgr = (ConnectivityManager)
    getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    boolean isWifiConn = networkInfo.isConnected();
    networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    boolean isMobileConn = networkInfo.isConnected();
    System.out.println("Wifi connected: " + isWifiConn);
    System.out.println("Mobile connected: " + isMobileConn);
  }

  private void registerUser() {
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();

    if(TextUtils.isEmpty(email)){
      Toast.makeText(getApplicationContext(),/*"pliz enter email"*/R.string.askEmail/*stringAskEmail*/,Toast.LENGTH_LONG).show();
      return;
    }

    if(TextUtils.isEmpty(password)){
      Toast.makeText(getApplicationContext(), /*"pliz enter pass"*/ R.string.askPass/*stringAskPassword*/,Toast.LENGTH_LONG).show();
      return;
    }

    progressDialog.setMessage("register ..."/*stringProgressRegistration*/);
    progressDialog.show();

    firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                  finish();
                  startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                  Toast.makeText(getApplicationContext(), /*"success register"*/R.string.successRegister/*stringSuccesRegister*/,Toast.LENGTH_SHORT).show();
                }
                else {
                  Toast.makeText(getApplicationContext(), /*"fail register"*/R.string.failRegister/*stringFailRegister*/,Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
              }
            });
  }

  @Override
  public void onClick(View view) {
    if(view == buttonSignup){
      registerUser();
    }

    if(view == textViewSignin) {
      // will open activity
      /*  finish();*/
      startActivity(new Intent(this, LoginActivity.class));
    }
  }
}