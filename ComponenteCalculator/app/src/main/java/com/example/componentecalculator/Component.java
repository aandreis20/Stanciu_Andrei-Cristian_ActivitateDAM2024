package com.example.componentecalculator;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Components")
public class Component implements Parcelable, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String category;
    private int price;
    private boolean discount;
    private int quantity;

    public Component() {
        this.name = "i5";
        this.category = "Procesoare";
        this.price = 1000;
        this.discount = true;
        this.quantity = 10;
    }


    public Component(String name,
                     String category,
                     int price,
                     boolean discount,
                     int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }

    protected Component(Parcel in) {
        name = in.readString();
        category = in.readString();
        price = in.readInt();
        discount = in.readByte() != 0;
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeInt(price);
        dest.writeByte((byte) (discount ? 1 : 0));
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Component> CREATOR = new Creator<Component>() {
        @Override
        public Component createFromParcel(Parcel in) {
            return new Component(in);
        }

        @Override
        public Component[] newArray(int size) {
            return new Component[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Component{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", availability=" + discount +
                ", quantity=" + quantity +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
