package com.example.schoolproject;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LoginRegistration extends AppCompatActivity {
    Button mButtonLogin;
    Button mButtonRegister;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_registration );

        mButtonLogin = findViewById(R.id.nextLoginButton);
        mButtonRegister = findViewById(R.id.nextRegisterButton);
        img = findViewById(R.id.MoveBack);
    }

    public void gotoNext(View view) {

        Intent i = new Intent(LoginRegistration.this, Register.class);
        startActivity(i);

    }

    public void goBack(View view) {

        Intent b = new Intent(LoginRegistration.this, Splash.class);
        startActivity(b);
    }

    public void GoToNext(View view) {

        Intent r = new Intent(LoginRegistration.this, LoginActivity.class);
        startActivity(r);

    }
}