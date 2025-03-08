package com.akstechies.promoteyourproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText productNameEdt, productPriceEdt, productImageLinkEdt, productSuitedForEdt, productLinkEdt, productDescriptionEdt;
    private Button updateProductBtn, deleteProductBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String productId;
    private ProductRVModel productRVModel;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productNameEdt = findViewById(R.id.idTIEProductName);
        productPriceEdt = findViewById(R.id.idTIEProductPrice);
        productImageLinkEdt = findViewById(R.id.idTIEProductImageLink);
        productSuitedForEdt = findViewById(R.id.idTIEProductSuitedFor);
        productLinkEdt = findViewById(R.id.idTIEProductLink);
        productDescriptionEdt = findViewById(R.id.idTIEProductDescription);

        updateProductBtn = findViewById(R.id.idBtnUpdateProduct);
        deleteProductBtn = findViewById(R.id.idBtnDeleteProduct);
        loadingPB = findViewById(R.id.idPBLoading);

        firebaseDatabase = FirebaseDatabase.getInstance();

        productRVModel = getIntent().getParcelableExtra("product");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if(productRVModel != null) {
            productNameEdt.setText(productRVModel.getProductName());
            productPriceEdt.setText(productRVModel.getProductPrice());
            productImageLinkEdt.setText(productRVModel.getProductImageLink());
            productSuitedForEdt.setText(productRVModel.getProductSuitedFor());
            productLinkEdt.setText(productRVModel.getProductLink());
            productDescriptionEdt.setText(productRVModel.getProductDescription());

            productId = productRVModel.getProductId();
        }

        databaseReference = firebaseDatabase.getReference("Products").child(productId);

        updateProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);

                String productName = productNameEdt.getText().toString();
                String productPrice = productPriceEdt.getText().toString();
                String productImageLink = productImageLinkEdt.getText().toString();
                String productSuitedFor = productSuitedForEdt.getText().toString();
                String productLink = productLinkEdt.getText().toString();
                String productDescription = productDescriptionEdt.getText().toString();

                //Add to database
                Map<String, Object> map = new HashMap<>();
                map.put("productName", productName);   //key name same as in productRVModel
                map.put("productPrice", productPrice);
                map.put("productImageLink", productImageLink);
                map.put("productSuitedFor", productSuitedFor);
                map.put("productLink", productLink);
                map.put("productDescription", productDescription);
                map.put("productId", productId);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        databaseReference.updateChildren(map);
                        Toast.makeText(EditProductActivity.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProductActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(EditProductActivity.this, "Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }

    public void deleteProduct() {
        databaseReference.removeValue();
        Toast.makeText(EditProductActivity.this, "Product Removed...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditProductActivity.this, MainActivity.class));
    }
}