package com.akstechies.promoteyourproducts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProductRVAdapter.ProductClickInterface{

    private RecyclerView productRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<ProductRVModel> productRVModelArrayList;
    private RelativeLayout mainBottomSheetRL;
    //After creating adapter
    private ProductRVAdapter productRVAdapter;
    private FirebaseAuth mAuth;
    private String userId;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productRV = findViewById(R.id.idRVProducts);
        addFAB = findViewById(R.id.idFABAddProduct);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        productRVModelArrayList = new ArrayList<>();
        productRVAdapter = new ProductRVAdapter(productRVModelArrayList, this, this);
        mainBottomSheetRL = findViewById(R.id.idTLBottomSheet);
        //set layout manager for recycler view
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productRVAdapter);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //Logics
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Products");
                Intent i = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(i);
            }
        });
        getAllCourses();
    }

    private void getAllCourses() {
        productRVModelArrayList.clear();

        //Read all data from db
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Called when new child added
                loadingPB.setVisibility(View.GONE);
                productRVModelArrayList.add(snapshot.getValue(ProductRVModel.class));
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    @Override
    public void onProductClick(int position) {
        displayBottomSheet(productRVModelArrayList.get(position));
    }

    //Display Bottom Sheet
    public void displayBottomSheet(ProductRVModel productRVModel) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialogue, mainBottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.show();

        TextView productName = layout.findViewById(R.id.idTVProductName);
        TextView productDescription = layout.findViewById(R.id.idTVProductDescription);
        TextView productSuitedFor = layout.findViewById(R.id.idTVProductSuitedFor);
        TextView productPrice= layout.findViewById(R.id.idTVProductPrice);
        ImageView productImage = layout.findViewById(R.id.idIVProductImage);
        Button editButton = layout.findViewById(R.id.idBtnEditProduct);
        Button viewDetailsButton = layout.findViewById(R.id.idBtnViewProduct);

        productName.setText(productRVModel.getProductName());
        productDescription.setText(productRVModel.getProductDescription());
        productSuitedFor.setText(productRVModel.getProductSuitedFor());
        productPrice.setText("Rs: " + productRVModel.getProductPrice());
        Picasso.get().load(productRVModel.getProductImageLink()).into(productImage);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId.equals(productRVModel.getUserId())) {
                    Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
                    intent.putExtra("product", productRVModel); //Edit product parcelable key
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "You must be Authentic use to edit this..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(productRVModel.getProductLink()));
                startActivity(i);
            }
        });
    }

    //Logout menu inflate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.idItemLogout:
                Toast.makeText(this, "Logging you out..", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent( MainActivity.this, LoginActivity.class));
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}