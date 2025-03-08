package com.akstechies.promoteyourproducts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {

    private ArrayList<ProductRVModel> productRVModelArrayList;
    private Context context;
    int lastPosition = -1;
    private ProductClickInterface productClickInterface;

    //create construct but don't select ProductClickInterface

    public ProductRVAdapter(ArrayList<ProductRVModel> productRVModelArrayList, Context context, ProductClickInterface productClickInterface) {
        this.productRVModelArrayList = productRVModelArrayList;
        this.context = context;
        this.productClickInterface = productClickInterface;
    }

    @NonNull
    @Override
    public ProductRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout file
        View view = LayoutInflater.from(context).inflate(R.layout.product_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVAdapter.ViewHolder holder, int position) {
        //setting the data
        ProductRVModel productRVModel = productRVModelArrayList.get(position);
        holder.productNameTV.setText(productRVModel.getProductName());
        holder.productPriceTV.setText("Rs: " + productRVModel.getProductPrice());
        Picasso.get().load(productRVModel.getProductImageLink()).into(holder.productImageIV);

        //Adding animation
        setAnimation(holder.itemView, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //productClickInterface.onProductClick(position); //->was giving error
                productClickInterface.onProductClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productRVModelArrayList.size();
    }
    public interface ProductClickInterface {
        void onProductClick(int position);
    }

    //changes
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTV, productPriceTV;
        private ImageView productImageIV;

        public ViewHolder(@NonNull View itemView) { //inflate layout file and add animation
            super(itemView);

            productNameTV = itemView.findViewById(R.id.idTVProductName);
            productPriceTV = itemView.findViewById(R.id.idTVProductPrice);
            productImageIV = itemView.findViewById(R.id.idIVProductImage);
        }
    }

    private void setAnimation(View itemView, int position) {
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

}
