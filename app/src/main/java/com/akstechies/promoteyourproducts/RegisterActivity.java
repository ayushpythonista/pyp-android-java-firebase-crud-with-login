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

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText userNameEdt, passwordEdt, confirmPwdEdt;
    private Button registerBtn;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    private TextView loginTV;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEdt = findViewById(R.id.idTIEUserName);
        passwordEdt = findViewById(R.id.idTIEPassword);
        confirmPwdEdt = findViewById(R.id.idTIEConfirmPassword);
        loginTV = findViewById(R.id.idTVLogin);

        registerBtn = findViewById(R.id.idBtnRegister);

        loadingPB = findViewById(R.id.idPBLoading);

        mAuth = FirebaseAuth.getInstance();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);

                String username = userNameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                String confirmPwd = confirmPwdEdt.getText().toString();

                if(!password.equals(confirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Both password don't match", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Add all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}