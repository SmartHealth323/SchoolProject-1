package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity<forgotTextLink> extends AppCompatActivity {
    Button mButtonLogin;
    EditText mTextEmail;
    EditText mTextPassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    ImageView img;
    TextView forgotTextLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login2 );
        mButtonLogin = (Button) findViewById(R.id.login);
        mTextEmail = (EditText) findViewById(R.id.txt_email);
        mTextPassword = (EditText) findViewById(R.id.txt_password);
        forgotTextLink = findViewById(R.id.ForgotPassword);
        img = (ImageView) findViewById(R.id.GoBack);

        firebaseAuth = FirebaseAuth.getInstance();



        mButtonLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mTextEmail.getText().toString().trim();
                String password = mTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){

                    mTextEmail.setError("Email required");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mTextEmail.setError("Please Enter a valid Email");
                    mTextEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){

                   mTextPassword.setError("password is required");
                   mTextPassword.requestFocus();
                   return;
                }

                if(password.length() < 6){

                    mTextPassword.setError("minimum should be 6 characters");
                    mTextPassword.requestFocus();
                    return;
                }

                //authenticate the user
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( LoginActivity.this, "Logged in successful", Toast.LENGTH_SHORT ).show();
                            startActivity(new Intent(getApplicationContext(), Symptoms.class));
                        }else{
                            Toast.makeText( LoginActivity.this, "Error Login " + task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );

            }

        } );

        }

    public void goBack(View view) {

        Intent i = new Intent(LoginActivity.this, LoginRegistration.class);
        startActivity(i);

    }


    public void forgotTextLink(View view){

        forgotTextLink.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){

                final EditText resetMail = new EditText( view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder( view.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Please validate the registered Email");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton( "yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        //Extracting Email and Send the Reset Link

                        String mail = resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener( new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid){

                                Toast.makeText( LoginActivity.this, "Reset Link has been sent to your Email", Toast.LENGTH_SHORT ).show();

                            }
                        } ).addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e){
                                Toast.makeText( LoginActivity.this, "Error! Link has not been sent" + e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        } );

                    }
                } );
                passwordResetDialog.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                        //close the dialog

                    }
                } );

                passwordResetDialog.create().show();
            }
        } );
    }
}