package com.example.schoolproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +      //at least 1 digit
                    "(?=.*[a-z])" +      //at least one lower case letter
                    "(?=.*[A-Z])" +      //at least one upper case letter
                    "(?=.*[@#$%^&+=])" +  //at least one special character
                    "(?=\\S+$)" +         //no white space
                    "{6,}" +              //at least 6 characters
                    "$");
    private static final Pattern EMAIL_ADDRESS =
            Pattern.compile(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                     ")+"
            );



    //variables
    Button nextBtn;
    EditText mEmail;
    EditText mTextFirstName;
    TextView mTitleText;
    EditText mTextLastName;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    ProgressDialog loadingBar;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        //hooks
        nextBtn =  findViewById(R.id.next_register);
        mEmail = findViewById(R.id.txt_email);
        mTitleText = findViewById(R.id.title_text);
        mTextFirstName =  findViewById(R.id.txt_firstName);
        mTextLastName = findViewById(R.id.txt_lastName);
        mTextPassword = findViewById(R.id.txt_password);
        mTextCnfPassword = findViewById(R.id.txt_cnf_password);
        backBtn = findViewById(R.id.back_arrow);

        loadingBar = new ProgressDialog(this);

        fAuth = FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            finish();
        }


        nextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String email = mEmail.getText().toString().trim();
                String password = mTextPassword.getText().toString().trim();
                String CnfPassword = mTextCnfPassword.getText().toString().trim();

                if (TextUtils.isEmpty( email )) {

                    Toast.makeText( Register.this, "Email is required", Toast.LENGTH_SHORT ).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {

                    Toast.makeText( Register.this, "Please Enter a Valid Email", Toast.LENGTH_SHORT ).show();
                }
                else if (TextUtils.isEmpty( password )) {

                    Toast.makeText( Register.this, "Password is Required", Toast.LENGTH_SHORT ).show();
                }
                else if (!PASSWORD_PATTERN.matcher( password ).matches()) {

                    Toast.makeText( Register.this, "Password is too week", Toast.LENGTH_SHORT ).show();
                }
                else if (password.length() < 6) {

                    Toast.makeText( Register.this, "Minimum should be 6 characters", Toast.LENGTH_SHORT ).show();

                }
                else if (TextUtils.isEmpty(CnfPassword)) {

                    Toast.makeText( Register.this, "Confirmation Password is required", Toast.LENGTH_SHORT ).show();
                }
                else if (!password.equals(CnfPassword)) {

                    Toast.makeText( Register.this, "Password do not match", Toast.LENGTH_SHORT ).show();
                }
                else {

                    loadingBar.setTitle("Creating New Account");
                    loadingBar.setMessage("Please wait, while we are creating you a New Account");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                //registering the user using firebase

                fAuth.createUserWithEmailAndPassword( email, password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){

                        if (task.isSuccessful()) {

                            Toast.makeText( Register.this, "Successfully Registered", Toast.LENGTH_SHORT ).show();
                            loadingBar.dismiss();
                            startActivity( new Intent( getApplicationContext(), SignUpActivity.class ) );
                            finish();


                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText( Register.this, "You are already Registered", Toast.LENGTH_SHORT ).show();
                            } else {
                                Toast.makeText( Register.this, " " + task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                            }


                            Toast.makeText( Register.this, "Registration Error " + task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                            loadingBar.dismiss();
                        }

                    }
                } );

            }

            }
        } );

    }


    public void goBack(View view) {

        Intent ic = new Intent( Register.this, LoginRegistration.class);
        startActivity(ic);
    }

    public void callNextRegister(View view) {

        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);

        //adding transitions
        Pair[] pairs = new Pair[3];

        pairs[0] = new Pair<View,String>(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair<View,String>(mTitleText, "transition_title_text");
        pairs[2] = new Pair<View,String>(nextBtn, "transition_next_btn");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this, pairs);
            startActivity(intent, options.toBundle());
        }
        else{

            startActivity( intent );
        }

    }
}