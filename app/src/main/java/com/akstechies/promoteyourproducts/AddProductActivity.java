package com.akstechies.promoteyourproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductActivity extends AppCompatActivity {

    private TextInputEditText productNameEdt, productPriceEdt, productImageLinkEdt, productSuitedForEdt, productLinkEdt, productDescriptionEdt;
    private Button addProductBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String productId, userId;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEdt = findViewById(R.id.idTIEProductName);
        productPriceEdt = findViewById(R.id.idTIEProductPrice);
        productImageLinkEdt = findViewById(R.id.idTIEProductImageLink);
        productSuitedForEdt = findViewById(R.id.idTIEProductSuitedFor);
        productLinkEdt = findViewById(R.id.idTIEProductLink);
        productDescriptionEdt = findViewById(R.id.idTIEProductDescription);

        addProductBtn = findViewById(R.id.idBtnAddProduct);
        loadingPB = findViewById(R.id.idPBLoading);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("Products");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                String productName = productNameEdt.getText().toString();
                String productPrice = productPriceEdt.getText().toString();
                String productImageLink = productImageLinkEdt.getText().toString();
                String productSuitedFor = productSuitedForEdt.getText().toString();
                String productLink = productLinkEdt.getText().toString();
                String productDescription = productDescriptionEdt.getText().toString();
                productId = databaseReference.push().getKey(); //get the instance of firebase and get reference to child jobs then get the key. the key will be unique.

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userId = user.getUid();

                //Passing variables to model class -> ProductRVModel
                ProductRVModel productRVModel = new ProductRVModel(productName, productPrice, productImageLink, productSuitedFor, productLink, productDescription, productId, userId);

                //Add to database
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.child(productId).setValue(productRVModel);
                        Toast.makeText(AddProductActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(AddProductActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}