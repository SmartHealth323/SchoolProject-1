package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    EditText phoneText;
    EditText codeText;
    LinearLayout linearLayout;
    CountryCodePicker countryCodePicker;
    Button VerifyOtp;

    String  checker = "", phoneNumber = "";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth firebaseAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_phone2 );

        firebaseAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        countryCodePicker = findViewById(R.id.code_picker);
        VerifyOtp = findViewById(R.id.nextOtp);
        countryCodePicker = findViewById(R.id.code_picker);
        linearLayout = findViewById(R.id.phoneAuth);


        countryCodePicker.registerCarrierNumberEditText(phoneText);


        VerifyOtp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (VerifyOtp.getText().equals("Submit") || checker.equals("Code sent"))
                {
                    String verificationCode = codeText.getText().toString();
                    if (verificationCode.equals(""))
                    {
                        Toast.makeText( PhoneActivity.this, "Input the Code First", Toast.LENGTH_SHORT ).show();
                    }
                    else
                    {
                        loadingBar.setTitle("Code Verification");
                        loadingBar.setMessage("Please Wait as the code being verified");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                        signInWithPhoneAuthCredential(credential);

                    }
                }
                else
                {
                    phoneNumber = countryCodePicker.getFullNumberWithPlus();
                    if (!phoneNumber.equals(""))
                    {
                        loadingBar.setTitle("Verifying  Phone Number");
                        loadingBar.setMessage("Please Wait as the Phone Number is being verified");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+254" + phoneNumber, 60, TimeUnit.SECONDS, PhoneActivity.this, mCallbacks );

                    }
                    else
                    {
                        Toast.makeText( PhoneActivity.this, "Enter a Valid Phone Number", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        } );

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {

              signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e){

                Toast.makeText( PhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                loadingBar.dismiss();
               linearLayout.setVisibility(View.VISIBLE);
                VerifyOtp.setText("SignUp");
                codeText.setVisibility(View.GONE);


            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken){
                super.onCodeSent( s, forceResendingToken );

                mVerificationId = s;
                mResendToken = forceResendingToken;

                linearLayout.setVisibility( View.GONE );
                checker = "Code Sent";
                VerifyOtp.setText("Submit");
                codeText.setVisibility(View.VISIBLE);

                loadingBar.dismiss();
                Toast.makeText( PhoneActivity.this, "The code has been sent.Please wait", Toast.LENGTH_SHORT ).show();

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if (task.isSuccessful()){

                            loadingBar.dismiss();
                            Toast.makeText( PhoneActivity.this, "Successful Registration", Toast.LENGTH_SHORT ).show();
                        }
                        else{

                            loadingBar.dismiss();

                            Toast.makeText( PhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }

    private void sendUserToSymptomsActivity(){

        Intent intent = new Intent(getApplicationContext(), Symptoms.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void goBack(View view){

        Intent goBack = new Intent(PhoneActivity.this, SignUpActivity.class);
        startActivity(goBack);
        finish();
    }
}