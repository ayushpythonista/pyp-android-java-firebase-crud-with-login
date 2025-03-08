package com.akstechies.promoteyourproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText userNameEdt, passwordEdt;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    private TextView registerTV;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEdt = findViewById(R.id.idTIEUserName);
        passwordEdt = findViewById(R.id.idTIEPassword);
        loginBtn = findViewById(R.id.idBtnLogin);
        loadingPB = findViewById(R.id.idPBLoading);
        mAuth = FirebaseAuth.getInstance();
        registerTV = findViewById(R.id.idTVRegister);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String username = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter all required fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Logged IN..", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //If user is logged in -> redirect to dashboard
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }

    }
}