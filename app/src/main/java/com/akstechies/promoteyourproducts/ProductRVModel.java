package com.akstechies.promoteyourproducts;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductRVModel implements Parcelable {

    private String productName;
    private String productPrice;
    private String productImageLink;
    private String productSuitedFor;
    private String productLink;
    private String productDescription;
    private String productId;
    private String userId;

    //Constructor -> required to perform crud operation

    public ProductRVModel() {

    }

    //Generate Constructor, Getter and Setter => Right Click->Generate


    public ProductRVModel(String productName, String productPrice, String productImageLink, String productSuitedFor, String productLink, String productDescription, String productId, String userId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageLink = productImageLink;
        this.productSuitedFor = productSuitedFor;
        this.productLink = productLink;
        this.productDescription = productDescription;
        this.productId = productId;
        this.userId = userId;
    }

    protected ProductRVModel(Parcel in) {
        productName = in.readString();
        productPrice = in.readString();
        productImageLink = in.readString();
        productSuitedFor = in.readString();
        productLink = in.readString();
        productDescription = in.readString();
        productId = in.readString();
        userId = in.readString();
    }

    public static final Creator<ProductRVModel> CREATOR = new Creator<ProductRVModel>() {
        @Override
        public ProductRVModel createFromParcel(Parcel in) {
            return new ProductRVModel(in);
        }

        @Override
        public ProductRVModel[] newArray(int size) {
            return new ProductRVModel[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }

    public String getProductSuitedFor() {
        return productSuitedFor;
    }

    public void setProductSuitedFor(String productSuitedFor) {
        this.productSuitedFor = productSuitedFor;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(productPrice);
        parcel.writeString(productImageLink);
        parcel.writeString(productSuitedFor);
        parcel.writeString(productLink);
        parcel.writeString(productDescription);
        parcel.writeString(productId);
    }
}
